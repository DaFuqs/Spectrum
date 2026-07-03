package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.blocks.chests.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.registries.*;
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

public record CompactingChestStatusUpdatePayload(BlockPos pos, long timeStamp) implements CustomPacketPayload {
	
	public static final Type<CompactingChestStatusUpdatePayload> ID = SpectrumC2SPackets.makeId("compacting_chest_status_update");
	public static final StreamCodec<FriendlyByteBuf, CompactingChestStatusUpdatePayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, CompactingChestStatusUpdatePayload::pos,
			ByteBufCodecs.VAR_LONG, CompactingChestStatusUpdatePayload::timeStamp,
			CompactingChestStatusUpdatePayload::new
	);
	
	public static void sendCompactingChestStatusUpdate(CompactingChestBlockEntity chest) {
		PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) chest.getLevel(), new ChunkPos(chest.getBlockPos()), new CompactingChestStatusUpdatePayload(chest.getBlockPos(), chest.getCraftingTimeStamp()));
	}
	
	@SuppressWarnings("resource")
	public static void execute(CompactingChestStatusUpdatePayload payload, IPayloadContext context) {
		Level level = context.player().level();
		Optional<CompactingChestBlockEntity> entity = level.getBlockEntity(payload.pos, SpectrumBlockEntities.COMPACTING_CHEST.get());
		entity.ifPresent(compactingChestBlockEntity -> compactingChestBlockEntity.setCraftingTimeStamp(payload.timeStamp()));
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
