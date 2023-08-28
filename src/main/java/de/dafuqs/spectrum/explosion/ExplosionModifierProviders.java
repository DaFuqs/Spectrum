package de.dafuqs.spectrum.explosion;

import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.item.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ExplosionModifierProviders {
	
	protected static Map<Item, ExplosionModifier> MODIFIERS = new Object2ObjectOpenHashMap<>();
	
	public static @Nullable ExplosionModifier getModifier(ItemStack stack) {
		return MODIFIERS.getOrDefault(stack.getItem(), null);
	}
	
	public static void registerForModifier(ItemConvertible provider, ExplosionModifier modifier) {
		MODIFIERS.put(provider.asItem(), modifier);
	}
	
	
	protected static Map<Item, ExplosionArchetype> ARCHETYPES = new Object2ObjectOpenHashMap<>();
	
	public static @Nullable ExplosionArchetype getArchetype(ItemStack stack) {
		return ARCHETYPES.getOrDefault(stack.getItem(), null);
	}
	
	public static void registerForArchetype(ItemConvertible provider, ExplosionArchetype modifier) {
		ARCHETYPES.put(provider.asItem(), modifier);
	}
	
	public static Set<Item> getProviders() {
		Set<Item> set = new HashSet<>();
		set.addAll(ARCHETYPES.keySet());
		set.addAll(MODIFIERS.keySet());
		return set;
	}
	
	
	public static void register() {
		registerForArchetype(Items.GLOWSTONE_DUST, ExplosionArchetype.DAMAGE_ENTITIES);
		registerForArchetype(Items.GUNPOWDER, ExplosionArchetype.DESTROY_BLOCKS);
		registerForArchetype(SpectrumItems.MIDNIGHT_ABERRATION, ExplosionArchetype.ALL);
		
		registerForModifier(Items.FIRE_CHARGE, ExplosionModifiers.FIRE);
		registerForModifier(Items.TNT, ExplosionModifiers.EXPLOSION_BOOST);
		registerForModifier(SpectrumItems.STORM_STONE, ExplosionModifiers.LIGHTNING);
		registerForModifier(SpectrumItems.NEOLITH, ExplosionModifiers.MAGIC);
		//registerModifier(SpectrumItems.MIDNIGHT_CHIP, ExplosionModifiers.LOOTING);
		registerForModifier(SpectrumBlocks.INCANDESCENT_AMALGAM, ExplosionModifiers.INCANDESCENCE);
		registerForModifier(SpectrumItems.REFINED_BLOODSTONE, ExplosionModifiers.PRIMORDIAL_FIRE);
		registerForModifier(Items.CHORUS_FRUIT, ExplosionModifiers.STARRY);
		registerForModifier(Items.END_ROD, ExplosionModifiers.KILL_ZONE);
		
	}
	
}
