package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

public class ResonanceEnchantment extends SpectrumEnchantment {

	public ResonanceEnchantment(Enchantment.Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.DIGGER, slotTypes, unlockAdvancementIdentifier);
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

	public boolean canAccept(Enchantment other) {
		return super.canAccept(other) && other != SpectrumEnchantments.PEST_CONTROL && other != SpectrumEnchantments.AUTO_SMELT && other != Enchantments.FORTUNE;
	}

}