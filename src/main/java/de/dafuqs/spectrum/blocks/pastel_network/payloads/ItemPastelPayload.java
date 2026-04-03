package de.dafuqs.spectrum.blocks.pastel_network.payloads;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.PastelNodeBlockEntity;
import de.dafuqs.spectrum.helpers.InWorldInteractionHelper;
import de.dafuqs.spectrum.particle.client.PastelTransmissionParticle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

public class ItemPastelPayload implements PastelPayload {
	
	public static final MapCodec<de.dafuqs.spectrum.blocks.pastel_network.payloads.ItemPastelPayload> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			ItemStack.CODEC.fieldOf("stack").forGetter(de.dafuqs.spectrum.blocks.pastel_network.payloads.ItemPastelPayload::getItemStack)
	).apply(i, de.dafuqs.spectrum.blocks.pastel_network.payloads.ItemPastelPayload::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, de.dafuqs.spectrum.blocks.pastel_network.payloads.ItemPastelPayload> STREAM_CODEC = StreamCodec.composite(
			ItemStack.STREAM_CODEC, de.dafuqs.spectrum.blocks.pastel_network.payloads.ItemPastelPayload::getItemStack,
			de.dafuqs.spectrum.blocks.pastel_network.payloads.ItemPastelPayload::new
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
	
	public MapCodec<de.dafuqs.spectrum.blocks.pastel_network.payloads.ItemPastelPayload> codec() {
		return CODEC;
	}
	
	public StreamCodec<RegistryFriendlyByteBuf, de.dafuqs.spectrum.blocks.pastel_network.payloads.ItemPastelPayload> streamCodec() {
		return STREAM_CODEC;
	}
}
