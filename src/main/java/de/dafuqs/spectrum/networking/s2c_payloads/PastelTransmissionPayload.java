package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.*;
import net.minecraft.network.protocol.common.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.*;
import net.neoforged.neoforge.network.handling.*;
import org.jetbrains.annotations.*;

import java.util.*;

public record PastelTransmissionPayload(int networkColor, int travelTime, PastelTransmission transmission)
		implements CustomPacketPayload {
	
	public static final Type<PastelTransmissionPayload> ID = SpectrumC2SPackets.makeId("pastel_transmission");
	public static final StreamCodec<RegistryFriendlyByteBuf, PastelTransmissionPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, PastelTransmissionPayload::networkColor,
			ByteBufCodecs.INT, PastelTransmissionPayload::travelTime,
			PastelTransmission.PACKET_CODEC, PastelTransmissionPayload::transmission,
			PastelTransmissionPayload::new
	);
	
	// TODO: we should probably also send the transmission to players that track the destination pos
	public static void sendPastelTransmissionParticle(ServerPastelNetwork network, int travelTime, @NotNull PastelTransmission transmission) {
		Packet<?> packet = new ClientboundCustomPayloadPacket(new PastelTransmissionPayload(network.getColor(), travelTime, transmission));
		Set<ServerPlayer> targetPlayers = new HashSet<ServerPlayer>();
		targetPlayers.addAll(network.getLevel().getChunkSource().chunkMap.getPlayers(new ChunkPos(transmission.getNodePositions().getFirst()), false));
		targetPlayers.addAll(network.getLevel().getChunkSource().chunkMap.getPlayers(new ChunkPos(transmission.getNodePositions().getLast()), false));
		
		for (ServerPlayer player : targetPlayers) {
			player.connection.send(packet);
		}
	}
	
	@SuppressWarnings("resource")
	public static void execute(PastelTransmissionPayload payload, IPayloadContext context) {
		int color = payload.networkColor();
		int travelTime = payload.travelTime();
		PastelTransmission transmission = payload.transmission;
		BlockPos spawnPos = transmission.getStartPos();
		context.player().level().addParticle(new PastelTransmissionParticleEffect(transmission.getNodePositions(), transmission.getVariant(), travelTime, color), spawnPos.getX() + 0.5, spawnPos.getY() + 0.5, spawnPos.getZ() + 0.5, 0, 0, 0);
	}
	
	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}