package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.api.item.*;
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
import org.jetbrains.annotations.*;

import java.util.*;

public record BlackHoleChestStatusUpdatePayload(BlockPos pos, boolean isFull, boolean canStoreExperience, long storedExperience, long maxStoredExperience) implements CustomPacketPayload {
	
	public static final Type<BlackHoleChestStatusUpdatePayload> ID = SpectrumC2SPackets.makeId("black_hole_chest_status_update");
	public static final StreamCodec<FriendlyByteBuf, BlackHoleChestStatusUpdatePayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, BlackHoleChestStatusUpdatePayload::pos,
			ByteBufCodecs.BOOL, BlackHoleChestStatusUpdatePayload::isFull,
			ByteBufCodecs.BOOL, BlackHoleChestStatusUpdatePayload::canStoreExperience,
			ByteBufCodecs.VAR_LONG, BlackHoleChestStatusUpdatePayload::storedExperience,
			ByteBufCodecs.VAR_LONG, BlackHoleChestStatusUpdatePayload::maxStoredExperience,
			BlackHoleChestStatusUpdatePayload::new
	);
	
	public static void sendBlackHoleChestUpdate(BlackHoleChestBlockEntity chest) {
		var xpStack = chest.getItem(BlackHoleChestBlockEntity.EXPERIENCE_STORAGE_PROVIDER_ITEM_SLOT);
		
		long storedXP = 0;
		long maxStoredXP = 0;
		
		if (xpStack.getItem() instanceof ExperienceStorageItem experienceStorageItem && chest.getLevel() != null) {
			storedXP = ExperienceStorageItem.getStoredExperience(xpStack);
			maxStoredXP = experienceStorageItem.getMaxStoredExperience(chest.getLevel().registryAccess(), xpStack);
		}
		
		PacketDistributor.sendToPlayersTrackingChunk(
				(ServerLevel) chest.getLevel(), new ChunkPos(chest.getBlockPos()),
				new BlackHoleChestStatusUpdatePayload(chest.getBlockPos(), chest.isFullServer(), chest.canStoreExperience(), storedXP, maxStoredXP)
		);
	}
	
	@SuppressWarnings("resource")
	public static void execute(BlackHoleChestStatusUpdatePayload payload, IPayloadContext context) {
		var level = context.player().level();
		Optional<BlackHoleChestBlockEntity> entity = level.getBlockEntity(payload.pos, SpectrumBlockEntities.BLACK_HOLE_CHEST.get());
		entity.ifPresent(chest -> {
			chest.setFull(payload.isFull);
			chest.setHasXPStorage(payload.canStoreExperience);
			chest.setXPData(payload.storedExperience, payload.maxStoredExperience);
		});
	}
	
	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}