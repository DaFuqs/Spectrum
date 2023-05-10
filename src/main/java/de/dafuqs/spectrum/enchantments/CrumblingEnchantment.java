package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

public class CrumblingEnchantment extends SpectrumEnchantment{

    public CrumblingEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
        super(weight, EnchantmentTarget.DIGGER, slotTypes, unlockAdvancementIdentifier);
    }

    @Override
    public int getMinPower(int level) {
        return 20;
    }

    @Override
    public int getMaxPower(int level) {
        return 30;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return other != Enchantments.FORTUNE && super.canAccept(other);
    }
}
