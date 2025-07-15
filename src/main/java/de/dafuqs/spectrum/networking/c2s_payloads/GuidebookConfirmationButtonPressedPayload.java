package de.dafuqs.spectrum.networking.c2s_payloads;

import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.progression.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;

public record GuidebookConfirmationButtonPressedPayload(String confirmationString) implements CustomPacketPayload {
	
	public static final Type<GuidebookConfirmationButtonPressedPayload> ID = SpectrumC2SPackets.makeId("confirmation_button_pressed");
	public static final StreamCodec<FriendlyByteBuf, GuidebookConfirmationButtonPressedPayload> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, GuidebookConfirmationButtonPressedPayload::confirmationString, GuidebookConfirmationButtonPressedPayload::new);
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
	
	public static ServerPlayNetworking.PlayPayloadHandler<GuidebookConfirmationButtonPressedPayload> getPayloadHandler() {
		return (payload, context) -> {
			ServerPlayer player = context.player();
			SpectrumAdvancementCriteria.CONFIRMATION_BUTTON_PRESSED.trigger(player, payload.confirmationString);
			player.level().playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
		};
		
	}
	
}
