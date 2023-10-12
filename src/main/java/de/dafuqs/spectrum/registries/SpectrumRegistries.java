package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.entity.variants.*;
import de.dafuqs.spectrum.explosion.*;
import de.dafuqs.spectrum.items.tools.*;
import net.fabricmc.fabric.api.event.registry.*;
import net.minecraft.tag.*;
import net.minecraft.util.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.*;

import java.util.*;

public class SpectrumRegistries {
	
	private static final Identifier INK_COLORS_ID = SpectrumCommon.locate("ink_color");
	public static final RegistryKey<Registry<InkColor>> INK_COLORS_KEY = RegistryKey.ofRegistry(INK_COLORS_ID);
	public static final Registry<InkColor> INK_COLORS = FabricRegistryBuilder.createSimple(InkColor.class, INK_COLORS_ID).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	
	private static final Identifier LIZARD_FRILL_VARIANT_ID = SpectrumCommon.locate("lizard_frill_variant");
	private static final Identifier LIZARD_HORN_VARIANT_ID = SpectrumCommon.locate("lizard_horn_variant");
	
	public static final RegistryKey<Registry<LizardFrillVariant>> LIZARD_FRILL_VARIANT_KEY = RegistryKey.ofRegistry(LIZARD_FRILL_VARIANT_ID);
	public static final RegistryKey<Registry<LizardHornVariant>> LIZARD_HORN_VARIANT_KEY = RegistryKey.ofRegistry(LIZARD_HORN_VARIANT_ID);
	
	public static final Registry<LizardFrillVariant> LIZARD_FRILL_VARIANT = FabricRegistryBuilder.createSimple(LizardFrillVariant.class, LIZARD_FRILL_VARIANT_ID).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final Registry<LizardHornVariant> LIZARD_HORN_VARIANT = FabricRegistryBuilder.createSimple(LizardHornVariant.class, LIZARD_HORN_VARIANT_ID).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	
	public static final Registry<GlassArrowVariant> GLASS_ARROW_VARIANT = FabricRegistryBuilder.createSimple(GlassArrowVariant.class, SpectrumCommon.locate("glass_arrow_variant")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final Registry<ExplosionModifierType> EXPLOSION_MODIFIER_TYPES = FabricRegistryBuilder.createSimple(ExplosionModifierType.class, SpectrumCommon.locate("explosion_effect_family")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final Registry<ExplosionModifier> EXPLOSION_MODIFIERS = FabricRegistryBuilder.createSimple(ExplosionModifier.class, SpectrumCommon.locate("explosion_effect_modifier")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	
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
		
		GlassArrowVariant.init();
	}
	
}
