package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import org.jspecify.annotations.Nullable;

public class DiagonalBlock extends Block {
	
	public static final MapCodec<DiagonalBlock> CODEC = simpleCodec(DiagonalBlock::new);
	
	public static final BooleanProperty DIAGONAL = BooleanProperty.create("diagonal");
	
	public DiagonalBlock(Properties settings) {
		super(settings);
		registerDefaultState(defaultBlockState().setValue(DIAGONAL, false));
	}
	
	@Override
	public MapCodec<? extends DiagonalBlock> codec() {
		return CODEC;
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
		var state = super.getStateForPlacement(ctx);
		var player = ctx.getPlayer();
		
		if (player != null && state != null) {
			var yaw = player.getYRot() + 180 + 360;
			var arc = yaw % 90;
			
			return state.setValue(DIAGONAL, arc > 25 && arc < 65);
		}
		
		return super.getStateForPlacement(ctx);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(DIAGONAL);
	}
}
