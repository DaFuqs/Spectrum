package de.dafuqs.spectrum.status_effects;

import de.dafuqs.spectrum.api.item.SleepStatusAffectingItem;
import de.dafuqs.spectrum.registries.SpectrumEntityTypeTags;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalEntityTypeTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;

import java.util.ArrayList;

public class SleepStatusEffect extends SpectrumStatusEffect {

    private final float baseStrength;

    public SleepStatusEffect(StatusEffectCategory category, float baseStrength, int color) {
        super(category, color);
        this.baseStrength = baseStrength;
    }

    public static float getSleepVulnerability(StatusEffectInstance instance, LivingEntity entity) {
        var type = entity.getType();
        var vulnerability = ((SleepStatusEffect) instance.getEffectType()).baseStrength;

        if (type.isIn(SpectrumEntityTypeTags.CONSTRUCTS))
            return 0;

        if (entity.hasStatusEffect(SpectrumStatusEffects.IMMUNITY))
            vulnerability *= 0.25F;

        if (entity instanceof ServerPlayerEntity player) {
            return vulnerability * getPlayerMod(player);
        }
        else if (type.isIn(SpectrumEntityTypeTags.SLEEP_WEAK)) {
            vulnerability *= 2.5F;
        }
        else if (type.isIn(SpectrumEntityTypeTags.SLEEP_RESISTANT)) {
            vulnerability *= 0.5F;
        }
        else if (isResistant(entity)) {
            vulnerability *= 0.1F;
        }

        return Math.max(vulnerability, 0);
    }

    public static float getPlayerMod(ServerPlayerEntity player) {
        float mod = Math.min((player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST)) / 48000F), 3F);
        var multiplier = 1F;

        var trinkets = TrinketsApi.getTrinketComponent(player).map(TrinketComponent::getAllEquipped).orElse(new ArrayList<>());
        for (Pair<SlotReference, ItemStack> trinketPair : trinkets) {
            var effect = getModFor(player, trinketPair.getRight());
            mod += effect.getLeft();
            multiplier += effect.getRight();
        }

        for (Hand hand : Hand.values()) {
            var handStack = player.getStackInHand(hand);
            var effect = getModFor(player, handStack);
            mod += effect.getLeft();
            multiplier += effect.getRight();
        }

        for (ItemStack armor : player.getInventory().armor) {
            var effect = getModFor(player, armor);
            mod += effect.getLeft();
            multiplier += effect.getRight();
        }

        return mod * multiplier;
    }

    private static Pair<Float, Float> getModFor(ServerPlayerEntity player, ItemStack stack) {
        if (stack.getItem() instanceof SleepStatusAffectingItem sleepItem) {
            return new Pair<>(sleepItem.getSleepFlatModifier(player, stack), sleepItem.getSleepMultModifier(player, stack));
        }
        return new Pair<>(0F, 0F);
    }

    public static boolean isResistant(LivingEntity entity) {
        if (entity.hasStatusEffect(SpectrumStatusEffects.FRENZY))
            return true;

        var type = entity.getType();
        return type.isIn(SpectrumEntityTypeTags.SLEEP_IMMUNEISH) || type.isIn(ConventionalEntityTypeTags.BOSSES);
    }

    public SleepThreshold getThreshold(float effect) {
        var threshold = SleepThreshold.NON_PLAYER;
        var vals = SleepThreshold.values();
        for (int i = 1; i < vals.length; i++) {
            if (effect >= vals[i].breakpoint)
                threshold = vals[i];
        }
        return threshold;
    }

    public enum SleepThreshold {
        NON_PLAYER(0F),
        SCREEN_EFFECTS(0.5F),
        WEAK_EFFECTS(1F),
        STRONG_EFFECTS(2.5F);

        public final float breakpoint;

        SleepThreshold(float breakpoint) {
            this.breakpoint = breakpoint;
        }
    }
}
