package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.networking.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.handling.*;
import javax.annotation.*;

import java.util.*;

public record UpdateBlockEntityInkPayload(BlockPos pos, Map<InkColor, Long> storage, long currentTotal) implements CustomPacketPayload {
	
	public static final Type<UpdateBlockEntityInkPayload> ID = SpectrumC2SPackets.makeId("update_block_entity_ink");
	public static final StreamCodec<FriendlyByteBuf, UpdateBlockEntityInkPayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, UpdateBlockEntityInkPayload::pos,
			ByteBufCodecs.map(HashMap::new, InkColor.PACKET_CODEC, ByteBufCodecs.VAR_LONG),
			UpdateBlockEntityInkPayload::storage,
			ByteBufCodecs.VAR_LONG, UpdateBlockEntityInkPayload::currentTotal,
			UpdateBlockEntityInkPayload::new
	);
	
	@SuppressWarnings("deprecation")
	public static void updateBlockEntityInk(BlockPos pos, InkStorage inkStorage, ServerPlayer player) {
		PacketDistributor.sendToPlayer(player, new UpdateBlockEntityInkPayload(pos, inkStorage.getEnergy(), inkStorage.getCurrentTotal()));
	}
	
	@SuppressWarnings("resource")
	public static void execute(UpdateBlockEntityInkPayload payload, IPayloadContext context) {
		Level level = context.player().level();
		BlockEntity blockEntity = level.getBlockEntity(payload.pos);
		if (blockEntity instanceof InkStorageBlockEntity<?> inkStorageBlockEntity) {
			inkStorageBlockEntity.getEnergyStorage().setEnergy(payload.storage, payload.currentTotal);
		}
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}