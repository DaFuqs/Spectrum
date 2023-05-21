package de.dafuqs.spectrum.enchantments;

import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;

public class VoidingEnchantment extends SpectrumEnchantment {
	
	public VoidingEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.DIGGER, slotTypes, unlockAdvancementIdentifier);
	}
	
	@Override
	public int getMinPower(int level) {
		return 25;
	}
	
	@Override
	public int getMaxPower(int level) {
		return 50;
	}
	
	@Override
	public int getMaxLevel() {
		return 1;
	}
	
	@Override
	public boolean isCursed() {
		return true;
	}
	
}
