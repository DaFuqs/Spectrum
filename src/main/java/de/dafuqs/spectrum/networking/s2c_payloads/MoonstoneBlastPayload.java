package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.spells.*;
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
import org.jetbrains.annotations.*;

public record MoonstoneBlastPayload(double x, double y, double z, Vec3 data, Vec3 playerVelocity) implements CustomPacketPayload {
	
	public static final Type<MoonstoneBlastPayload> ID = SpectrumC2SPackets.makeId("moonstone_blast");
	public static final StreamCodec<FriendlyByteBuf, MoonstoneBlastPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.DOUBLE, MoonstoneBlastPayload::x,
			ByteBufCodecs.DOUBLE, MoonstoneBlastPayload::y,
			ByteBufCodecs.DOUBLE, MoonstoneBlastPayload::z,
			PacketCodecHelper.VEC3D, MoonstoneBlastPayload::data,
			PacketCodecHelper.VEC3D, MoonstoneBlastPayload::playerVelocity,
			MoonstoneBlastPayload::new
	);
	
	public static void sendMoonstoneBlast(ServerLevel serverWorld, MoonstoneStrike moonstoneStrike, float pitch) {
		for (ServerPlayer player : serverWorld.getChunkSource().chunkMap.getPlayers(new ChunkPos(BlockPos.containing(moonstoneStrike.getX(), moonstoneStrike.getY(), moonstoneStrike.getZ())), false)) {
			PacketDistributor.sendToPlayer(
					player,
					new MoonstoneBlastPayload(moonstoneStrike.getX(), moonstoneStrike.getY(), moonstoneStrike.getZ(), new Vec3(moonstoneStrike.getPower(), moonstoneStrike.getKnockbackMod(), pitch), moonstoneStrike.getAffectedPlayers().getOrDefault(player, Vec3.ZERO))
			);
		}
	}
	
	public static void execute(MoonstoneBlastPayload payload, IPayloadContext context) {
		Player player = context.player();
		Vec3 playerVelocity = payload.playerVelocity();
		Vec3 data = payload.data;
		MoonstoneStrike.create(player.level(), null, null, payload.x, payload.y, payload.z, (float) data.x, (float) data.y, (float) data.z);
		player.setDeltaMovement(player.getDeltaMovement().add(playerVelocity.x, playerVelocity.y, playerVelocity.z));
	}
	
	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return ID;
	}
	
}