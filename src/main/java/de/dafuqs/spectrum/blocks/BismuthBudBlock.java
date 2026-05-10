package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.blocks.crystallarieum.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.Nullable;

public class BismuthBudBlock extends SpectrumClusterBlock {
	
	public static final int GROWTH_CHECK_RADIUS = 3;
	public static final int GROWTH_CHECK_TRIES = 5;
	public static final TagKey<Block> CONSUMED_TAG_TO_GROW = BlockTags.BEACON_BASE_BLOCKS;
	public static final BlockState CONSUMED_TARGET_STATE = Blocks.COBBLESTONE.defaultBlockState();
	
	public final AmethystClusterBlock grownBlock;
	
	public BismuthBudBlock(Properties settings, GrowthStage growthStage, @Nullable AmethystClusterBlock grownBlock) {
		super(settings, growthStage);
		this.grownBlock = grownBlock;
	}

//	@Override
//	public MapCodec<? extends BismuthBudBlock> getCodec() {
//		//TODO: Make the codec
//		return null;
//	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		super.randomTick(state, world, pos, random);
		if (!world.isClientSide() && grownBlock != null && searchAndConsumeBlock(world, pos, GROWTH_CHECK_RADIUS, CONSUMED_TAG_TO_GROW, CONSUMED_TARGET_STATE, GROWTH_CHECK_TRIES, random)) {
			BlockState newState = grownBlock.defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(WATERLOGGED, state.getValue(WATERLOGGED));
			world.setBlockAndUpdate(pos, newState);
			world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.CHAIN_PLACE, SoundSource.BLOCKS, 0.8F, 0.9F + random.nextFloat() * 0.2F);
			
			Vec3 sourcePos = new Vec3(pos.getX() + 0.5D, pos.getY() + growthStage.height / 16.0, pos.getZ() + 0.5D);
			Vec3 randomOffset = new Vec3(0.25, growthStage.height / 32.0, 0.25);
			Vec3 randomVelocity = new Vec3(0.1, 0.1, 0.1);
			PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity(world, sourcePos, ColoredCraftingParticleEffect.WHITE, 2, randomOffset, randomVelocity);
			PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity(world, sourcePos, ColoredCraftingParticleEffect.LIME, 2, randomOffset, randomVelocity);
			PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity(world, sourcePos, ColoredCraftingParticleEffect.PURPLE, 2, randomOffset, randomVelocity);
			PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity(world, sourcePos, ColoredCraftingParticleEffect.ORANGE, 2, randomOffset, randomVelocity);
		}
	}
	
	public static boolean searchAndConsumeBlock(Level world, BlockPos pos, int radius, TagKey<Block> tagKey, BlockState targetState, int tries, RandomSource random) {
		for (int i = 0; i < tries; i++) {
			BlockPos offsetPos = pos.offset(radius - random.nextInt(1 + radius + radius), radius - random.nextInt(1 + radius + radius), radius - random.nextInt(1 + radius + radius));
			BlockState offsetState = world.getBlockState(offsetPos);
			if (offsetState.is(tagKey)) {
				world.setBlockAndUpdate(offsetPos, targetState);
				world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), offsetState.getSoundType().getBreakSound(), SoundSource.BLOCKS, 0.8F, 0.9F + random.nextFloat() * 0.2F);
				return true;
			}
		}
		return false;
	}
	
}
