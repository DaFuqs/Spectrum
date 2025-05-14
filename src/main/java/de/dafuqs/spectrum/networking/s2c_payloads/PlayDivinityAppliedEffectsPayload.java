package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.client.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.player.*;

public record PlayDivinityAppliedEffectsPayload() implements CustomPacketPayload {
	
	public static final Type<PlayDivinityAppliedEffectsPayload> ID = SpectrumC2SPackets.makeId("play_divinity_applied_effects");
	public static final StreamCodec<FriendlyByteBuf, PlayDivinityAppliedEffectsPayload> CODEC = StreamCodec.of((buf, value) -> {
	}, buf -> new PlayDivinityAppliedEffectsPayload());
	
	public static void playDivinityAppliedEffects(ServerPlayer player) {
		ServerPlayNetworking.send(player, new PlayDivinityAppliedEffectsPayload());
	}
	
	@SuppressWarnings("resource")
	@Environment(EnvType.CLIENT)
	public static void execute(PlayDivinityAppliedEffectsPayload payload, ClientPlayNetworking.Context context) {
		Minecraft client = context.client();
		Player player = client.player;
		client.particleEngine.createTrackingEmitter(player, SpectrumParticleTypes.DIVINITY, 30);
		client.gameRenderer.displayItemActivation(SpectrumItems.DIVINATION_HEART.getDefaultInstance());
		client.level.playSound(null, player.blockPosition(), SpectrumSoundEvents.FAILING_PLACED, SoundSource.PLAYERS, 1.0F, 1.0F);
		ParticleHelper.playParticleWithPatternAndVelocityClient(player.level(), player.position(), ColoredCraftingParticleEffect.WHITE, VectorPattern.SIXTEEN, 0.4);
		ParticleHelper.playParticleWithPatternAndVelocityClient(player.level(), player.position(), ColoredCraftingParticleEffect.RED, VectorPattern.SIXTEEN, 0.4);
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
