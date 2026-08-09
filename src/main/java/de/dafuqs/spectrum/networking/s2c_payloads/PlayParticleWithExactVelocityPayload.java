package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.handling.*;

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
	public static void playParticleWithExactVelocity(ServerLevel world, Vec3 position, ParticleOptions particleEffect, int amount, Vec3 velocity) {
		PacketDistributor.sendToPlayersTrackingChunk(
				world, new ChunkPos(BlockPos.containing(position)),
				new PlayParticleWithExactVelocityPayload(position, particleEffect, amount, velocity)
		);
	}
	
	@SuppressWarnings("resource")
	public static void execute(PlayParticleWithExactVelocityPayload payload, IPayloadContext context) {
		Level level = context.player().level();
		
		for (int i = 0; i < payload.amount; i++) {
			level.addParticle(
					payload.particle, payload.pos.x(), payload.pos.y(), payload.pos.z(), payload.velocity.x(),
					payload.velocity.y(), payload.velocity.z()
			);
		}
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
	
}