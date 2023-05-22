package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.enchantments.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.registry.*;

public class SpectrumEnchantments {
	
	public static final SpectrumEnchantment RESONANCE = new ResonanceEnchantment(Enchantment.Rarity.VERY_RARE, SpectrumCommon.locate("unlocks/enchantments/resonance"), EquipmentSlot.MAINHAND); // Silk Touch, just for different blocks
	public static final SpectrumEnchantment VOIDING = new VoidingEnchantment(Enchantment.Rarity.RARE, SpectrumCommon.locate("spectrum"), EquipmentSlot.MAINHAND); // Voids all items mined
	public static SpectrumEnchantment PEST_CONTROL; // Kills silverfish when mining infested blocks
	public static SpectrumEnchantment FOUNDRY; // applies smelting recipe before dropping items after mining
	public static SpectrumEnchantment INVENTORY_INSERTION; // don't drop items into the world, add to inv instead
	public static SpectrumEnchantment EXUBERANCE; // Drops more XP on kill
	public static SpectrumEnchantment TREASURE_HUNTER; // Drops mob heads
	public static SpectrumEnchantment FIRST_STRIKE; // Increased damage if enemy has full health
	public static SpectrumEnchantment IMPROVED_CRITICAL; // Increased damage when landing a critical hit
	public static SpectrumEnchantment INERTIA; // Decreases mining speed, but increases with each mined block of the same type
	public static SpectrumEnchantment CLOVERS_FAVOR; // Increases drop chance of <1 loot drops
	public static SpectrumEnchantment TIGHT_GRIP; // Increases attack speed
	public static SpectrumEnchantment DISARMING; // Drops mob equipment on hit (and players, but way less often)
	public static SpectrumEnchantment SNIPER; // Increases projectile speed => increased damage + range
	public static SpectrumEnchantment STEADFAST; // ItemStacks with this enchantment are not destroyed by cactus, fire, lava, ...
	public static SpectrumEnchantment INDESTRUCTIBLE; // Make tools not use up durability
	public static SpectrumEnchantment BIG_CATCH; // Increase the chance to reel in entities instead of fishing loot
	public static SpectrumEnchantment RAZING; // increased mining speed for very hard blocks
	public static SpectrumEnchantment INEXORABLE; // prevents mining & movement slowdowns

