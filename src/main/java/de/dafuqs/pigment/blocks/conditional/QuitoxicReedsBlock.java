package de.dafuqs.pigment.blocks.conditional;

import de.dafuqs.pigment.PigmentBlockTags;
import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.interfaces.Cloakable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class QuitoxicReedsBlock extends SugarCaneBlock implements Cloakable, Waterloggable {

    public static final IntProperty AGE = Properties.AGE_15;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private boolean wasLastCloaked;

    public QuitoxicReedsBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(WATERLOGGED, false).with(AGE, 0));
    }

    @Override
    public boolean isCloaked(PlayerEntity playerEntity, BlockState blockState) {
        return playerEntity.getArmor() < 1;
    }

    @Override
    public boolean wasLastCloaked() {
        return wasLastCloaked;
    }

    @Override
    public void setLastCloaked(boolean lastCloaked) {
        wasLastCloaked = lastCloaked;
    }

    @Deprecated
    @Environment(EnvType.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        checkCloak(state);
        return super.isSideInvisible(state, stateFrom, direction);
    }

    public void setCloaked() {
        // Colored Logs => Oak logs
        PigmentCommon.getBlockCloaker().swapModel(this.getDefaultState().with(WATERLOGGED, false), Blocks.AIR.getDefaultState()); // block
        PigmentCommon.getBlockCloaker().swapModel(this.getDefaultState().with(WATERLOGGED, true), Blocks.WATER.getDefaultState()); // block
        PigmentCommon.getBlockCloaker().swapModel(this.asItem(), Items.SUGAR_CANE); // item
    }

    public void setUncloaked() {
        PigmentCommon.getBlockCloaker().unswapAllBlockStates(this);
        PigmentCommon.getBlockCloaker().unswapModel(this.asItem());
    }

    @Deprecated
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        return getCloakedDroppedStacks(state, builder);
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        boolean bl = fluidState.getFluid() == Fluids.WATER;
        return super.getPlacementState(ctx).with(WATERLOGGED, bl);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        } else {
            if (state.get(WATERLOGGED)) {
                world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            }
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE, WATERLOGGED);
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.isAir(pos.up())) {
            int i;
            for(i = 1; world.getBlockState(pos.down(i)).isOf(this); ++i) {
            }

            if (i < 6) {
                int j = state.get(AGE);
                if (j == 15) {
                    world.setBlockState(pos.up(), this.getDefaultState());
                    world.setBlockState(pos, state.with(AGE, 0), 4);
                } else {
                    world.setBlockState(pos, state.with(AGE, j + 1), 4);
                }
            }
        }

    }

    /**
     * Can be placed in 1 high water / liquid crystal, growing on clay only
     * @param state
     * @param world
     * @param pos
     * @return
     */
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState bottomBlockState = world.getBlockState(pos.down());
        if (bottomBlockState.isOf(this)) {
            return true;
        } else {
            BlockState topBlockState = world.getBlockState(pos.up());
            if (bottomBlockState.isIn(PigmentBlockTags.QUITOXIC_REEDS_PLANTABLE) && (world.isAir(pos.up()) || topBlockState.isOf(this))) {
                // check next to fluid
                FluidState fluidState = world.getFluidState(pos);
                return fluidState.isIn(FluidTags.WATER); // || fluidState.isIn(PigmentFluidTags.LIQUID_CRYSTAL); // todo: liquid crystal logged
            }
            return false;
        }
    }

}
