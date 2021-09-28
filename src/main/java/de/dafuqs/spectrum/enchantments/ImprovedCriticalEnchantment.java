package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;

public class ImprovedCriticalEnchantment extends Enchantment {

    public ImprovedCriticalEnchantment(Rarity weight, EquipmentSlot... slotTypes) {
        super(weight, EnchantmentTarget.WEAPON, slotTypes);
    }

    public int getMinPower(int level) {
        return 10;
    }

    public int getMaxPower(int level) {
        return super.getMinPower(level) + 30;
    }

    public int getMaxLevel() {
        return SpectrumCommon.CONFIG.ImprovedCriticalMaxLevel;
    }

    public boolean isTreasure() {
        return false;
    }

    public boolean isAvailableForEnchantedBookOffer() {
        return true;
    }

    public boolean isAvailableForRandomSelection() {
        return false;
    }

    public boolean canAccept(Enchantment other) {
        return other == Enchantments.SHARPNESS && super.canAccept(other);
    }

}

