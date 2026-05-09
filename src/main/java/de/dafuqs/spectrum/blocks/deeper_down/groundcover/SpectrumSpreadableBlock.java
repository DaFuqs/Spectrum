package de.dafuqs.spectrum.blocks.deeper_down.groundcover;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.lighting.*;
import javax.annotation.*;

// TODO: unused????
public class SpectrumSpreadableBlock extends SnowyDirtBlock {
	
	protected final @Nullable Block blockAbleToSpreadTo;
	private final BlockState deadState;
	
	public SpectrumSpreadableBlock(Properties settings, @Nullable Block blockAbleToSpreadTo, BlockState deadState) {
		super(settings);
		this.blockAbleToSpreadTo = blockAbleToSpreadTo;
		this.deadState = deadState;
	}
	
	@Override
	public MapCodec<? extends SpectrumSpreadableBlock> codec() {
		//TODO: Make the codec
		return null;
	}
	
	private static boolean canSpread(BlockState state, LevelReader world, BlockPos pos) {
		BlockPos blockPos = pos.above();
		return canSurviveForSpread(state, world, pos) && !world.getFluidState(blockPos).is(FluidTags.WATER);
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (!canSurviveForSpread(state, world, pos)) {
			world.setBlockAndUpdate(pos, deadState);
		} else {
			if (world.getMaxLocalRawBrightness(pos.above()) >= 9) {
				BlockState blockState = this.defaultBlockState();
				
				for (int i = 0; i < 4; ++i) {
					BlockPos blockPos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
					if (blockAbleToSpreadTo != null && world.getBlockState(blockPos).is(blockAbleToSpreadTo) && canSpread(blockState, world, blockPos)) {
						world.setBlockAndUpdate(blockPos, blockState.setValue(SNOWY, world.getBlockState(blockPos.above()).is(Blocks.SNOW)));
					}
				}
			}
		}
	}
	
	private static boolean canSurviveForSpread(BlockState state, LevelReader world, BlockPos pos) {
		BlockPos blockPos = pos.above();
		BlockState blockState = world.getBlockState(blockPos);
		if (blockState.is(Blocks.SNOW) && blockState.getValue(SnowLayerBlock.LAYERS) == 1) {
			return true;
		} else if (blockState.getFluidState().getAmount() == 8) {
			return false;
		} else {
			int i = LightEngine.getLightBlockInto(world, state, pos, blockState, blockPos, Direction.UP, blockState.getLightBlock(world, blockPos));
			return i < world.getMaxLightLevel();
		}
	}
	
}