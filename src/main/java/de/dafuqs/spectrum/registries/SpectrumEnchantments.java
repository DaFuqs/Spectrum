package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.enchantments.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumEnchantments {
	
	public static final SpectrumEnchantment RESONANCE = new ResonanceEnchantment(Enchantment.Rarity.VERY_RARE, new Identifier(SpectrumCommon.MOD_ID, "progression/enchantments/resonance"), EquipmentSlot.MAINHAND); // Silk Touch, just for different blocks
	public static final SpectrumEnchantment PEST_CONTROL = new PestControlEnchantment(Enchantment.Rarity.VERY_RARE, new Identifier(SpectrumCommon.MOD_ID, "progression/enchantments/pest_control"), EquipmentSlot.MAINHAND); // Kills silverfish when mining infested blocks
	public static final SpectrumEnchantment FOUNDRY = new AutoSmeltEnchantment(Enchantment.Rarity.RARE, new Identifier(SpectrumCommon.MOD_ID, "progression/enchantments/autosmelt"), EquipmentSlot.MAINHAND); // applies smelting recipe before dropping items after mining
	public static final SpectrumEnchantment INVENTORY_INSERTION = new InventoryInsertionEnchantment(Enchantment.Rarity.RARE, new Identifier(SpectrumCommon.MOD_ID, "progression/enchantments/inventory_insertion"), EquipmentSlot.MAINHAND); // don't drop items into the world, add to inv instead
	public static final SpectrumEnchantment VOIDING = new VoidingEnchantment(Enchantment.Rarity.RARE, new Identifier(SpectrumCommon.MOD_ID, "spectrum"), EquipmentSlot.MAINHAND); // Voids all items mined
	public static final SpectrumEnchantment EXUBERANCE = new ExuberanceEnchantment(Enchantment.Rarity.UNCOMMON, new Identifier(SpectrumCommon.MOD_ID, "progression/enchantments/exuberance"), EquipmentSlot.MAINHAND); // Drops more XP on kill
	public static final SpectrumEnchantment TREASURE_HUNTER = new TreasureHunterEnchantment(Enchantment.Rarity.RARE, new Identifier(SpectrumCommon.MOD_ID, "progression/enchantments/treasure_hunter"), EquipmentSlot.MAINHAND); // Drops mob heads
	public static final SpectrumEnchantment FIRST_STRIKE = new FirstStrikeEnchantment(Enchantment.Rarity.RARE, new Identifier(SpectrumCommon.MOD_ID, "progression/enchantments/first_strike"), EquipmentSlot.MAINHAND); // Increased damage if enemy has full health
	public static final SpectrumEnchantment IMPROVED_CRITICAL = new ImprovedCriticalEnchantment(Enchantment.Rarity.RARE, new Identifier(SpectrumCommon.MOD_ID, "progression/enchantments/improved_critical"), EquipmentSlot.MAINHAND); // Increased damage when landing a critical hit
	public static final SpectrumEnchantment INERTIA = new InertiaEnchantment(Enchantment.Rarity.VERY_RARE, new Identifier(SpectrumCommon.MOD_ID, "progression/enchantments/inertia"), EquipmentSlot.MAINHAND); // Decreases mining speed, but increases with each mined block of the same type
	public static final SpectrumEnchantment CLOVERS_FAVOR = new CloversFavorEnchantment(Enchantment.Rarity.RARE, new Identifier(SpectrumCommon.MOD_ID, "progression/enchantments/clovers_favor"), EquipmentSlot.MAINHAND); // Increases drop chance of <1 loot drops
	public static final SpectrumEnchantment TIGHT_GRIP = new TightGripEnchantment(Enchantment.Rarity.RARE, new Identifier(SpectrumCommon.MOD_ID, "progression/enchantments/tight_grip"), EquipmentSlot.MAINHAND); // Increases attack speed
	public static final SpectrumEnchantment DISARMING = new DisarmingEnchantment(Enchantment.Rarity.VERY_RARE, new Identifier(SpectrumCommon.MOD_ID, "progression/enchantments/disarming"), EquipmentSlot.MAINHAND); // Drops mob equipment on hit (and players, but way less often)
	public static final SpectrumEnchantment SNIPER = new SniperEnchantment(Enchantment.Rarity.VERY_RARE, new Identifier(SpectrumCommon.MOD_ID, "progression/enchantments/sniper"), EquipmentSlot.MAINHAND); // Increases projectile speed => increased damage + range
	public static final SpectrumEnchantment STEADFAST = new DamageProofEnchantment(Enchantment.Rarity.COMMON, new Identifier(SpectrumCommon.MOD_ID, "progression/enchantments/steadfast"), EquipmentSlot.MAINHAND); // ItemStacks with this enchantment are not destroyed by cactus, fire, lava, ...
	public static final SpectrumEnchantment INDESTRUCTIBLE = new IndestructibleEnchantment(Enchantment.Rarity.RARE, new Identifier(SpectrumCommon.MOD_ID, "progression/enchantments/indestructible"), EquipmentSlot.MAINHAND); // Make tools not use up durability
	
	public static void register() {
		Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "resonance"), RESONANCE);
		Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "voiding"), VOIDING);
		
		if (SpectrumCommon.CONFIG.PestControlEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "pest_control"), PEST_CONTROL);
		}
		if (SpectrumCommon.CONFIG.AutoSmeltEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "autosmelt"), FOUNDRY);
		}
		if (SpectrumCommon.CONFIG.InventoryInsertionEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "inventory_insertion"), INVENTORY_INSERTION);
		}
		if (SpectrumCommon.CONFIG.ExuberanceEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "exuberance"), EXUBERANCE);
		}
		if (SpectrumCommon.CONFIG.TreasureHunterEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "treasure_hunter"), TREASURE_HUNTER);
		}
		if (SpectrumCommon.CONFIG.DisarmingEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "disarming"), DISARMING);
		}
		if (SpectrumCommon.CONFIG.FirstStrikeEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "first_strike"), FIRST_STRIKE);
		}
		if (SpectrumCommon.CONFIG.ImprovedCriticalEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "improved_critical"), IMPROVED_CRITICAL);
		}
		if (SpectrumCommon.CONFIG.InertiaEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "inertia"), INERTIA);
		}
		if (SpectrumCommon.CONFIG.CloversFavorEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "clovers_favor"), CLOVERS_FAVOR);
		}
		if (SpectrumCommon.CONFIG.SniperEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "sniper"), SNIPER);
		}
		if (SpectrumCommon.CONFIG.TightGripEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "tight_grip"), TIGHT_GRIP);
		}
		if (SpectrumCommon.CONFIG.SteadfastEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "steadfast"), STEADFAST);
		}
		if (SpectrumCommon.CONFIG.IndestructibleEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "indestructible"), INDESTRUCTIBLE);
		}
	}
	
}
