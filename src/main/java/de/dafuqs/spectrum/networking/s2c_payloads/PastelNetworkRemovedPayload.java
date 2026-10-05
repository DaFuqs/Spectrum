package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.networking.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.handling.*;

import java.util.*;

public record PastelNetworkRemovedPayload(UUID networkUUID) implements CustomPacketPayload {
	
	public static final Type<PastelNetworkRemovedPayload> ID = SpectrumC2SPackets.makeId("pastel_network_removed");
	public static final StreamCodec<FriendlyByteBuf, PastelNetworkRemovedPayload> CODEC = StreamCodec.composite(
			UUIDUtil.STREAM_CODEC, PastelNetworkRemovedPayload::networkUUID,
			PastelNetworkRemovedPayload::new
	);
	
	public static void send(ServerPastelNetwork network) {
		PacketDistributor.sendToPlayersInDimension(network.getLevel(), new PastelNetworkRemovedPayload(network.getUUID()));
	}
	
	public static void execute(PastelNetworkRemovedPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> Pastel.getClientInstance().removeNetwork(payload.networkUUID));
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}