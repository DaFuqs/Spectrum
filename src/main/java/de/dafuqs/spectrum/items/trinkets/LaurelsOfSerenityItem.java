package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.apache.commons.lang3.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class LaurelsOfSerenityItem extends InkDrainTrinketItem implements SleepStatusAffectingItem, ExpandedStatTooltip {
    
    public LaurelsOfSerenityItem(Settings settings) {
        super(settings, SpectrumCommon.locate("unlocks/trinkets/laurels_of_serenity"), InkColors.PURPLE);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.spectrum.laurels_of_serenity.tooltip").formatted(Formatting.GRAY));
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public void expandTooltip(ItemStack stack, @Nullable PlayerEntity player, List<Text> tooltip, TooltipContext context) {
        var resist = getSleepResistance(player, stack);
        if (resist == 0)
            return;
        
        tooltip.add(Text.translatable("item.spectrum.laurels_of_serenity.tooltip.post", StringUtils.left(String.valueOf((1 - getDetectionRangeMultiplier(stack)) * 100), 4)).formatted(Formatting.BLUE));
        tooltip.add(Text.translatable("info.spectrum.tooltip.sleep_resist.positive", StringUtils.left(String.valueOf(resist * 100), 4)).styled(s -> s.withColor(SpectrumStatusEffects.ETERNAL_SLUMBER_COLOR)));
    }

    public float getDetectionRangeMultiplier(ItemStack stack) {
        FixedSingleInkStorage inkStorage = getEnergyStorage(stack);
        var bonus = getBonus(inkStorage.getEnergy(inkStorage.getStoredColor()));
        return (float) (1 - bonus * 0.15); //TODO: reduce to 0.1 once the ink trinket cap is set back to 1.6 billion
    }

    @Override
    public float getSleepResistance(PlayerEntity player, ItemStack stack) {
        FixedSingleInkStorage inkStorage = getEnergyStorage(stack);
        return (float) (0.1 * getBonus(inkStorage.getEnergy(inkStorage.getStoredColor()))); //TODO: also tweak this when that time comes
    }

    public double getBonus(long storedInk) {
        if (storedInk < 100) {
            return 0;
        } else {
            return 1 + (int) (Math.log(storedInk / 100.0f) / Math.log(8));
        }
    }

    public static float getEffectFor(LivingEntity livingEntity) {
        var comp = TrinketsApi.getTrinketComponent(livingEntity);

        if (comp.isEmpty())
            return 1F;
        
        var trinket = comp.get().getEquipped(SpectrumItems.LAURELS_OF_SERENITY);

        if (trinket.isEmpty())
            return 1F;
        
        return ((LaurelsOfSerenityItem) SpectrumItems.LAURELS_OF_SERENITY).getDetectionRangeMultiplier(trinket.get(0).getRight());
    }
}
