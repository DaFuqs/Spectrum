package de.dafuqs.spectrum.blocks.decay;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
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
import org.jetbrains.annotations.*;

public class FadingBlock extends DecayBlock {
	
	public static final MapCodec<FadingBlock> CODEC = simpleCodec(FadingBlock::new);
	
	public FadingBlock(Properties settings) {
		super(settings, SpectrumConfig.CONFIG.FadingSpreadChanceOnRandomTick.get(), SpectrumConfig.CONFIG.FadingCanDestroyBlockEntities.get(), 1, 1F, UniformInt.of(0, 1));
	}
	
	@Override
	public MapCodec<? extends FadingBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.setPlacedBy(world, pos, state, placer, itemStack);
		
		if (!world.isClientSide) {
			world.playSound(null, pos, SpectrumSoundEvents.FADING_PLACED, SoundSource.BLOCKS, 0.5F, 1.0F);
		} else {
			RandomSource random = world.getRandom();
			world.addParticle(ParticleTypes.POOF, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F), 0.05, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F));
			
			for (int i = 0; i < 10; i++) {
				world.addParticle(ColoredCraftingParticleEffect.GRAY, pos.getX() + random.nextFloat(), pos.getY() + 1, pos.getZ() + random.nextFloat(), ((-1.0F + random.nextFloat() * 2.0F) / 12.0F), 0.05, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F));
			}
		}
	}
	
	@Override
	protected @Nullable BlockState getSpreadState(BlockState stateToSpreadFrom, BlockState stateToSpreadTo, Level world, BlockPos stateToSpreadToPos) {
		if (stateToSpreadTo.is(SpectrumBlockTags.FADING_SPECIAL_CONVERSIONS)) {
			return stateToSpreadFrom.setValue(CONVERSION, Conversion.SPECIAL);
		} else if (stateToSpreadTo.is(SpectrumBlockTags.FADING_CONVERSIONS)) {
			return stateToSpreadFrom.setValue(CONVERSION, Conversion.DEFAULT);
		}
		return null;
	}
	
}
