package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import org.jetbrains.annotations.*;

public class SleepStatusEffect extends SpectrumStatusEffect {

    private final boolean scales;

    public SleepStatusEffect(StatusEffectCategory category, int color, boolean scales) {
        super(category, color);
        this.scales = scales;
    }
    
    // oh my god
    // TODO: can the tag check be implemented into the entities base attribute modifier somehow?
    public static float getSleepResistance(@Nullable StatusEffectInstance sleepEffect, LivingEntity entity) {

        var type = entity.getType();
        
        if (sleepEffect == null || type.isIn(SpectrumEntityTypeTags.SOULLESS))
            return Float.MAX_VALUE;

        float scaling;
        if (entity instanceof PlayerEntity player && player.getWorld().isClient()) {
            scaling = (float) MiscPlayerDataComponent.get(player).getLastSyncedSleepPotency();
        }
        else {
            scaling = (float) entity.getAttributeValue(SpectrumEntityAttributes.MENTAL_PRESENCE);
        }

        if (type.isIn(SpectrumEntityTypeTags.SLEEP_WEAK)) {
            scaling /= 3F;
        } else if (type.isIn(SpectrumEntityTypeTags.SLEEP_RESISTANT)) {
            scaling *= 2.0F;
        } else if (isImmuneish(entity)) {
            scaling *= 10F;
        }
        
        return scaling;
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

    /**
     * @return -1 = false
     */
    public static float getGeneralSleepResistanceIfEntityHasSoporificEffect(LivingEntity entity) {
        if (SpectrumStatusEffectTags.hasEffectWithTag(entity, SpectrumStatusEffectTags.SOPORIFIC)) {
            return getSleepResistance(entity.getStatusEffect(getStrongestSleepEffect(entity)), entity);
        }
        return -1F;
    }

    /**
     * @return -1 = false
     */
    public static float getSleepScaling(LivingEntity entity) {
        var potency = getGeneralSleepResistanceIfEntityHasSoporificEffect(entity);
        
        if (potency == -1 || potency >= 1)
            return -1;

        // Converts a range of [0, infinity] to [0, 2]
        // Also accounts for a smaller resist meaning stronger sleep
        return 2 * (float) Math.pow(1 - potency, 2);
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
