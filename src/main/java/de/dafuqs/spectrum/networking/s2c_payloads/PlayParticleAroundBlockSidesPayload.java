package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.phys.*;

import java.util.function.*;

public record PlayParticleAroundBlockSidesPayload(
		BlockPos pos, int quantity, Vec3 velocity, ParticleOptions particle, Direction[] sides
) implements CustomPacketPayload {
	
	public static final Type<PlayParticleAroundBlockSidesPayload> ID = SpectrumC2SPackets.makeId(
			"play_particle_around_block_sides");
	public static final StreamCodec<RegistryFriendlyByteBuf, PlayParticleAroundBlockSidesPayload> CODEC
			= StreamCodec.composite(
			BlockPos.STREAM_CODEC, PlayParticleAroundBlockSidesPayload::pos,
			ByteBufCodecs.VAR_INT, PlayParticleAroundBlockSidesPayload::quantity,
			PacketCodecHelper.VEC3D, PlayParticleAroundBlockSidesPayload::velocity,
			ParticleTypes.STREAM_CODEC, PlayParticleAroundBlockSidesPayload::particle,
			PacketCodecHelper.array(Direction.class, Direction.STREAM_CODEC), PlayParticleAroundBlockSidesPayload::sides,
			PlayParticleAroundBlockSidesPayload::new
	);
	
	public static void playParticleAroundBlockSides(
			ServerLevel level, int quantity, BlockPos pos, Vec3 velocity, ParticleOptions particleEffect,
			Predicate<ServerPlayer> sendCheck, Direction... sides
	) {
		Packet<?> packet = new ClientboundCustomPayloadPacket(
				new PlayParticleAroundBlockSidesPayload(pos, quantity, velocity, particleEffect, sides));
		
		for (ServerPlayer player : level.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false)) {
			if (sendCheck.test(player))
				continue;
			
			player.connection.send(packet);
		}
	}
	
	public static void execute(PlayParticleAroundBlockSidesPayload payload, IPayloadContext context) {
		ParticleHelper.playParticleAroundBlockSides(
				context.player()
						.level(), payload.particle, payload.pos, payload.sides, payload.quantity, payload.velocity
		);
	}
	
	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}