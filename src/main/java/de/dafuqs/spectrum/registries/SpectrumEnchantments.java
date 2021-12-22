package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.enchantments.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumEnchantments {

	public static final Enchantment RESONANCE = new ResonanceEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND); // Silk Touch, just for different blocks
	public static final Enchantment PEST_CONTROL = new PestControlEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND); // Kills silverfish when mining infested blocks
	public static final Enchantment AUTO_SMELT = new AutoSmeltEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND); // applies smelting recipe before dropping items after mining
	public static final Enchantment INVENTORY_INSERTION = new InventoryInsertionEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND); // don't drop items into the world, add to inv instead
	public static final Enchantment VOIDING = new VoidingEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND); // Voids all items mined
	public static final Enchantment EXUBERANCE = new ExuberanceEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND); // Drops more XP on kill
	public static final Enchantment TREASURE_HUNTER = new TreasureHunterEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND); // Drops mob heads
	public static final Enchantment FIRST_STRIKE = new FirstStrikeEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND); // Increased damage if enemy has full health
	public static final Enchantment IMPROVED_CRITICAL = new ImprovedCriticalEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND); // Increased damage when landing a critical hit
	public static final Enchantment INERTIA = new InertiaEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND); // Decreases mining speed, but increases with each mined block of the same type
	public static final Enchantment CLOVERS_FAVOR = new CloversFavorEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND); // Increases drop chance of <1 loot drops
	public static final Enchantment TIGHT_GRIP = new TightGripEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND); // Increases attack speed
	public static final Enchantment DISARMING = new DisarmingEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND); // Drops mob equipment on hit (and players, but way less often)
	public static final Enchantment SNIPER = new SniperEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND); // Increases projectile speed => increased damage + range
	public static final Enchantment DAMAGE_PROOF = new DamageProofEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND); // ItemStacks with this enchantment are not destroyed by cactus, fire, lava, ...

	public static void register() {
		Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "resonance"), RESONANCE);
		
		if(SpectrumCommon.CONFIG.PestControlEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "pest_control"), PEST_CONTROL);
		}
		if(SpectrumCommon.CONFIG.AutoSmeltEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "autosmelt"), AUTO_SMELT);
		}
		if(SpectrumCommon.CONFIG.InventoryInsertionEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "inventory_insertion"), INVENTORY_INSERTION);
		}
		if(SpectrumCommon.CONFIG.VoidingEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "voiding"), VOIDING);
		}
		if(SpectrumCommon.CONFIG.ExuberanceEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "exuberance"), EXUBERANCE);
		}
		if(SpectrumCommon.CONFIG.TreasureHunterEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "treasure_hunter"), TREASURE_HUNTER);
		}
		if(SpectrumCommon.CONFIG.DisarmingEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "disarming"), DISARMING);
		}
		if(SpectrumCommon.CONFIG.FirstStrikeEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "first_strike"), FIRST_STRIKE);
		}
		if(SpectrumCommon.CONFIG.ImprovedCriticalEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "improved_critical"), IMPROVED_CRITICAL);
		}
		if(SpectrumCommon.CONFIG.InertiaEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "inertia"), INERTIA);
		}
		if(SpectrumCommon.CONFIG.CloversFavorEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "clovers_favor"), CLOVERS_FAVOR);
		}
		if(SpectrumCommon.CONFIG.SniperEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "sniper"), SNIPER);
		}
		if(SpectrumCommon.CONFIG.TightGripEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "tight_grip"), TIGHT_GRIP);
		}
		if(SpectrumCommon.CONFIG.DamageProofEnchantmentEnabled) {
			Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "damage_proof"), DAMAGE_PROOF);
		}
	}

}
