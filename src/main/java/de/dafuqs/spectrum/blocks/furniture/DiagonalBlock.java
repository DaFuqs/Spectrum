package de.dafuqs.spectrum.blocks.furniture;

import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import org.jetbrains.annotations.*;

public class DiagonalBlock extends Block {
	
	public static final BooleanProperty DIAGONAL = BooleanProperty.of("diagonal");
	
	public DiagonalBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(DIAGONAL, false));
	}
	
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		var state = super.getPlacementState(ctx);
		var player = ctx.getPlayer();
		
		if (player != null && state != null) {
			var yaw = player.getYaw() + 180 + 360;
			var arc = yaw % 90;
			
			return state.with(DIAGONAL, arc > 25 && arc < 65);
		}
		
		return super.getPlacementState(ctx);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(DIAGONAL);
	}
}
