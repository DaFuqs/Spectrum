package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;

public class InertiaEnchantment extends SpectrumEnchantment {
	
	public InertiaEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.DIGGER, slotTypes, unlockAdvancementIdentifier);
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
		return SpectrumCommon.CONFIG.InertiaMaxLevel;
	}
	
	@Override
	public boolean canAccept(Enchantment other) {
		return other != Enchantments.EFFICIENCY && super.canAccept(other);
	}
	
}
