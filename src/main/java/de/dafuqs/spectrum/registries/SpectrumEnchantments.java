package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.enchantments.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.registry.*;

public class SpectrumEnchantments {
	
	public static final SpectrumEnchantment RESONANCE = new ResonanceEnchantment(Enchantment.Rarity.VERY_RARE, SpectrumAdvancements.ENCHANTMENTS_RESONANCE_USAGE, EquipmentSlot.MAINHAND); // Silk Touch, just for different blocks
	public static final SpectrumEnchantment VOIDING = new VoidingEnchantment(Enchantment.Rarity.RARE, SpectrumAdvancements.ENCHANTMENTS_VOIDING_USAGE, EquipmentSlot.MAINHAND); // Voids all items mined
	public static final SpectrumEnchantment PEST_CONTROL = new PestControlEnchantment(Enchantment.Rarity.VERY_RARE, SpectrumAdvancements.ENCHANTMENTS_PEST_CONTROL, EquipmentSlot.MAINHAND); // Kills silverfish when mining infested blocks
	public static final SpectrumEnchantment FOUNDRY = new FoundryEnchantment(Enchantment.Rarity.RARE, SpectrumAdvancements.ENCHANTMENTS_FOUNDRY, EquipmentSlot.MAINHAND); // applies smelting recipe before dropping items after mining
	public static final SpectrumEnchantment INVENTORY_INSERTION = new InventoryInsertionEnchantment(Enchantment.Rarity.RARE, SpectrumAdvancements.ENCHANTMENTS_INVENTORY_INSERTION, EquipmentSlot.MAINHAND); // don't drop items into the world, add to inv instead
	public static final SpectrumEnchantment EXUBERANCE = new ExuberanceEnchantment(Enchantment.Rarity.UNCOMMON, SpectrumAdvancements.ENCHANTMENTS_EXUBERANCE, EquipmentSlot.MAINHAND); // Drops more XP on kill
	public static final SpectrumEnchantment TREASURE_HUNTER = new TreasureHunterEnchantment(Enchantment.Rarity.RARE, SpectrumAdvancements.ENCHANTMENTS_TREASURE_HUNTER, EquipmentSlot.MAINHAND); // Drops mob heads
	public static final SpectrumEnchantment DISARMING = new DisarmingEnchantment(Enchantment.Rarity.VERY_RARE, SpectrumAdvancements.ENCHANTMENTS_DISARMING, EquipmentSlot.MAINHAND); // Drops mob equipment on hit (and players, but way less often)
	public static final SpectrumEnchantment FIRST_STRIKE = new FirstStrikeEnchantment(Enchantment.Rarity.RARE, SpectrumAdvancements.ENCHANTMENTS_FIRST_STRIKE, EquipmentSlot.MAINHAND); // Increased damage if enemy has full health
	public static final SpectrumEnchantment IMPROVED_CRITICAL = new ImprovedCriticalEnchantment(Enchantment.Rarity.RARE, SpectrumAdvancements.ENCHANTMENTS_IMPROVED_CRITICAL, EquipmentSlot.MAINHAND); // Increased damage when landing a critical hit
	public static final SpectrumEnchantment INERTIA = new InertiaEnchantment(Enchantment.Rarity.VERY_RARE, SpectrumAdvancements.ENCHANTMENTS_INERTIA, EquipmentSlot.MAINHAND); // Decreases mining speed, but increases with each mined block of the same type
	public static final SpectrumEnchantment CLOVERS_FAVOR = new CloversFavorEnchantment(Enchantment.Rarity.RARE, SpectrumAdvancements.ENCHANTMENTS_CLOVERS_FAVOR, EquipmentSlot.MAINHAND); // Increases drop chance of <1 loot drops
	public static final SpectrumEnchantment SNIPER = new SniperEnchantment(Enchantment.Rarity.VERY_RARE, SpectrumAdvancements.ENCHANTMENTS_SNIPER, EquipmentSlot.MAINHAND); // Increases projectile speed => increased damage + range
	public static final SpectrumEnchantment TIGHT_GRIP = new TightGripEnchantment(Enchantment.Rarity.RARE, SpectrumAdvancements.ENCHANTMENTS_TIGHT_GRIP, EquipmentSlot.MAINHAND); // Increases attack speed
	public static final SpectrumEnchantment STEADFAST = new SteadfastEnchantment(Enchantment.Rarity.COMMON, SpectrumAdvancements.ENCHANTMENTS_STEADFAST, EquipmentSlot.MAINHAND); // ItemStacks with this enchantment are not destroyed by cactus, fire, lava, ...
	public static final SpectrumEnchantment INDESTRUCTIBLE = new IndestructibleEnchantment(Enchantment.Rarity.RARE, SpectrumAdvancements.ENCHANTMENTS_INDESTRUCTIBLE, EquipmentSlot.MAINHAND); // Make tools not use up durability
	public static final SpectrumEnchantment BIG_CATCH = new BigCatchEnchantment(Enchantment.Rarity.RARE, SpectrumAdvancements.ENCHANTMENTS_BIG_CATCH, EquipmentSlot.MAINHAND); // Increase the chance to reel in entities instead of fishing loot
	public static final SpectrumEnchantment SERENDIPITY_REEL = new SerendipityReelEnchantment(Enchantment.Rarity.RARE, SpectrumAdvancements.ENCHANTMENTS_SERENDIPITY_REEL, EquipmentSlot.MAINHAND); // Increase luck when fishing
	public static final SpectrumEnchantment RAZING = new RazingEnchantment(Enchantment.Rarity.UNCOMMON, SpectrumAdvancements.ENCHANTMENTS_RAZING, EquipmentSlot.MAINHAND); // increased mining speed for very hard blocks
	public static final SpectrumEnchantment INEXORABLE = new InexorableEnchantment(Enchantment.Rarity.VERY_RARE, SpectrumAdvancements.ENCHANTMENTS_INEXORABLE, EquipmentSlot.MAINHAND, EquipmentSlot.CHEST, EquipmentSlot.OFFHAND); // prevents mining & movement slowdowns
	
