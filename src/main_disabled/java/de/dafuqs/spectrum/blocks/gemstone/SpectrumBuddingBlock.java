package de.dafuqs.spectrum.blocks.gemstone;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;

public class SpectrumBuddingBlock extends SpectrumGemstoneBlock {

	private static final Direction[] DIRECTIONS = Direction.values();
	private final Block smallBlock;
	private final Block mediumBlock;
	private final Block largeBlock;
	private final Block clusterBlock;
	
	public SpectrumBuddingBlock(Properties settings, Block smallBlock, Block mediumBlock, Block largeBlock, Block clusterBlock, SoundEvent hitSoundEvent, SoundEvent chimeSoundEvent) {
		super(settings, hitSoundEvent, chimeSoundEvent);
		
		this.smallBlock = smallBlock;
		this.mediumBlock = mediumBlock;
		this.largeBlock = largeBlock;
		this.clusterBlock = clusterBlock;
	}

	@Override
	public MapCodec<? extends SpectrumBuddingBlock> codec() {
		//TODO: Make the codec
		return null;
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (random.nextInt(5) == 0) {
			Direction direction = UPDATE_SHAPE_ORDER[random.nextInt(UPDATE_SHAPE_ORDER.length)];
			BlockPos blockPos = pos.relative(direction);
			BlockState blockState = world.getBlockState(blockPos);
			Block block = null;
			if (BuddingAmethystBlock.canClusterGrowAtState(blockState)) {
				block = smallBlock;
			} else if (blockState.is(smallBlock) && blockState.getValue(AmethystClusterBlock.FACING) == direction) {
				block = mediumBlock;
			} else if (blockState.is(mediumBlock) && blockState.getValue(AmethystClusterBlock.FACING) == direction) {
				block = largeBlock;
			} else if (blockState.is(largeBlock) && blockState.getValue(AmethystClusterBlock.FACING) == direction) {
				block = clusterBlock;
			}
			
			if (block != null) {
				world.setBlockAndUpdate(blockPos, block.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction).setValue(AmethystClusterBlock.WATERLOGGED, blockState.getFluidState().getType() == Fluids.WATER));
			}
		}
	}
	
}
