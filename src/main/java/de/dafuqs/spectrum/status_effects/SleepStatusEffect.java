package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import org.jspecify.annotations.Nullable;

public class SleepStatusEffect extends MobEffect {

    private final boolean scales;
	
	public SleepStatusEffect(MobEffectCategory category, int color, boolean scales) {
        super(category, color);
        this.scales = scales;
    }
	
    // oh my god
    // TODO: can the tag check be implemented into the entities base attribute modifier somehow?
	public static float getSleepResistance(@Nullable MobEffectInstance sleepEffect, LivingEntity entity) {

        var type = entity.getType();
		
		if (sleepEffect == null || type.is(SpectrumEntityTypeTags.SOULLESS))
            return Float.MAX_VALUE;

        float scaling;
		if (entity instanceof Player player && player.level().isClientSide()) {
            scaling = (float) MiscPlayerDataComponent.get(player).getLastSyncedSleepPotency();
        }
        else {
            scaling = (float) entity.getAttributeValue(SpectrumEntityAttributes.MENTAL_PRESENCE);
        }
		
		if (type.is(SpectrumEntityTypeTags.SLEEP_WEAK)) {
            scaling /= 3F;
		} else if (type.is(SpectrumEntityTypeTags.SLEEP_RESISTANT)) {
            scaling *= 2.0F;
        } else if (isImmuneish(entity)) {
            scaling *= 10F;
        }
        
        return scaling;
    }
    
    // TODO: can the tag check be implemented into the entities base attribute modifier somehow?
    public static boolean isImmuneish(LivingEntity entity) {
		if (entity.hasEffect(SpectrumStatusEffects.FRENZY))
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
        if (!isConstruct(entity.getType()) && SpectrumStatusEffectTags.hasEffectWithTag(entity, SpectrumStatusEffectTags.SOPORIFIC)) {
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
		if (entity.hasEffect(SpectrumStatusEffects.FATAL_SLUMBER)) {
            return SpectrumStatusEffects.FATAL_SLUMBER;
        } else if (entity.hasEffect(SpectrumStatusEffects.ETERNAL_SLUMBER)) {
            return SpectrumStatusEffects.ETERNAL_SLUMBER;
        } else if (entity.hasEffect(SpectrumStatusEffects.SOMNOLENCE)) {
            return SpectrumStatusEffects.SOMNOLENCE;
        }
        return null;
    }

    // Sleep effects don't scale except for uh, calming ufck
	//TODO verify that you can't more than one level of eternal or fatal slumber
//    @Override
//    public double adjustModifierAmount(int amplifier, EntityAttributeModifier modifier) {
//        if (scales)
//            return super.adjustModifierAmount(amplifier, modifier);
//        return modifier.getValue();
//    }
}
