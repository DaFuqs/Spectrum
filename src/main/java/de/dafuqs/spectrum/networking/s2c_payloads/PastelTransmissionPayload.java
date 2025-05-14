package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.networking.*;
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

public record PastelTransmissionPayload(int networkColor, int travelTime, PastelTransmission transmission) implements CustomPacketPayload {
	
	public static final Type<PastelTransmissionPayload> ID = SpectrumC2SPackets.makeId("pastel_transmission");
	public static final StreamCodec<RegistryFriendlyByteBuf, PastelTransmissionPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, PastelTransmissionPayload::networkColor,
			ByteBufCodecs.INT, PastelTransmissionPayload::travelTime,
			PastelTransmission.PACKET_CODEC, PastelTransmissionPayload::transmission,
			PastelTransmissionPayload::new
	);
	
	// TODO: we should probably also send the transmission to players that track the destination pos
	public static void sendPastelTransmissionParticle(ServerPastelNetwork network, int travelTime, @NotNull PastelTransmission transmission) {
		for (ServerPlayer player : PlayerLookup.tracking(network.getWorld(), transmission.getStartPos())) {
			ServerPlayNetworking.send(player, new PastelTransmissionPayload(network.getColor(), travelTime, transmission));
		}
	}
	
	@SuppressWarnings("resource")
	@Environment(EnvType.CLIENT)
	public static void execute(PastelTransmissionPayload payload, ClientPlayNetworking.Context context) {
		Minecraft client = context.client();
		
		int color = payload.networkColor();
		int travelTime = payload.travelTime();
		PastelTransmission transmission = payload.transmission;
		BlockPos spawnPos = transmission.getStartPos();
		client.level.addParticle(new PastelTransmissionParticleEffect(transmission.getNodePositions(), transmission.getVariant().toStack(), travelTime, color), spawnPos.getX() + 0.5, spawnPos.getY() + 0.5, spawnPos.getZ() + 0.5, 0, 0, 0);
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
