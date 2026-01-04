package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.*;
import net.minecraft.client.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.player.*;
import net.neoforged.api.distmarker.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.handling.*;
import org.jetbrains.annotations.*;

public record PlayAscensionAppliedEffectsPayload() implements CustomPacketPayload {
	
	public static final Type<PlayAscensionAppliedEffectsPayload> ID = SpectrumC2SPackets.makeId("play_ascension_applied_effects");
	public static final StreamCodec<FriendlyByteBuf, PlayAscensionAppliedEffectsPayload> CODEC = StreamCodec.of((buf, value) -> {
			}, buf -> new PlayAscensionAppliedEffectsPayload()
	);
	
	public static void playAscensionAppliedEffects(ServerPlayer player) {
		PacketDistributor.sendToPlayer(player, new PlayAscensionAppliedEffectsPayload());
	}
	
	public static void execute(PlayAscensionAppliedEffectsPayload payload, IPayloadContext context) {
		execute(context.player());
	}
	
	@SuppressWarnings("resource")
	private static void execute(Player player) {
		var level = player.level();
		level.playSound(null, player.blockPosition(), SpectrumSoundEvents.FADING_PLACED, SoundSource.PLAYERS, 1.0F, 1.0F);
		Minecraft.getInstance()
				.getSoundManager()
				.play(new DivinitySoundInstance());
	}
	
	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}