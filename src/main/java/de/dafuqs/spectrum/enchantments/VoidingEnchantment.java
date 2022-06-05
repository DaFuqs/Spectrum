package de.dafuqs.spectrum.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

public class VoidingEnchantment extends SpectrumEnchantment {
	
	public VoidingEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.DIGGER, slotTypes, unlockAdvancementIdentifier);
	}
	
	public int getMinPower(int level) {
		return 25;
	}
	
	public int getMaxPower(int level) {
		return 50;
	}
	
	public int getMaxLevel() {
		return 1;
	}
	
	public boolean isCursed() {
		return true;
	}
	
	public boolean canAccept(Enchantment other) {
		return super.canAccept(other);
	}
	
}
