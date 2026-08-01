package de.dafuqs.spectrum.blocks.pastel_network.payloads;

import com.mojang.blaze3d.vertex.*;
import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.particle.client.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.renderer.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;
import org.jspecify.annotations.Nullable;

public interface PastelPayload {
	
	Codec<PastelPayload> CODEC = SpectrumRegistries.PASTEL_PAYLOAD.byNameCodec().dispatch(PastelPayload::codec, codec -> codec);
	StreamCodec<RegistryFriendlyByteBuf, PastelPayload> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);
	
	MapCodec<? extends PastelPayload> codec();
	StreamCodec<RegistryFriendlyByteBuf, ? extends PastelPayload> streamCodec();
	
	/**
	 *
	 * @param level the level
	 * @param destination the position of the node to arrive at
	 * @param destinationNode the pastel node. Can be null if it was broken while the transmission was underway
	 */
	void arriveAtDestination(Level level, BlockPos destination, @Nullable PastelNodeBlockEntity destinationNode);
	
	void render(PastelTransmissionParticle pastelTransmissionParticle, Level level, PoseStack poseStack, MultiBufferSource vertexConsumers, int light);
	
	// void spawnTravelParticles(Level level); // TODO
	
}
