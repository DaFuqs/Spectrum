package de.dafuqs.spectrum.entity;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.entity.variants.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.syncher.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

public class SpectrumTrackedDataHandlerRegistry {
	
	private static final DeferredRegister<EntityDataSerializer<?>> REGISTRAR = DeferredRegister.create(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, SpectrumCommon.MOD_ID);
	
	public static final EntityDataSerializer<InkColor> INK_COLOR = EntityDataSerializer.forValueType(ByteBufCodecs.registry(SpectrumRegistries.INK_COLOR.key()));
	public static final EntityDataSerializer<GlassArrowVariant> GLASS_ARROW_VARIANT = EntityDataSerializer.forValueType(ByteBufCodecs.registry(SpectrumRegistries.GLASS_ARROW_VARIANT.key()));
	
	public static final EntityDataSerializer<LizardFrillVariant> LIZARD_FRILL_VARIANT = EntityDataSerializer.forValueType(ByteBufCodecs.registry(SpectrumRegistries.LIZARD_FRILL_VARIANT.key()));
	public static final EntityDataSerializer<LizardHornVariant> LIZARD_HORN_VARIANT = EntityDataSerializer.forValueType(ByteBufCodecs.registry(SpectrumRegistries.LIZARD_HORN_VARIANT.key()));
	public static final EntityDataSerializer<KindlingVariant> KINDLING_VARIANT = EntityDataSerializer.forValueType(ByteBufCodecs.registry(SpectrumRegistries.KINDLING_VARIANT.key()));
	
	public static void register(IEventBus eventBus) {
		REGISTRAR.register("ink_color", () -> INK_COLOR);
		REGISTRAR.register("glass_arrow_variant", () -> GLASS_ARROW_VARIANT);
		REGISTRAR.register("lizard_frill_variant", () -> LIZARD_FRILL_VARIANT);
		REGISTRAR.register("lizard_horn_variant", () -> LIZARD_HORN_VARIANT);
		REGISTRAR.register("kindling_variant", () -> KINDLING_VARIANT);
		REGISTRAR.register(eventBus);
	}
	
}
