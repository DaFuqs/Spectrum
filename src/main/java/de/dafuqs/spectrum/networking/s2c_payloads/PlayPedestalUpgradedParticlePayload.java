package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

public record PlayPedestalUpgradedParticlePayload(BlockPos pedestalPos, PedestalRecipeTier newTier) implements CustomPacketPayload {
	
	public static final Type<PlayPedestalUpgradedParticlePayload> ID = SpectrumC2SPackets.makeId("play_pedestal_upgraded_particle");
	public static final StreamCodec<FriendlyByteBuf, PlayPedestalUpgradedParticlePayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, PlayPedestalUpgradedParticlePayload::pedestalPos,
			PedestalRecipeTier.PACKET_CODEC, PlayPedestalUpgradedParticlePayload::newTier,
			PlayPedestalUpgradedParticlePayload::new
	);
	
	public static void spawnPedestalUpgradeParticles(Level world, BlockPos pedestalPos, @NotNull PedestalVariant newPedestalVariant) {
		for (ServerPlayer player : PlayerLookup.tracking((ServerLevel) world, pedestalPos)) {
			ServerPlayNetworking.send(player, new PlayPedestalUpgradedParticlePayload(pedestalPos, newPedestalVariant.getRecipeTier()));
		}
	}
	
	@Environment(EnvType.CLIENT)
	public static void execute(PlayPedestalUpgradedParticlePayload payload, ClientPlayNetworking.Context context) {
		PedestalBlock.spawnUpgradeParticleEffectsForTier(payload.pedestalPos, payload.newTier);
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
