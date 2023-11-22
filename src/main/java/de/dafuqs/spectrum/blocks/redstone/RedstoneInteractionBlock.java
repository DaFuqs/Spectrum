package de.dafuqs.spectrum.blocks.redstone;

import net.minecraft.block.*;
import net.minecraft.block.enums.*;
import net.minecraft.item.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

public class RedstoneInteractionBlock extends Block {
	
	public static final BooleanProperty TRIGGERED = Properties.TRIGGERED;
	public static final EnumProperty<JigsawOrientation> ORIENTATION = Properties.ORIENTATION;
	
	public RedstoneInteractionBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(ORIENTATION, JigsawOrientation.EAST_UP).with(TRIGGERED, false));
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(ORIENTATION, rotation.getDirectionTransformation().mapJigsawOrientation(state.get(ORIENTATION)));
	}
	
	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.with(ORIENTATION, mirror.getDirectionTransformation().mapJigsawOrientation(state.get(ORIENTATION)));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(ORIENTATION, TRIGGERED);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction direction = ctx.getPlayerLookDirection().getOpposite();
		Direction direction2 = switch (direction) {
			case DOWN -> ctx.getPlayer().getHorizontalFacing().getOpposite();
			case UP -> ctx.getPlayer().getHorizontalFacing();
			case NORTH, SOUTH, WEST, EAST -> Direction.UP;
		};
		
		return this.getDefaultState()
				.with(ORIENTATION, JigsawOrientation.byDirections(direction, direction2))
				.with(TRIGGERED, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
	}
	
}
