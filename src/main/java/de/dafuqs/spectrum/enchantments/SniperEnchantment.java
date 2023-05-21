package de.dafuqs.spectrum.enchantments;

import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;

/**
 * Increases the speed of shot arrows and makes them invisible
 */
public class SniperEnchantment extends SpectrumEnchantment {
	
	public SniperEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.CROSSBOW, slotTypes, unlockAdvancementIdentifier);
	}
	
	@Override
	public int getMinPower(int level) {
		return 20;
	}
	
	@Override
	public int getMaxPower(int level) {
		return super.getMinPower(level) + 30;
	}
	
	@Override
	public int getMaxLevel() {
		return 2;
	}
	
	@Override
	public boolean canAccept(Enchantment other) {
		return other != Enchantments.MULTISHOT && super.canAccept(other);
	}
	
}