	public static void register() {
		register("resonance", RESONANCE);
		register("voiding", VOIDING);

		if (SpectrumCommon.CONFIG.PestControlEnchantmentEnabled) {
			register("pest_control", PEST_CONTROL);
		}
		if (SpectrumCommon.CONFIG.FoundryEnchantmentEnabled) {
			register("autosmelt", FOUNDRY);
		}
		if (SpectrumCommon.CONFIG.InventoryInsertionEnchantmentEnabled) {
			register("inventory_insertion", INVENTORY_INSERTION);
		}
		if (SpectrumCommon.CONFIG.ExuberanceEnchantmentEnabled) {
			register("exuberance", EXUBERANCE);
		}
		if (SpectrumCommon.CONFIG.TreasureHunterEnchantmentEnabled) {
			register("treasure_hunter", TREASURE_HUNTER);
		}
		if (SpectrumCommon.CONFIG.DisarmingEnchantmentEnabled) {
			register("disarming", DISARMING);
		}
		if (SpectrumCommon.CONFIG.FirstStrikeEnchantmentEnabled) {
			register("first_strike", FIRST_STRIKE);
		}
		if (SpectrumCommon.CONFIG.ImprovedCriticalEnchantmentEnabled) {
			register("improved_critical", IMPROVED_CRITICAL);
		}
		if (SpectrumCommon.CONFIG.InertiaEnchantmentEnabled) {
			register("inertia", INERTIA);
		}
		if (SpectrumCommon.CONFIG.CloversFavorEnchantmentEnabled) {
			register("clovers_favor", CLOVERS_FAVOR);
		}
		if (SpectrumCommon.CONFIG.SniperEnchantmentEnabled) {
			register("sniper", SNIPER);
		}
		if (SpectrumCommon.CONFIG.TightGripEnchantmentEnabled) {
			register("tight_grip", TIGHT_GRIP);
		}
		if (SpectrumCommon.CONFIG.SteadfastEnchantmentEnabled) {
			register("steadfast", STEADFAST);
		}
		if (SpectrumCommon.CONFIG.IndestructibleEnchantmentEnabled) {
			register("indestructible", INDESTRUCTIBLE);
		}
		if (SpectrumCommon.CONFIG.BigCatchEnchantmentEnabled) {
			register("big_catch", BIG_CATCH);
		}
		if (SpectrumCommon.CONFIG.SerendipityReelEnchantmentEnabled) {
			register("serendipity_reel", SERENDIPITY_REEL);
		}
		if (SpectrumCommon.CONFIG.RazingEnchantmentEnabled) {
			register("razing", RAZING);
		}
		if (SpectrumCommon.CONFIG.InexorableEnchantmentEnabled) {
			register("inexorable", INEXORABLE);
		}
		
	}
	
	private static SpectrumEnchantment register(String name, SpectrumEnchantment enchantment) {
		return Registry.register(Registries.ENCHANTMENT, SpectrumCommon.locate(name), enchantment);
	}
	
}
