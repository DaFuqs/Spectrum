package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.blocks.present.*;
import de.dafuqs.spectrum.networking.*;
import it.unimi.dsi.fastutil.objects.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;

import java.util.*;

public record PlayPresentOpeningParticlesPayload(BlockPos presentPos, Map<Integer, Integer> colors) implements CustomPacketPayload {
	
	public static final Type<PlayPresentOpeningParticlesPayload> ID = SpectrumC2SPackets.makeId("play_present_opening_particles");
	public static final StreamCodec<FriendlyByteBuf, PlayPresentOpeningParticlesPayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, PlayPresentOpeningParticlesPayload::presentPos,
			ByteBufCodecs.map(Object2IntArrayMap::new, ByteBufCodecs.INT, ByteBufCodecs.INT), PlayPresentOpeningParticlesPayload::colors,
			PlayPresentOpeningParticlesPayload::new
	);
	
	public static void playPresentOpeningParticles(ServerLevel serverWorld, BlockPos presentPos, Map<Integer, Integer> colors) {
		for (ServerPlayer player : PlayerLookup.tracking(serverWorld, presentPos)) {
			ServerPlayNetworking.send(player, new PlayPresentOpeningParticlesPayload(presentPos, colors));
		}
	}
	
	@SuppressWarnings("resource")
	@Environment(EnvType.CLIENT)
	public static void execute(PlayPresentOpeningParticlesPayload payload, ClientPlayNetworking.Context context) {
		Minecraft client = context.client();
		PresentBlock.spawnParticles(client.level, payload.presentPos, payload.colors);
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
