package de.dafuqs.pigment.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.passive.CowEntity;

public class ExuberanceEnchantment extends Enchantment {

    public ExuberanceEnchantment(Rarity weight, EquipmentSlot... slotTypes) {
        super(weight, EnchantmentTarget.WEAPON, slotTypes);
    }

    public int getMinPower(int level) {
        return 10;
    }

    public int getMaxPower(int level) {
        return super.getMinPower(level) + 30;
    }

    public int getMaxLevel() {
        return 5;
    }

    public boolean isTreasure() {
        return false;
    }

    public boolean isAvailableForEnchantedBookOffer() {
        return true;
    }

    public boolean isAvailableForRandomSelection() {
        return true;
    }

    public boolean canAccept(Enchantment other) {
        return super.canAccept(other);
    }

}