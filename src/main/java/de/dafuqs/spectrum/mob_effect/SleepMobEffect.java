package de.dafuqs.spectrum.mob_effect;

import de.dafuqs.spectrum.attachment_types.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.*;
import net.neoforged.neoforge.common.*;
import org.jspecify.annotations.*;

import java.util.*;

public class SleepMobEffect extends MobEffect {
	
	private final boolean scales;
	
	public SleepMobEffect(MobEffectCategory category, int color, boolean scales) {
		super(category, color);
		this.scales = scales;
	}
	
	// oh my god
	public static float getSleepResistance(@Nullable MobEffectInstance sleepEffect, LivingEntity entity) {
		var type = entity.getType();
		
		if (sleepEffect == null || type.is(SpectrumEntityTypeTags.SOULLESS))
			return Float.MAX_VALUE;
		
		float scaling = 1.0F;
		if (type.is(SpectrumEntityTypeTags.SLEEP_WEAK)) {
			scaling /= 3F;
		} else if (type.is(SpectrumEntityTypeTags.SLEEP_RESISTANT)) {
			scaling *= 2.0F;
		} else if (isImmuneish(entity)) {
			scaling *= 10F;
		}
		
		return scaling;
	}
	
	public static boolean isImmuneish(LivingEntity entity) {
		if (entity.hasEffect(SpectrumMobEffects.FRENZY))
			return true;
		
		var type = entity.getType();
		if (type.is(SpectrumEntityTypeTags.SLEEP_WEAK))
			return false;
		
		return type.is(SpectrumEntityTypeTags.SLEEP_IMMUNEISH) || isConstruct(type);
	}
	
	/**
	 * @return -1 = false
	 */
	public static float getGeneralSleepResistanceIfEntityHasSoporificEffect(LivingEntity entity) {
		if (!isConstruct(entity.getType()) && SpectrumMobEffectTags.has(entity, SpectrumMobEffectTags.SOPORIFIC)) {
			return getSleepResistance(entity.getEffect(getStrongestSleepEffect(entity)), entity);
		}
		return -1F;
	}
	
	/**
	 * @return -1 = false
	 */
	public static float getSleepScaling(@Nullable LivingEntity entity) {
		if (entity == null) return -1;
		var potency = getGeneralSleepResistanceIfEntityHasSoporificEffect(entity);
		
		if (potency == -1 || potency >= 1)
			return -1;
		
		// Converts a range of [0, infinity] to [0, 2]
		// Also accounts for a smaller resist meaning stronger sleep
		return 2 * (float) Math.pow(1 - potency, 2);
	}
	
	private static boolean isConstruct(EntityType<?> type) {
		return type.is(SpectrumEntityTypeTags.SOULLESS);
	}
	
	public static @Nullable Holder<MobEffect> getStrongestSleepEffect(LivingEntity entity) {
		if (entity.hasEffect(SpectrumMobEffects.FATAL_SLUMBER)) {
			return SpectrumMobEffects.FATAL_SLUMBER;
		} else if (entity.hasEffect(SpectrumMobEffects.ETERNAL_SLUMBER)) {
			return SpectrumMobEffects.ETERNAL_SLUMBER;
		} else if (entity.hasEffect(SpectrumMobEffects.SOMNOLENCE)) {
			return SpectrumMobEffects.SOMNOLENCE;
		}
		return null;
	}
	
	@Override
	public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
		Holder<MobEffect> holder = effectInstance.getEffect();
		
		if (holder.is(SpectrumMobEffects.SOMNOLENCE) || holder.is(SpectrumMobEffects.CALMING)) {
			cures.add(SpectrumEffectCures.SEDATIVES);
		}
	}
	
	// Sleep effects don't scale except for calming
	@Override
	public void addAttributeModifiers(AttributeMap attributeMap, int amplifier) {
		super.addAttributeModifiers(attributeMap, scales ? amplifier : 0);
	}
	
}
