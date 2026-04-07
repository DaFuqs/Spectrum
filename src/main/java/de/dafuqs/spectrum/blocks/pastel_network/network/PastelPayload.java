package de.dafuqs.spectrum.blocks.pastel_network.network;

import com.mojang.blaze3d.vertex.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.client.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.items.*;
import net.neoforged.neoforge.registries.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public interface PastelPayload {
	
	Codec<PastelPayload> CODEC = SpectrumRegistries.PASTEL_PAYLOAD_TYPE.byNameCodec().dispatch(PastelPayload::codec, codec -> codec);
	StreamCodec<RegistryFriendlyByteBuf, PastelPayload> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);
	
	DeferredRegister<MapCodec<? extends PastelPayload>> REGISTRAR = DeferredRegister.create(SpectrumRegistryKeys.PASTEL_PAYLOAD_TYPE, SpectrumCommon.MOD_ID);
	
	static void register(IEventBus modBus) {
		REGISTRAR.register("item", () -> ItemPastelPayload.CODEC);

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
	
	class ItemPastelPayload implements PastelPayload {
		
		public static final MapCodec<ItemPastelPayload> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				ItemStack.CODEC.fieldOf("stack").forGetter(ItemPastelPayload::getItemStack)
		).apply(i, ItemPastelPayload::new));
		
		public static final StreamCodec<RegistryFriendlyByteBuf, ItemPastelPayload> STREAM_CODEC = StreamCodec.composite(
				ItemStack.STREAM_CODEC, ItemPastelPayload::getItemStack,
				ItemPastelPayload::new
		);
		
		private final ItemStack itemStack;
		
		public ItemPastelPayload(ItemStack itemStack) {
			this.itemStack = itemStack;
		}
		
		public ItemStack getItemStack() {
			return itemStack;
		}
		
		public void arriveAtDestination(Level level, BlockPos destination, @Nullable PastelNodeBlockEntity destinationNode) {
			int inserted = 0;
			int count = itemStack.getCount();
			if (destinationNode != null) {
				IItemHandler destinationStorage = destinationNode.getConnectedStorage();
				if (destinationStorage != null) {
					inserted = count;
					inserted -= ItemHandlerHelper.insertItemStacked(destinationStorage, itemStack.copyWithCount(count), false).getCount();
					destinationNode.addItemCountUnderway(-count);
				}
			}
			
			int amount = itemStack.getCount();
			if (inserted != amount) {
				long diff = amount - inserted;
				InWorldInteractionHelper.scatter(level, destination.getX() + 0.5, destination.getY() + 0.5, destination.getZ() + 0.5, itemStack, diff);
				if (destinationNode != null) {
					destinationNode.addItemCountUnderway(-diff);
				}
			}
		}
		
		public void render(PastelTransmissionParticle particle, Level level, PoseStack poseStack, final MultiBufferSource vertexConsumers, int light) {
			particle.itemRenderer.renderStatic(this.itemStack, ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, poseStack, vertexConsumers, level, 0);
		}
		
		public MapCodec<ItemPastelPayload> codec() {
			return CODEC;
		}
		
		public StreamCodec<RegistryFriendlyByteBuf, ItemPastelPayload> streamCodec() {
			return STREAM_CODEC;
		}
	}
	
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
