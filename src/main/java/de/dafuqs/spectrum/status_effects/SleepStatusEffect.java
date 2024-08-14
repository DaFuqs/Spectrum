package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SleepStatusEffect extends SpectrumStatusEffect {

    private final float baseStrength;

    public SleepStatusEffect(StatusEffectCategory category, float baseStrength, int color) {
        super(category, color);
        this.baseStrength = baseStrength;
    }
    
    // oh my god
    public static float getSleepVulnerability(@Nullable StatusEffectInstance instance, LivingEntity entity) {
        var type = entity.getType();

        if (instance == null || !(instance.getEffectType() instanceof SleepStatusEffect) || type.isIn(SpectrumEntityTypeTags.SOULLESS))
            return 0;

        var vulnerability = ((SleepStatusEffect) instance.getEffectType()).baseStrength * (instance.getAmplifier() + 1);
        if (entity.hasStatusEffect(SpectrumStatusEffects.IMMUNITY))
            vulnerability *= 0.334F;

        if (entity.hasStatusEffect(SpectrumStatusEffects.DIVINITY))
            vulnerability *= 0.875F;

        if (entity instanceof PlayerEntity player) {
            return vulnerability * getPlayerMod(player);
        } else if (type.isIn(SpectrumEntityTypeTags.SLEEP_WEAK)) {
            vulnerability *= 2.5F;
        } else if (type.isIn(SpectrumEntityTypeTags.SLEEP_RESISTANT)) {
            vulnerability *= 0.5F;
        }
        else if (isImmuneish(entity)) {
            vulnerability *= 0.1F;
        }

        if (entity.hasStatusEffect(SpectrumStatusEffects.CALMING))
            vulnerability *= 2;

        return Math.max(vulnerability, 0);
    }

    /**
     * Get vulnerability based on the strongest sleep effect
     */
    // TODO: Why is that not an attribute? It should be one
    public static float getGeneralSleepVulnerability(LivingEntity entity) {
        if (entity.hasStatusEffect(SpectrumStatusEffects.FATAL_SLUMBER)) {
            return getSleepVulnerability(entity.getStatusEffect(SpectrumStatusEffects.FATAL_SLUMBER), entity) * 2F;
        }
        else if (entity.hasStatusEffect(SpectrumStatusEffects.ETERNAL_SLUMBER)) {
            return getSleepVulnerability(entity.getStatusEffect(SpectrumStatusEffects.ETERNAL_SLUMBER), entity);
        }
        else if (entity.hasStatusEffect(SpectrumStatusEffects.SOMNOLENCE)) {
            return getSleepVulnerability(entity.getStatusEffect(SpectrumStatusEffects.SOMNOLENCE), entity);
        }
        return 0F;
    }
    
    public static StatusEffect getStrongestSleepEffect(LivingEntity entity) {
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
    
    // wtf
    public static float getPlayerMod(PlayerEntity player) {
        float mod = 1F;

        var trinkets = TrinketsApi.getTrinketComponent(player).map(TrinketComponent::getAllEquipped).orElse(new ArrayList<>());
        for (Pair<SlotReference, ItemStack> trinketPair : trinkets) {
            mod *= getModFor(player, trinketPair.getRight());
        }

        for (Hand hand : Hand.values()) {
            var handStack = player.getStackInHand(hand);
            mod *= getModFor(player, handStack);
        }

        for (ItemStack armor : player.getInventory().armor) {
            mod *= getModFor(player, armor);
        }

        return mod;
    }
    
    // oh no
    private static float getModFor(PlayerEntity player, ItemStack stack) {
        if (stack.getItem() instanceof SleepStatusAffectingItem sleepItem) {
            return 1 - sleepItem.getSleepResistance(player, stack);
        }
        return 1F;
    }
    
    // (⊙﹏⊙)
    public static boolean isImmuneish(LivingEntity entity) {
        if (entity.hasStatusEffect(SpectrumStatusEffects.FRENZY))
            return true;

        var type = entity.getType();
        if (type.isIn(SpectrumEntityTypeTags.SLEEP_WEAK))
            return false;
        
        return type.isIn(SpectrumEntityTypeTags.SLEEP_IMMUNEISH);
    }
    
    public static boolean hasSleepEffect(LivingEntity livingEntity) {
        for (StatusEffect statusEffect : livingEntity.getActiveStatusEffects().keySet()) {
            if (SpectrumStatusEffectTags.isIn(SpectrumStatusEffectTags.SOPORIFIC, statusEffect)) {
                return true;
            }
        }
        return false;
    }
    
}
