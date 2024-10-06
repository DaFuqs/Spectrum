package de.dafuqs.spectrum.items.tools;

import net.minecraft.enchantment.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;

public class GlintlessPickaxe extends SpectrumPickaxeItem {

    public GlintlessPickaxe(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        var enchants = EnchantmentHelper.get(stack);
        getDefaultEnchantments().forEach((e, i) -> {
            enchants.remove(e);
        });
        return !enchants.isEmpty();
    }
}
