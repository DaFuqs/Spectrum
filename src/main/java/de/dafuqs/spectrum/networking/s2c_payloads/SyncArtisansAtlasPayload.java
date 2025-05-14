package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.items.map.*;
import de.dafuqs.spectrum.networking.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.resources.*;

import java.util.*;

public record SyncArtisansAtlasPayload(Optional<ResourceLocation> targetId, ClientboundMapItemDataPacket packet) implements CustomPacketPayload {
	
	public static final Type<SyncArtisansAtlasPayload> ID = SpectrumC2SPackets.makeId("sync_artisans_atlas");
	public static final StreamCodec<RegistryFriendlyByteBuf, SyncArtisansAtlasPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), SyncArtisansAtlasPayload::targetId,
			ClientboundMapItemDataPacket.STREAM_CODEC, SyncArtisansAtlasPayload::packet,
			SyncArtisansAtlasPayload::new
	);
	
	@Environment(EnvType.CLIENT)
	public static void execute(SyncArtisansAtlasPayload payload, ClientPlayNetworking.Context context) {
		var client = context.client();
		if (client.level == null) return;
		PacketUtils.ensureRunningOnSameThread(payload.packet, client.getConnection(), client);
		var mapRenderer = client.gameRenderer.getMapRenderer();
		var mapIdComponent = payload.packet.mapId();
		var mapState = client.level.getMapData(mapIdComponent);
		if (mapState == null) {
			mapState = new ArtisansAtlasState(payload.packet.scale(), payload.packet.locked(), client.level.dimension());
			client.level.overrideMapData(mapIdComponent, mapState);
		}
		if (payload.packet.decorations().isPresent())
			mapState.addClientSideDecorations(payload.packet.decorations().get());
		if (payload.packet.colorPatch().isPresent())
			payload.packet.colorPatch().get().applyToMap(mapState);
		if (mapState instanceof ArtisansAtlasState artisansAtlasState)
			artisansAtlasState.setTargetId(payload.targetId.orElse(null));
		mapRenderer.update(mapIdComponent, mapState);
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
	
}
