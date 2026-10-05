package de.dafuqs.spectrum.blocks.pastel_network.payloads;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

public class SpectrumPastelPayloadTypes {
	
	public static final DeferredRegister<PastelPayloadType> REGISTRAR = DeferredRegister.create(SpectrumRegistryKeys.PASTEL_PAYLOAD_TYPE, SpectrumCommon.MOD_ID);
	
	public static final DeferredHolder<PastelPayloadType, ItemPastelPayloadType> ITEM = REGISTRAR.register("item", ItemPastelPayloadType::new);
	public static final DeferredHolder<PastelPayloadType, FluidPastelPayloadType> FLUID = REGISTRAR.register("fluid", FluidPastelPayloadType::new);
	public static final  DeferredHolder<PastelPayloadType, InkPastelPayloadType> INK = REGISTRAR.register("ink", InkPastelPayloadType::new);
	
	public static void register(IEventBus modBus) {
		REGISTRAR.register(modBus);
	}
	
}