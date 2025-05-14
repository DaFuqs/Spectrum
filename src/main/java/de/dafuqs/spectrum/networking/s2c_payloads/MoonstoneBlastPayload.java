package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.spells.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.phys.*;

public record MoonstoneBlastPayload(double x, double y, double z, float power, float knockbackMod, Vec3 playerVelocity) implements CustomPacketPayload {
	
	public static final Type<MoonstoneBlastPayload> ID = SpectrumC2SPackets.makeId("moonstone_blast");
	public static final StreamCodec<FriendlyByteBuf, MoonstoneBlastPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.DOUBLE, MoonstoneBlastPayload::x,
			ByteBufCodecs.DOUBLE, MoonstoneBlastPayload::y,
			ByteBufCodecs.DOUBLE, MoonstoneBlastPayload::z,
			ByteBufCodecs.FLOAT, MoonstoneBlastPayload::power,
			ByteBufCodecs.FLOAT, MoonstoneBlastPayload::knockbackMod,
			PacketCodecHelper.VEC3D, MoonstoneBlastPayload::playerVelocity,
			MoonstoneBlastPayload::new
	);
	
	public static void sendMoonstoneBlast(ServerLevel serverWorld, MoonstoneStrike moonstoneStrike) {
		for (ServerPlayer player : PlayerLookup.tracking(serverWorld, BlockPos.containing(moonstoneStrike.getX(), moonstoneStrike.getY(), moonstoneStrike.getZ()))) {
			Vec3 playerVelocity = moonstoneStrike.getAffectedPlayers().getOrDefault(player, Vec3.ZERO);
			ServerPlayNetworking.send(player, new MoonstoneBlastPayload(moonstoneStrike.getX(), moonstoneStrike.getY(), moonstoneStrike.getZ(), moonstoneStrike.getPower(), moonstoneStrike.getKnockbackMod(), playerVelocity));
		}
	}
	
	@SuppressWarnings("resource")
	@Environment(EnvType.CLIENT)
	public static void execute(MoonstoneBlastPayload payload, ClientPlayNetworking.Context context) {
		Minecraft client = context.client();
		ClientLevel world = client.level;
		Player player = context.player();
		Vec3 playerVelocity = payload.playerVelocity();
		MoonstoneStrike.create(world, null, null, payload.x, payload.y, payload.z, payload.power, payload.knockbackMod);
		player.setDeltaMovement(player.getDeltaMovement().add(playerVelocity.x, playerVelocity.y, playerVelocity.z));
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
