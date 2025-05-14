package de.dafuqs.spectrum.networking.c2s_payloads;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.inventories.slots.*;
import de.dafuqs.spectrum.networking.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;

public record SetShadowSlotPayload(int screenHandlerSyncId, int slotId,
								   ItemStack shadowStack) implements CustomPacketPayload {
	
	public static final Type<SetShadowSlotPayload> ID = SpectrumC2SPackets.makeId("set_shadow_slot");
	public static final StreamCodec<RegistryFriendlyByteBuf, SetShadowSlotPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, SetShadowSlotPayload::screenHandlerSyncId,
			ByteBufCodecs.INT, SetShadowSlotPayload::slotId,
			ItemStack.STREAM_CODEC, SetShadowSlotPayload::shadowStack,
			SetShadowSlotPayload::new
	);
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
	
	public static ServerPlayNetworking.PlayPayloadHandler<SetShadowSlotPayload> getPayloadHandler() {
		return (payload, context) -> {
			ServerPlayer player = context.player();
			AbstractContainerMenu screenHandler = player.containerMenu;
			
			if (screenHandler == null || screenHandler.containerId != payload.screenHandlerSyncId) {
				return;
			}
			
			Slot slot = screenHandler.getSlot(payload.slotId);
			if (slot == null || !(slot instanceof ShadowSlot) || !(slot.container instanceof FilterConfigurable.FilterInventory filterInventory)) {
				return;
			}
			
			filterInventory.getClicker().clickShadowSlot(screenHandler.containerId, slot, payload.shadowStack());
		};
	}
	
}
