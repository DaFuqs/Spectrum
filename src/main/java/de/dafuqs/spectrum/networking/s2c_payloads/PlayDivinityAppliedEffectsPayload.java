package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.neoforged.api.distmarker.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.handling.*;
import javax.annotation.*;

public record PlayDivinityAppliedEffectsPayload() implements CustomPacketPayload {
	
	public static final Type<PlayDivinityAppliedEffectsPayload> ID = SpectrumC2SPackets.makeId("play_divinity_applied_effects");
	public static final StreamCodec<FriendlyByteBuf, PlayDivinityAppliedEffectsPayload> CODEC = StreamCodec.of((buf, value) -> {
			}, buf -> new PlayDivinityAppliedEffectsPayload()
	);
	
	public static void playDivinityAppliedEffects(ServerPlayer player) {
		PacketDistributor.sendToPlayer(player, new PlayDivinityAppliedEffectsPayload());
	}
	
	public static void execute(PlayDivinityAppliedEffectsPayload payload, IPayloadContext context) {
		execute(context.player());
	}
	
	private static void execute(Player player) {
		Level level = player.level();
		Minecraft client = Minecraft.getInstance();
		client.particleEngine.createTrackingEmitter(player, SpectrumParticleTypes.DIVINITY, 30);
		client.gameRenderer.displayItemActivation(SpectrumItems.DIVINATION_HEART.get().getDefaultInstance());
		level.playSound(null, player.blockPosition(), SpectrumSoundEvents.FAILING_PLACED, SoundSource.PLAYERS, 1.0F, 1.0F);
		ParticleHelper.playParticleWithPatternAndVelocityClient(level, player.position(), ColoredCraftingParticleEffect.WHITE, VectorPattern.SIXTEEN, 0.4);
		ParticleHelper.playParticleWithPatternAndVelocityClient(level, player.position(), ColoredCraftingParticleEffect.RED, VectorPattern.SIXTEEN, 0.4);
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}