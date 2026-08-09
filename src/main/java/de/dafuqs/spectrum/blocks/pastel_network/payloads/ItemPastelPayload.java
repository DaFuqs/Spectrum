package de.dafuqs.spectrum.blocks.pastel_network.payloads;

import com.mojang.blaze3d.vertex.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.client.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.items.*;
import org.jspecify.annotations.*;

public record ItemPastelPayload(ItemStack itemStack) implements PastelPayload {
	
	public static final MapCodec<ItemPastelPayload> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			ItemStack.CODEC.fieldOf("stack").forGetter(ItemPastelPayload::itemStack)
	).apply(i, ItemPastelPayload::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, ItemPastelPayload> STREAM_CODEC = StreamCodec.composite(
			ItemStack.STREAM_CODEC, ItemPastelPayload::itemStack,
			ItemPastelPayload::new
	);
	
	public void renderAfterEntities(PastelTransmissionParticle particle, Level level, PoseStack poseStack, final MultiBufferSource vertexConsumers, Camera camera, float tickDelta) {
		Vec3 cameraPos = camera.getPosition();
		Vec3 pos = particle.getPos();
		
		poseStack.pushPose();
		
		poseStack.translate(pos.x() - cameraPos.x, pos.y() - cameraPos.y, pos.z() - cameraPos.z);
		int light = particle.getLightColor(tickDelta);
		poseStack.mulPose(camera.rotation());
		poseStack.scale(0.65F, 0.65F, 0.65F);
		poseStack.translate(0, -0.15, 0);
		particle.itemRenderer.renderStatic(this.itemStack, ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, poseStack, vertexConsumers, level, 0);
		poseStack.popPose();
	}
	
	public MapCodec<ItemPastelPayload> codec() {
		return CODEC;
	}
	
	public StreamCodec<RegistryFriendlyByteBuf, ItemPastelPayload> streamCodec() {
		return STREAM_CODEC;
	}
	
	@Override
	public void arriveAtDestination(Level level, BlockPos destination, @Nullable PastelNodeBlockEntity destinationNode) {
		int inserted = 0;
		int count = itemStack.getCount();
		if (destinationNode != null) {
			IItemHandler destinationStorage = ItemPastelPayloadType.getConnectedItemStorage(destinationNode);
			if (destinationStorage != null) {
				inserted = count;
				inserted -= ItemHandlerHelper.insertItemStacked(destinationStorage, itemStack.copyWithCount(count), false).getCount();
				destinationNode.addUnderway(SpectrumPastelPayloadTypes.ITEM.getKey(), -count);
			}
		}
		
		int amount = itemStack.getCount();
		if (inserted != amount) {
			long diff = amount - inserted;
			InWorldInteractionHelper.scatter(level, destination.getX() + 0.5, destination.getY() + 0.5, destination.getZ() + 0.5, itemStack, diff);
			if (destinationNode != null) {
				destinationNode.addUnderway(SpectrumPastelPayloadTypes.ITEM.getKey(), -diff);
			}
		}
	}
	
}
