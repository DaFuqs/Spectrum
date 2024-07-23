package de.dafuqs.spectrum.blocks.deeper_down.flora;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
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
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class TriStateVineBlock extends PlantBlock implements Fertilizable {

    public static final EnumProperty<LifeStage> LIFE_STAGE = EnumProperty.of("life_stage", LifeStage.class);
    private final int minHeight;
    private final float growthTickChance, spreadChance, overgrowth;

    public TriStateVineBlock(Settings settings, int minHeight, float growthChance, float spreadChance, float overgrowth) {
        super(settings);
        setDefaultState(getDefaultState().with(LIFE_STAGE, LifeStage.GROWING));
        this.minHeight = minHeight;
        this.growthTickChance = growthChance;
        this.spreadChance = spreadChance;
        this.overgrowth = overgrowth;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        var reference = BlockReference.of(state, pos);
        var handStack = player.getStackInHand(hand);
        var creative = player.getAbilities().creativeMode;

        if (handStack.isIn(ConventionalItemTags.SHEARS)) {
            if (reference.getProperty(LIFE_STAGE) != LifeStage.GROWING)
                return ActionResult.FAIL;

            if (!creative)
                handStack.damage(1, player, p -> p.sendToolBreakStatus(hand));

            reference.setProperty(LIFE_STAGE, LifeStage.MATURE);
            reference.update(world);

            world.playSound(null, pos, SoundEvents.BLOCK_BEEHIVE_SHEAR, SoundCategory.BLOCKS, 1.0F, MathHelper.nextBetween(world.random, 0.6F, 1.0F));
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, reference.getState()));
            return ActionResult.success(world.isClient());
        }
        else if (handStack.isOf(SpectrumItems.MOONSTRUCK_NECTAR)) {
            if (reference.getProperty(LIFE_STAGE) != LifeStage.MATURE)
                return ActionResult.FAIL;

            if (!creative)
                handStack.decrement(1);

            reference.setProperty(LIFE_STAGE, LifeStage.GROWING);
            reference.update(world);

            world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS, 1.0F, MathHelper.nextBetween(world.random, 0.6F, 1.0F));
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, reference.getState()));
            return ActionResult.success(world.isClient());
        }

        return ActionResult.PASS;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        var world = ctx.getWorld();
        var pos = ctx.getBlockPos();

        var state = getDefaultState();
        var roof = BlockReference.of(world, pos.up());

        if (!canPlaceAt(world.getBlockState(pos), world, pos) || !world.isAir(pos))
            return null;

        if (roof.isOf(this)) {
            state = state.with(LIFE_STAGE, roof.getProperty(LIFE_STAGE));
            roof.setProperty(LIFE_STAGE, LifeStage.STALK);
            roof.update(world);
        }

        return state;
    }

    abstract boolean hasGrowthActions();

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {}

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(LIFE_STAGE) != LifeStage.MATURE;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextFloat() >= growthTickChance)
            return;

        var reference = BlockReference.of(state, pos);
        var stage = reference.getProperty(LIFE_STAGE);

        if (hasGrowthActions() && random.nextBoolean() || stage != LifeStage.GROWING) {
            grow(world, random, pos, state);
        }
        else {
            if (!canGrow(world, random, pos, state) || random.nextFloat() >= spreadChance)
                return;

            reference.setProperty(LIFE_STAGE, LifeStage.STALK);
            reference.update(world);

            var sprigState = getDefaultState();
            var height = getCurrentHeight(world, reference.pos);

            if (height >= minHeight && random.nextFloat() >= overgrowth) {
                sprigState = sprigState.with(LIFE_STAGE, LifeStage.MATURE);
            }

            world.setBlockState(reference.pos.down(), sprigState);
        }
    }

    protected int getCurrentHeight(World world, BlockPos pos) {
        var state = world.getBlockState(pos);
        var count = 0;

        while (state.isOf(this)) {
            count++;
            state = world.getBlockState(pos.up(count));
        }

        return count;
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
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return world.isAir(pos.down());
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (canPlaceAt(state, world, pos))
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
        if (!canPlaceAt(state, world, pos)) {
            world.scheduleBlockTick(pos, this, 1);
        }

        return state;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        var roof = pos.up();
        var roofState = world.getBlockState(roof);

        if (roofState.isOf(this))
            return true;

        return canPlantOnTop(roofState, world, roof);
    }

    @Override
    public boolean canPlantOnTop(BlockState roof, BlockView world, BlockPos pos) {
        return roof.isSideSolidFullSquare(world, pos, Direction.DOWN);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIFE_STAGE);
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