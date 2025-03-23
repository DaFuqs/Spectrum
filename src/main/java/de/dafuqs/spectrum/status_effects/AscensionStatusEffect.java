package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.*;
import net.minecraft.server.network.*;
import net.minecraft.world.*;

public class AscensionStatusEffect extends SpectrumStatusEffect {
	
	public static final int MUSIC_DURATION_TICKS = 288 * 20;
	public static final int MUSIC_INTRO_TICKS = 56 * 20; // 56 seconds
	
	public AscensionStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
		super(statusEffectCategory, color);
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		World world = entity.getWorld();
		if (world.isClient) {
			ParticleHelper.playParticleWithPatternAndVelocityClient(entity.getWorld(), entity.getPos(), SpectrumParticleTypes.WHITE_SPARKLE_RISING, VectorPattern.EIGHT, 0.2);
		}
	}
	
	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return duration % 4 == 0;
	}
	
	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		super.onApplied(entity, attributes, amplifier);
		if (entity instanceof ServerPlayerEntity player) {
			SpectrumS2CPacketSender.playAscensionAppliedEffects(player);
		}
	}
	
	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		super.onRemoved(entity, attributes, amplifier);
		
		// only apply divinity if ascension ran out
		// does not apply when curing the effect by other means, such as drinking milk
		// which would trigger a ConcurrentModificationException
		StatusEffectInstance instance = entity.getStatusEffect(this);
		if (instance == null) { // null if the effect ran out; non-null for milk and stuff
			entity.addStatusEffect(new StatusEffectInstance(SpectrumStatusEffects.DIVINITY, MUSIC_DURATION_TICKS - MUSIC_INTRO_TICKS, DivinityStatusEffect.ASCENSION_AMPLIFIER));
		}
	}
	
}