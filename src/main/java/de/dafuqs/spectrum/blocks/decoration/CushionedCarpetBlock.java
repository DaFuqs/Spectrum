package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

public class CushionedCarpetBlock extends CarpetBlock {
	
	public static final MapCodec<CushionedCarpetBlock> CODEC = simpleCodec(CushionedCarpetBlock::new);
	
	public CushionedCarpetBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends CushionedCarpetBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
	}
}
