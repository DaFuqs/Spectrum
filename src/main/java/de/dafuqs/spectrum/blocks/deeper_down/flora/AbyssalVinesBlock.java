package de.dafuqs.spectrum.blocks.deeper_down.flora;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;

public class AbyssalVinesBlock extends PlantBlock implements Fertilizable {

    public static final BooleanProperty BERRIES = Properties.BERRIES;
    public static final EnumProperty<LifeStage> LIFE_STAGE = EnumProperty.of("life_stage", LifeStage.class);

    public AbyssalVinesBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(BERRIES, false).with(LIFE_STAGE, LifeStage.GROWING));
    }
    
    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(SpectrumItems.FISSURE_PLUM);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return !state.get(BERRIES);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return world.isAir(pos.down());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        var reference = BlockReference.of(state, pos);
        var handStack = player.getStackInHand(hand);

        if (handStack.isIn(ConventionalItemTags.SHEARS)) {
            if (reference.getProperty(LIFE_STAGE) != LifeStage.GROWING)
                return ActionResult.FAIL;

            handStack.damage(1, player, p -> p.sendToolBreakStatus(hand));
            reference.setProperty(LIFE_STAGE, LifeStage.MATURE);
            reference.update(world);

            world.playSound(null, pos, SoundEvents.BLOCK_BEEHIVE_SHEAR, SoundCategory.BLOCKS, 1.0F, MathHelper.nextBetween(world.random, 0.6F, 1.0F));
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, reference.getState()));
            return ActionResult.success(world.isClient());
        }

        if (!reference.getProperty(BERRIES))
            return ActionResult.FAIL;

        reference.setProperty(BERRIES, false);
        reference.update(world);
        world.playSound(null, pos, SoundEvents.BLOCK_CAVE_VINES_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, MathHelper.nextBetween(world.random, 0.8F, 1.2F));
        InventoryHelper.insertOrDropItemstacks(player.getInventory(), world, pos, SpectrumItems.FISSURE_PLUM.getDefaultStack());

        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, reference.getState()));
        return ActionResult.success(world.isClient());
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        var reference = BlockReference.of(state, pos);
        var growthChance = 0.8F;

        for (int offset = 0; true; offset++) {
            var ref = BlockReference.of(world, pos.add(0, offset, 0));

            if (ref.isOf(SpectrumBlocks.SHALE_CLAY))
                return;

            if (ref.isIn(SpectrumBlockTags.GROWTH_ACCELERATORS)) {
                growthChance = 0.5F;
            }

            if (!ref.isOf(this))
                break;
        }

        if (random.nextFloat() < growthChance)
            return;

        if (!reference.getProperty(BERRIES))
            tryGrowBerries(reference, world);
        reference.update(world);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {

        if (random.nextFloat() < 0.7F)
            return;

        var reference = BlockReference.of(state, pos);
        var stage = reference.getProperty(LIFE_STAGE);

        if (random.nextBoolean() || stage != LifeStage.GROWING) {
            grow(world, random, pos, state);
        }
        else {
            if (!canGrow(world, random, pos, state) || random.nextFloat() < 0.6F)
                return;

            reference.setProperty(LIFE_STAGE, LifeStage.STALK);
            reference.update(world);

            var sprigState = getDefaultState();
            var sprig = pos.down();

            if (world.getBlockState(pos.up()).isOf(this) && world.getBlockState(pos.up(2)).isOf(this) && random.nextFloat() < 0.15F) {
                sprigState = sprigState.with(LIFE_STAGE, LifeStage.MATURE);
            }

            world.setBlockState(sprig, sprigState);
        }
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        var roof = BlockReference.of(world, pos.up());

        if (roof.isOf(this)) {
            roof.setProperty(LIFE_STAGE, getLowestLifeStage(world, pos.down(), state.get(LIFE_STAGE)));
            roof.update(world);
        }

        scheduleBreakCheck(world, pos);
    }

    public LifeStage getLowestLifeStage(WorldAccess world, BlockPos pos, LifeStage stage) {
        var state = world.getBlockState(pos);
        var lastStage = stage;
        while (state.isOf(this)) {
            lastStage = state.get(LIFE_STAGE);
            pos = pos.down();
            state = world.getBlockState(pos);
        }
        return lastStage;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (canPlantOnTop(state, world, pos))
            return;

        scheduleBreakCheck(world, pos);
        world.breakBlock(pos, true);
    }

    private void scheduleBreakCheck(WorldAccess world, BlockPos pos) {
        var underside = pos.down();
        if (world.getBlockState(underside).isOf(this))
            world.scheduleBlockTick(underside, this, 1);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!canPlantOnTop(state, world, pos)) {
            world.scheduleBlockTick(pos, this, 1);
        }

        return state;
    }

    public void tryGrowBerries(BlockReference reference, World world) {
         int berryCount = 0;

        for (int i = 0; i < 3; i++) {
            var uRef = BlockReference.of(world, reference.pos.add(0, i, 0));
            var dRef = BlockReference.of(world, reference.pos.add(0, -i, 0));

            berryCount += checkForBerries(uRef);
            berryCount += checkForBerries(dRef);

            if (i == 1 && (reference.pos.getY() % 5 == 0 && berryCount == 2) || (reference.pos.getY() % 7 == 0 && berryCount == 1))
                return;
        }

        if (berryCount >= 3)
            return;

        reference.setProperty(BERRIES, true);
    }

    private int checkForBerries(BlockReference ref) {
        if (ref.isOf(this) && ref.getProperty(BERRIES)) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        var roof = pos.up();
        var roofState = world.getBlockState(roof);

        if (roofState.isOf(this))
            return true;

        return roofState.isSideSolidFullSquare(world, roof, Direction.DOWN);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BERRIES, LIFE_STAGE);
    }

    public enum LifeStage implements StringIdentifiable {
        STALK("stalk"),
        GROWING("growing"),
        MATURE("mature");

        private final String name;

        LifeStage(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return name;
        }
    }
}