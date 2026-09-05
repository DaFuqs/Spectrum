package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.block.state.*;

public class SpectrumCounterDirectionalPlacedBlock extends SpectrumDirectionalBlock {
	
	public static final MapCodec<SpectrumCounterDirectionalPlacedBlock> CODEC = simpleCodec(SpectrumCounterDirectionalPlacedBlock::new);
	
	public SpectrumCounterDirectionalPlacedBlock(Properties properties) {
		super(properties);
		registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
	}
	
	@Override
	public MapCodec<? extends SpectrumCounterDirectionalPlacedBlock> codec() {
		return CODEC;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getClickedFace().getOpposite());
	}
	
}
