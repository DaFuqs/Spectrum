package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.registries.SpectrumItemTags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class IndestructibleEnchantment extends SpectrumEnchantment {
	
	public IndestructibleEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.BREAKABLE, slotTypes, unlockAdvancementIdentifier);
	}
	
	public int getMinPower(int level) {
		return 30;
	}
	
	public int getMaxPower(int level) {
		return super.getMinPower(level) + 30;
	}
	
	public int getMaxLevel() {
		return 1;
	}
	
	public boolean canAccept(Enchantment other) {
		return super.canAccept(other);
	}
	
	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		return stack.isDamageable() && !stack.isIn(SpectrumItemTags.INDESTRUCTIBLE_BLACKLISTED);
	}
	
}
