package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class CloversFavorEnchantment extends SpectrumEnchantment {

	public CloversFavorEnchantment(Rarity weight, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.WEAPON, slotTypes);
	}

	public int getMinPower(int level) {
		return 20;
	}

	public int getMaxPower(int level) {
		return super.getMinPower(level) + 30;
	}

	public int getMaxLevel() {
		return SpectrumCommon.CONFIG.CloversFavorMaxLevel;
	}

	public boolean canAccept(Enchantment other) {
		return super.canAccept(other);
	}

}
