package de.dafuqs.spectrum.registries;

import net.minecraft.enchantment.*;
import net.minecraft.entity.damage.*;
import net.minecraft.item.*;

import java.util.*;

public class SpectrumItemStackDamageImmunities {
	
	// Items immune to all kinds of damage (minus out of world - otherwise items would fall endlessly when falling into the end, causing lag)
	private static final List<Item> generalImmunities = new ArrayList<>();
	// Items immune to some forms of damage
	private static final HashMap<String, List<Item>> damageSourceImmunities = new HashMap<>();
	
	public static void registerDefaultItemStackImmunities() {
		generalImmunities.add(SpectrumBlocks.CRACKED_END_PORTAL_FRAME.asItem());
		
		SpectrumItemStackDamageImmunities.addImmunity(Items.NETHER_STAR, DamageSource.IN_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(Items.NETHER_STAR, DamageSource.ON_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(Items.NETHER_STAR, DamageSource.LAVA);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.LAVA_SPONGE.asItem(), DamageSource.IN_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.LAVA_SPONGE.asItem(), DamageSource.ON_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.LAVA_SPONGE.asItem(), DamageSource.LAVA);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.WET_LAVA_SPONGE.asItem(), DamageSource.IN_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.WET_LAVA_SPONGE.asItem(), DamageSource.ON_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.WET_LAVA_SPONGE.asItem(), DamageSource.LAVA);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.DOOMBLOOM.asItem(), DamageSource.IN_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.DOOMBLOOM.asItem(), DamageSource.ON_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.DOOMBLOOM.asItem(), DamageSource.LAVA);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumItems.DOOMBLOOM_SEED, DamageSource.IN_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumItems.DOOMBLOOM_SEED, DamageSource.ON_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumItems.DOOMBLOOM_SEED, DamageSource.LAVA);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumItems.DOOMBLOOM_SEED, SpectrumDamageSources.PRIMORDIAL_FIRE);
		
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumItems.PURE_NETHERITE_SCRAP, DamageSource.IN_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.PURE_NETHERITE_SCRAP_BLOCK.asItem(), DamageSource.IN_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.SMALL_NETHERITE_SCRAP_BUD.asItem(), DamageSource.IN_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.LARGE_NETHERITE_SCRAP_BUD.asItem(), DamageSource.IN_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.NETHERITE_SCRAP_CLUSTER.asItem(), DamageSource.IN_FIRE);
		
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumItems.PURE_NETHERITE_SCRAP, DamageSource.ON_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.PURE_NETHERITE_SCRAP_BLOCK.asItem(), DamageSource.ON_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.SMALL_NETHERITE_SCRAP_BUD.asItem(), DamageSource.ON_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.LARGE_NETHERITE_SCRAP_BUD.asItem(), DamageSource.ON_FIRE);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.NETHERITE_SCRAP_CLUSTER.asItem(), DamageSource.ON_FIRE);
		
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumItems.PURE_NETHERITE_SCRAP, DamageSource.LAVA);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.PURE_NETHERITE_SCRAP_BLOCK.asItem(), DamageSource.LAVA);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.SMALL_NETHERITE_SCRAP_BUD.asItem(), DamageSource.LAVA);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.LARGE_NETHERITE_SCRAP_BUD.asItem(), DamageSource.LAVA);
		SpectrumItemStackDamageImmunities.addImmunity(SpectrumBlocks.NETHERITE_SCRAP_CLUSTER.asItem(), DamageSource.LAVA);
		
	}
	
	public static void addImmunity(Item item, DamageSource damageSource) {
		addImmunity(item, damageSource.name);
	}
	
	public static void addImmunity(Item item, String damageSourceName) {
		if (damageSourceImmunities.containsKey(damageSourceName)) {
			damageSourceImmunities.get(damageSourceName).add(item);
		} else {
			ArrayList<Item> newList = new ArrayList<>();
			newList.add(item);
			damageSourceImmunities.put(damageSourceName, newList);
		}
	}
	
	public static boolean isDamageImmune(ItemStack itemStack, DamageSource damageSource) {
		if (damageSource.isOutOfWorld()) {
			return false;
		}
		
		Item item = itemStack.getItem();
		// is item resistant to all forms of damage?
		if (generalImmunities.contains(item)) {
			return true;
			// does itemStack have Damage Proof enchantment?
		} else if (EnchantmentHelper.getLevel(SpectrumEnchantments.STEADFAST, itemStack) > 0) {
			return true;
			// is item immune to this specific kind of damage?
		} else if (damageSourceImmunities.containsKey(damageSource.getName())) {
			return damageSourceImmunities.get(damageSource.getName()).contains(item);
		} else {
			return false;
		}
	}
	
	public static boolean isFireDamageImmune(ItemStack itemStack) {
		return isDamageImmune(itemStack, DamageSource.IN_FIRE);
	}
	
}
