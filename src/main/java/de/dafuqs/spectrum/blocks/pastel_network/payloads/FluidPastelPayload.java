package de.dafuqs.spectrum.blocks.pastel_network.payloads;

import com.mojang.blaze3d.vertex.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.neoforged.neoforge.capabilities.*;
import net.neoforged.neoforge.fluids.*;
import net.neoforged.neoforge.fluids.capability.*;
import net.neoforged.neoforge.items.*;
import org.jetbrains.annotations.*;

public record FluidPastelPayload(FluidStack fluidStack) implements PastelPayload {
	
	public static final MapCodec<FluidPastelPayload> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			FluidStack.CODEC.fieldOf("stack").forGetter(FluidPastelPayload::fluidStack)
	).apply(i, FluidPastelPayload::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, FluidPastelPayload> STREAM_CODEC = StreamCodec.composite(
			FluidStack.STREAM_CODEC, FluidPastelPayload::fluidStack,
			FluidPastelPayload::new
	);
	
	public void render(PastelTransmissionParticle particle, Level level, PoseStack poseStack, final MultiBufferSource vertexConsumers, int light) {
		particle.itemRenderer.renderStatic(this.fluidStack.getFluid().getBucket().getDefaultInstance(), ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, poseStack, vertexConsumers, level, 0);
	}
	
	public MapCodec<FluidPastelPayload> codec() {
		return CODEC;
	}
	
	public StreamCodec<RegistryFriendlyByteBuf, FluidPastelPayload> streamCodec() {
		return STREAM_CODEC;
	}
	
	@Override
	public void arriveAtDestination(Level level, BlockPos destination, @Nullable PastelNodeBlockEntity destinationNode) {
		if (destinationNode != null) {
			@Nullable IFluidHandler destinationHandler = FluidPastelPayloadType.getConnectedFluidStorage(destinationNode);
			if (destinationHandler != null) {
				destinationHandler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
			}
		}
	}
	
}
