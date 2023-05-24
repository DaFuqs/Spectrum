package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.color.*;
import de.dafuqs.spectrum.spells.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.particle.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.registry.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SpectrumS2CPacketSender {

	/**
	 * Play particle effect
	 *
	 * @param world          the world
	 * @param position       the pos of the particles
	 * @param particleEffect The particle effect to play
	 */
	public static void playParticleWithRandomOffsetAndVelocity(ServerWorld world, Vec3d position, @NotNull ParticleEffect particleEffect, int amount, Vec3d randomOffset, Vec3d randomVelocity) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeDouble(position.x);
		buf.writeDouble(position.y);
		buf.writeDouble(position.z);
		buf.writeIdentifier(Registries.PARTICLE_TYPE.getId(particleEffect.getType()));
		buf.writeInt(amount);
		buf.writeDouble(randomOffset.x);
		buf.writeDouble(randomOffset.y);
		buf.writeDouble(randomOffset.z);
		buf.writeDouble(randomVelocity.x);
		buf.writeDouble(randomVelocity.y);
		buf.writeDouble(randomVelocity.z);
		
		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking(world, BlockPos.ofFloored(position))) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_PARTICLE_WITH_RANDOM_OFFSET_AND_VELOCITY, buf);
		}
	}

	/**
	 * Play particle effect
	 *
	 * @param world          the world
	 * @param position       the pos of the particles
	 * @param particleEffect The particle effect to play
	 */
	public static void playParticles(ServerWorld world, BlockPos position, ParticleEffect particleEffect, int amount) {
		playParticleWithExactVelocity(world, Vec3d.ofCenter(position), particleEffect, amount, Vec3d.ZERO);
	}

	/**
	 * Play particle effect
	 *
	 * @param world          the world
	 * @param position       the pos of the particles
	 * @param particleEffect The particle effect to play
	 */
	public static void playParticleWithExactVelocity(ServerWorld world, @NotNull Vec3d position, @NotNull ParticleEffect particleEffect, int amount, @NotNull Vec3d velocity) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeDouble(position.x);
		buf.writeDouble(position.y);
		buf.writeDouble(position.z);
		buf.writeIdentifier(Registries.PARTICLE_TYPE.getId(particleEffect.getType()));
		buf.writeInt(amount);
		buf.writeDouble(velocity.x);
		buf.writeDouble(velocity.y);
		buf.writeDouble(velocity.z);
		
		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking(world, BlockPos.ofFloored(position))) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_PARTICLE_WITH_EXACT_VELOCITY, buf);
		}
	}

	/**
	 * Play particles matching a spawn pattern
	 *
	 * @param world          the world
	 * @param position       the pos of the particles
	 * @param particleEffect The particle effect to play
	 */
	public static void playParticleWithPatternAndVelocity(@Nullable PlayerEntity notThisPlayerEntity, ServerWorld world, @NotNull Vec3d position, @NotNull ParticleEffect particleEffect, @NotNull VectorPattern pattern, double velocity) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeDouble(position.x);
		buf.writeDouble(position.y);
		buf.writeDouble(position.z);
		buf.writeIdentifier(Registries.PARTICLE_TYPE.getId(particleEffect.getType()));
		buf.writeInt(pattern.ordinal());
		buf.writeDouble(velocity);
		
		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking(world, BlockPos.ofFloored(position))) {
			if (!player.equals(notThisPlayerEntity)) {
				ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_PARTICLE_PACKET_WITH_PATTERN_AND_VELOCITY_ID, buf);
			}
		}
	}

	/**
	 * @param world     the world
	 * @param blockPos  the blockpos of the pedestal
	 * @param itemStack the itemstack that was crafted
	 */
	public static void sendPlayPedestalCraftingFinishedParticle(World world, BlockPos blockPos, ItemStack itemStack) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBlockPos(blockPos);
		buf.writeItemStack(itemStack);
		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, blockPos)) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_PEDESTAL_CRAFTING_FINISHED_PARTICLE_PACKET_ID, buf);
		}
	}
	
	public static void sendPlayFusionCraftingInProgressParticles(World world, BlockPos blockPos) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBlockPos(blockPos);
		
		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, blockPos)) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_FUSION_CRAFTING_IN_PROGRESS_PARTICLE_PACKET_ID, buf);
		}
	}
	
	public static void sendPlayFusionCraftingFinishedParticles(World world, BlockPos blockPos, @NotNull ItemStack itemStack) {
		Optional<DyeColor> optionalItemColor = ColorRegistry.ITEM_COLORS.getMapping(itemStack.getItem());
		
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBlockPos(blockPos);
		
		if (optionalItemColor.isPresent()) {
			buf.writeInt(optionalItemColor.get().ordinal());
		} else {
            buf.writeInt(DyeColor.LIGHT_GRAY.ordinal());
        }

        // Iterate over all players tracking a position in the world and send the packet to each player
        for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, blockPos)) {
            ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_FUSION_CRAFTING_FINISHED_PARTICLE_PACKET_ID, buf);
        }
    }

    public static void sendPastelTransmissionParticle(PastelNetwork network, int travelTime, @NotNull PastelTransmission transfer) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeUuid(network.getUUID());
		buf.writeInt(travelTime);
		PastelTransmission.writeToBuf(buf, transfer);
	
		for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) network.getWorld(), transfer.getStartPos())) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.PASTEL_TRANSMISSION, buf);
		}
	}
	
	public static void playColorTransmissionParticle(ServerWorld world, @NotNull ColoredTransmission transfer) {
		BlockPos blockPos = BlockPos.ofFloored(transfer.getOrigin());
		
		PacketByteBuf buf = PacketByteBufs.create();
		ColoredTransmission.writeToBuf(buf, transfer);
		
		for (ServerPlayerEntity player : PlayerLookup.tracking(world, blockPos)) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.COLOR_TRANSMISSION, buf);
		}
	}
	
	public static void playTransmissionParticle(ServerWorld world, @NotNull TypedTransmission transmission) {
		BlockPos blockPos = BlockPos.ofFloored(transmission.getOrigin());
		
		PacketByteBuf buf = PacketByteBufs.create();
		TypedTransmission.writeToBuf(buf, transmission);
		
		for (ServerPlayerEntity player : PlayerLookup.tracking(world, blockPos)) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.TYPED_TRANSMISSION, buf);
		}
	}
	
	public static void sendPlayBlockBoundSoundInstance(SoundEvent soundEvent, @NotNull ServerWorld world, BlockPos blockPos, int maxDurationTicks) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeIdentifier(Registries.SOUND_EVENT.getId(soundEvent));
		buf.writeIdentifier(Registries.BLOCK.getId(world.getBlockState(blockPos).getBlock()));
		buf.writeBlockPos(blockPos);
		buf.writeInt(maxDurationTicks);
		
		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking(world, blockPos)) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_BLOCK_BOUND_SOUND_INSTANCE, buf);
		}
	}
	
	public static void sendPlayTakeOffBeltSoundInstance(ServerPlayerEntity playerEntity) {
		PacketByteBuf buf = PacketByteBufs.create();
		ServerPlayNetworking.send(playerEntity, SpectrumS2CPackets.PLAY_TAKE_OFF_BELT_SOUND_INSTANCE, buf);
	}
	
	public static void sendCancelBlockBoundSoundInstance(@NotNull ServerWorld world, BlockPos blockPos) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeIdentifier(new Identifier("stop"));
		buf.writeIdentifier(Registries.BLOCK.getId(world.getBlockState(blockPos).getBlock()));
		buf.writeBlockPos(blockPos);
		buf.writeInt(1);
		
		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking(world, blockPos)) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_BLOCK_BOUND_SOUND_INSTANCE, buf);
		}
	}
	
	public static void spawnPedestalUpgradeParticles(World world, BlockPos blockPos, @NotNull PedestalVariant newPedestalVariant) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBlockPos(blockPos);
		buf.writeInt(newPedestalVariant.getRecipeTier().ordinal());
		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, blockPos)) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_PEDESTAL_UPGRADED_PARTICLE_PACKET_ID, buf);
		}
	}
	
	public static void spawnPedestalStartCraftingParticles(PedestalBlockEntity pedestalBlockEntity) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBlockPos(pedestalBlockEntity.getPos());
		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) pedestalBlockEntity.getWorld(), pedestalBlockEntity.getPos())) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_PEDESTAL_START_CRAFTING_PARTICLE_PACKET_ID, buf);
		}
	}
	
	public static void sendPlayShootingStarParticles(@NotNull ShootingStarEntity shootingStarEntity) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeDouble(shootingStarEntity.getPos().getX());
		buf.writeDouble(shootingStarEntity.getPos().getY());
		buf.writeDouble(shootingStarEntity.getPos().getZ());
		buf.writeInt(shootingStarEntity.getShootingStarType().ordinal());
		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) shootingStarEntity.world, shootingStarEntity.getBlockPos())) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_SHOOTING_STAR_PARTICLES, buf);
		}
	}
	
	public static void startSkyLerping(@NotNull ServerWorld serverWorld, int duration) {
		PacketByteBuf buf = PacketByteBufs.create();
		long timeOfDay = serverWorld.getTimeOfDay();
		buf.writeLong(timeOfDay);
		buf.writeLong(timeOfDay + duration);
		
		for (ServerPlayerEntity player : serverWorld.getPlayers()) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.START_SKY_LERPING, buf);
		}
	}
	
	public static void playMemoryManifestingParticles(ServerWorld serverWorld, @NotNull BlockPos blockPos, EntityType<?> entityType, int amount) {
		Pair<Integer, Integer> eggColors = MemoryBlockEntity.getEggColorsForEntity(entityType);
		
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBlockPos(blockPos);
		buf.writeInt(eggColors.getLeft());
		buf.writeInt(eggColors.getRight());
		buf.writeInt(amount);
		
		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking(serverWorld, blockPos)) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_MEMORY_MANIFESTING_PARTICLES, buf);
		}
	}
	
	public static void sendBossBarUpdatePropertiesPacket(UUID uuid, boolean serpentMusic, Collection<ServerPlayerEntity> players) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeUuid(uuid);
		buf.writeBoolean(serpentMusic);
		
		for (ServerPlayerEntity player : players) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.UPDATE_BOSS_BAR, buf);
		}
	}
	
	public static void updateBlockEntityInk(BlockPos pos, InkStorage inkStorage, ServerPlayerEntity player) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBlockPos(pos);
		buf.writeLong(inkStorage.getCurrentTotal());
		
		Map<InkColor, Long> colors = inkStorage.getEnergy();
		buf.writeInt(colors.size());
		for (Map.Entry<InkColor, Long> color : colors.entrySet()) {
			buf.writeString(color.getKey().toString());
			buf.writeLong(color.getValue());
		}
		
		ServerPlayNetworking.send(player, SpectrumS2CPackets.UPDATE_BLOCK_ENTITY_INK, buf);
	}
	
	public static void sendInkColorSelected(@Nullable InkColor color, ServerPlayerEntity player) {
		PacketByteBuf packetByteBuf = PacketByteBufs.create();
		if (color == null) {
			packetByteBuf.writeBoolean(false);
		} else {
			packetByteBuf.writeBoolean(true);
			packetByteBuf.writeString(color.toString());
		}
		ServerPlayNetworking.send(player, SpectrumS2CPackets.INK_COLOR_SELECTED, packetByteBuf);
	}
	
	public static void playInkEffectParticles(ServerWorld serverWorld, InkColor inkColor, Vec3d effectPos, float potency) {
		PacketByteBuf packetByteBuf = PacketByteBufs.create();
		packetByteBuf.writeString(inkColor.toString());
		packetByteBuf.writeDouble(effectPos.x);
		packetByteBuf.writeDouble(effectPos.y);
		packetByteBuf.writeDouble(effectPos.z);
		packetByteBuf.writeFloat(potency);
		
		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking(serverWorld, BlockPos.ofFloored(effectPos))) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_INK_EFFECT_PARTICLES, packetByteBuf);
		}
	}
	
	public static void playPresentOpeningParticles(ServerWorld serverWorld, BlockPos pos, Map<DyeColor, Integer> colors) {
		PacketByteBuf packetByteBuf = PacketByteBufs.create();
		packetByteBuf.writeBlockPos(pos);
		packetByteBuf.writeInt(colors.size());
		for (Map.Entry<DyeColor, Integer> color : colors.entrySet()) {
			packetByteBuf.writeByte(color.getKey().getId());
			packetByteBuf.writeByte(color.getValue());
		}
		
		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking(serverWorld, pos)) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_PRESENT_OPENING_PARTICLES, packetByteBuf);
		}
	}
	
	public static void playAscensionAppliedEffects(ServerPlayerEntity player) {
		ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_ASCENSION_APPLIED_EFFECTS, PacketByteBufs.create());
	}
	
	public static void playDivinityAppliedEffects(ServerPlayerEntity player) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeInt(player.getId());
		ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_DIVINITY_APPLIED_EFFECTS, buf);
	}

	public static void sendMoonstoneBlast(ServerWorld serverWorld, MoonstoneStrike moonstoneStrike) {
		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking(serverWorld, BlockPos.ofFloored(moonstoneStrike.getX(), moonstoneStrike.getY(), moonstoneStrike.getZ()))) {
			Vec3d playerVelocity = moonstoneStrike.getAffectedPlayers().getOrDefault(player, Vec3d.ZERO);

			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeDouble(moonstoneStrike.getX());
			buf.writeDouble(moonstoneStrike.getY());
			buf.writeDouble(moonstoneStrike.getZ());
			buf.writeFloat(moonstoneStrike.getPower());
			buf.writeDouble(playerVelocity.x);
			buf.writeDouble(playerVelocity.y);
			buf.writeDouble(playerVelocity.z);

			ServerPlayNetworking.send(player, SpectrumS2CPackets.MOONSTONE_BLAST, buf);
		}
	}
	
}