package de.dafuqs.spectrum.blocks.deeper_down.flora;

import de.dafuqs.spectrum.helpers.BlockReference;
import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;

public class AbyssalVineBlock extends TriStateVineBlock {

    public static final BooleanProperty BERRIES = Properties.BERRIES;

    public AbyssalVineBlock(Settings settings) {
        super(settings, 5, 0.3F, 0.4F, 0.667F);
        setDefaultState(getDefaultState().with(BERRIES, false));
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return !state.get(BERRIES);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        var reference = BlockReference.of(state, pos);
        var superSucc = super.onUse(state, world, pos, player, hand, hit);

        if (superSucc.shouldIncrementStat()) {
            return superSucc;
        }

        if (!reference.getProperty(BERRIES))
            return ActionResult.FAIL;

        reference.setProperty(BERRIES, false);
        reference.update(world);
        world.playSound(null, pos, SoundEvents.BLOCK_CAVE_VINES_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, MathHelper.nextBetween(world.random, 0.8F, 1.2F));
        player.getInventory().offerOrDrop(SpectrumItems.FISSURE_PLUM.getDefaultStack());

        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, reference.getState()));
        return ActionResult.SUCCESS;
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
    public boolean hasRandomTicks(BlockState state) {
        return super.hasRandomTicks(state) || !state.get(BERRIES);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return SpectrumItems.FISSURE_PLUM.getDefaultStack();
    }

    @Override
    boolean hasGrowthActions() {
        return true;
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
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(BERRIES);
    }
}
