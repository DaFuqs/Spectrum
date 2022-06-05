package de.dafuqs.spectrum.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

/**
 * Increases the speed of shot arrows and makes them invisible
 */
public class SniperEnchantment extends SpectrumEnchantment {
	
	public SniperEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.CROSSBOW, slotTypes, unlockAdvancementIdentifier);
	}
	
	public int getMinPower(int level) {
		return 20;
	}
	
	public int getMaxPower(int level) {
		return super.getMinPower(level) + 30;
	}
	
	public int getMaxLevel() {
		return 2;
	}
	
	public boolean canAccept(Enchantment other) {
		return other != Enchantments.MULTISHOT && super.canAccept(other);
	}
	
}

