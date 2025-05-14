package de.dafuqs.spectrum.networking.c2s_payloads;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.inventory.*;

import java.util.*;

public record InkColorSelectedC2SPayload(Optional<Holder<InkColor>> inkColor) implements CustomPacketPayload {
	
	public static final Type<InkColorSelectedC2SPayload> ID = SpectrumC2SPackets.makeId("ink_color_select");
	public static final StreamCodec<RegistryFriendlyByteBuf, InkColorSelectedC2SPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.optional(ByteBufCodecs.holderRegistry(SpectrumRegistries.INK_COLOR.key())), InkColorSelectedC2SPayload::inkColor,
			InkColorSelectedC2SPayload::new
	);
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
	
	@SuppressWarnings("resource")
	public static ServerPlayNetworking.PlayPayloadHandler<InkColorSelectedC2SPayload> getPayloadHandler() {
		return (payload, context) -> {
			ServerPlayer player = context.player();
			AbstractContainerMenu screenHandler = player.containerMenu;
			if (screenHandler instanceof InkColorSelectedPacketReceiver inkColorSelectedPacketReceiver) {
				
				Optional<Holder<InkColor>> inkColor = payload.inkColor();
				
				// send the newly selected color to all players that have the same gui open
				// this is minus the player that selected that entry (since they have that info already)
				inkColorSelectedPacketReceiver.onInkColorSelectedPacket(inkColor);
				for (ServerPlayer serverPlayer : context.server().getPlayerList().getPlayers()) {
					if (serverPlayer.containerMenu instanceof InkColorSelectedPacketReceiver receiver && receiver.getBlockEntity() != null && receiver.getBlockEntity() == inkColorSelectedPacketReceiver.getBlockEntity()) {
						InkColorSelectedS2CPayload.sendInkColorSelected(inkColor, serverPlayer);
					}
				}
			}
		};
	}
	
}
