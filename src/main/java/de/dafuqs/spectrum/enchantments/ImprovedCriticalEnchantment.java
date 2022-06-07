package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

public class ImprovedCriticalEnchantment extends SpectrumEnchantment {
	
	public ImprovedCriticalEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.WEAPON, slotTypes, unlockAdvancementIdentifier);
	}
	
	public static float getCritMultiplier(int critMultiplierLevel) {
		return SpectrumCommon.CONFIG.ImprovedCriticalExtraDamageMultiplierPerLevel * critMultiplierLevel;
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
	
	public boolean canAccept(Enchantment other) {
		return other != Enchantments.SHARPNESS && super.canAccept(other);
	}
	
}

