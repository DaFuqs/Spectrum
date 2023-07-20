package de.dafuqs.spectrum.blocks.memory;

import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.loot.context.*;
import net.minecraft.server.world.*;
import net.minecraft.state.*;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MemoryBlock extends BlockWithEntity implements Waterloggable {
	
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	protected static final VoxelShape SHAPE = Block.createCuboidShape(4.0D, 2.0D, 4.0D, 12.0D, 14.0D, 12.0D);
	
	public MemoryBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(WATERLOGGED, false));
	}
	
	@Override
	protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new MemoryBlockEntity(pos, state);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	public void onPlaced(@NotNull World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof MemoryBlockEntity memoryBlockEntity) {
			memoryBlockEntity.setData(placer, itemStack);
		}
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.randomTick(state, world, pos, random);
		
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof MemoryBlockEntity memoryBlockEntity) {
			memoryBlockEntity.advanceManifesting(world, pos);
		}
	}
	
	@Override
	public BlockState getPlacementState(@NotNull ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		return this.getDefaultState().with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
	}
	
	// drop the memory when broken
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.@NotNull Builder builder) {
		BlockEntity blockEntity = builder.getNullable(LootContextParameters.BLOCK_ENTITY);
		if (blockEntity instanceof MemoryBlockEntity memoryBlockEntity) {
			return List.of(memoryBlockEntity.getMemoryItemStack());
		} else {
			return super.getDroppedStacks(state, builder);
		}
	}
	
	@Override
	public FluidState getFluidState(@NotNull BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(@NotNull BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}
	
}
