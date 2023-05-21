package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;

public class ImprovedCriticalEnchantment extends SpectrumEnchantment {
	
	public ImprovedCriticalEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.WEAPON, slotTypes, unlockAdvancementIdentifier);
	}
	
	public static float getCritMultiplier(int critMultiplierLevel) {
		return SpectrumCommon.CONFIG.ImprovedCriticalExtraDamageMultiplierPerLevel * critMultiplierLevel;
	}
	
	@Override
	public int getMinPower(int level) {
		return 10;
	}
	
	@Override
	public int getMaxPower(int level) {
		return super.getMinPower(level) + 30;
	}
	
	@Override
	public int getMaxLevel() {
		return SpectrumCommon.CONFIG.ImprovedCriticalMaxLevel;
	}
	
	@Override
	public boolean canAccept(Enchantment other) {
		return other != Enchantments.SHARPNESS && super.canAccept(other);
	}
	
}

