package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.SpectrumClient;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.particle_spawner.ParticleSpawnerBlockEntity;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlock;
import de.dafuqs.spectrum.blocks.shooting_star.ShootingStarBlock;
import de.dafuqs.spectrum.entity.entity.ShootingStarEntity;
import de.dafuqs.spectrum.enums.PedestalRecipeTier;
import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.mixin.client.accessors.BossBarHudAccessor;
import de.dafuqs.spectrum.particle.ParticlePattern;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import de.dafuqs.spectrum.render.bossbar.SpectrumClientBossBar;
import de.dafuqs.spectrum.sound.CraftingBlockSoundInstance;
import de.dafuqs.spectrum.sound.TakeOffBeltSoundInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class SpectrumS2CPacketReceiver {
	
	@Environment(EnvType.CLIENT)
	public static void registerS2CReceivers() {
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_PARTICLE_AT_EXACT_BLOCK_POSITION_WITHOUT_VELOCITY_ID, (client, handler, buf, responseSender) -> {
			BlockPos position = buf.readBlockPos();
			ParticleType<?> particleType = Registry.PARTICLE_TYPE.get(buf.readIdentifier());
			int amount = buf.readInt();
			if (particleType instanceof ParticleEffect particleEffect) {
				client.execute(() -> {
					// Everything in this lambda is running on the render thread
					for (int i = 0; i < amount; i++) {
						client.getInstance().player.world.addParticle(particleEffect, position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, 0, 0, 0);
					}
				});
			}
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_PARTICLE_PACKET_WITH_RANDOM_OFFSET_AND_VELOCITY_ID, (client, handler, buf, responseSender) -> {
			Vec3d position = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			ParticleType<?> particleType = Registry.PARTICLE_TYPE.get(buf.readIdentifier());
			int amount = buf.readInt();
			Vec3d randomOffset = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			Vec3d randomVelocity = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			if (particleType instanceof ParticleEffect particleEffect) {
				client.execute(() -> {
					// Everything in this lambda is running on the render thread
					
					Random random = client.world.random;
					
					for (int i = 0; i < amount; i++) {
						double randomOffsetX = randomOffset.x - random.nextDouble() * randomOffset.x * 2;
						double randomOffsetY = randomOffset.y - random.nextDouble() * randomOffset.y * 2;
						double randomOffsetZ = randomOffset.z - random.nextDouble() * randomOffset.z * 2;
						double randomVelocityX = randomVelocity.x - random.nextDouble() * randomVelocity.x * 2;
						double randomVelocityY = randomVelocity.y - random.nextDouble() * randomVelocity.y * 2;
						double randomVelocityZ = randomVelocity.z - random.nextDouble() * randomVelocity.z * 2;
						
						client.getInstance().player.world.addParticle(particleEffect,
								position.getX() + randomOffsetX, position.getY() + randomOffsetY, position.getZ() + randomOffsetZ,
								randomVelocityX, randomVelocityY, randomVelocityZ);
					}
				});
			}
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_PARTICLE_PACKET_WITH_EXACT_OFFSET_AND_VELOCITY_ID, (client, handler, buf, responseSender) -> {
			Vec3d position = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			ParticleType<?> particleType = Registry.PARTICLE_TYPE.get(buf.readIdentifier());
			int amount = buf.readInt();
			Vec3d randomOffset = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			Vec3d velocity = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			if (particleType instanceof ParticleEffect particleEffect) {
				client.execute(() -> {
					// Everything in this lambda is running on the render thread
					for (int i = 0; i < amount; i++) {
						client.getInstance().player.world.addParticle(particleEffect,
								position.getX() + randomOffset.x, position.getY() + randomOffset.y, position.getZ() + randomOffset.z,
								velocity.x, velocity.y, velocity.z);
					}
				});
			}
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_PARTICLE_PACKET_WITH_PATTERN_AND_VELOCITY_ID, (client, handler, buf, responseSender) -> {
			Vec3d position = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			ParticleType<?> particleType = Registry.PARTICLE_TYPE.get(buf.readIdentifier());
			ParticlePattern pattern = ParticlePattern.values()[buf.readInt()];
			double velocity = buf.readDouble();
			if (particleType instanceof ParticleEffect particleEffect) {
				client.execute(() -> {
					// Everything in this lambda is running on the render thread
					playParticleWithPatternAndVelocityClient(client.world, position, particleEffect, pattern, velocity);
				});
			}
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_LIGHT_CREATED_PACKET_ID, (client, handler, buf, responseSender) -> {
			BlockPos position = buf.readBlockPos();
			client.execute(() -> {
				Random random = client.world.random;
				// Everything in this lambda is running on the render thread
				for (int i = 0; i < 20; i++) {
					client.getInstance().player.world.addParticle(SpectrumParticleTypes.SPARKLESTONE_SPARKLE, position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, 0.3 - random.nextFloat() * 0.6, 0.3 - random.nextFloat() * 0.6, 0.3 - random.nextFloat() * 0.6);
				}
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.START_SKY_LERPING, (client, handler, buf, responseSender) -> {
			DimensionType dimensionType = client.world.getDimension();
			long sourceTime = buf.readLong();
			long targetTime = buf.readLong();
			SpectrumClient.skyLerper.trigger(dimensionType, sourceTime, client.getTickDelta(), targetTime);
			
			if (client.world.isSkyVisible(client.player.getBlockPos())) {
				client.player.playSound(SpectrumSoundEvents.CELESTIAL_POCKET_WATCH_FLY_BY, SoundCategory.NEUTRAL, 0.15F, 1.0F);
			}
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_PEDESTAL_CRAFTING_FINISHED_PARTICLE_PACKET_ID, (client, handler, buf, responseSender) -> {
			BlockPos position = buf.readBlockPos(); // the block pos of the pedestal
			ItemStack itemStack = buf.readItemStack(); // the item stack that was crafted
			client.execute(() -> {
				Random random = client.world.random;
				// Everything in this lambda is running on the render thread
				for (int i = 0; i < 10; i++) {
					client.getInstance().player.world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack), position.getX() + 0.5, position.getY() + 1, position.getZ() + 0.5, 0.15 - random.nextFloat() * 0.3, random.nextFloat() * 0.15 + 0.1, 0.15 - random.nextFloat() * 0.3);
				}
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_SHOOTING_STAR_PARTICLES, (client, handler, buf, responseSender) -> {
			double x = buf.readDouble();
			double y = buf.readDouble();
			double z = buf.readDouble();
			ShootingStarBlock.Type shootingStarType = ShootingStarBlock.Type.getType(buf.readInt());
			
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				ShootingStarEntity.playHitParticles(client.getInstance().player.world, x, y, z, shootingStarType, 25);
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_FUSION_CRAFTING_FINISHED_PARTICLE_PACKET_ID, (client, handler, buf, responseSender) -> {
			BlockPos position = buf.readBlockPos();
			DyeColor dyeColor = DyeColor.values()[buf.readInt()];
			client.execute(() -> {
				Vec3d sourcePos = new Vec3d(position.getX() + 0.5, position.getY() + 1, position.getZ() + 0.5);
				
				Vec3f color = ColorHelper.getVec(dyeColor);
				float velocityModifier = 0.25F;
				for (Vec3d velocity : Support.VECTORS_16) {
					client.getInstance().player.world.addParticle(
							new ParticleSpawnerParticleEffect(new Identifier("spectrum:particle/liquid_crystal_sparkle"), 0.0F, color, 1.5F, 40, false, true),
							sourcePos.x, sourcePos.y, sourcePos.z,
							velocity.x * velocityModifier, 0.0F, velocity.z * velocityModifier
					);
				}
				
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_MEMORY_MANIFESTING_PARTICLES, (client, handler, buf, responseSender) -> {
			BlockPos position = buf.readBlockPos();
			int color1 = buf.readInt();
			int color2 = buf.readInt();
			int amount = buf.readInt();
			
			client.execute(() -> {
				Random random = client.world.random;
				
				Vec3f colorVec1 = ColorHelper.colorIntToVec(color1);
				Vec3f colorVec2 = ColorHelper.colorIntToVec(color2);
				
				for (int i = 0; i < amount; i++) {
					int randomLifetime = 30 + random.nextInt(20);
					
					// color1
					client.getInstance().player.world.addParticle(
							new ParticleSpawnerParticleEffect(new Identifier("spectrum:particle/liquid_crystal_sparkle"), 0.5F, colorVec1, 1.0F, randomLifetime, false, true),
							position.getX() + 0.5, position.getY() + 0.5, position.getZ(),
							0.15 - random.nextFloat() * 0.3, random.nextFloat() * 0.15 + 0.1, 0.15 - random.nextFloat() * 0.3
					);
					
					// color2
					client.getInstance().player.world.addParticle(
							new ParticleSpawnerParticleEffect(new Identifier("spectrum:particle/liquid_crystal_sparkle"), 0.5F, colorVec2, 1.0F, randomLifetime, false, true),
							position.getX() + 0.5, position.getY(), position.getZ() + 0.5,
							0.15 - random.nextFloat() * 0.3, random.nextFloat() * 0.15 + 0.1, 0.15 - random.nextFloat() * 0.3
					);
				}
				
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_PEDESTAL_UPGRADED_PARTICLE_PACKET_ID, (client, handler, buf, responseSender) -> {
			BlockPos position = buf.readBlockPos(); // the block pos of the pedestal
			PedestalRecipeTier tier = PedestalRecipeTier.values()[buf.readInt()]; // the item stack that was crafted
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				PedestalBlock.spawnUpgradeParticleEffectsForTier(position, tier);
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.CHANGE_PARTICLE_SPAWNER_SETTINGS_CLIENT_PACKET_ID, (client, handler, buf, responseSender) -> {
			BlockPos pos = buf.readBlockPos();
			if (client.getInstance().world.getBlockEntity(pos) instanceof ParticleSpawnerBlockEntity) {
				((ParticleSpawnerBlockEntity) client.getInstance().world.getBlockEntity(pos)).applySettings(buf);
			}
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.INITIATE_ITEM_TRANSFER, (client, handler, buf, responseSender) -> {
			ItemTransfer itemTransfer = ItemTransfer.readFromBuf(buf);
			BlockPos blockPos = itemTransfer.getOrigin();
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				client.getInstance().player.world.addImportantParticle(new ItemTransferParticleEffect(itemTransfer), true, (double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.INITIATE_TRANSPHERE, (client, handler, buf, responseSender) -> {
			Transphere transphere = Transphere.readFromBuf(buf);
			BlockPos blockPos = transphere.getOrigin();
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				client.getInstance().player.world.addImportantParticle(new TransphereParticleEffect(transphere), true, (double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.INITIATE_EXPERIENCE_TRANSFER, (client, handler, buf, responseSender) -> {
			ExperienceTransfer experienceTransfer = ExperienceTransfer.readFromBuf(buf);
			BlockPos blockPos = experienceTransfer.getOrigin();
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				client.getInstance().player.world.addImportantParticle(new ExperienceTransferParticleEffect(experienceTransfer), true, (double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.INITIATE_BLOCK_POS_EVENT_TRANSFER, (client, handler, buf, responseSender) -> {
			BlockPosEventTransfer blockPosEventTransfer = BlockPosEventTransfer.readFromBuf(buf);
			BlockPos blockPos = blockPosEventTransfer.getOrigin();
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				client.getInstance().player.world.addImportantParticle(new BlockPosEventTransferParticleEffect(blockPosEventTransfer), true, (double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.INITIATE_WIRELESS_REDSTONE_TRANSMISSION, (client, handler, buf, responseSender) -> {
			WirelessRedstoneTransmission wirelessRedstoneTransmission = WirelessRedstoneTransmission.readFromBuf(buf);
			BlockPos blockPos = wirelessRedstoneTransmission.getOrigin();
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				for (int i = 0; i < 10; i++) {
					client.getInstance().player.world.addImportantParticle(new WirelessRedstoneTransmissionParticleEffect(wirelessRedstoneTransmission), true, (double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
				}
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_ITEM_ENTITY_ABSORBED_PARTICLE_EFFECT_PACKET_ID, (client, handler, buf, responseSender) -> {
			double posX = buf.readDouble();
			double posY = buf.readDouble();
			double posZ = buf.readDouble();
			
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				client.getInstance().player.world.addParticle(SpectrumParticleTypes.BLUE_BUBBLE_POP, posX, posY, posZ, 0, 0, 0);
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_EXPERIENCE_ORB_ENTITY_ABSORBED_PARTICLE_EFFECT_PACKET_ID, (client, handler, buf, responseSender) -> {
			double posX = buf.readDouble();
			double posY = buf.readDouble();
			double posZ = buf.readDouble();
			
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				client.getInstance().player.world.addParticle(SpectrumParticleTypes.GREEN_BUBBLE_POP, posX, posY, posZ, 0, 0, 0);
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_BLOCK_BOUND_SOUND_INSTANCE, (client, handler, buf, responseSender) -> {
			if (SpectrumCommon.CONFIG.BlockSoundVolume > 0) {
				Identifier soundEffectIdentifier = buf.readIdentifier();
				Identifier blockIdentifier = buf.readIdentifier();
				BlockPos blockPos = buf.readBlockPos();
				int maxDurationTicks = buf.readInt();
				
				client.execute(() -> {
					if (soundEffectIdentifier.getPath().equals("stop")) {
						CraftingBlockSoundInstance.stopPlayingOnPos(blockPos);
					} else {
						SoundEvent soundEvent = Registry.SOUND_EVENT.get(soundEffectIdentifier);
						Block block = Registry.BLOCK.get(blockIdentifier);
						
						CraftingBlockSoundInstance.startSoundInstance(soundEvent, blockPos, block, maxDurationTicks);
					}
				});
			}
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_TAKE_OFF_BELT_SOUND_INSTANCE, (client, handler, buf, responseSender) -> {
			client.execute(TakeOffBeltSoundInstance::startSoundInstance);
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.UPDATE_BOSS_BAR, (client, handler, buf, responseSender) -> {
			UUID bossBarUUID = buf.readUuid();
			boolean hasSerpentMusic = buf.readBoolean();
			
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				BossBarHud bossBarHud = client.getInstance().inGameHud.getBossBarHud();
				Map<UUID, ClientBossBar> bossBars = ((BossBarHudAccessor) bossBarHud).getBossBars();
				if (bossBars.containsKey(bossBarUUID)) {
					ClientBossBar clientBossBar = bossBars.get(bossBarUUID);
					if (clientBossBar instanceof SpectrumClientBossBar spectrumClientBossBar) {
						spectrumClientBossBar.setSerpentMusic(hasSerpentMusic);
					}
				}
			});
		});
	}
	
	public static void playParticleWithPatternAndVelocityClient(World world, Vec3d position, ParticleEffect particleEffect, @NotNull ParticlePattern pattern, double velocity) {
		for (Vec3d vec3d : pattern.getVectors()) {
			world.addParticle(particleEffect, position.getX(), position.getY(), position.getZ(), vec3d.x * velocity, vec3d.y * velocity, vec3d.z * velocity);
		}
	}
	
}