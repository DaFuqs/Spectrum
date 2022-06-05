package de.dafuqs.spectrum.blocks.decoration;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.Direction;

public class CardinalFacingBlock extends FacingBlock {
	
	public static final BooleanProperty CARDINAL_FACING = BooleanProperty.of("cardinal_facing");
	
	public CardinalFacingBlock(Settings settings) {
		super(settings);
		this.setDefaultState((this.stateManager.getDefaultState()).with(CARDINAL_FACING, false));
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction facing = ctx.getPlayerFacing();
		boolean facingVertical = facing.equals(Direction.EAST) || facing.equals(Direction.WEST);
		return (this.getDefaultState().with(CARDINAL_FACING, facingVertical));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(CARDINAL_FACING);
	}
	
	
}
