package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;

import java.util.*;

public class ImprovedCriticalEnchantment extends SpectrumEnchantment {
	
	public static final UUID EXTRA_CRIT_DAMAGE_MULTIPLIER_ATTRIBUTE_UUID = UUID.fromString("e9bca8d4-9dcb-4e9e-8a7b-48b129c7ec5a");
	
	public ImprovedCriticalEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.WEAPON, slotTypes, unlockAdvancementIdentifier);
	}
	
	public static float getAddtionalCritDamageMultiplier(int improvedCriticalLevel) {
		return SpectrumCommon.CONFIG.ImprovedCriticalExtraDamageMultiplierPerLevel * improvedCriticalLevel;
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

