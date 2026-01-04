package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.enchantment.*;

@SuppressWarnings("unused")
public class SpectrumEnchantments {
	
	public static final ResourceKey<Enchantment> BIG_CATCH = of("big_catch"); // Increase the chance to reel in entities instead of fishing loot
	public static final ResourceKey<Enchantment> CLOVERS_FAVOR = of("clovers_favor"); // Increases drop chance of <1 loot drops
	public static final ResourceKey<Enchantment> DISARMING = of("disarming"); // Drops mob equipment on hit (and players, but way less often)
	public static final ResourceKey<Enchantment> EXUBERANCE = of("exuberance"); // Drops more XP on kill and when mining
	public static final ResourceKey<Enchantment> FIRST_STRIKE = of("first_strike"); // Increased damage if the enemy has full health
	public static final ResourceKey<Enchantment> FOUNDRY = of("foundry"); // applies smelting recipe before dropping items after mining
	public static final ResourceKey<Enchantment> IMPROVED_CRITICAL = of("improved_critical"); // Increased damage when landing a critical hit
	public static final ResourceKey<Enchantment> INDESTRUCTIBLE = of("indestructible"); // Make tools not use up durability
	public static final ResourceKey<Enchantment> INERTIA = of("inertia"); // Decreases mining speed, but increases with each mined block of the same type
	public static final ResourceKey<Enchantment> INEXORABLE = of("inexorable"); // prevents mining & movement slowdowns
	public static final ResourceKey<Enchantment> INVENTORY_INSERTION = of("inventory_insertion"); // don't drop items into the world, add to inv instead
	public static final ResourceKey<Enchantment> PEST_CONTROL = of("pest_control"); // Kills silverfish when mining infested blocks
	public static final ResourceKey<Enchantment> RAZING = of("razing"); // increased mining speed for very hard blocks
	public static final ResourceKey<Enchantment> RESONANCE = of("resonance"); // Silk Touch and more, just for different blocks
	public static final ResourceKey<Enchantment> SERENDIPITY_REEL = of("serendipity_reel"); // Chance to pull in multiple loot stacks when fishing
	public static final ResourceKey<Enchantment> SNIPING = of("sniping"); // Increases projectile speed => increased damage + range
	public static final ResourceKey<Enchantment> STEADFAST = of("steadfast"); // ItemStacks with this enchantment are not destroyed by cactus, fire, lava, ...
	public static final ResourceKey<Enchantment> TIGHT_GRIP = of("tight_grip"); // Increases attack speed
	public static final ResourceKey<Enchantment> TREASURE_HUNTER = of("treasure_hunter"); // Drops mob heads on kill
	public static final ResourceKey<Enchantment> VOIDING = of("voiding"); // Voids mined blocks
	
	private static ResourceKey<Enchantment> of(String name) {
		return ResourceKey.create(Registries.ENCHANTMENT, SpectrumCommon.locate(name));
	}
	
}
