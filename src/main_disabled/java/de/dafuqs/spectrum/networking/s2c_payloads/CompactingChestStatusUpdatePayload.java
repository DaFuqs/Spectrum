package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.blocks.chests.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;

public record CompactingChestStatusUpdatePayload(BlockPos pos, boolean hasToCraft) implements CustomPacketPayload {
	
	public static final Type<CompactingChestStatusUpdatePayload> ID = SpectrumC2SPackets.makeId("compacting_chest_status_update");
	public static final StreamCodec<FriendlyByteBuf, CompactingChestStatusUpdatePayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, CompactingChestStatusUpdatePayload::pos,
			ByteBufCodecs.BOOL, CompactingChestStatusUpdatePayload::hasToCraft,
			CompactingChestStatusUpdatePayload::new
	);
	
	public static void sendCompactingChestStatusUpdate(CompactingChestBlockEntity chest) {
		for (ServerPlayer player : PlayerLookup.tracking(chest)) {
			ServerPlayNetworking.send(player, new CompactingChestStatusUpdatePayload(chest.getBlockPos(), chest.hasToCraft()));
		}
	}
	
	@SuppressWarnings("resource")
	@Environment(EnvType.CLIENT)
	public static void execute(CompactingChestStatusUpdatePayload payload, ClientPlayNetworking.Context context) {
		Minecraft client = context.client();
		var entity = client.level.getBlockEntity(payload.pos, SpectrumBlockEntities.COMPACTING_CHEST);
		entity.ifPresent(compactingChestBlockEntity -> compactingChestBlockEntity.shouldCraft(payload.hasToCraft));
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
