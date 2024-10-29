package de.dafuqs.spectrum.enchantments;

import com.sammy.malum.common.item.curiosities.weapons.scythe.MalumScytheItem;
import com.sammy.malum.registry.common.item.EnchantmentRegistry;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.SpectrumIntegrationPacks;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
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
	public boolean isAcceptableItem(ItemStack stack) {
		var item = stack.getItem();
		if(SpectrumIntegrationPacks.isIntegrationPackActive(SpectrumIntegrationPacks.MALUM_ID) &&
				item instanceof MalumScytheItem)
		{
			return true;
		}

		return super.isAcceptableItem(stack);
	}
	@Override
	public boolean canAccept(Enchantment other) {
		return super.canAccept(other) &&
				!(SpectrumIntegrationPacks.isIntegrationPackActive(SpectrumIntegrationPacks.MALUM_ID) && other == EnchantmentRegistry.REBOUND.get());
	}

}

