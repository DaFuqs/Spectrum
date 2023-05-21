package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

public class ExuberanceEnchantment extends SpectrumEnchantment {
	
	public ExuberanceEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.WEAPON, slotTypes, unlockAdvancementIdentifier);
	}
	
	public static float getExuberanceMod(PlayerEntity breakingPlayer) {
		if (breakingPlayer != null && EnchantmentHelper.getLevel(SpectrumEnchantments.EXUBERANCE, breakingPlayer.getMainHandStack()) > 0) {
			int exuberanceLevel = EnchantmentHelper.getEquipmentLevel(SpectrumEnchantments.EXUBERANCE, breakingPlayer);
			return getExuberanceMod(exuberanceLevel);
		} else {
			return 1.0F;
		}
	}
	
	public static float getExuberanceMod(int level) {
		return 1.0F + level * SpectrumCommon.CONFIG.ExuberanceBonusExperiencePercentPerLevel;
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
		return SpectrumCommon.CONFIG.ExuberanceMaxLevel;
	}
	
	@Override
	public boolean canAccept(Enchantment other) {
		return super.canAccept(other);
	}
	
	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		return super.isAcceptableItem(stack) || EnchantmentTarget.DIGGER.isAcceptableItem(stack.getItem()) || stack.getItem() instanceof SpectrumFishingRodItem;
	}
	
}