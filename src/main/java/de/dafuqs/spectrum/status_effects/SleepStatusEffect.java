package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import org.jetbrains.annotations.*;

public class SleepStatusEffect extends SpectrumStatusEffect {
    
    public SleepStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
    
    // oh my god
    // TODO: can the tag check be implemented into the entities base attribute modifier somehow?
    public static float getSleepResistanceModifier(@Nullable StatusEffectInstance strongestSleepEffect, LivingEntity entity) {
        var type = entity.getType();
        
        if (strongestSleepEffect == null || type.isIn(SpectrumEntityTypeTags.SOULLESS))
            return Float.MAX_VALUE;
        
        float resistance = (float) entity.getAttributeValue(SpectrumEntityAttributes.INDUCED_SLEEP_RESISTANCE);
        if (type.isIn(SpectrumEntityTypeTags.SLEEP_WEAK)) {
            resistance *= 0.4F;
        } else if (type.isIn(SpectrumEntityTypeTags.SLEEP_RESISTANT)) {
            resistance *= 2.0F;
        } else if (isImmuneish(entity)) {
            resistance *= 10F;
        }
        
        return Math.max(resistance, 0);
    }
    
    // TODO: can the tag check be implemented into the entities base attribute modifier somehow?
    public static boolean isImmuneish(LivingEntity entity) {
        if (entity.hasStatusEffect(SpectrumStatusEffects.FRENZY))
            return true;
        
        var type = entity.getType();
        if (type.isIn(SpectrumEntityTypeTags.SLEEP_WEAK))
            return false;
        
        return type.isIn(SpectrumEntityTypeTags.SLEEP_IMMUNEISH);
    }
    
    public static float getGeneralSleepResistanceIfEntityHasSoporificEffect(LivingEntity entity) {
        if (SpectrumStatusEffectTags.hasEffectWithTag(entity, SpectrumStatusEffectTags.SOPORIFIC)) {
            return getSleepResistanceModifier(entity.getStatusEffect(SpectrumStatusEffects.ETERNAL_SLUMBER), entity);
        }
        return -1F;
    }
    
    public static @Nullable StatusEffect getStrongestSleepEffect(LivingEntity entity) {
        if (entity.hasStatusEffect(SpectrumStatusEffects.FATAL_SLUMBER)) {
            return SpectrumStatusEffects.FATAL_SLUMBER;
        }
        else if (entity.hasStatusEffect(SpectrumStatusEffects.ETERNAL_SLUMBER)) {
            return SpectrumStatusEffects.ETERNAL_SLUMBER;
        }
        else if (entity.hasStatusEffect(SpectrumStatusEffects.SOMNOLENCE)) {
            return SpectrumStatusEffects.SOMNOLENCE;
        }
        return null;
    }
    
}
