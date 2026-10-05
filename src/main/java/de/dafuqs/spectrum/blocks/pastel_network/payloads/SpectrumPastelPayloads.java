package de.dafuqs.spectrum.blocks.pastel_network.payloads;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

public class SpectrumPastelPayloads {
	
	public static final DeferredRegister<MapCodec<? extends PastelPayload>> REGISTRAR = DeferredRegister.create(SpectrumRegistryKeys.PASTEL_PAYLOAD, SpectrumCommon.MOD_ID);
	
	public static final DeferredHolder<MapCodec<? extends PastelPayload>, MapCodec<ItemPastelPayload>> ITEM = REGISTRAR.register("item", () -> ItemPastelPayload.CODEC);
	public static final DeferredHolder<MapCodec<? extends PastelPayload>, MapCodec<FluidPastelPayload>> FLUID = REGISTRAR.register("fluid", () -> FluidPastelPayload.CODEC);
	public static final DeferredHolder<MapCodec<? extends PastelPayload>, MapCodec<InkPastelPayload>> INK = REGISTRAR.register("ink", () -> InkPastelPayload.CODEC);
	
	public static void register(IEventBus modBus) {
		REGISTRAR.register(modBus);
	}
	
}
