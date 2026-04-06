package de.dafuqs.spectrum.blocks.pastel_network.payloads;

import com.mojang.blaze3d.vertex.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.particle.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.neoforged.neoforge.fluids.*;
import net.neoforged.neoforge.fluids.capability.*;
import org.jetbrains.annotations.*;

import java.util.*;

public record InkPastelPayload(List<InkAmount> inkAmount) implements PastelPayload {
	
	public static final MapCodec<InkPastelPayload> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			InkAmount.CODEC.listOf().fieldOf("stack").forGetter(InkPastelPayload::inkAmount)
	).apply(i, InkPastelPayload::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, InkPastelPayload> STREAM_CODEC = StreamCodec.composite(
			InkAmount.STREAM_CODEC.apply(ByteBufCodecs.list()), InkPastelPayload::inkAmount,
			InkPastelPayload::new
	);
	
	public void render(PastelTransmissionParticle particle, Level level, PoseStack poseStack, final MultiBufferSource vertexConsumers, int light) {
		particle.itemRenderer.renderStatic(PigmentItem.byColor(this.inkAmount.get(0).color()).getDefaultInstance(), ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, poseStack, vertexConsumers, level, 0);
	}
	
	public MapCodec<InkPastelPayload> codec() {
		return CODEC;
	}
	
	public StreamCodec<RegistryFriendlyByteBuf, InkPastelPayload> streamCodec() {
		return STREAM_CODEC;
	}
	
	@Override
	public void arriveAtDestination(Level level, BlockPos destination, @Nullable PastelNodeBlockEntity destinationNode) {
		if (destinationNode != null) {
			@Nullable InkStorageBlockEntity<?> destinationHandler = InkPastelPayloadType.getConnectedInkStorage(destinationNode);
			if (destinationHandler != null) {
				InkStorage destinationStorage = destinationHandler.getEnergyStorage();
				for(InkAmount ic : inkAmount) {
					destinationStorage.addEnergy(ic.color(), ic.amount());
					destinationHandler.setInkDirty();
				}
			}
		}
	}
	
}
