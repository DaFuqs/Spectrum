package de.dafuqs.spectrum.networking.c2s_payloads;

import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.networking.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.inventory.*;

public record WorkstaffToggleSelectedPayload(int index) implements CustomPacketPayload {
	
	public static final Type<WorkstaffToggleSelectedPayload> ID = SpectrumC2SPackets.makeId("workstaff_toggle_selected");
	public static final StreamCodec<RegistryFriendlyByteBuf, WorkstaffToggleSelectedPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, WorkstaffToggleSelectedPayload::index,
			WorkstaffToggleSelectedPayload::new
	);
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
	
	public static ServerPlayNetworking.PlayPayloadHandler<WorkstaffToggleSelectedPayload> getPayloadHandler() {
		return (payload, context) -> {
			ServerPlayer player = context.player();
			AbstractContainerMenu screenHandler = player.containerMenu;
			if (screenHandler instanceof WorkstaffScreenHandler workstaffScreenHandler) {
				WorkstaffItem.GUIToggle toggle = WorkstaffItem.GUIToggle.values()[payload.index];
				workstaffScreenHandler.onWorkstaffToggleSelectionPacket(toggle);
			}
		};
	}
	
}
