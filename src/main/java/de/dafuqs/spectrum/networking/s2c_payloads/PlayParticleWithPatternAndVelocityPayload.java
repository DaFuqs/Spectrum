package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.*;
import net.minecraft.network.protocol.common.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.network.handling.*;
import org.jspecify.annotations.*;

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
	 * @param level          the world
	 * @param position       the pos of the particles
	 * @param particleEffect The particle effect to play
	 */
	public static void playParticleWithPatternAndVelocity(@Nullable Player notThisPlayerEntity, ServerLevel level, Vec3 position, ParticleOptions particleEffect, VectorPattern pattern, double velocity) {
		Packet<?> packet = new ClientboundCustomPayloadPacket(new PlayParticleWithPatternAndVelocityPayload(position, particleEffect, pattern, velocity));
		
		for (ServerPlayer player : level.getChunkSource().chunkMap.getPlayers(
				new ChunkPos(BlockPos.containing(position)), false)) {
			if (notThisPlayerEntity != null && notThisPlayerEntity.equals(player))
				continue;
			
			player.connection.send(packet);
		}
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
	
	public static void execute(PlayParticleWithPatternAndVelocityPayload payload, IPayloadContext context) {
		ParticleHelper.playParticleWithPatternAndVelocityClient(context.player().level(), payload.pos, payload.effect, payload.pattern, payload.velocity);
	}
}