package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

public class ResonanceEnchantment extends SpectrumEnchantment {
	
	public ResonanceEnchantment(Enchantment.Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.DIGGER, slotTypes, unlockAdvancementIdentifier);
	}
	
	@Override
	public int getMinPower(int level) {
		return 25;
	}
	
	@Override
	public int getMaxPower(int level) {
		return super.getMinPower(level) + 75;
	}
	
	@Override
	public int getMaxLevel() {
		return 1;
	}
	
	@Override
	public boolean canAccept(Enchantment other) {
		return super.canAccept(other) && other != SpectrumEnchantments.PEST_CONTROL && other != Enchantments.SILK_TOUCH && other != Enchantments.FORTUNE;
	}
	
	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		return super.isAcceptableItem(stack) || stack.getItem() instanceof ShearsItem;
	}
	
}