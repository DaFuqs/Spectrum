package de.dafuqs.spectrum.blocks.pastel_network.payloads;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.resources.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

public abstract class PastelPayloadType {
	
	public static final DeferredRegister<PastelPayloadType> REGISTRAR = DeferredRegister.create(SpectrumRegistryKeys.PASTEL_PAYLOAD_TYPE, SpectrumCommon.MOD_ID);
	public static final DeferredHolder<PastelPayloadType, ItemPastelPayloadType> ITEM = REGISTRAR.register("item", ItemPastelPayloadType::new);
	//DeferredHolder<MapCodec<? extends PastelPayload>, MapCodec<FluidPastelPayload>> FLUID = REGISTRAR.register("fluid", () -> FluidPastelPayload.CODEC);
	//DeferredHolder<MapCodec<? extends PastelPayload>, MapCodec<InkPastelPayload>> INK = REGISTRAR.register("ink", () -> InkPastelPayload.CODEC);
	
	public static void register(IEventBus modBus) {
		REGISTRAR.register(modBus);
	}
	
	protected final ResourceLocation id;
	protected final MapCodec<? extends PastelPayload> codec;
	
	public PastelPayloadType(ResourceLocation id, MapCodec<? extends PastelPayload> codec) {
		this.id = id;
		this.codec = codec;
	}
	
	public abstract void tryTransferToType(PastelTransmissionLogic logic, PastelNodeBlockEntity sourceNode, PastelNodeType type, PastelTransmissionLogic.TransferMode transferMode);
	
}