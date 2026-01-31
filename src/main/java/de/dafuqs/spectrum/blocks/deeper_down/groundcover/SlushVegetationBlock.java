package de.dafuqs.spectrum.blocks.deeper_down.groundcover;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.lighting.*;
import net.neoforged.neoforge.common.*;
import org.jetbrains.annotations.*;

public class SlushVegetationBlock extends SnowyDirtBlock {
	
	public SlushVegetationBlock(Properties settings) {
		super(settings);
	}
	
	public static final MapCodec<SlushVegetationBlock> CODEC = simpleCodec(SlushVegetationBlock::new);
	
	@Override
	public MapCodec<? extends SlushVegetationBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (!canSurvive(state, world, pos)) {
			world.setBlockAndUpdate(pos, SpectrumBlocks.SLUSH.get().defaultBlockState());
		}
	}
	
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockPos blockPos = pos.above();
		BlockState blockState = level.getBlockState(blockPos);
		if (blockState.is(Blocks.SNOW) && blockState.getValue(SnowLayerBlock.LAYERS) == 1) {
			return true;
		} else if (blockState.getFluidState().getAmount() == 8) {
			return false;
		} else {
			int light = LightEngine.getLightBlockInto(level, state, pos, blockState, blockPos, Direction.UP, blockState.getLightBlock(level, blockPos));
			return light < level.getMaxLightLevel();
		}
	}
	
	@Override
	public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
		ItemStack handStack = context.getItemInHand();
		if (itemAbility == ItemAbilities.HOE_TILL && handStack.canPerformAction(itemAbility) && HoeItem.onlyIfAirAbove(context)) {
			return SpectrumBlocks.TILLED_SLUSH.get().defaultBlockState();
		}
		return null;
	}
	
}
