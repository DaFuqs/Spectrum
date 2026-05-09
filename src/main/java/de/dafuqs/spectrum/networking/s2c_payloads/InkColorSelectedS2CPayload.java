package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.inventory.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.handling.*;
import javax.annotation.*;

import java.util.*;

public record InkColorSelectedS2CPayload(Optional<Holder<InkColor>> inkColor) implements CustomPacketPayload {
	
	public static final Type<InkColorSelectedS2CPayload> ID = SpectrumC2SPackets.makeId("ink_color_selected");
	public static final StreamCodec<RegistryFriendlyByteBuf, InkColorSelectedS2CPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.optional(ByteBufCodecs.holderRegistry(SpectrumRegistries.INK_COLOR.key())),
			InkColorSelectedS2CPayload::inkColor,
			InkColorSelectedS2CPayload::new
	);
	
	public static void sendInkColorSelected(Optional<Holder<InkColor>> inkColor, ServerPlayer player) {
		PacketDistributor.sendToPlayer(player, new InkColorSelectedS2CPayload(inkColor));
	}
	
	public static void execute(InkColorSelectedS2CPayload payload, IPayloadContext context) {
		AbstractContainerMenu screenHandler = context.player().containerMenu;
		if (screenHandler instanceof InkColorSelectedPacketReceiver inkColorSelectedPacketReceiver) {
			inkColorSelectedPacketReceiver.onInkColorSelectedPacket(payload.inkColor());
		}
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
	
}