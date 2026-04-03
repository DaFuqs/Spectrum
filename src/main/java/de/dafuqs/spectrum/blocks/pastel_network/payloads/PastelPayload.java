package de.dafuqs.spectrum.blocks.pastel_network.payloads;

import com.mojang.blaze3d.vertex.*;
import com.mojang.serialization.*;
import com.sammy.malum.registry.common.worldgen.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.particle.client.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.renderer.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.feature.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

public interface PastelPayload {
	
	Codec<PastelPayload> CODEC = SpectrumRegistries.PASTEL_PAYLOAD_TYPE.byNameCodec().dispatch(PastelPayload::codec, codec -> codec);
	StreamCodec<RegistryFriendlyByteBuf, PastelPayload> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);
	
	DeferredRegister<MapCodec<? extends PastelPayload>> REGISTRAR = DeferredRegister.create(SpectrumRegistryKeys.PASTEL_PAYLOAD_TYPE, SpectrumCommon.MOD_ID);
	
	DeferredHolder<MapCodec<? extends PastelPayload>, MapCodec<ItemPastelPayload>> ITEM = REGISTRAR.register("item", () -> ItemPastelPayload.CODEC);
	//DeferredHolder<MapCodec<? extends PastelPayload>, MapCodec<FluidPastelPayload>> FLUID = REGISTRAR.register("fluid", () -> FluidPastelPayload.CODEC);
	//DeferredHolder<MapCodec<? extends PastelPayload>, MapCodec<InkPastelPayload>> INK = REGISTRAR.register("ink", () -> InkPastelPayload.CODEC);
	
	static void register(IEventBus modBus) {
		REGISTRAR.register(modBus);
	}
	
	MapCodec<? extends PastelPayload> codec();
	StreamCodec<RegistryFriendlyByteBuf, ? extends PastelPayload> streamCodec();
	
	/**
	 *
	 * @param level the level
	 * @param destination the position of the node to arrive at
	 * @param destinationNode the pastel node. Can be null if it was broken while the transmission was underway
	 */
	void arriveAtDestination(Level level, BlockPos destination, PastelNodeBlockEntity destinationNode);
	
	void render(PastelTransmissionParticle pastelTransmissionParticle, Level level, PoseStack poseStack, MultiBufferSource vertexConsumers, int light);
	
	// void spawnTravelParticles(Level level); // TODO
	
	/*class FluidPastelPayload implements PastelPayload {
		private FluidStack fluidStack;
		public FluidPastelPayload(FluidStack fluidStack) {
			this.fluidStack = fluidStack;
		}
	}
	
	class InkPastelPayload implements PastelPayload {
		private InkAmount inkAmount;
		public InkPastelPayload(InkAmount inkAmount) {
			this.inkAmount = inkAmount;
		}
	}*/
	
}
