package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.tags.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;

import java.util.*;

/**
 * Making Items immune to certain forms of damage
 */
public class DamageImmunityHelper {
	
	public static boolean isImmuneTo(ItemStack stack, DamageSource damageSource) {
		// otherwise items would fall endlessly when falling into the end, causing lag
		if (damageSource.is(DamageTypes.FELL_OUT_OF_WORLD)) {
			return false;
		}
		
		if (EnchantmentHelper.hasTag(stack, SpectrumEnchantmentTags.PREVENTS_ITEM_DAMAGE)) {
			return true;
		}

		// is item immune to this specific kind of damage?
		List<TagKey<DamageType>> immunities = stack.get(SpectrumDataComponentTypes.DAMAGE_IMMUNE);
		if(immunities == null) {
			return false;
		}
		
		for(TagKey<DamageType> immunity : immunities) {
			if(damageSource.is(immunity)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isImmuneTo(ItemStack stack, TagKey<DamageType> damageTypeTag) {
		List<TagKey<DamageType>> immunities = stack.get(SpectrumDataComponentTypes.DAMAGE_IMMUNE);
		return immunities != null && immunities.contains(damageTypeTag);
	}
	
}
