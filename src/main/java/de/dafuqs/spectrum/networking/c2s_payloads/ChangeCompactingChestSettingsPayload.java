package de.dafuqs.spectrum.networking.c2s_payloads;

import de.dafuqs.spectrum.blocks.chests.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.networking.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.world.level.block.entity.*;

public record ChangeCompactingChestSettingsPayload(AutoCraftingMode mode) implements CustomPacketPayload {
	
	public static final CustomPacketPayload.Type<ChangeCompactingChestSettingsPayload> ID = SpectrumC2SPackets.makeId("change_compacting_chest_settings");
	public static final StreamCodec<FriendlyByteBuf, ChangeCompactingChestSettingsPayload> CODEC = StreamCodec.composite(
			PacketCodecHelper.enumOf(AutoCraftingMode::values),
			ChangeCompactingChestSettingsPayload::mode,
			ChangeCompactingChestSettingsPayload::new
	);
	
	public static ServerPlayNetworking.PlayPayloadHandler<ChangeCompactingChestSettingsPayload> getPayloadHandler() {
		return (payload, context) -> {
			// receive the client packet...
			if (context.player().containerMenu instanceof CompactingChestScreenHandler compactingChestScreenHandler) {
				BlockEntity blockEntity = compactingChestScreenHandler.getBlockEntity();
				if (blockEntity instanceof CompactingChestBlockEntity compactingChestBlockEntity) {
					// apply the new settings
					compactingChestBlockEntity.applySettings(payload);
				}
			}
		};
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
	
}
