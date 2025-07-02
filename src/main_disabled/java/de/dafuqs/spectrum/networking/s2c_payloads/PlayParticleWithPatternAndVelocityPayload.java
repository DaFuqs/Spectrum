package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

public record PlayParticleWithPatternAndVelocityPayload(Vec3 pos, ParticleOptions effect, VectorPattern pattern, double velocity) implements CustomPacketPayload {
	
	public static final Type<PlayParticleWithPatternAndVelocityPayload> ID = SpectrumC2SPackets.makeId("play_particle_with_pattern_and_velocity");
	public static final StreamCodec<RegistryFriendlyByteBuf, PlayParticleWithPatternAndVelocityPayload> CODEC = StreamCodec.composite(
			PacketCodecHelper.VEC3D, PlayParticleWithPatternAndVelocityPayload::pos,
			ParticleTypes.STREAM_CODEC, PlayParticleWithPatternAndVelocityPayload::effect,
			VectorPattern.PACKET_CODEC, PlayParticleWithPatternAndVelocityPayload::pattern,
			ByteBufCodecs.DOUBLE, PlayParticleWithPatternAndVelocityPayload::velocity,
			PlayParticleWithPatternAndVelocityPayload::new
	);
	
	/**
	 * Play particles matching a spawn pattern
	 *
	 * @param world          the world
	 * @param position       the pos of the particles
	 * @param particleEffect The particle effect to play
	 */
	public static void playParticleWithPatternAndVelocity(@Nullable Player notThisPlayerEntity, ServerLevel world, @NotNull Vec3 position, @NotNull ParticleOptions particleEffect, @NotNull VectorPattern pattern, double velocity) {
		for (ServerPlayer player : PlayerLookup.tracking(world, BlockPos.containing(position))) {
			if (!player.equals(notThisPlayerEntity)) {
				ServerPlayNetworking.send(player, new PlayParticleWithPatternAndVelocityPayload(position, particleEffect, pattern, velocity));
			}
		}
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
	
	@SuppressWarnings("resource")
	@Environment(EnvType.CLIENT)
	public static void execute(PlayParticleWithPatternAndVelocityPayload payload, ClientPlayNetworking.Context context) {
		ParticleHelper.playParticleWithPatternAndVelocityClient(context.client().level, payload.pos, payload.effect, payload.pattern, payload.velocity);
	}
}
