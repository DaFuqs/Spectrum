package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.networking.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;

public record PlayPedestalStartCraftingParticlePayload(BlockPos pedestalPos) implements CustomPacketPayload {
	
	public static final Type<PlayPedestalStartCraftingParticlePayload> ID = SpectrumC2SPackets.makeId("play_pedestal_start_crafting_particle");
	public static final StreamCodec<FriendlyByteBuf, PlayPedestalStartCraftingParticlePayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, PlayPedestalStartCraftingParticlePayload::pedestalPos,
			PlayPedestalStartCraftingParticlePayload::new
	);
	
	public static void spawnPedestalStartCraftingParticles(PedestalBlockEntity pedestalBlockEntity) {
		for (ServerPlayer player : PlayerLookup.tracking((ServerLevel) pedestalBlockEntity.getLevel(), pedestalBlockEntity.getBlockPos())) {
			ServerPlayNetworking.send(player, new PlayPedestalStartCraftingParticlePayload(pedestalBlockEntity.getBlockPos()));
		}
	}
	
	@SuppressWarnings("resource")
	@Environment(EnvType.CLIENT)
	public static void execute(PlayPedestalStartCraftingParticlePayload payload, ClientPlayNetworking.Context context) {
		Minecraft client = context.client();
		PedestalBlockEntity.spawnCraftingStartParticles(client.level, payload.pedestalPos);
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
