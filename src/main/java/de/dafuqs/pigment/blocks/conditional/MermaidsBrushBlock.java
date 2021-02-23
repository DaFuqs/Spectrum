package de.dafuqs.pigment.blocks.conditional;

import de.dafuqs.pigment.PigmentBlockTags;
import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.PigmentItems;
import de.dafuqs.pigment.interfaces.Cloakable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.SimpleBlockFeature;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class MermaidsBrushBlock extends PlantBlock implements Cloakable, Waterloggable {

    protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);

    public static final IntProperty AGE = Properties.AGE_7;
    private boolean wasLastCloaked;

    public MermaidsBrushBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(AGE, 0));
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
    @Override
    @Environment(EnvType.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        checkCloak(state);
        return super.isSideInvisible(state, stateFrom, direction);
    }

    @Override
    public void setCloaked() {
        // Colored Logs => Oak logs
        PigmentCommon.getBlockCloaker().swapModel(this.getDefaultState(), Blocks.WATER.getDefaultState()); // block
        PigmentCommon.getBlockCloaker().swapModel(this.asItem(), Items.SEAGRASS); // item
    }

    @Override
    public void setUncloaked() {
        PigmentCommon.getBlockCloaker().unswapAllBlockStates(this);
        PigmentCommon.getBlockCloaker().unswapModel(this.asItem());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if(wasLastCloaked) {
            return EMPTY_SHAPE;
        } else {
            return SHAPE;
        }
    }

    @Deprecated
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        return getCloakedDroppedStacks(state, builder);
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        } else {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return Fluids.WATER.getStill(false);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int age = state.get(AGE);
        if (age == 7) {
            ItemEntity pearlEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(PigmentItems.MERMAIDS_GEM, 1));
            world.spawnEntity(pearlEntity);
            world.setBlockState(pos, state.with(AGE, 0), 4);
        } else {
            if(random.nextFloat() < 0.1) {
                world.setBlockState(pos, state.with(AGE, age + 1), 4);
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
        FluidState fluidState = world.getFluidState(pos);
        return fluidState.isIn(FluidTags.WATER) && world.getBlockState(pos.down()).isIn(PigmentBlockTags.MERMAIDS_BRUSH_PLANTABLE);
    }

}