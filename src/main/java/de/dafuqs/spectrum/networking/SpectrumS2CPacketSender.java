package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.blocks.memory.MemoryBlockEntity;
import de.dafuqs.spectrum.blocks.pedestal.PedestalVariant;
import de.dafuqs.spectrum.entity.entity.ShootingStarEntity;
import de.dafuqs.spectrum.particle.ParticlePattern;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.color.ColorRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class SpectrumS2CPacketSender {
	
	/**
	 * Play particle effect
	 *
	 * @param world                    the world of the pedestal
	 * @param position                 the pos of the particles
	 * @param particleEffectIdentifier The particle effect identifier to play
	 */
	public static void playParticleWithRandomOffsetAndVelocity(ServerWorld world, BlockPos position, Identifier particleEffectIdentifier, int amount) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBlockPos(position);
		buf.writeIdentifier(particleEffectIdentifier);
		buf.writeInt(amount);
		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking(world, position)) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_PARTICLE_AT_EXACT_BLOCK_POSITION_WITHOUT_VELOCITY_ID, buf);
		}
	}
	
	/**
	 * Play particle effect
	 *
	 * @param world          the world of the pedestal
	 * @param position       the pos of the particles
	 * @param particleEffect The particle effect to play
	 */
	public static void playParticleWithRandomOffsetAndVelocity(ServerWorld world, BlockPos position, ParticleType particleEffect, int amount) {
		playParticleWithRandomOffsetAndVelocity(world, position, Registry.PARTICLE_TYPE.getId(particleEffect), amount);
	}
	
	/**
	 * Play particle effect
	 *
	 * @param world                    the world of the pedestal
	 * @param position                 the pos of the particles
	 * @param particleEffectIdentifier The particle effect identifier to play
	 */
	public static void playParticleWithRandomOffsetAndVelocity(ServerWorld world, @NotNull Vec3d position, Identifier particleEffectIdentifier, int amount, @NotNull Vec3d randomOffset, @NotNull Vec3d randomVelocity) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeDouble(position.x);
		buf.writeDouble(position.y);
		buf.writeDouble(position.z);
		buf.writeIdentifier(particleEffectIdentifier);
		buf.writeInt(amount);
		buf.writeDouble(randomOffset.x);
		buf.writeDouble(randomOffset.y);
		buf.writeDouble(randomOffset.z);
		buf.writeDouble(randomVelocity.x);
		buf.writeDouble(randomVelocity.y);
		buf.writeDouble(randomVelocity.z);
		
		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking(world, new BlockPos(position))) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_PARTICLE_PACKET_WITH_RANDOM_OFFSET_AND_VELOCITY_ID, buf);
		}
	}
	
	/**
	 * Play anvil crafting particle effect
	 *
	 * @param world          the world of the pedestal
	 * @param position       the pos of the particles
	 * @param particleEffect The particle effect to play
	 */
	public static void playParticleWithExactOffsetAndVelocity(ServerWorld world, Vec3d position, @NotNull ParticleEffect particleEffect, int amount, Vec3d randomOffset, Vec3d randomVelocity) {
		playParticleWithExactOffsetAndVelocity(world, position, Registry.PARTICLE_TYPE.getId(particleEffect.getType()), amount, randomOffset, randomVelocity);
	}
	
	/**
	 * Play anvil crafting particle effect
	 *
	 * @param world                    the world of the pedestal
	 * @param position                 the pos of the particles
	 * @param particleEffectIdentifier The particle effect identifier to play
	 */
	public static void playParticleWithExactOffsetAndVelocity(ServerWorld world, @NotNull Vec3d position, Identifier particleEffectIdentifier, int amount, @NotNull Vec3d randomOffset, @NotNull Vec3d velocity) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeDouble(position.x);
		buf.writeDouble(position.y);
		buf.writeDouble(position.z);
		buf.writeIdentifier(particleEffectIdentifier);
		buf.writeInt(amount);
		buf.writeDouble(randomOffset.x);
		buf.writeDouble(randomOffset.y);
		buf.writeDouble(randomOffset.z);
		buf.writeDouble(velocity.x);
		buf.writeDouble(velocity.y);
		buf.writeDouble(velocity.z);
		
		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking(world, new BlockPos(position))) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_PARTICLE_PACKET_WITH_EXACT_OFFSET_AND_VELOCITY_ID, buf);
		}
	}
	
	/**
	 * Play particles matching a spawn pattern
	 *
	 * @param world          the world of the pedestal
	 * @param position       the pos of the particles
	 * @param particleEffect The particle effect to play
	 */
	public static void playParticleWithPatternAndVelocity(@Nullable PlayerEntity notThisPlayerEntity, ServerWorld world, @NotNull Vec3d position, @NotNull ParticleEffect particleEffect, @NotNull ParticlePattern pattern, double velocity) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeDouble(position.x);
		buf.writeDouble(position.y);
		buf.writeDouble(position.z);
		buf.writeIdentifier(Registry.PARTICLE_TYPE.getId(particleEffect.getType()));
		buf.writeInt(pattern.ordinal());
		buf.writeDouble(velocity);
		
		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking(world, new BlockPos(position))) {
			if (notThisPlayerEntity == null || !player.equals(notThisPlayerEntity)) {
				ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_PARTICLE_PACKET_WITH_PATTERN_AND_VELOCITY_ID, buf);
			}
		}
	}
	
	/**
	 * Play anvil crafting particle effect
	 *
	 * @param world          the world of the pedestal
	 * @param position       the pos of the particles
	 * @param particleEffect The particle effect to play
	 */
	public static void playParticleWithRandomOffsetAndVelocity(ServerWorld world, Vec3d position, @NotNull ParticleEffect particleEffect, int amount, Vec3d randomOffset, Vec3d randomVelocity) {
		playParticleWithRandomOffsetAndVelocity(world, position, Registry.PARTICLE_TYPE.getId(particleEffect.getType()), amount, randomOffset, randomVelocity);
	}
	
	/**
	 * @param world    the world of the pedestal
	 * @param blockPos the blockpos of the newly created light
	 */
	public static void sendLightCreatedParticle(World world, BlockPos blockPos) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBlockPos(blockPos);
		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, blockPos)) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_LIGHT_CREATED_PACKET_ID, buf);
		}
	}
	
	/**
	 * @param world     the world of the pedestal
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
	
	public static void sendItemTransferPacket(ServerWorld world, @NotNull ItemTransfer itemTransfer) {
		BlockPos blockPos = itemTransfer.getOrigin();
		
		PacketByteBuf buf = PacketByteBufs.create();
		ItemTransfer.writeToBuf(buf, itemTransfer);
		
		for (ServerPlayerEntity player : PlayerLookup.tracking(world, blockPos)) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.INITIATE_ITEM_TRANSFER, buf);
		}
	}
	
	public static void playTransphereParticle(ServerWorld world, @NotNull Transphere transphere) {
		BlockPos blockPos = transphere.getOrigin();
		
		PacketByteBuf buf = PacketByteBufs.create();
		Transphere.writeToBuf(buf, transphere);
		
		for (ServerPlayerEntity player : PlayerLookup.tracking(world, blockPos)) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.INITIATE_TRANSPHERE, buf);
		}
	}
	
	public static void sendExperienceOrbTransferPacket(ServerWorld world, @NotNull ExperienceTransfer experienceTransfer) {
		BlockPos blockPos = experienceTransfer.getOrigin();
		
		PacketByteBuf buf = PacketByteBufs.create();
		ExperienceTransfer.writeToBuf(buf, experienceTransfer);
		
		for (ServerPlayerEntity player : PlayerLookup.tracking(world, blockPos)) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.INITIATE_EXPERIENCE_TRANSFER, buf);
		}
	}
	
	public static void sendBlockPosEventTransferPacket(ServerWorld world, @NotNull BlockPosEventTransfer blockPosEventTransfer) {
		BlockPos blockPos = blockPosEventTransfer.getOrigin();
		
		PacketByteBuf buf = PacketByteBufs.create();
		BlockPosEventTransfer.writeToBuf(buf, blockPosEventTransfer);
		
		for (ServerPlayerEntity player : PlayerLookup.tracking(world, blockPos)) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.INITIATE_BLOCK_POS_EVENT_TRANSFER, buf);
		}
	}
	
	public static void sendWirelessRedstonePacket(ServerWorld world, @NotNull WirelessRedstoneTransmission wirelessRedstoneTransmission) {
		BlockPos blockPos = wirelessRedstoneTransmission.getOrigin();
		
		PacketByteBuf buf = PacketByteBufs.create();
		WirelessRedstoneTransmission.writeToBuf(buf, wirelessRedstoneTransmission);
		
		for (ServerPlayerEntity player : PlayerLookup.tracking(world, blockPos)) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.INITIATE_WIRELESS_REDSTONE_TRANSMISSION, buf);
		}
	}
	
	public static void sendPlayItemEntityAbsorbedParticle(World world, @NotNull ItemEntity itemEntity) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeDouble(itemEntity.getPos().x);
		buf.writeDouble(itemEntity.getPos().y);
		buf.writeDouble(itemEntity.getPos().z);
		
		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, itemEntity.getBlockPos())) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_ITEM_ENTITY_ABSORBED_PARTICLE_EFFECT_PACKET_ID, buf);
		}
	}
	
	public static void sendPlayExperienceOrbEntityAbsorbedParticle(World world, @NotNull ExperienceOrbEntity experienceOrbEntity) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeDouble(experienceOrbEntity.getPos().x);
		buf.writeDouble(experienceOrbEntity.getPos().y);
		buf.writeDouble(experienceOrbEntity.getPos().z);
		
		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, experienceOrbEntity.getBlockPos())) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_EXPERIENCE_ORB_ENTITY_ABSORBED_PARTICLE_EFFECT_PACKET_ID, buf);
		}
	}
	
	public static void sendPlayBlockBoundSoundInstance(SoundEvent soundEvent, @NotNull ServerWorld world, BlockPos blockPos, int maxDurationTicks) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeIdentifier(Registry.SOUND_EVENT.getId(soundEvent));
		buf.writeIdentifier(Registry.BLOCK.getId(world.getBlockState(blockPos).getBlock()));
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
		buf.writeIdentifier(Registry.BLOCK.getId(world.getBlockState(blockPos).getBlock()));
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
	
}