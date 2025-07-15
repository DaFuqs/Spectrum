package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.block.state.*;

public class SpectrumLineFacingBlock extends SpectrumFacingBlock {
	
	public static final MapCodec<SpectrumLineFacingBlock> CODEC = simpleCodec(SpectrumLineFacingBlock::new);
	
	public SpectrumLineFacingBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends SpectrumLineFacingBlock> codec() {
		return CODEC;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getNearestLookingDirection());
	}
	
}
