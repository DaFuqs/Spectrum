package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;

public class CushionedFacingBlock extends SpectrumFacingBlock {
	
	public static final MapCodec<CushionedFacingBlock> CODEC = simpleCodec(CushionedFacingBlock::new);
	
	public CushionedFacingBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends CushionedFacingBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
	}
}
