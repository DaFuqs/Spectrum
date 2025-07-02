package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import org.jetbrains.annotations.*;
import org.joml.*;

public record PlayMemoryManifestingParticlesPayload(BlockPos pos, int eggColor1, int eggColor2, int amount) implements CustomPacketPayload {
	
	public static final Type<PlayMemoryManifestingParticlesPayload> ID = SpectrumC2SPackets.makeId("play_memory_manifesting_particles");
	public static final StreamCodec<FriendlyByteBuf, PlayMemoryManifestingParticlesPayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, PlayMemoryManifestingParticlesPayload::pos,
			ByteBufCodecs.INT, PlayMemoryManifestingParticlesPayload::eggColor1,
			ByteBufCodecs.INT, PlayMemoryManifestingParticlesPayload::eggColor2,
			ByteBufCodecs.INT, PlayMemoryManifestingParticlesPayload::amount,
			PlayMemoryManifestingParticlesPayload::new
	);
	
	public static void playMemoryManifestingParticles(ServerLevel serverWorld, @NotNull BlockPos pos, EntityType<?> entityType, int amount) {
		Tuple<Integer, Integer> eggColors = MemoryBlockEntity.getEggColorsForEntity(entityType);
		for (ServerPlayer player : PlayerLookup.tracking(serverWorld, pos)) {
			ServerPlayNetworking.send(player, new PlayMemoryManifestingParticlesPayload(pos, eggColors.getA(), eggColors.getB(), amount));
		}
	}
	
	@SuppressWarnings("resource")
	@Environment(EnvType.CLIENT)
	public static void execute(PlayMemoryManifestingParticlesPayload payload, ClientPlayNetworking.Context context) {
		Minecraft client = context.client();
		RandomSource random = client.level.random;
		
		Vector3f colorVec1 = SpectrumColorHelper.colorIntToVec(payload.eggColor1);
		Vector3f colorVec2 = SpectrumColorHelper.colorIntToVec(payload.eggColor1);
		
		BlockPos pos = payload.pos;
		for (int i = 0; i < payload.amount; i++) {
			int randomLifetime = 30 + random.nextInt(20);
			
			// color1
			client.level.addParticle(
					new DynamicParticleEffect(ColoredCraftingParticleEffect.WHITE.getType(), 0.5F, colorVec1, 1.0F, randomLifetime, false, true),
					pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ(),
					0.15 - random.nextFloat() * 0.3, random.nextFloat() * 0.15 + 0.1, 0.15 - random.nextFloat() * 0.3
			);
			
			// color2
			client.level.addParticle(
					new DynamicParticleEffect(ColoredCraftingParticleEffect.WHITE.getType(), 0.5F, colorVec2, 1.0F, randomLifetime, false, true),
					pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
					0.15 - random.nextFloat() * 0.3, random.nextFloat() * 0.15 + 0.1, 0.15 - random.nextFloat() * 0.3
			);
		}
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
