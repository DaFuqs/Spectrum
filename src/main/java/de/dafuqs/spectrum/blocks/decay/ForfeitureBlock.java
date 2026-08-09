package de.dafuqs.spectrum.blocks.decay;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import org.jspecify.annotations.*;

public class ForfeitureBlock extends DecayBlock {
	
	public static final MapCodec<ForfeitureBlock> CODEC = simpleCodec(ForfeitureBlock::new);
	
	// A special version of ruin that spreads indefinitely, even through air.
	// There are no brakes on the Forfeiture train
	public ForfeitureBlock(Properties settings) {
		super(settings, SpectrumConfig.CONFIG.ForfeitureDecayTickRate.get(), SpectrumConfig.CONFIG.ForfeitureCanDestroyBlockEntities.get(), 4, 7.5F, UniformInt.of(3, 4));
		registerDefaultState(getStateDefinition().any().setValue(CONVERSION, Conversion.NONE));
	}
	
	@Override
	public MapCodec<? extends ForfeitureBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.setPlacedBy(world, pos, state, placer, itemStack);
		
		if (!world.isClientSide()) {
			world.playSound(null, pos, SpectrumSoundEvents.FORFEITURE_PLACED, SoundSource.BLOCKS, 0.5F, 1.0F);
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
		if (stateToSpreadTo.is(SpectrumBlockTags.FORFEITURE_SAFE)) {
			return null;
		}
		
		if (stateToSpreadTo.is(SpectrumBlockTags.FORFEITURE_SPECIAL_CONVERSIONS)) {
			return this.defaultBlockState().setValue(CONVERSION, Conversion.SPECIAL);
		} else if (stateToSpreadTo.is(SpectrumBlockTags.FORFEITURE_CONVERSIONS)) {
			// Protect the end portal to not lock players in the dim
			if (world.dimension().equals(Level.END) && Math.abs(stateToSpreadToPos.getX()) < 8 && Math.abs(stateToSpreadToPos.getZ()) < 8) {
				return null;
			}
			
			return this.defaultBlockState().setValue(CONVERSION, Conversion.DEFAULT);
		}
		return stateToSpreadFrom.setValue(CONVERSION, Conversion.NONE);
	}
	
}
