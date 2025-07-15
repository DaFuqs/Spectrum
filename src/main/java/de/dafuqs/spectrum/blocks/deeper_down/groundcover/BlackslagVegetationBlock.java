package de.dafuqs.spectrum.blocks.deeper_down.groundcover;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.lighting.*;

public class BlackslagVegetationBlock extends SnowyDirtBlock {
	
	public static final MapCodec<BlackslagVegetationBlock> CODEC = simpleCodec(BlackslagVegetationBlock::new);
	
	public BlackslagVegetationBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends BlackslagVegetationBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (!canSurvive(state, world, pos)) {
			world.setBlockAndUpdate(pos, SpectrumBlocks.BLACKSLAG.defaultBlockState());
		}
	}
	
	private static boolean canSurvive(BlockState state, BlockGetter world, BlockPos pos) {
		BlockPos blockPos = pos.above();
		BlockState blockState = world.getBlockState(blockPos);
		if (blockState.is(Blocks.SNOW) && blockState.getValue(SnowLayerBlock.LAYERS) == 1) {
			return true;
		} else if (blockState.getFluidState().getAmount() == 8) {
			return false;
		} else {
			int light = LightEngine.getLightBlockInto(world, state, pos, blockState, blockPos, Direction.UP, blockState.getLightBlock(world, blockPos));
			return light < world.getMaxLightLevel();
		}
	}
	
}