package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.client.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;

public record PlayAscensionAppliedEffectsPayload() implements CustomPacketPayload {
	
	public static final Type<PlayAscensionAppliedEffectsPayload> ID = SpectrumC2SPackets.makeId("play_ascension_applied_effects");
	public static final StreamCodec<FriendlyByteBuf, PlayAscensionAppliedEffectsPayload> CODEC = StreamCodec.of((buf, value) -> {
	}, buf -> new PlayAscensionAppliedEffectsPayload());
	
	public static void playAscensionAppliedEffects(ServerPlayer player) {
		ServerPlayNetworking.send(player, new PlayAscensionAppliedEffectsPayload());
	}
	
	@SuppressWarnings("resource")
	@Environment(EnvType.CLIENT)
	public static void execute(PlayAscensionAppliedEffectsPayload payload, ClientPlayNetworking.Context context) {
		Minecraft client = context.client();
		client.level.playSound(null, client.player.blockPosition(), SpectrumSoundEvents.FADING_PLACED, SoundSource.PLAYERS, 1.0F, 1.0F);
		client.getSoundManager().play(new DivinitySoundInstance());
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
