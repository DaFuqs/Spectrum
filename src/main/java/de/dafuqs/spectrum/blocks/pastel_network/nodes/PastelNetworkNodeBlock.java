package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class PastelNetworkNodeBlock extends FacingBlock {
	
	public static final DirectionProperty FACING = Properties.FACING;
	
	protected static final VoxelShape SHAPE_UP = Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D);
	protected static final VoxelShape SHAPE_DOWN = Block.createCuboidShape(4.0D, 8.0D, 4.0D, 12.0D, 16.0D, 12.0D);
	protected static final VoxelShape SHAPE_NORTH = Block.createCuboidShape(4.0D, 4.0D, 8.0D, 12.0D, 12.0D, 16.0D);
	protected static final VoxelShape SHAPE_SOUTH = Block.createCuboidShape(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 8.0D);
	protected static final VoxelShape SHAPE_EAST = Block.createCuboidShape(0.0D, 4.0D, 4.0D, 8.0D, 12.0D, 12.0D);
	protected static final VoxelShape SHAPE_WEST = Block.createCuboidShape(8.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D);
	
	protected final Text nodeTypeTooltipText;
	protected static final Text rangeTooltipText = new TranslatableText("block.spectrum.pastel_network_nodes.tooltip.range").formatted(Formatting.GRAY);
	
	public PastelNetworkNodeBlock(Settings settings, String tooltipName) {
		super(settings);
		this.nodeTypeTooltipText = new TranslatableText(tooltipName);
	}
	
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction direction = ctx.getSide();
		BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos().offset(direction.getOpposite()));
		return blockState.isOf(this) && blockState.get(FACING) == direction ? this.getDefaultState().with(FACING, direction.getOpposite()) : this.getDefaultState().with(FACING, direction);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		PastelNetworkNodeBlockEntity pastelNetworkNodeBlockEntity = getBlockEntity(world, pos);
		if(pastelNetworkNodeBlockEntity != null) {
			Direction facingDirection = state.get(FACING);
			pastelNetworkNodeBlockEntity.add(world, pos, facingDirection);
		}
	}
	
	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		super.onBreak(world, pos, state, player);
		PastelNetworkNodeBlockEntity pastelNetworkNodeBlockEntity = getBlockEntity(world, pos);
		if(pastelNetworkNodeBlockEntity != null) {
			pastelNetworkNodeBlockEntity.remove();
		}
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(nodeTypeTooltipText);
		tooltip.add(rangeTooltipText);
	}
	
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		super.neighborUpdate(state, world, pos, block, fromPos, notify);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		switch (state.get(FACING)) {
			case UP -> {
				return SHAPE_UP;
			}
			case DOWN -> {
				return SHAPE_DOWN;
			}
			case NORTH -> {
				return SHAPE_NORTH;
			}
			case EAST -> {
				return SHAPE_EAST;
			}
			case SOUTH -> {
				return SHAPE_SOUTH;
			}
			default -> {
				return SHAPE_WEST;
			}
		}
	}
	
	public @Nullable PastelNetworkNodeBlockEntity getBlockEntity(World world, BlockPos blockPos) {
		BlockEntity blockEntity = world.getBlockEntity(blockPos);
		if(blockEntity instanceof PastelNetworkNodeBlockEntity pastelNetworkNodeBlockEntity) {
			return pastelNetworkNodeBlockEntity;
		}
		return null;
	}
	
}