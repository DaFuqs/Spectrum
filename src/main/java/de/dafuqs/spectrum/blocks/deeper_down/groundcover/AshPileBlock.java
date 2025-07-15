package de.dafuqs.spectrum.blocks.deeper_down.groundcover;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

public class AshPileBlock extends SnowLayerBlock {
	
	public static final MapCodec<AshPileBlock> CODEC = simpleCodec(AshPileBlock::new);
	
	public AshPileBlock(Properties settings) {
		super(settings);
	}

//	@Override
//	public MapCodec<? extends AshPileBlock> getCodec() {
//		//TODO: Make the codec
//		return CODEC;
//	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos.below());
		if (blockState.is(SpectrumBlocks.ASH))
			return true;
		return super.canSurvive(state, world, pos);
	}
	
	@Override
	public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
	}
}
