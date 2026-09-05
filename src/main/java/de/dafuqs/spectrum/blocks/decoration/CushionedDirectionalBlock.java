package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;

public class CushionedDirectionalBlock extends SpectrumDirectionalBlock {
	
	public static final MapCodec<CushionedDirectionalBlock> CODEC = simpleCodec(CushionedDirectionalBlock::new);
	
	public CushionedDirectionalBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends CushionedDirectionalBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
	}
}
