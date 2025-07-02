package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
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

import java.util.*;

public record UpdateBlockEntityInkPayload(BlockPos pos, Map<InkColor, Long> storage, long currentTotal) implements CustomPacketPayload {
	
	public static final Type<UpdateBlockEntityInkPayload> ID = SpectrumC2SPackets.makeId("update_block_entity_ink");
	public static final StreamCodec<FriendlyByteBuf, UpdateBlockEntityInkPayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, UpdateBlockEntityInkPayload::pos,
			ByteBufCodecs.map(HashMap::new, InkColor.PACKET_CODEC, ByteBufCodecs.VAR_LONG), UpdateBlockEntityInkPayload::storage,
			ByteBufCodecs.VAR_LONG, UpdateBlockEntityInkPayload::currentTotal,
			UpdateBlockEntityInkPayload::new
	);
	
	@SuppressWarnings("deprecation")
	public static void updateBlockEntityInk(BlockPos pos, InkStorage inkStorage, ServerPlayer player) {
		ServerPlayNetworking.send(player, new UpdateBlockEntityInkPayload(pos, inkStorage.getEnergy(), inkStorage.getCurrentTotal()));
	}
	
	@SuppressWarnings("resource")
	@Environment(EnvType.CLIENT)
	public static void execute(UpdateBlockEntityInkPayload payload, ClientPlayNetworking.Context context) {
		Minecraft client = context.client();
		BlockEntity blockEntity = client.level.getBlockEntity(payload.pos);
		if (blockEntity instanceof InkStorageBlockEntity<?> inkStorageBlockEntity) {
			inkStorageBlockEntity.getEnergyStorage().setEnergy(payload.storage, payload.currentTotal);
		}
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
