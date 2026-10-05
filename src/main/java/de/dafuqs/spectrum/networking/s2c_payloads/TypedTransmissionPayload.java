package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.handling.*;

public record TypedTransmissionPayload(TypedTransmission transmission) implements CustomPacketPayload {
	
	public static final Type<TypedTransmissionPayload> ID = SpectrumC2SPackets.makeId("typed_transmission");
	public static final StreamCodec<RegistryFriendlyByteBuf, TypedTransmissionPayload> CODEC = StreamCodec.composite(
			TypedTransmission.PACKET_CODEC, TypedTransmissionPayload::transmission,
			TypedTransmissionPayload::new
	);
	
	public static void playTransmissionParticle(ServerLevel world, TypedTransmission transmission) {
		PacketDistributor.sendToPlayersTrackingChunk(
				world, new ChunkPos(BlockPos.containing(transmission.getOrigin())),
				new TypedTransmissionPayload(transmission)
		);
	}
	
	@SuppressWarnings("resource")
	public static void execute(TypedTransmissionPayload payload, IPayloadContext context) {
		Level level = context.player().level();
		TypedTransmission transmission = payload.transmission();
		switch (transmission.getVariant()) {
			case BLOCK_POS -> level.addAlwaysVisibleParticle(
					new TransmissionParticleEffect(SpectrumParticleTypes.BLOCK_POS_EVENT_TRANSMISSION, transmission.getDestination(), transmission.getArrivalInTicks()),
					true, transmission.getOrigin().x(), transmission.getOrigin().y(), transmission.getOrigin().z(), 0.0D, 0.0D, 0.0D
			);
			case ITEM -> level.addAlwaysVisibleParticle(
					new TransmissionParticleEffect(SpectrumParticleTypes.ITEM_TRANSMISSION, transmission.getDestination(), transmission.getArrivalInTicks()),
					true, transmission.getOrigin().x(), transmission.getOrigin().y(), transmission.getOrigin().z(), 0.0D, 0.0D, 0.0D
			);
			case EXPERIENCE -> level.addAlwaysVisibleParticle(
					new TransmissionParticleEffect(SpectrumParticleTypes.EXPERIENCE_TRANSMISSION, transmission.getDestination(), transmission.getArrivalInTicks()),
					true, transmission.getOrigin().x(), transmission.getOrigin().y(), transmission.getOrigin().z(), 0.0D, 0.0D, 0.0D
			);
			case HUMMINGSTONE -> level.addAlwaysVisibleParticle(
					new TransmissionParticleEffect(SpectrumParticleTypes.HUMMINGSTONE_TRANSMISSION, transmission.getDestination(), transmission.getArrivalInTicks()),
					true, transmission.getOrigin().x(), transmission.getOrigin().y(), transmission.getOrigin().z(), 0.0D, 0.0D, 0.0D
			);
			case REDSTONE -> level.addAlwaysVisibleParticle(
					new TransmissionParticleEffect(SpectrumParticleTypes.WIRELESS_REDSTONE_TRANSMISSION, transmission.getDestination(), transmission.getArrivalInTicks()),
					true, transmission.getOrigin().x(), transmission.getOrigin().y(), transmission.getOrigin().z(), 0.0D, 0.0D, 0.0D
			);
		}
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}