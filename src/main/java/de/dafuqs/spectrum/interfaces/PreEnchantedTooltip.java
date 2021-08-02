package de.dafuqs.spectrum.interfaces;

import de.dafuqs.spectrum.registries.SpectrumDefaultEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.List;

public interface PreEnchantedTooltip {

    default void addPreEnchantedTooltip(List<Text> tooltip, ItemStack itemStack) {
        if(EnchantmentHelper.get(itemStack).isEmpty() && SpectrumDefaultEnchantments.hasDefaultEnchants(itemStack.getItem())) {
            SpectrumDefaultEnchantments.DefaultEnchantment defaultEnchantment = SpectrumDefaultEnchantments.getDefaultEnchantment(itemStack.getItem());
            tooltip.add(new TranslatableText("items.spectrum.default_enchantment_tooltip", defaultEnchantment.enchantment.getName(defaultEnchantment.level)).formatted(Formatting.DARK_GRAY));
        }
    }

}
