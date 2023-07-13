package de.dafuqs.spectrum.enchantments;

import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;

public class RazingEnchantment extends SpectrumEnchantment {
	
	public RazingEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.DIGGER, slotTypes, unlockAdvancementIdentifier);
	}
	
	@Override
	public int getMinPower(int level) {
		return 20;
	}

    @Override
    public int getMaxPower(int level) {
        return 30;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return other != Enchantments.FORTUNE && super.canAccept(other);
    }
}
