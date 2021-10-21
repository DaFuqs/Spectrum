package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;

public class ResonanceEnchantment extends Enchantment {

	public ResonanceEnchantment(Enchantment.Rarity weight, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.DIGGER, slotTypes);
	}

	public int getMinPower(int level) {
		return 15;
	}

	public int getMaxPower(int level) {
		return super.getMinPower(level) + 50;
	}

	public int getMaxLevel() {
		return 1;
	}

	public boolean isTreasure() {
		return true;
	}

	public boolean isAvailableForEnchantedBookOffer() {
		return false;
	}

	public boolean isAvailableForRandomSelection() {
		return false;
	}

	public boolean canAccept(Enchantment other) {
		return super.canAccept(other) && other != SpectrumEnchantments.PEST_CONTROL && other != SpectrumEnchantments.AUTO_SMELT && other != Enchantments.FORTUNE;
	}

}