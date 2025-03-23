package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

public class IndestructibleEnchantment extends SpectrumEnchantment {
	
	public IndestructibleEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.BREAKABLE, slotTypes, unlockAdvancementIdentifier);
	}
	
	@Override
	public int getMinPower(int level) {
		return 30;
	}
	
	@Override
	public int getMaxPower(int level) {
		return super.getMinPower(level) + 30;
	}
	
	@Override
	public int getMaxLevel() {
		return 1;
	}
	
	@Override
	public boolean canAccept(Enchantment other) {
		return super.canAccept(other) && other != Enchantments.INFINITY && other != Enchantments.UNBREAKING && other != Enchantments.EFFICIENCY && other != Enchantments.MENDING && other != Enchantments.PROTECTION && other != Enchantments.BINDING_CURSE;
	}
	
	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		return super.isAcceptableItem(stack) && !stack.isIn(SpectrumItemTags.INDESTRUCTIBLE_BLACKLISTED);
	}
	
}
