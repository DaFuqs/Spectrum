package de.dafuqs.spectrum.explosion;

import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.item.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ExplosionModifierProviders {
	
	protected static Map<Item, ExplosionModifier> PROVIDERS = new Object2ObjectOpenHashMap<>();
	
	public static @Nullable ExplosionModifier get(ItemStack stack) {
		return PROVIDERS.getOrDefault(stack.getItem(), null);
	}
	
	public static void registerModifier(ItemConvertible provider, ExplosionModifier modifier) {
		PROVIDERS.put(provider.asItem(), modifier);
	}
	
	public static Set<Item> getProviders() {
		return PROVIDERS.keySet();
	}
	
	public static void register() {
		registerModifier(Items.FIRE_CHARGE, SpectrumExplosionEffects.FIRE);
		registerModifier(Items.SOUL_SAND, SpectrumExplosionEffects.SOUL_FIRE);
		registerModifier(SpectrumBlocks.INCANDESCENT_AMALGAM, SpectrumExplosionEffects.EXPLOSION_BOOST);
		registerModifier(SpectrumItems.STORM_STONE, SpectrumExplosionEffects.LIGHTNING);
		registerModifier(SpectrumItems.NEOLITH, SpectrumExplosionEffects.MAGIC);
		registerModifier(SpectrumItems.MIDNIGHT_CHIP, SpectrumExplosionEffects.LOOTING);
		registerModifier(SpectrumItems.MIDNIGHT_ABERRATION, SpectrumExplosionEffects.LOOTING);
		registerModifier(SpectrumItems.REFINED_BLOODSTONE, SpectrumExplosionEffects.PRIMORDIAL_FIRE);
		registerModifier(Items.CHORUS_FRUIT, SpectrumExplosionEffects.STARRY);
	}
	
}
