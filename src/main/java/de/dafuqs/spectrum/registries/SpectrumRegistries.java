package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
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
	
	private static final Identifier LIZARD_SCALE_VARIANT_ID = SpectrumCommon.locate("lizard_scale_variant");
	private static final Identifier LIZARD_FRILL_VARIANT_ID = SpectrumCommon.locate("lizard_frill_variant");
	private static final Identifier LIZARD_HORN_VARIANT_ID = SpectrumCommon.locate("lizard_horn_variant");
	
	public static final RegistryKey<Registry<LizardScaleVariant>> LIZARD_SCALE_VARIANT_KEY = RegistryKey.ofRegistry(LIZARD_SCALE_VARIANT_ID);
	public static final RegistryKey<Registry<LizardFrillVariant>> LIZARD_FRILL_VARIANT_KEY = RegistryKey.ofRegistry(LIZARD_FRILL_VARIANT_ID);
	public static final RegistryKey<Registry<LizardHornVariant>> LIZARD_HORN_VARIANT_KEY = RegistryKey.ofRegistry(LIZARD_HORN_VARIANT_ID);
	
	public static final Registry<LizardScaleVariant> LIZARD_SCALE_VARIANT = FabricRegistryBuilder.createSimple(LizardScaleVariant.class, LIZARD_SCALE_VARIANT_ID).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final Registry<LizardFrillVariant> LIZARD_FRILL_VARIANT = FabricRegistryBuilder.createSimple(LizardFrillVariant.class, LIZARD_FRILL_VARIANT_ID).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final Registry<LizardHornVariant> LIZARD_HORN_VARIANT = FabricRegistryBuilder.createSimple(LizardHornVariant.class, LIZARD_HORN_VARIANT_ID).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	
	public static final Registry<GlassArrowVariant> GLASS_ARROW_VARIANT = FabricRegistryBuilder.createSimple(GlassArrowVariant.class, SpectrumCommon.locate("glass_arrow_variant")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final Registry<ExplosionModifierType> EXPLOSION_MODIFIER_TYPES = FabricRegistryBuilder.createSimple(ExplosionModifierType.class, SpectrumCommon.locate("explosion_effect_family")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final Registry<ExplosionModifier> EXPLOSION_MODIFIERS = FabricRegistryBuilder.createSimple(ExplosionModifier.class, SpectrumCommon.locate("explosion_effect_modifier")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	
	public static <T> T getRandomTagEntry(Registry<T> registry, TagKey<T> tag, Random random, T fallback) {
		Optional<RegistryEntryList.Named<T>> naturals = registry.getEntryList(tag);
		if (naturals.isPresent()) {
			return naturals.get().get(random.nextInt(naturals.get().size())).value();
		} else {
			return fallback;
		}
	}
	
	public static void register() {
		GlassArrowVariant.init();
		LizardScaleVariant.init();
		LizardFrillVariant.init();
		LizardHornVariant.init();
	}
	
}
