package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.blocks.particle_spawner.ParticleSpawnerBlockEntity;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlock;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.color.ColorRegistry;
import de.dafuqs.spectrum.sound.BlockBoundSoundInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Random;

public class SpectrumS2CPackets {
	
	public static final Identifier PLAY_LIGHT_CREATED_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "play_light_created_particle");
	public static final Identifier PLAY_PEDESTAL_CRAFTING_FINISHED_PARTICLE_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "play_pedestal_crafting_finished_particle");
	public static final Identifier PLAY_PEDESTAL_UPGRADED_PARTICLE_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "play_pedestal_upgraded_particle");
	public static final Identifier PLAY_FUSION_CRAFTING_FINISHED_PARTICLE_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "play_fusion_crafting_finished_particle");
	public static final Identifier PLAY_PARTICLE_AT_EXACT_BLOCK_POSITION_WITHOUT_VELOCITY_ID = new Identifier(SpectrumCommon.MOD_ID, "play_particle");
	public static final Identifier PLAY_PARTICLE_PACKET_WITH_RANDOM_OFFSET_AND_VELOCITY_ID = new Identifier(SpectrumCommon.MOD_ID, "play_particle_with_offset");
	public static final Identifier PLAY_PARTICLE_PACKET_WITH_EXACT_OFFSET_AND_VELOCITY_ID = new Identifier(SpectrumCommon.MOD_ID, "play_particle_with_random_offset_and_velocity");
	public static final Identifier CHANGE_PARTICLE_SPAWNER_SETTINGS_CLIENT_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "change_particle_spawner_settings_client");
	public static final Identifier INITIATE_ITEM_TRANSFER = new Identifier(SpectrumCommon.MOD_ID, "initiate_item_transfer");
	public static final Identifier INITIATE_TRANSPHERE = new Identifier(SpectrumCommon.MOD_ID, "initiate_transphere");
	public static final Identifier INITIATE_EXPERIENCE_TRANSFER = new Identifier(SpectrumCommon.MOD_ID, "initiate_experience_transfer");
	public static final Identifier INITIATE_WIRELESS_REDSTONE_TRANSMISSION = new Identifier(SpectrumCommon.MOD_ID, "initiate_wireless_redstone_transmission");
	public static final Identifier PLAY_ITEM_ENTITY_ABSORBED_PARTICLE_EFFECT_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "item_entity_absorbed");
	public static final Identifier PLAY_EXPERIENCE_ORB_ENTITY_ABSORBED_PARTICLE_EFFECT_PACKET_ID = new Identifier(SpectrumCommon.MOD_ID, "experience_orb_entity_absorbed");
	public static final Identifier PLAY_BLOCK_BOUND_SOUND_INSTANCE = new Identifier(SpectrumCommon.MOD_ID, "play_pedestal_crafting_sound_instance");

	@Environment(EnvType.CLIENT)
	public static void registerS2CReceivers() {
		ClientPlayNetworking.registerGlobalReceiver(PLAY_PARTICLE_AT_EXACT_BLOCK_POSITION_WITHOUT_VELOCITY_ID, (client, handler, buf, responseSender) -> {
			BlockPos position = buf.readBlockPos();
			ParticleType<?> particleType = Registry.PARTICLE_TYPE.get(buf.readIdentifier());
			int amount = buf.readInt();
			if(particleType instanceof ParticleEffect particleEffect) {
				client.execute(() -> {
					// Everything in this lambda is running on the render thread
					for(int i = 0; i < amount; i++) {
						MinecraftClient.getInstance().player.getEntityWorld().addParticle(particleEffect, position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, 0, 0, 0);
					}
				});
			}
		});
		
		ClientPlayNetworking.registerGlobalReceiver(PLAY_PARTICLE_PACKET_WITH_RANDOM_OFFSET_AND_VELOCITY_ID, (client, handler, buf, responseSender) -> {
			Vec3d position = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			ParticleType<?> particleType = Registry.PARTICLE_TYPE.get(buf.readIdentifier());
			int amount = buf.readInt();
			Vec3d randomOffset = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			Vec3d randomVelocity = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			if(particleType instanceof ParticleEffect particleEffect) {
				client.execute(() -> {
					// Everything in this lambda is running on the render thread
					
					Random random = client.world.random;
					
					for(int i = 0; i < amount; i++) {
						double randomOffsetX = randomOffset.x - random.nextDouble() * randomOffset.x * 2;
						double randomOffsetY = randomOffset.y - random.nextDouble() * randomOffset.y * 2;
						double randomOffsetZ = randomOffset.z - random.nextDouble() * randomOffset.z * 2;
						double randomVelocityX = randomVelocity.x - random.nextDouble() * randomVelocity.x * 2;
						double randomVelocityY = randomVelocity.y - random.nextDouble() * randomVelocity.y * 2;
						double randomVelocityZ = randomVelocity.z - random.nextDouble() * randomVelocity.z * 2;
					
						MinecraftClient.getInstance().player.getEntityWorld().addParticle(particleEffect,
								position.getX() + randomOffsetX, position.getY() + randomOffsetY, position.getZ() + randomOffsetZ,
								randomVelocityX, randomVelocityY, randomVelocityZ);
					}
				});
			}
		});
		
		ClientPlayNetworking.registerGlobalReceiver(PLAY_PARTICLE_PACKET_WITH_EXACT_OFFSET_AND_VELOCITY_ID, (client, handler, buf, responseSender) -> {
			Vec3d position = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			ParticleType<?> particleType = Registry.PARTICLE_TYPE.get(buf.readIdentifier());
			int amount = buf.readInt();
			Vec3d randomOffset = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			Vec3d velocity = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			if(particleType instanceof ParticleEffect particleEffect) {
				client.execute(() -> {
					// Everything in this lambda is running on the render thread
					for(int i = 0; i < amount; i++) {
						MinecraftClient.getInstance().player.getEntityWorld().addParticle(particleEffect,
								position.getX() + randomOffset.x, position.getY() + randomOffset.y, position.getZ() + randomOffset.z,
								velocity.x, velocity.y, velocity.z);
					}
				});
			}
		});

		ClientPlayNetworking.registerGlobalReceiver(PLAY_LIGHT_CREATED_PACKET_ID, (client, handler, buf, responseSender) -> {
			BlockPos position = buf.readBlockPos();
			client.execute(() -> {
				Random random = client.world.random;
				// Everything in this lambda is running on the render thread
				for(int i = 0; i < 20; i++) {
					MinecraftClient.getInstance().player.getEntityWorld().addParticle(SpectrumParticleTypes.SPARKLESTONE_SPARKLE, position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, 0.3 - random.nextFloat() * 0.6, 0.3 - random.nextFloat() * 0.6, 0.3 - random.nextFloat() * 0.6);
				}
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(PLAY_PEDESTAL_CRAFTING_FINISHED_PARTICLE_PACKET_ID, (client, handler, buf, responseSender) -> {
			BlockPos position = buf.readBlockPos(); // the block pos of the pedestal
			ItemStack itemStack = buf.readItemStack(); // the item stack that was crafted
			client.execute(() -> {
				Random random = client.world.random;
				// Everything in this lambda is running on the render thread
				for(int i = 0; i < 10; i++) {
					MinecraftClient.getInstance().player.getEntityWorld().addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack), position.getX() + 0.5, position.getY() + 1, position.getZ() + 0.5, 0.15 - random.nextFloat() * 0.3, random.nextFloat() * 0.15 + 0.1, 0.15 - random.nextFloat() * 0.3);
				}
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(PLAY_FUSION_CRAFTING_FINISHED_PARTICLE_PACKET_ID, (client, handler, buf, responseSender) -> {
			BlockPos position = buf.readBlockPos();
			DyeColor dyeColor = DyeColor.values()[buf.readInt()];
			client.execute(() -> {
				Vec3d sourcePos = new Vec3d(position.getX() + 0.5, position.getY() + 1, position.getZ() + 0.5);
				
				Vec3f color = ColorRegistry.getColor(dyeColor);
				float velocityModifier = 0.3F;
				for(Vec3d velocity : Support.VECTORS_16) {
					MinecraftClient.getInstance().player.getEntityWorld().addParticle(
							new ParticleSpawnerParticleEffect(new Identifier("spectrum:particle/liquid_crystal_sparkle"), color, 1.0F, 40, false),
							sourcePos.x, sourcePos.y, sourcePos.z,
							velocity.x * velocityModifier, velocity.y * velocityModifier, velocity.z * velocityModifier
					);
				}

			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(PLAY_PEDESTAL_UPGRADED_PARTICLE_PACKET_ID, (client, handler, buf, responseSender) -> {
			BlockPos position = buf.readBlockPos(); // the block pos of the pedestal
			PedestalBlock.PedestalVariant variant = PedestalBlock.PedestalVariant.values()[buf.readInt()]; // the item stack that was crafted
			client.execute(() -> {
				Random random = client.world.random;
				// Everything in this lambda is running on the render thread
				PedestalBlock.spawnUpgradeParticleEffectsForTier(position, variant);
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(CHANGE_PARTICLE_SPAWNER_SETTINGS_CLIENT_PACKET_ID, (client, handler, buf, responseSender) -> {
			BlockPos pos = buf.readBlockPos();
			if(MinecraftClient.getInstance().world.getBlockEntity(pos) instanceof ParticleSpawnerBlockEntity) {
				((ParticleSpawnerBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(pos)).applySettings(buf);
			}
		});

		ClientPlayNetworking.registerGlobalReceiver(INITIATE_ITEM_TRANSFER, (client, handler, buf, responseSender) -> {
			ItemTransfer itemTransfer = ItemTransfer.readFromBuf(buf);
			BlockPos blockPos = itemTransfer.getOrigin();
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				for(int i = 0; i < 10; i++) {
					MinecraftClient.getInstance().player.getEntityWorld().addImportantParticle(new ItemTransferParticleEffect(itemTransfer), true, (double)blockPos.getX() + 0.5D, (double)blockPos.getY() + 0.5D, (double)blockPos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
				}
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(INITIATE_TRANSPHERE, (client, handler, buf, responseSender) -> {
			Transphere transphere = Transphere.readFromBuf(buf);
			BlockPos blockPos = transphere.getOrigin();
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				for(int i = 0; i < 10; i++) {
					MinecraftClient.getInstance().player.getEntityWorld().addImportantParticle(new TransphereParticleEffect(transphere), true, (double)blockPos.getX() + 0.5D, (double)blockPos.getY() + 0.5D, (double)blockPos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
				}
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(INITIATE_EXPERIENCE_TRANSFER, (client, handler, buf, responseSender) -> {
			ExperienceTransfer experienceTransfer = ExperienceTransfer.readFromBuf(buf);
			BlockPos blockPos = experienceTransfer.getOrigin();
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				for(int i = 0; i < 10; i++) {
					MinecraftClient.getInstance().player.getEntityWorld().addImportantParticle(new ExperienceTransferParticleEffect(experienceTransfer), true, (double)blockPos.getX() + 0.5D, (double)blockPos.getY() + 0.5D, (double)blockPos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
				}
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(INITIATE_WIRELESS_REDSTONE_TRANSMISSION, (client, handler, buf, responseSender) -> {
			WirelessRedstoneTransmission wirelessRedstoneTransmission = WirelessRedstoneTransmission.readFromBuf(buf);
			BlockPos blockPos = wirelessRedstoneTransmission.getOrigin();
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				for(int i = 0; i < 10; i++) {
					MinecraftClient.getInstance().player.getEntityWorld().addImportantParticle(new WirelessRedstoneTransmissionParticleEffect(wirelessRedstoneTransmission), true, (double)blockPos.getX() + 0.5D, (double)blockPos.getY() + 0.5D, (double)blockPos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
				}
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(PLAY_ITEM_ENTITY_ABSORBED_PARTICLE_EFFECT_PACKET_ID, (client, handler, buf, responseSender) -> {
			double posX = buf.readDouble();
			double posY = buf.readDouble();
			double posZ = buf.readDouble();

			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				MinecraftClient.getInstance().player.getEntityWorld().addParticle(SpectrumParticleTypes.BLUE_BUBBLE_POP, posX, posY, posZ, 0, 0, 0);
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(PLAY_EXPERIENCE_ORB_ENTITY_ABSORBED_PARTICLE_EFFECT_PACKET_ID, (client, handler, buf, responseSender) -> {
			double posX = buf.readDouble();
			double posY = buf.readDouble();
			double posZ = buf.readDouble();

			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				MinecraftClient.getInstance().player.getEntityWorld().addParticle(SpectrumParticleTypes.GREEN_BUBBLE_POP, posX, posY, posZ, 0, 0, 0);
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(PLAY_BLOCK_BOUND_SOUND_INSTANCE, (client, handler, buf, responseSender) -> {
			Identifier soundEffectIdentifier = buf.readIdentifier();
			Identifier blockIdentifier = buf.readIdentifier();
			BlockPos blockPos = buf.readBlockPos();
			int maxDurationTicks = buf.readInt();

			client.execute(() -> {
				if(soundEffectIdentifier.getPath().equals("stop")) {
					BlockBoundSoundInstance.stopPlayingOnPos(blockPos);
				} else {
					SoundEvent soundEvent = Registry.SOUND_EVENT.get(soundEffectIdentifier);
					Block block = Registry.BLOCK.get(blockIdentifier);
					
					BlockBoundSoundInstance.startSoundInstance(soundEvent, blockPos, block, maxDurationTicks);
				}
			});
		});

	}

	/**
	 * Play particle effect
	 * @param world the world of the pedestal
	 * @param position the pos of the particles
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
	 * @param world the world of the pedestal
	 * @param position the pos of the particles
	 * @param particleEffect The particle effect to play
	 */
	public static void playParticleWithRandomOffsetAndVelocity(ServerWorld world, BlockPos position, ParticleType particleEffect, int amount) {
		playParticleWithRandomOffsetAndVelocity(world, position, Registry.PARTICLE_TYPE.getId(particleEffect), amount);
	}
	
	/**
	 * Play particle effect
	 * @param world the world of the pedestal
	 * @param position the pos of the particles
	 * @param particleEffectIdentifier The particle effect identifier to play
	 */
	public static void playParticleWithRandomOffsetAndVelocity(ServerWorld world, Vec3d position, Identifier particleEffectIdentifier, int amount, Vec3d randomOffset, Vec3d randomVelocity) {
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
	 * @param world the world of the pedestal
	 * @param position the pos of the particles
	 * @param particleEffect The particle effect to play
	 */
	public static void playParticleWithExactOffsetAndVelocity(ServerWorld world, Vec3d position, ParticleEffect particleEffect, int amount, Vec3d randomOffset, Vec3d randomVelocity) {
		playParticleWithExactOffsetAndVelocity(world, position, Registry.PARTICLE_TYPE.getId(particleEffect.getType()), amount, randomOffset, randomVelocity);
	}
	
	/**
	 * Play anvil crafting particle effect
	 * @param world the world of the pedestal
	 * @param position the pos of the particles
	 * @param particleEffectIdentifier The particle effect identifier to play
	 */
	public static void playParticleWithExactOffsetAndVelocity(ServerWorld world, Vec3d position, Identifier particleEffectIdentifier, int amount, Vec3d randomOffset, Vec3d velocity) {
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
	 * Play anvil crafting particle effect
	 * @param world the world of the pedestal
	 * @param position the pos of the particles
	 * @param particleEffect The particle effect to play
	 */
	public static void playParticleWithRandomOffsetAndVelocity(ServerWorld world, Vec3d position, ParticleEffect particleEffect, int amount, Vec3d randomOffset, Vec3d randomVelocity) {
		playParticleWithRandomOffsetAndVelocity(world, position, Registry.PARTICLE_TYPE.getId(particleEffect.getType()), amount, randomOffset, randomVelocity);
	}

	/**
	 *
	 * @param world the world of the pedestal
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
	 *
	 * @param world the world of the pedestal
	 * @param blockPos the blockpos of the pedestal
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
	
	public static void sendPlayFusionCraftingFinishedParticles(World world, BlockPos blockPos, ItemStack itemStack) {
		Optional<DyeColor> optionalItemColor = ColorRegistry.ITEM_COLORS.getMapping(itemStack.getItem());
		
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBlockPos(blockPos);
		
		if(optionalItemColor.isPresent()) {
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

	public static void sendWirelessRedstonePacket(ServerWorld world, WirelessRedstoneTransmission wirelessRedstoneTransmission) {
		BlockPos blockPos = wirelessRedstoneTransmission.getOrigin();

		PacketByteBuf buf = PacketByteBufs.create();
		WirelessRedstoneTransmission.writeToBuf(buf, wirelessRedstoneTransmission);

		for (ServerPlayerEntity player : PlayerLookup.tracking(world, blockPos)) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.INITIATE_WIRELESS_REDSTONE_TRANSMISSION, buf);
		}
	}

	// TODO: merge with sendPlayExperienceOrbEntityAbsorbedParticle
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

	public static void sendPlayBlockBoundSoundInstance(SoundEvent soundEvent, ServerWorld world, BlockPos blockPos, int maxDurationTicks) {
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
	
	public static void sendCancelBlockBoundSoundInstance(ServerWorld world, BlockPos blockPos) {
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
	
	public static void spawnPedestalUpgradeParticles(World world, BlockPos blockPos, PedestalBlock.PedestalVariant newPedestalVariant) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBlockPos(blockPos);
		buf.writeInt(newPedestalVariant.ordinal());
		// Iterate over all players tracking a position in the world and send the packet to each player
		for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, blockPos)) {
			ServerPlayNetworking.send(player, SpectrumS2CPackets.PLAY_PEDESTAL_UPGRADED_PARTICLE_PACKET_ID, buf);
		}
	}

}