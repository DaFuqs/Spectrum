package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.magic.*;
import de.dafuqs.spectrum.networking.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.handling.*;

public record MoonstoneStrikePayload(double x, double y, double z, float power, float knockbackMod, Vec3 playerVelocity) implements CustomPacketPayload {
	
	public static final Type<MoonstoneStrikePayload> ID = SpectrumC2SPackets.makeId("moonstone_blast");
	public static final StreamCodec<FriendlyByteBuf, MoonstoneStrikePayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.DOUBLE, MoonstoneStrikePayload::x,
			ByteBufCodecs.DOUBLE, MoonstoneStrikePayload::y,
			ByteBufCodecs.DOUBLE, MoonstoneStrikePayload::z,
			ByteBufCodecs.FLOAT, MoonstoneStrikePayload::power,
			ByteBufCodecs.FLOAT, MoonstoneStrikePayload::knockbackMod,
			PacketCodecHelper.VEC3D, MoonstoneStrikePayload::playerVelocity,
			MoonstoneStrikePayload::new
	);
	
	public static void sendMoonstoneStrike(ServerLevel serverWorld, MoonstoneStrike moonstoneStrike) {
		for (ServerPlayer player : serverWorld.getChunkSource().chunkMap.getPlayers(new ChunkPos(BlockPos.containing(moonstoneStrike.getX(), moonstoneStrike.getY(), moonstoneStrike.getZ())), false)) {
			PacketDistributor.sendToPlayer(
					player,
					new MoonstoneStrikePayload(moonstoneStrike.getX(), moonstoneStrike.getY(), moonstoneStrike.getZ(), moonstoneStrike.getPower(), moonstoneStrike.getKnockbackMod(), moonstoneStrike.getAffectedPlayers().getOrDefault(player, Vec3.ZERO))
			);
		}
	}
	
	public static void execute(MoonstoneStrikePayload payload, IPayloadContext context) {
		Player player = context.player();
		Vec3 playerVelocity = payload.playerVelocity();
		MoonstoneStrike.create(player.level(), null, null, payload.x, payload.y, payload.z, payload.power, payload.knockbackMod);
		player.setDeltaMovement(player.getDeltaMovement().add(playerVelocity.x, playerVelocity.y, playerVelocity.z));
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
	
}