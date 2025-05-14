package de.dafuqs.spectrum.api.item;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.tags.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;

import java.util.*;

/**
 * Making Items immune to certain forms of damage
 */
public class ItemDamageImmunity {

	private static final Map<Item, List<TagKey<DamageType>>> damageSourceImmunities = new HashMap<>();
	
	public static void registerImmunity(ItemLike itemConvertible, TagKey<DamageType> damageTypeTag) {
		Item item = itemConvertible.asItem();
		List<TagKey<DamageType>> current = damageSourceImmunities.getOrDefault(item, new ArrayList<>());
		current.add(damageTypeTag);
		damageSourceImmunities.put(item, current);
	}
	
	public static boolean isImmuneTo(ItemStack itemStack, DamageSource damageSource) {
		// otherwise items would fall endlessly when falling into the end, causing lag
		if (damageSource.is(DamageTypes.FELL_OUT_OF_WORLD)) {
			return false;
		}
		
		if (EnchantmentHelper.hasTag(itemStack, SpectrumEnchantmentTags.PREVENTS_ITEM_DAMAGE)) {
			return true;
		}

		// is item immune to this specific kind of damage?
		Item item = itemStack.getItem();
		if (damageSourceImmunities.containsKey(item)) {
			for (TagKey<DamageType> type : damageSourceImmunities.get(item)) {
				if (damageSource.is(type)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean isImmuneTo(ItemStack itemStack, TagKey<DamageType> damageTypeTag) {
		Item item = itemStack.getItem();
		if (damageSourceImmunities.containsKey(item)) {
			for (TagKey<DamageType> type : damageSourceImmunities.get(item)) {
				if (type.equals(damageTypeTag)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
}
