package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.*;
import org.jetbrains.annotations.*;

public class SleepStatusEffect extends SpectrumStatusEffect {

    private final boolean scales;

    public SleepStatusEffect(StatusEffectCategory category, int color, boolean scales) {
        super(category, color);
        this.scales = scales;
    }
    
    // oh my god
    // TODO: can the tag check be implemented into the entities base attribute modifier somehow?
    public static float getSleepEffectStrength(@Nullable StatusEffectInstance sleepEffect, LivingEntity entity) {
        var type = entity.getType();
        
        if (sleepEffect == null || type.isIn(SpectrumEntityTypeTags.SOULLESS))
            return 0;
        
        float resistance = (float) entity.getAttributeValue(SpectrumEntityAttributes.INDUCED_SLEEP_VULNERABILITY);
        if (type.isIn(SpectrumEntityTypeTags.SLEEP_WEAK)) {
            resistance *= 3F;
        } else if (type.isIn(SpectrumEntityTypeTags.SLEEP_RESISTANT)) {
            resistance /= 2.0F;
        } else if (isImmuneish(entity)) {
            resistance /= 10F;
        }
        
        return Math.max(resistance - 1, 0);
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
    
    public static float getGeneralSleepStrengthIfEntityHasSoporificEffect(LivingEntity entity) {
        if (SpectrumStatusEffectTags.hasEffectWithTag(entity, SpectrumStatusEffectTags.SOPORIFIC)) {
            return getSleepEffectStrength(entity.getStatusEffect(getStrongestSleepEffect(entity)), entity);
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

    // Sleep effects don't scale except for uh, calming ufck
    @Override
    public double adjustModifierAmount(int amplifier, EntityAttributeModifier modifier) {
        if (scales)
            return super.adjustModifierAmount(amplifier, modifier);
        return modifier.getValue();
    }
}
