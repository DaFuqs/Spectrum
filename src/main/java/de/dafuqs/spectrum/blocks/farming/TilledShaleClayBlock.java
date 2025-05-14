package de.dafuqs.spectrum.blocks.farming;

import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;

public class TilledShaleClayBlock extends ImmutableFarmlandBlock {
	public TilledShaleClayBlock(Properties settings, BlockState bareState) {
		super(settings, bareState);
	}

//	@Override
//	public MapCodec<? extends TilledShaleClayBlock> getCodec() {
//		//TODO: Make the codec
//		return CODEC;
//	}

	@Override
	public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		entity.causeFallDamage(fallDistance, 2.0F, world.damageSources().fall());
	}

}
