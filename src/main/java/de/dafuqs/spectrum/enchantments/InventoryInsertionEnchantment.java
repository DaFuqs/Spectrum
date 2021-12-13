package de.dafuqs.spectrum.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class InventoryInsertionEnchantment extends SpectrumEnchantment {

	public InventoryInsertionEnchantment(Rarity weight, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.DIGGER, slotTypes);
	}

	public int getMinPower(int level) {
		return 15;
	}

	public int getMaxPower(int level) {
		return super.getMinPower(level) + 30;
	}

	public int getMaxLevel() {
		return 1;
	}

	public boolean canAccept(Enchantment other) {
		return super.canAccept(other);
	}

}