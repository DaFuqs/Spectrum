package de.dafuqs.spectrum.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public abstract class SpectrumEnchantment extends Enchantment {
	
	protected SpectrumEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
		super(weight, type, slotTypes);
	}
	
	public boolean isTreasure() {
		return false;
	}
	
	public boolean isAvailableForEnchantedBookOffer() {
		return false;
	}
	
	public boolean isAvailableForRandomSelection() {
		return false;
	}
	
}
