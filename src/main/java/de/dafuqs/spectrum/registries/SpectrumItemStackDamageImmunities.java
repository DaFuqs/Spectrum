package de.dafuqs.spectrum.registries;

import net.minecraft.enchantment.*;
import net.minecraft.entity.damage.*;
import net.minecraft.item.*;
import net.minecraft.registry.tag.*;

import java.util.*;

public class SpectrumItemStackDamageImmunities {
	
	// Items immune to some forms of damage
	private static final HashMap<Item, List<TagKey<DamageType>>> damageSourceImmunities = new HashMap<>();
	
	public static void registerDefaultItemStackImmunities() {
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.CRACKED_END_PORTAL_FRAME, DamageTypeTags.IS_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.CRACKED_END_PORTAL_FRAME, DamageTypeTags.IS_EXPLOSION);
		
		SpectrumItemStackDamageImmunities.addImmunity(Items.NETHER_STAR, DamageTypeTags.IS_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(Items.NETHER_STAR, DamageTypeTags.IS_EXPLOSION);
		
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.LAVA_SPONGE.asItem(), DamageTypeTags.IS_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.WET_LAVA_SPONGE.asItem(), DamageTypeTags.IS_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.DOOMBLOOM.asItem(), DamageTypeTags.IS_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumItems.DOOMBLOOM_SEED, DamageTypeTags.IS_FIRE);
		
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumItems.PURE_NETHERITE_SCRAP, DamageTypeTags.IS_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.PURE_NETHERITE_SCRAP_BLOCK, DamageTypeTags.IS_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.SMALL_NETHERITE_SCRAP_BUD, DamageTypeTags.IS_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.LARGE_NETHERITE_SCRAP_BUD, DamageTypeTags.IS_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.NETHERITE_SCRAP_CLUSTER, DamageTypeTags.IS_FIRE);
	}
	
	public static void addImmunity(ItemConvertible itemConvertible, TagKey<DamageType> damageTypeTag) {
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
