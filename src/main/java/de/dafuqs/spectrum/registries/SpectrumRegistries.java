package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.deeper_down.weather.WeatherState;
import de.dafuqs.spectrum.entity.variants.*;
import de.dafuqs.spectrum.explosion.*;
import de.dafuqs.spectrum.items.tools.*;
import net.fabricmc.fabric.api.event.registry.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.*;
import net.minecraft.util.math.random.Random;

import java.util.*;

public class SpectrumRegistries {
	
	private static final Identifier INK_COLORS_ID = SpectrumCommon.locate("ink_color");
	public static final RegistryKey<Registry<InkColor>> INK_COLORS_KEY = RegistryKey.ofRegistry(INK_COLORS_ID);
	public static final Registry<InkColor> INK_COLORS = FabricRegistryBuilder.createSimple(INK_COLORS_KEY).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	private static final Identifier LIZARD_FRILL_VARIANT_ID = SpectrumCommon.locate("lizard_frill_variant");
	public static final RegistryKey<Registry<LizardFrillVariant>> LIZARD_FRILL_VARIANT_KEY = RegistryKey.ofRegistry(LIZARD_FRILL_VARIANT_ID);
	public static final Registry<LizardFrillVariant> LIZARD_FRILL_VARIANT = FabricRegistryBuilder.createSimple(LIZARD_FRILL_VARIANT_KEY).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	private static final Identifier LIZARD_HORN_VARIANT_ID = SpectrumCommon.locate("lizard_horn_variant");
	public static final RegistryKey<Registry<LizardHornVariant>> LIZARD_HORN_VARIANT_KEY = RegistryKey.ofRegistry(LIZARD_HORN_VARIANT_ID);
	public static final Registry<LizardHornVariant> LIZARD_HORN_VARIANT = FabricRegistryBuilder.createSimple(LIZARD_HORN_VARIANT_KEY).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	private static final Identifier KINDLING_VARIANT_ID = SpectrumCommon.locate("kindling_variant");
	public static final RegistryKey<Registry<KindlingVariant>> KINDLING_VARIANT_KEY = RegistryKey.ofRegistry(KINDLING_VARIANT_ID);
	public static final Registry<KindlingVariant> KINDLING_VARIANT = FabricRegistryBuilder.createSimple(KINDLING_VARIANT_KEY).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	private static final Identifier GLASS_ARROW_VARIANT_ID = SpectrumCommon.locate("glass_arrow_variant");
	public static final RegistryKey<Registry<GlassArrowVariant>> GLASS_ARROW_VARIANT_KEY = RegistryKey.ofRegistry(GLASS_ARROW_VARIANT_ID);
	public static final Registry<GlassArrowVariant> GLASS_ARROW_VARIANT = FabricRegistryBuilder.createSimple(GLASS_ARROW_VARIANT_KEY).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	private static final Identifier EXPLOSION_MODIFIER_TYPES_ID = SpectrumCommon.locate("explosion_effect_family");
	public static final RegistryKey<Registry<ExplosionModifierType>> EXPLOSION_MODIFIER_TYPES_KEY = RegistryKey.ofRegistry(EXPLOSION_MODIFIER_TYPES_ID);
	public static final Registry<ExplosionModifierType> EXPLOSION_MODIFIER_TYPES = FabricRegistryBuilder.createSimple(EXPLOSION_MODIFIER_TYPES_KEY).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	private static final Identifier EXPLOSION_MODIFIERS_ID = SpectrumCommon.locate("explosion_effect_modifier");
	public static final RegistryKey<Registry<ExplosionModifier>> EXPLOSION_MODIFIERS_KEY = RegistryKey.ofRegistry(EXPLOSION_MODIFIERS_ID);
	public static final Registry<ExplosionModifier> EXPLOSION_MODIFIERS = FabricRegistryBuilder.createSimple(EXPLOSION_MODIFIERS_KEY).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	private static final Identifier WEATHER_STATE_ID = SpectrumCommon.locate("weather_state");
	public static final RegistryKey<Registry<WeatherState>> WEATHER_STATE_KEY = RegistryKey.ofRegistry(WEATHER_STATE_ID);
	public static final Registry<WeatherState> WEATHER_STATES = FabricRegistryBuilder.createSimple(WEATHER_STATE_KEY).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	public static <T> T getRandomTagEntry(Registry<T> registry, TagKey<T> tag, Random random, T fallback) {
		Optional<RegistryEntryList.Named<T>> tagEntries = registry.getEntryList(tag);
		if (tagEntries.isPresent()) {
			return tagEntries.get().get(random.nextInt(tagEntries.get().size())).value();
		} else {
			return fallback;
		}
	}
	
	public static <T> List<RegistryEntry<T>> getEntries(Registry<T> registry, TagKey<T> tag) {
		Optional<RegistryEntryList.Named<T>> tagEntries = registry.getEntryList(tag);
		return tagEntries.map(registryEntries -> registryEntries.stream().toList()).orElseGet(List::of);
	}

	public static void register() {
		LizardFrillVariant.init();
		LizardHornVariant.init();
		KindlingVariant.init();
		GlassArrowVariant.init();
	}
	
}