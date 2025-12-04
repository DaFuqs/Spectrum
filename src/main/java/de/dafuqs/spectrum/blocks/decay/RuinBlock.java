package de.dafuqs.spectrum.blocks.decay;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class RuinBlock extends DecayBlock {
	
	public static final MapCodec<RuinBlock> CODEC = simpleCodec(RuinBlock::new);
	
	public RuinBlock(Properties settings) {
		super(settings, SpectrumCommon.CONFIG.RuinDecayTickRate, SpectrumCommon.CONFIG.RuinCanDestroyBlockEntities, 3, 5F, UniformInt.of(2, 3));
		registerDefaultState(getStateDefinition().any().setValue(CONVERSION, Conversion.NONE));
	}

	@Override
	public MapCodec<? extends RuinBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.setPlacedBy(world, pos, state, placer, itemStack);
		
		if (!world.isClientSide) {
			world.playSound(null, pos, SpectrumSoundEvents.RUIN_PLACED, SoundSource.BLOCKS, 0.5F, 1.0F);
		} else {
			RandomSource random = world.getRandom();
			world.addParticle(ParticleTypes.EXPLOSION, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F), 0.05, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F));
			world.addParticle(ParticleTypes.EXPLOSION_EMITTER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F), 0.05, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F));
			
			for (int i = 0; i < 40; i++) {
				world.addParticle(ColoredCraftingParticleEffect.GRAY, pos.getX() - 0.5 + random.nextFloat() * 2, pos.getY() + random.nextFloat(), pos.getZ() - 0.5 + random.nextFloat() * 2, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F), 0.05, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F));
			}
		}
	}
	
	@Override
	protected @Nullable BlockState getSpreadState(BlockState stateToSpreadFrom, BlockState stateToSpreadTo, Level world, BlockPos stateToSpreadToPos) {
		if (stateToSpreadTo.getCollisionShape(world, stateToSpreadToPos).isEmpty() || stateToSpreadTo.is(SpectrumBlockTags.RUIN_SAFE)) {
			return null;
		}
		if (SpectrumDimensionTags.is(world, SpectrumDimensionTags.RUIN_SAFE)) {
			return null;
		}
		
		if (stateToSpreadTo.is(SpectrumBlockTags.RUIN_SPECIAL_CONVERSIONS)) {
			return this.defaultBlockState().setValue(CONVERSION, Conversion.SPECIAL);
		} else if (stateToSpreadTo.is(SpectrumBlockTags.RUIN_CONVERSIONS)) {
			// Protect the end portal to not lock players in the dim
			if (world.dimension().equals(Level.END) && Math.abs(stateToSpreadToPos.getX()) < 8 && Math.abs(stateToSpreadToPos.getZ()) < 8) {
				return null;
			}
			
			return this.defaultBlockState().setValue(CONVERSION, Conversion.DEFAULT);
		}
		return stateToSpreadFrom.setValue(CONVERSION, Conversion.NONE);
	}
	
	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (player instanceof ServerPlayer serverPlayer && shouldCreatePortalFacingUp(level, pos, state).isPresent()) {
			SpectrumAdvancementCriteria.DEEPER_DOWN_PORTAL_OPENING.trigger(serverPlayer);
		}
		return super.playerWillDestroy(level, pos, state, player);
	}
	
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
		super.onRemove(state, level, pos, newState, moved);
		
		if (newState.isAir()) {
			Optional<Boolean> shouldCreatePortalFacingUp = shouldCreatePortalFacingUp(level, pos, state);
			if (shouldCreatePortalFacingUp.isPresent()) {
				level.setBlockAndUpdate(pos, SpectrumBlocks.DEEPER_DOWN_PORTAL.defaultBlockState().setValue(DeeperDownPortalBlock.FACING_UP, shouldCreatePortalFacingUp.get()));
			}
		}
	}
	
	protected Optional<Boolean> shouldCreatePortalFacingUp(Level level, BlockPos pos, BlockState state) {
		DecayBlock.Conversion conversion = state.getValue(RuinBlock.CONVERSION);
		if (level.dimension() == Level.NETHER) {
			if (pos.getY() == level.getMinBuildHeight() + level.dimensionType().logicalHeight() - 1) { // Attempt to match the nether ceiling. Tricky...
				return Optional.of(Boolean.TRUE);
			} else if (pos.getY() == level.getMinBuildHeight()) {
				return Optional.of(Boolean.FALSE);
			}
		} else if (conversion == Conversion.SPECIAL || level.dimension() == Level.OVERWORLD && pos.getY() == level.getMinBuildHeight()) {
			return Optional.of(Boolean.FALSE);
		} else if (level.dimension() == SpectrumDimensions.DIMENSION_KEY && pos.getY() == level.getMaxBuildHeight() - 1) { // highest layer cannot be built on
			return Optional.of(Boolean.TRUE);
		}
		return Optional.empty();
	}
	
	
}
