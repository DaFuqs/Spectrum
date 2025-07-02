package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import org.jetbrains.annotations.*;

public record TypedTransmissionPayload(TypedTransmission transmission) implements CustomPacketPayload {
	
	public static final Type<TypedTransmissionPayload> ID = SpectrumC2SPackets.makeId("typed_transmission");
	public static final StreamCodec<RegistryFriendlyByteBuf, TypedTransmissionPayload> CODEC = StreamCodec.composite(
			TypedTransmission.PACKET_CODEC, TypedTransmissionPayload::transmission,
			TypedTransmissionPayload::new
	);
	
	public static void playTransmissionParticle(ServerLevel world, @NotNull TypedTransmission transmission) {
		for (ServerPlayer player : PlayerLookup.tracking(world, BlockPos.containing(transmission.getOrigin()))) {
			ServerPlayNetworking.send(player, new TypedTransmissionPayload(transmission));
		}
	}
	
	@SuppressWarnings("resource")
	@Environment(EnvType.CLIENT)
	public static void execute(TypedTransmissionPayload payload, ClientPlayNetworking.Context context) {
		Minecraft client = context.client();
		if (client.level == null) return;
		TypedTransmission transmission = payload.transmission();
		switch (transmission.getVariant()) {
			case BLOCK_POS -> client.level.addAlwaysVisibleParticle(new TransmissionParticleEffect(SpectrumParticleTypes.BLOCK_POS_EVENT_TRANSMISSION, transmission.getDestination(), transmission.getArrivalInTicks()), true, transmission.getOrigin().x(), transmission.getOrigin().y(), transmission.getOrigin().z(), 0.0D, 0.0D, 0.0D);
			case ITEM -> client.level.addAlwaysVisibleParticle(new TransmissionParticleEffect(SpectrumParticleTypes.ITEM_TRANSMISSION, transmission.getDestination(), transmission.getArrivalInTicks()), true, transmission.getOrigin().x(), transmission.getOrigin().y(), transmission.getOrigin().z(), 0.0D, 0.0D, 0.0D);
			case EXPERIENCE -> client.level.addAlwaysVisibleParticle(new TransmissionParticleEffect(SpectrumParticleTypes.EXPERIENCE_TRANSMISSION, transmission.getDestination(), transmission.getArrivalInTicks()), true, transmission.getOrigin().x(), transmission.getOrigin().y(), transmission.getOrigin().z(), 0.0D, 0.0D, 0.0D);
			case HUMMINGSTONE -> client.level.addAlwaysVisibleParticle(new TransmissionParticleEffect(SpectrumParticleTypes.HUMMINGSTONE_TRANSMISSION, transmission.getDestination(), transmission.getArrivalInTicks()), true, transmission.getOrigin().x(), transmission.getOrigin().y(), transmission.getOrigin().z(), 0.0D, 0.0D, 0.0D);
			case REDSTONE -> client.level.addAlwaysVisibleParticle(new TransmissionParticleEffect(SpectrumParticleTypes.WIRELESS_REDSTONE_TRANSMISSION, transmission.getDestination(), transmission.getArrivalInTicks()), true, transmission.getOrigin().x(), transmission.getOrigin().y(), transmission.getOrigin().z(), 0.0D, 0.0D, 0.0D);
		}
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
