package de.dafuqs.spectrum.networking.s2c_payloads;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.handling.*;

public record PlayPedestalUpgradedParticlePayload(BlockPos pedestalPos, PedestalRecipeTier newTier) implements CustomPacketPayload {
	
	public static final Type<PlayPedestalUpgradedParticlePayload> ID = SpectrumC2SPackets.makeId("play_pedestal_upgraded_particle");
	public static final StreamCodec<FriendlyByteBuf, PlayPedestalUpgradedParticlePayload> CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, PlayPedestalUpgradedParticlePayload::pedestalPos,
			PedestalRecipeTier.PACKET_CODEC, PlayPedestalUpgradedParticlePayload::newTier,
			PlayPedestalUpgradedParticlePayload::new
	);
	
	public static void spawnPedestalUpgradeParticles(PedestalBlockEntity pedestalBlockEntity, PedestalVariant newPedestalVariant) {
		PacketDistributor.sendToPlayersTrackingChunk(
				(ServerLevel) pedestalBlockEntity.getLevel(), new ChunkPos(pedestalBlockEntity.getBlockPos()),
				new PlayPedestalUpgradedParticlePayload(pedestalBlockEntity.getBlockPos(), newPedestalVariant.getRecipeTier())
		);
	}
	
	public static void execute(PlayPedestalUpgradedParticlePayload payload, IPayloadContext context) {
		spawnUpgradeParticleEffectsForTier(context.player().level(), payload.pedestalPos, payload.newTier);
	}
	/**
	 * Called when a pedestal is upgraded to a new tier
	 * (like amethyst to the cmy variant). Spawns lots of matching particles.
	 *
	 * @param newPedestalRecipeTier The tier the pedestal has been upgraded to
	 */
	public static void spawnUpgradeParticleEffectsForTier(Level world, BlockPos blockPos, PedestalRecipeTier newPedestalRecipeTier) {
		RandomSource random = world.getRandom();
		
		switch (newPedestalRecipeTier) {
			case COMPLEX -> {
				ParticleOptions particleEffect = ColoredCraftingParticleEffect.WHITE;
				for (int i = 0; i < 25; i++) {
					float randomZ = random.nextFloat() * 1.2F;
					world.addParticle(particleEffect, blockPos.getX() + 1.1, blockPos.getY(), blockPos.getZ() + randomZ, 0.0D, 0.03D, 0.0D);
				}
				for (int i = 0; i < 25; i++) {
					float randomZ = random.nextFloat() * 1.2F;
					world.addParticle(particleEffect, blockPos.getX() - 0.1, blockPos.getY(), blockPos.getZ() + randomZ, 0.0D, 0.03D, 0.0D);
				}
				for (int i = 0; i < 25; i++) {
					float randomX = random.nextFloat() * 1.2F;
					world.addParticle(particleEffect, blockPos.getX() + randomX, blockPos.getY(), blockPos.getZ() + 1.1, 0.0D, 0.03D, 0.0D);
				}
				for (int i = 0; i < 25; i++) {
					float randomX = random.nextFloat() * 1.2F;
					world.addParticle(particleEffect, blockPos.getX() + randomX, blockPos.getY(), blockPos.getZ() - 0.1, 0.0D, 0.03D, 0.0D);
				}
			}
			case ADVANCED -> {
				ParticleOptions particleEffect = ColoredCraftingParticleEffect.BLACK;
				for (int i = 0; i < 25; i++) {
					float randomZ = random.nextFloat() * 1.2F;
					world.addParticle(particleEffect, blockPos.getX() + 1.1, blockPos.getY(), blockPos.getZ() + randomZ, 0.0D, 0.03D, 0.0D);
				}
				for (int i = 0; i < 25; i++) {
					float randomZ = random.nextFloat() * 1.2F;
					world.addParticle(particleEffect, blockPos.getX() - 0.1, blockPos.getY(), blockPos.getZ() + randomZ, 0.0D, 0.03D, 0.0D);
				}
				for (int i = 0; i < 25; i++) {
					float randomX = random.nextFloat() * 1.2F;
					world.addParticle(particleEffect, blockPos.getX() + randomX, blockPos.getY(), blockPos.getZ() + 1.1, 0.0D, 0.03D, 0.0D);
				}
				for (int i = 0; i < 25; i++) {
					float randomX = random.nextFloat() * 1.2F;
					world.addParticle(particleEffect, blockPos.getX() + randomX, blockPos.getY(), blockPos.getZ() - 0.1, 0.0D, 0.03D, 0.0D);
				}
			}
			case SIMPLE -> {
				ParticleOptions particleEffectC = ColoredCraftingParticleEffect.CYAN;
				ParticleOptions particleEffectM = ColoredCraftingParticleEffect.MAGENTA;
				ParticleOptions particleEffectY = ColoredCraftingParticleEffect.YELLOW;
				for (int i = 0; i < 25; i++) {
					float randomZ = random.nextFloat() * 1.2F;
					world.addParticle(particleEffectY, blockPos.getX() + 1.1, blockPos.getY() + 0.1, blockPos.getZ() + randomZ, 0.0D, 0.05D, 0.0D);
				}
				for (int i = 0; i < 25; i++) {
					float randomZ = random.nextFloat() * 1.2F;
					world.addParticle(particleEffectC, blockPos.getX() - 0.1, blockPos.getY() + 0.1, blockPos.getZ() + randomZ, 0.0D, 0.05D, 0.0D);
				}
				for (int i = 0; i < 25; i++) {
					float randomX = random.nextFloat() * 1.2F;
					world.addParticle(particleEffectM, blockPos.getX() + randomX, blockPos.getY() + 0.1, blockPos.getZ() + 1.1, 0.0D, 0.05D, 0.0D);
				}
				for (int i = 0; i < 25; i++) {
					float randomX = random.nextFloat() * 1.2F;
					world.addParticle(particleEffectM, blockPos.getX() + randomX, blockPos.getY() + 0.1, blockPos.getZ() - 0.1, 0.0D, 0.05D, 0.0D);
				}
			}
			case BASIC -> {
			}
		}
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}