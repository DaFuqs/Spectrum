package de.dafuqs.spectrum.registries;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class SpectrumDefaultEnchantments {

	private static final HashMap<Item, DefaultEnchantment> defaultEnchantments = new HashMap<>();

	public static class DefaultEnchantment {
		public Enchantment enchantment;
		public int level;

		public DefaultEnchantment(Enchantment enchantment, int level) {
			this.enchantment = enchantment;
			this.level = level;
		}
	}

	public static void registerDefaultEnchantments() {
		// early game tools
		SpectrumDefaultEnchantments.registerDefaultEnchantment(SpectrumItems.LOOTING_FALCHION, Enchantments.LOOTING, 3);
		SpectrumDefaultEnchantments.registerDefaultEnchantment(SpectrumItems.SILKER_PICKAXE, Enchantments.SILK_TOUCH, 1);
		SpectrumDefaultEnchantments.registerDefaultEnchantment(SpectrumItems.FORTUNE_PICKAXE, Enchantments.FORTUNE, 3);
		SpectrumDefaultEnchantments.registerDefaultEnchantment(SpectrumItems.MULTITOOL, Enchantments.EFFICIENCY, 1);
		SpectrumDefaultEnchantments.registerDefaultEnchantment(SpectrumItems.VOIDING_PICKAXE, SpectrumEnchantments.VOIDING, 1);
		SpectrumDefaultEnchantments.registerDefaultEnchantment(SpectrumItems.RESONANT_PICKAXE, SpectrumEnchantments.RESONANCE, 1);

		// Bedrock tools
		SpectrumDefaultEnchantments.registerDefaultEnchantment(SpectrumItems.BEDROCK_PICKAXE, Enchantments.SILK_TOUCH, 1);
		SpectrumDefaultEnchantments.registerDefaultEnchantment(SpectrumItems.BEDROCK_AXE, Enchantments.EFFICIENCY, 5);
		SpectrumDefaultEnchantments.registerDefaultEnchantment(SpectrumItems.BEDROCK_SHOVEL, Enchantments.EFFICIENCY, 5);
		SpectrumDefaultEnchantments.registerDefaultEnchantment(SpectrumItems.BEDROCK_SWORD, Enchantments.SHARPNESS, 5);
		SpectrumDefaultEnchantments.registerDefaultEnchantment(SpectrumItems.BEDROCK_BOW, Enchantments.POWER, 5);
		SpectrumDefaultEnchantments.registerDefaultEnchantment(SpectrumItems.BEDROCK_CROSSBOW, Enchantments.QUICK_CHARGE, 3);
		SpectrumDefaultEnchantments.registerDefaultEnchantment(SpectrumItems.BEDROCK_SHEARS, Enchantments.SILK_TOUCH, 1);
		SpectrumDefaultEnchantments.registerDefaultEnchantment(SpectrumItems.BEDROCK_HOE, Enchantments.FORTUNE, 3);
		SpectrumDefaultEnchantments.registerDefaultEnchantment(SpectrumItems.BEDROCK_FISHING_ROD, Enchantments.LUCK_OF_THE_SEA, 3);

		// bedrock armor
		SpectrumDefaultEnchantments.registerDefaultEnchantment(SpectrumItems.BEDROCK_HELMET, Enchantments.PROJECTILE_PROTECTION, 4);
		SpectrumDefaultEnchantments.registerDefaultEnchantment(SpectrumItems.BEDROCK_CHESTPLATE, Enchantments.PROTECTION, 4);
		SpectrumDefaultEnchantments.registerDefaultEnchantment(SpectrumItems.BEDROCK_LEGGINGS, Enchantments.BLAST_PROTECTION, 4);
		SpectrumDefaultEnchantments.registerDefaultEnchantment(SpectrumItems.BEDROCK_BOOTS, Enchantments.FIRE_PROTECTION, 4);
	}
	
	public static void registerDefaultEnchantment(Item item, Enchantment enchantment, int level) {
		defaultEnchantments.put(item, new DefaultEnchantment(enchantment, level));
	}

	public static DefaultEnchantment getDefaultEnchantment(Item item) {
		return defaultEnchantments.getOrDefault(item, null);
	}
	
	public static ItemStack getDefaultEnchantedStack(Item item) {
		ItemStack itemStack = new ItemStack(item);
		SpectrumDefaultEnchantments.DefaultEnchantment def = getDefaultEnchantment(item);
		if(def != null) {
			itemStack.addEnchantment(def.enchantment, def.level);
		}
		return itemStack;
	}

}
