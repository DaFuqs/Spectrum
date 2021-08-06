package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.interfaces.Cloakable;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Hashtable;
import java.util.List;
import java.util.Random;

public class QuitoxicReedsBlock extends SugarCaneBlock implements Cloakable, Waterloggable {

    public static final IntProperty AGE = Properties.AGE_15;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public QuitoxicReedsBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(WATERLOGGED, false).with(AGE, 0));
        registerCloak();
    }

    @Override
    public Identifier getCloakAdvancementIdentifier() {
        return new Identifier(SpectrumCommon.MOD_ID, "craft_colored_sapling"); // TODO
    }

    @Override
    public Hashtable<BlockState, BlockState> getBlockStateCloaks() {
        Hashtable<BlockState, BlockState> hashtable = new Hashtable<>();
        for(int i = 0; i < 16; i++){
            hashtable.put(this.getDefaultState().with(WATERLOGGED, false).with(AGE, i), Blocks.AIR.getDefaultState());
            hashtable.put(this.getDefaultState().with(WATERLOGGED, true).with(AGE, i), Blocks.WATER.getDefaultState());
        }
        return hashtable;
    }

    @Override
    public Pair<Item, Item> getItemCloak() {
        return new Pair<>(this.asItem(), Blocks.SUGAR_CANE.asItem());
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
        if (world.isAir(pos.up()) || (world.isWater(pos.up()) && world.isAir(pos.up(2)))) {
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

    @Deprecated
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if(this.isVisibleTo(context)) {
            return SHAPE;
        }
        return EMPTY_SHAPE;
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
            if (bottomBlockState.isIn(SpectrumBlockTags.QUITOXIC_REEDS_PLANTABLE) && (world.isAir(pos.up()) || world.isAir(pos.up(2)) || topBlockState.isOf(this))) {
                FluidState fluidState = world.getFluidState(pos);
                return fluidState.isIn(FluidTags.WATER); // || fluidState.isIn(SpectrumFluidTags.LIQUID_CRYSTAL); // todo: liquid crystal logged
            }
            return false;
        }
    }

}
