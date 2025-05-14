package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.blocks.shooting_star.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.client.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

public record PlayShootingStarParticlesPayload(Vec3 shootingStarPos, ShootingStar.Variant variant) implements CustomPacketPayload {
	
	public static final Type<PlayShootingStarParticlesPayload> ID = SpectrumC2SPackets.makeId("play_shooting_star_particles");
	public static final StreamCodec<FriendlyByteBuf, PlayShootingStarParticlesPayload> CODEC = StreamCodec.composite(
			PacketCodecHelper.VEC3D, PlayShootingStarParticlesPayload::shootingStarPos,
			ShootingStar.Variant.PACKET_CODEC, PlayShootingStarParticlesPayload::variant,
			PlayShootingStarParticlesPayload::new
	);
	
	public static void sendPlayShootingStarParticles(@NotNull ShootingStarEntity shootingStarEntity) {
		for (ServerPlayer player : PlayerLookup.tracking((ServerLevel) shootingStarEntity.level(), shootingStarEntity.blockPosition())) {
			ServerPlayNetworking.send(player, new PlayShootingStarParticlesPayload(shootingStarEntity.position(), shootingStarEntity.getShootingStarType()));
		}
	}
	
	@SuppressWarnings("resource")
	@Environment(EnvType.CLIENT)
	public static void execute(PlayShootingStarParticlesPayload payload, ClientPlayNetworking.Context context) {
		Minecraft client = context.client();
		
		ShootingStarEntity.playHitParticles(client.level, payload.shootingStarPos.x, payload.shootingStarPos.y, payload.shootingStarPos.z, payload.variant, 25);
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
