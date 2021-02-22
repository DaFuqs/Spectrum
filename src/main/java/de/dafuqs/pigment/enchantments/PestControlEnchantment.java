package de.dafuqs.pigment.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.SilkTouchEnchantment;
import net.minecraft.entity.EquipmentSlot;

public class PestControlEnchantment extends SilkTouchEnchantment {

    public PestControlEnchantment(Rarity weight, EquipmentSlot... slotTypes) {
        super(weight, slotTypes);
    }

    public int getMinPower(int level) {
        return 10;
    }

    public int getMaxPower(int level) {
        return super.getMinPower(level) + 20;
    }

    public int getMaxLevel() {
        return 1;
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
        return super.canAccept(other) && other != PigmentEnchantments.RESONANCE;
    }

}