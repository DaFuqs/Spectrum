package de.dafuqs.spectrum.registries;

import net.minecraft.enchantment.*;
import net.minecraft.entity.damage.*;
import net.minecraft.item.*;
import net.minecraft.registry.tag.*;

import java.util.*;

public class SpectrumItemStackDamageImmunities {
	
	// Items immune to all kinds of damage (minus out of world - otherwise items would fall endlessly when falling into the end, causing lag)
	private static final List<Item> generalImmunities = new ArrayList<>();
	// Items immune to some forms of damage

	public static void registerDefaultItemStackImmunities() {
		generalImmunities.add(SpectrumBlocks.CRACKED_END_PORTAL_FRAME.asItem());
	}
	
	public static boolean isDamageImmune(ItemStack itemStack, DamageSource damageSource) {
		if (damageSource.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			return false;
		}

		Item item = itemStack.getItem();
		// is item resistant to all forms of damage?
		if (generalImmunities.contains(itemStack.getItem())) {
			return true;
			// does itemStack have Damage Proof enchantment?
		} else if (EnchantmentHelper.getLevel(SpectrumEnchantments.STEADFAST, itemStack) > 0) {
			return true;
			// is item immune to this specific kind of damage?
		} else
			return itemStack.isIn(SpectrumDamageSources.FIRE_IMMUNE_ITEMS) && damageSource.isIn(SpectrumDamageSources.ITEM_IMMUNITY);
	}
	
}
