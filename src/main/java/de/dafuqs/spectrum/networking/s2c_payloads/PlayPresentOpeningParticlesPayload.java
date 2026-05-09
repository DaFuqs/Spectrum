package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.present.*;
import de.dafuqs.spectrum.networking.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.handling.*;
import javax.annotation.*;

import java.util.*;

public record PlayPresentOpeningParticlesPayload(BlockPos presentPos, Map<InkColor, Integer> colors) implements CustomPacketPayload {
	
	public static final Type<PlayPresentOpeningParticlesPayload> ID = SpectrumC2SPackets.makeId("play_present_opening_particles");
	public static final StreamCodec<FriendlyByteBuf, PlayPresentOpeningParticlesPayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, PlayPresentOpeningParticlesPayload::presentPos,
			ByteBufCodecs.map(Object2IntArrayMap::new, InkColor.PACKET_CODEC, ByteBufCodecs.INT),
			PlayPresentOpeningParticlesPayload::colors,
			PlayPresentOpeningParticlesPayload::new
	);
	
	public static void playPresentOpeningParticles(ServerLevel serverWorld, BlockPos presentPos, Map<InkColor, Integer> colors) {
		PacketDistributor.sendToPlayersTrackingChunk(serverWorld, new ChunkPos(presentPos), new PlayPresentOpeningParticlesPayload(presentPos, colors));
	}
	
	public static void execute(PlayPresentOpeningParticlesPayload payload, IPayloadContext context) {
		PresentBlock.spawnParticlesClient(context.player().level(), payload.presentPos, payload.colors);
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}