	public static void register() {
		register("resonance", RESONANCE);
		register("voiding", VOIDING);

		if (SpectrumCommon.CONFIG.PestControlEnchantmentEnabled) {
			PEST_CONTROL = register("pest_control", new PestControlEnchantment(Enchantment.Rarity.VERY_RARE, SpectrumCommon.locate("unlocks/enchantments/pest_control"), EquipmentSlot.MAINHAND));
		}
		if (SpectrumCommon.CONFIG.FoundryEnchantmentEnabled) {
			FOUNDRY = register("autosmelt", new FoundryEnchantment(Enchantment.Rarity.RARE, SpectrumCommon.locate("unlocks/enchantments/autosmelt"), EquipmentSlot.MAINHAND));
		}
		if (SpectrumCommon.CONFIG.InventoryInsertionEnchantmentEnabled) {
			INVENTORY_INSERTION = register("inventory_insertion", new InventoryInsertionEnchantment(Enchantment.Rarity.RARE, SpectrumCommon.locate("unlocks/enchantments/inventory_insertion"), EquipmentSlot.MAINHAND));
		}
		if (SpectrumCommon.CONFIG.ExuberanceEnchantmentEnabled) {
			register("exuberance", new ExuberanceEnchantment(Enchantment.Rarity.UNCOMMON, SpectrumCommon.locate("unlocks/enchantments/exuberance"), EquipmentSlot.MAINHAND));
		}
		if (SpectrumCommon.CONFIG.TreasureHunterEnchantmentEnabled) {
			register("treasure_hunter", new TreasureHunterEnchantment(Enchantment.Rarity.RARE, SpectrumCommon.locate("unlocks/enchantments/treasure_hunter"), EquipmentSlot.MAINHAND));
		}
		if (SpectrumCommon.CONFIG.DisarmingEnchantmentEnabled) {
			register("disarming", new DisarmingEnchantment(Enchantment.Rarity.VERY_RARE, SpectrumCommon.locate("unlocks/enchantments/disarming"), EquipmentSlot.MAINHAND));
		}
		if (SpectrumCommon.CONFIG.FirstStrikeEnchantmentEnabled) {
			register("first_strike", new FirstStrikeEnchantment(Enchantment.Rarity.RARE, SpectrumCommon.locate("unlocks/enchantments/first_strike"), EquipmentSlot.MAINHAND));
		}
		if (SpectrumCommon.CONFIG.ImprovedCriticalEnchantmentEnabled) {
			register("improved_critical", new ImprovedCriticalEnchantment(Enchantment.Rarity.RARE, SpectrumCommon.locate("unlocks/enchantments/improved_critical"), EquipmentSlot.MAINHAND));
		}
		if (SpectrumCommon.CONFIG.InertiaEnchantmentEnabled) {
			register("inertia", new InertiaEnchantment(Enchantment.Rarity.VERY_RARE, SpectrumCommon.locate("unlocks/enchantments/inertia"), EquipmentSlot.MAINHAND));
		}
		if (SpectrumCommon.CONFIG.CloversFavorEnchantmentEnabled) {
			register("clovers_favor", new CloversFavorEnchantment(Enchantment.Rarity.RARE, SpectrumCommon.locate("unlocks/enchantments/clovers_favor"), EquipmentSlot.MAINHAND));
		}
		if (SpectrumCommon.CONFIG.SniperEnchantmentEnabled) {
			register("sniper", new SniperEnchantment(Enchantment.Rarity.VERY_RARE, SpectrumCommon.locate("unlocks/enchantments/sniper"), EquipmentSlot.MAINHAND));
		}
		if (SpectrumCommon.CONFIG.TightGripEnchantmentEnabled) {
			register("tight_grip", new TightGripEnchantment(Enchantment.Rarity.RARE, SpectrumCommon.locate("unlocks/enchantments/tight_grip"), EquipmentSlot.MAINHAND));
		}
		if (SpectrumCommon.CONFIG.SteadfastEnchantmentEnabled) {
			register("steadfast", new SteadfastEnchantment(Enchantment.Rarity.COMMON, SpectrumCommon.locate("unlocks/enchantments/steadfast"), EquipmentSlot.MAINHAND));
		}
		if (SpectrumCommon.CONFIG.IndestructibleEnchantmentEnabled) {
			register("indestructible", new IndestructibleEnchantment(Enchantment.Rarity.RARE, SpectrumCommon.locate("unlocks/enchantments/indestructible"), EquipmentSlot.MAINHAND));
		}
		if (SpectrumCommon.CONFIG.BigCatchEnchantmentEnabled) {
			register("big_catch", new BigCatchEnchantment(Enchantment.Rarity.RARE, SpectrumCommon.locate("unlocks/enchantments/big_catch"), EquipmentSlot.MAINHAND));
		}
		if (SpectrumCommon.CONFIG.RazingEnchantmentEnabled) {
			register("razing", new RazingEnchantment(Enchantment.Rarity.UNCOMMON, null, EquipmentSlot.MAINHAND));
		}
		if (SpectrumCommon.CONFIG.InexorableEnchantmentEnabled) {
			register("inexorable", new InexorableEnchantment(Enchantment.Rarity.VERY_RARE, null, EquipmentSlot.MAINHAND, EquipmentSlot.CHEST, EquipmentSlot.OFFHAND));
		}

	}

	private static SpectrumEnchantment register(String name, SpectrumEnchantment enchantment) {
		return Registry.register(Registries.ENCHANTMENT, SpectrumCommon.locate(name), enchantment);
	}

}
