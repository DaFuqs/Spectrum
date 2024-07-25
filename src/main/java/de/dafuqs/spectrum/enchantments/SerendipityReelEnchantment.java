package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.items.tools.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

public class SerendipityReelEnchantment extends SpectrumEnchantment {

	public SerendipityReelEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.FISHING_ROD, slotTypes, unlockAdvancementIdentifier);
	}

	@Override
	public int getMinPower(int level) {
		return 40;
	}

	@Override
	public int getMaxPower(int level) {
		return super.getMinPower(level) + 80;
	}

	@Override
	public int getMaxLevel() {
		return SpectrumCommon.CONFIG.SerendipityReelMaxLevel;
	}

	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		return stack.getItem() instanceof SpectrumFishingRodItem;
	}


}
