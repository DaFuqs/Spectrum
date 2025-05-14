package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.blocks.fusion_shrine.*;
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
import net.minecraft.world.level.block.entity.*;

public record PlayFusionCraftingInProgressParticlePayload(BlockPos pos) implements CustomPacketPayload {
	
	public static final Type<PlayFusionCraftingInProgressParticlePayload> ID = SpectrumC2SPackets.makeId("play_fusion_crafting_in_progress_particle");
	public static final StreamCodec<FriendlyByteBuf, PlayFusionCraftingInProgressParticlePayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, PlayFusionCraftingInProgressParticlePayload::pos,
			PlayFusionCraftingInProgressParticlePayload::new
	);
	
	public static void sendPlayFusionCraftingInProgressParticles(ServerLevel world, BlockPos pos) {
		for (ServerPlayer player : PlayerLookup.tracking(world, pos)) {
			ServerPlayNetworking.send(player, new PlayFusionCraftingInProgressParticlePayload(pos));
		}
	}
	
	@SuppressWarnings("resource")
	@Environment(EnvType.CLIENT)
	public static void execute(PlayFusionCraftingInProgressParticlePayload payload, ClientPlayNetworking.Context context) {
		Minecraft client = context.client();
		BlockEntity blockEntity = client.level.getBlockEntity(payload.pos);
		if (blockEntity instanceof FusionShrineBlockEntity fusionShrineBlockEntity) {
			fusionShrineBlockEntity.spawnCraftingParticles();
		}
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
