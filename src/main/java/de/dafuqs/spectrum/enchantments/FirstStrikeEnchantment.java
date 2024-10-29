package de.dafuqs.spectrum.enchantments;

import com.sammy.malum.common.item.curiosities.weapons.scythe.MalumScytheItem;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.SpectrumIntegrationPacks;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

public class FirstStrikeEnchantment extends SpectrumEnchantment {
	
	public FirstStrikeEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.WEAPON, slotTypes, unlockAdvancementIdentifier);
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
		return SpectrumCommon.CONFIG.FirstStrikeMaxLevel;
	}
	
	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		return super.isAcceptableItem(stack) || stack.getItem() instanceof AxeItem ||
				(SpectrumIntegrationPacks.isIntegrationPackActive(SpectrumIntegrationPacks.MALUM_ID) && stack.getItem() instanceof MalumScytheItem);
	}
	
}
