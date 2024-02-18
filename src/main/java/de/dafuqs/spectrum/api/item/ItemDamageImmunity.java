package de.dafuqs.spectrum.api.item;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.damage.*;
import net.minecraft.item.*;
import net.minecraft.registry.tag.*;

import java.util.*;

/**
 * Making Items immune to certain forms of damage
 */
public class ItemDamageImmunity {

	private static final Map<Item, List<TagKey<DamageType>>> damageSourceImmunities = new HashMap<>();
	
	public static void registerImmunity(ItemConvertible itemConvertible, TagKey<DamageType> damageTypeTag) {
		Item item = itemConvertible.asItem();
		List<TagKey<DamageType>> current = damageSourceImmunities.getOrDefault(item, new ArrayList<>());
		current.add(damageTypeTag);
		damageSourceImmunities.put(item, current);
	}
	
	public static boolean isImmuneTo(ItemStack itemStack, DamageSource damageSource) {
		// otherwise items would fall endlessly when falling into the end, causing lag
		if (damageSource.isOf(DamageTypes.OUT_OF_WORLD)) {
			return false;
		}
		
		// does itemStack have Damage Proof enchantment?
		if (EnchantmentHelper.getLevel(SpectrumEnchantments.STEADFAST, itemStack) > 0) {
			return true;
			// is item immune to this specific kind of damage?
		}
		
		Item item = itemStack.getItem();
		if (damageSourceImmunities.containsKey(item)) {
			for (TagKey<DamageType> type : damageSourceImmunities.get(item)) {
				if (damageSource.isIn(type)) {
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
