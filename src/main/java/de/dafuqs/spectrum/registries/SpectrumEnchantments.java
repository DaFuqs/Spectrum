package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.enchantments.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.registry.*;

public class SpectrumEnchantments {
	
	public static final SpectrumEnchantment RESONANCE = new ResonanceEnchantment(Enchantment.Rarity.VERY_RARE, SpectrumCommon.locate("unlocks/enchantments/resonance"), EquipmentSlot.MAINHAND); // Silk Touch, just for different blocks
	public static final SpectrumEnchantment VOIDING = new VoidingEnchantment(Enchantment.Rarity.RARE, SpectrumCommon.locate("spectrum"), EquipmentSlot.MAINHAND); // Voids all items mined
	public static final SpectrumEnchantment PEST_CONTROL = new PestControlEnchantment(Enchantment.Rarity.VERY_RARE, SpectrumCommon.locate("unlocks/enchantments/pest_control"), EquipmentSlot.MAINHAND); // Kills silverfish when mining infested blocks
	public static final SpectrumEnchantment FOUNDRY = new FoundryEnchantment(Enchantment.Rarity.RARE, SpectrumCommon.locate("unlocks/enchantments/autosmelt"), EquipmentSlot.MAINHAND); // applies smelting recipe before dropping items after mining
	public static final SpectrumEnchantment INVENTORY_INSERTION = new InventoryInsertionEnchantment(Enchantment.Rarity.RARE, SpectrumCommon.locate("unlocks/enchantments/inventory_insertion"), EquipmentSlot.MAINHAND); // don't drop items into the world, add to inv instead
	public static final SpectrumEnchantment EXUBERANCE = new ExuberanceEnchantment(Enchantment.Rarity.UNCOMMON, SpectrumCommon.locate("unlocks/enchantments/exuberance"), EquipmentSlot.MAINHAND); // Drops more XP on kill
	public static final SpectrumEnchantment TREASURE_HUNTER = new TreasureHunterEnchantment(Enchantment.Rarity.RARE, SpectrumCommon.locate("unlocks/enchantments/treasure_hunter"), EquipmentSlot.MAINHAND); // Drops mob heads
	public static final SpectrumEnchantment DISARMING = new DisarmingEnchantment(Enchantment.Rarity.VERY_RARE, SpectrumCommon.locate("unlocks/enchantments/disarming"), EquipmentSlot.MAINHAND); // Drops mob equipment on hit (and players, but way less often)
	public static final SpectrumEnchantment FIRST_STRIKE = new FirstStrikeEnchantment(Enchantment.Rarity.RARE, SpectrumCommon.locate("unlocks/enchantments/first_strike"), EquipmentSlot.MAINHAND); // Increased damage if enemy has full health
	public static final SpectrumEnchantment IMPROVED_CRITICAL = new ImprovedCriticalEnchantment(Enchantment.Rarity.RARE, SpectrumCommon.locate("unlocks/enchantments/improved_critical"), EquipmentSlot.MAINHAND); // Increased damage when landing a critical hit
	public static final SpectrumEnchantment INERTIA = new InertiaEnchantment(Enchantment.Rarity.VERY_RARE, SpectrumCommon.locate("unlocks/enchantments/inertia"), EquipmentSlot.MAINHAND); // Decreases mining speed, but increases with each mined block of the same type
	public static final SpectrumEnchantment CLOVERS_FAVOR = new CloversFavorEnchantment(Enchantment.Rarity.RARE, SpectrumCommon.locate("unlocks/enchantments/clovers_favor"), EquipmentSlot.MAINHAND); // Increases drop chance of <1 loot drops
	public static final SpectrumEnchantment SNIPER = new SniperEnchantment(Enchantment.Rarity.VERY_RARE, SpectrumCommon.locate("unlocks/enchantments/sniper"), EquipmentSlot.MAINHAND); // Increases projectile speed => increased damage + range
	public static final SpectrumEnchantment TIGHT_GRIP = new TightGripEnchantment(Enchantment.Rarity.RARE, SpectrumCommon.locate("unlocks/enchantments/tight_grip"), EquipmentSlot.MAINHAND); // Increases attack speed
	public static final SpectrumEnchantment STEADFAST = new SteadfastEnchantment(Enchantment.Rarity.COMMON, SpectrumCommon.locate("unlocks/enchantments/steadfast"), EquipmentSlot.MAINHAND); // ItemStacks with this enchantment are not destroyed by cactus, fire, lava, ...
	public static final SpectrumEnchantment INDESTRUCTIBLE = new IndestructibleEnchantment(Enchantment.Rarity.RARE, SpectrumCommon.locate("unlocks/enchantments/indestructible"), EquipmentSlot.MAINHAND); // Make tools not use up durability
	public static final SpectrumEnchantment BIG_CATCH = new BigCatchEnchantment(Enchantment.Rarity.RARE, SpectrumCommon.locate("unlocks/enchantments/big_catch"), EquipmentSlot.MAINHAND); // Increase the chance to reel in entities instead of fishing loot
	public static final SpectrumEnchantment RAZING = new RazingEnchantment(Enchantment.Rarity.UNCOMMON, SpectrumCommon.locate("unlocks/enchantments/razing"), EquipmentSlot.MAINHAND); // increased mining speed for very hard blocks
	public static final SpectrumEnchantment INEXORABLE = new InexorableEnchantment(Enchantment.Rarity.VERY_RARE, SpectrumCommon.locate("unlocks/enchantments/inexorable"), EquipmentSlot.MAINHAND, EquipmentSlot.CHEST, EquipmentSlot.OFFHAND); // prevents mining & movement slowdowns
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
