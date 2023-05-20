package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;

public class TightGripEnchantment extends SpectrumEnchantment {

	public TightGripEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.WEAPON, slotTypes, unlockAdvancementIdentifier);
	}
	
	@Override
	public int getMinPower(int level) {
		return 5;
	}
	
	@Override
	public int getMaxPower(int level) {
		return super.getMinPower(level) + 30;
	}
	
	@Override
	public int getMaxLevel() {
		return SpectrumCommon.CONFIG.TightGripMaxLevel;
	}
	
	@Override
	public boolean canAccept(Enchantment other) {
		return super.canAccept(other);
	}

	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		return super.isAcceptableItem(stack) || stack.getItem() instanceof AxeItem;
	}
}

