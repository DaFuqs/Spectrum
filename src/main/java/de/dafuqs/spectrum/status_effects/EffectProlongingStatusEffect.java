package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.effect.*;

public class EffectProlongingStatusEffect extends MobEffect {
	
	public static final float ADDITIONAL_EFFECT_DURATION_MODIFIER_PER_LEVEL = 0.25F;
	
	public EffectProlongingStatusEffect(MobEffectCategory category, int color) {
		super(category, color);
	}
	
	public static boolean canBeExtended(Holder<MobEffect> statusEffect) {
		return !statusEffect.is(SpectrumStatusEffectTags.NO_DURATION_EXTENSION);
	}
	
	public static int getExtendedDuration(int originalDuration, int prolongingAmplifier) {
		return (int) (originalDuration * (1 + ADDITIONAL_EFFECT_DURATION_MODIFIER_PER_LEVEL * prolongingAmplifier));
	}
	
}
