package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

public record PlayParticleWithExactVelocityPayload(Vec3 pos, ParticleOptions particle, int amount, Vec3 velocity) implements CustomPacketPayload {
	
	public static final Type<PlayParticleWithExactVelocityPayload> ID = SpectrumC2SPackets.makeId("play_particle_with_exact_velocity");
	public static final StreamCodec<RegistryFriendlyByteBuf, PlayParticleWithExactVelocityPayload> CODEC = StreamCodec.composite(
			PacketCodecHelper.VEC3D, PlayParticleWithExactVelocityPayload::pos,
			ParticleTypes.STREAM_CODEC, PlayParticleWithExactVelocityPayload::particle,
			ByteBufCodecs.INT, PlayParticleWithExactVelocityPayload::amount,
			PacketCodecHelper.VEC3D, PlayParticleWithExactVelocityPayload::velocity,
			PlayParticleWithExactVelocityPayload::new
	);
	
	/**
	 * Play particle effect
	 *
	 * @param world          the world
	 * @param position       the pos of the particles
	 * @param particleEffect The particle effect to play
	 */
	public static void playParticles(ServerLevel world, BlockPos position, ParticleOptions particleEffect, int amount) {
		playParticleWithExactVelocity(world, Vec3.atCenterOf(position), particleEffect, amount, Vec3.ZERO);
	}
	
	/**
	 * Play particle effect
	 *
	 * @param world          the world
	 * @param position       the pos of the particles
	 * @param particleEffect The particle effect to play
	 */
	public static void playParticleWithExactVelocity(ServerLevel world, @NotNull Vec3 position, @NotNull ParticleOptions particleEffect, int amount, @NotNull Vec3 velocity) {
		for (ServerPlayer player : PlayerLookup.tracking(world, BlockPos.containing(position))) {
			ServerPlayNetworking.send(player, new PlayParticleWithExactVelocityPayload(position, particleEffect, amount, velocity));
		}
	}
	
	@SuppressWarnings("resource")
	@Environment(EnvType.CLIENT)
	public static void execute(PlayParticleWithExactVelocityPayload payload, ClientPlayNetworking.Context context) {
		Minecraft client = context.client();
		ClientLevel world = client.level;
		
		for (int i = 0; i < payload.amount; i++) {
			world.addParticle(payload.particle, payload.pos.x(), payload.pos.y(), payload.pos.z(), payload.velocity.x(), payload.velocity.y(), payload.velocity.z());
		}
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
	
}
