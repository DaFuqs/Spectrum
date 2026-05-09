package de.dafuqs.spectrum.blocks.memory;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.phys.shapes.*;
import javax.annotation.*;

import java.util.*;

public class MemoryBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
	
	public static final MapCodec<MemoryBlock> CODEC = simpleCodec(MemoryBlock::new);
	
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape SHAPE = Block.box(4.0D, 2.0D, 4.0D, 12.0D, 14.0D, 12.0D);
	
	public MemoryBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
	}
	
	@Override
	public MapCodec<? extends MemoryBlock> codec() {
		return CODEC;
	}
	
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new MemoryBlockEntity(pos, state);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof MemoryBlockEntity memoryBlockEntity) {
			memoryBlockEntity.setData(placer, itemStack);
		}
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		super.randomTick(state, world, pos, random);
		
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof MemoryBlockEntity memoryBlockEntity) {
			memoryBlockEntity.advanceManifesting(world, pos);
		}
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
		return this.defaultBlockState().setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
	}
	
	// drop the memory when broken
	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		BlockEntity blockEntity = builder.getParameter(LootContextParams.BLOCK_ENTITY);
		if (blockEntity instanceof MemoryBlockEntity memoryBlockEntity) {
			return List.of(memoryBlockEntity.getMemoryItemStack());
		} else {
			return super.getDrops(state, builder);
		}
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (state.getValue(WATERLOGGED)) {
			world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
	}
	
}
