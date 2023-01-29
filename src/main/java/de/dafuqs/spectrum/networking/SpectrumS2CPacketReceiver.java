package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.particle_spawner.*;
import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.blocks.present.*;
import de.dafuqs.spectrum.blocks.shooting_star.*;
import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.enums.*;
import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.mixin.client.accessors.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.render.bossbar.*;
import de.dafuqs.spectrum.sound.*;
import de.dafuqs.spectrum.spells.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.gui.hud.*;
import net.minecraft.client.network.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.screen.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.*;
import net.minecraft.world.dimension.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class SpectrumS2CPacketReceiver {
	
	public static void registerS2CReceivers() {
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_PARTICLE_WITH_RANDOM_OFFSET_AND_VELOCITY, (client, handler, buf, responseSender) -> {
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

						client.world.addParticle(particleEffect,
								position.getX() + randomOffsetX, position.getY() + randomOffsetY, position.getZ() + randomOffsetZ,
								randomVelocityX, randomVelocityY, randomVelocityZ);
					}
				});
			}
		});

		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_PARTICLE_WITH_EXACT_VELOCITY, (client, handler, buf, responseSender) -> {
			double posX = buf.readDouble();
			double posY = buf.readDouble();
			double posZ = buf.readDouble();
			ParticleType<?> particleType = Registry.PARTICLE_TYPE.get(buf.readIdentifier());
			int amount = buf.readInt();
			double velocityX = buf.readDouble();
			double velocityY = buf.readDouble();
			double velocityZ = buf.readDouble();
			if (particleType instanceof ParticleEffect particleEffect) {
				client.execute(() -> {
					// Everything in this lambda is running on the render thread
					for (int i = 0; i < amount; i++) {
						client.world.addParticle(particleEffect,
								posX, posY, posZ,
								velocityX, velocityY, velocityZ);
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
					ParticleHelper.playParticleWithPatternAndVelocityClient(client.world, position, particleEffect, pattern, velocity);
				});
			}
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.START_SKY_LERPING, (client, handler, buf, responseSender) -> {
			DimensionType dimensionType = client.world.getDimension();
			long sourceTime = buf.readLong();
			long targetTime = buf.readLong();

			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				SpectrumClient.skyLerper.trigger(dimensionType, sourceTime, client.getTickDelta(), targetTime);
				if (client.world.isSkyVisible(client.player.getBlockPos())) {
					client.world.playSound(client.player.getBlockPos(), SpectrumSoundEvents.CELESTIAL_POCKET_WATCH_FLY_BY, SoundCategory.NEUTRAL, 0.15F, 1.0F, false);
				}
			});

		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_PEDESTAL_CRAFTING_FINISHED_PARTICLE_PACKET_ID, (client, handler, buf, responseSender) -> {
			BlockPos position = buf.readBlockPos(); // the block pos of the pedestal
			ItemStack itemStack = buf.readItemStack(); // the item stack that was crafted
			client.execute(() -> {
				Random random = client.world.random;
				// Everything in this lambda is running on the render thread
				for (int i = 0; i < 10; i++) {
					client.world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack), position.getX() + 0.5, position.getY() + 1, position.getZ() + 0.5, 0.15 - random.nextFloat() * 0.3, random.nextFloat() * 0.15 + 0.1, 0.15 - random.nextFloat() * 0.3);
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
				ShootingStarEntity.playHitParticles(client.world, x, y, z, shootingStarType, 25);
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
					client.world.addParticle(
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
					client.world.addParticle(
							new ParticleSpawnerParticleEffect(new Identifier("spectrum:particle/liquid_crystal_sparkle"), 0.5F, colorVec1, 1.0F, randomLifetime, false, true),
							position.getX() + 0.5, position.getY() + 0.5, position.getZ(),
							0.15 - random.nextFloat() * 0.3, random.nextFloat() * 0.15 + 0.1, 0.15 - random.nextFloat() * 0.3
					);
					
					// color2
					client.world.addParticle(
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
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_PEDESTAL_START_CRAFTING_PARTICLE_PACKET_ID, (client, handler, buf, responseSender) -> {
			BlockPos position = buf.readBlockPos(); // the block pos of the pedestal
			
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				PedestalBlockEntity.spawnCraftingStartParticles(client.world, position);
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.CHANGE_PARTICLE_SPAWNER_SETTINGS_CLIENT_PACKET_ID, (client, handler, buf, responseSender) -> {
			BlockPos pos = buf.readBlockPos();
			if (client.world.getBlockEntity(pos) instanceof ParticleSpawnerBlockEntity) {
				((ParticleSpawnerBlockEntity) client.world.getBlockEntity(pos)).applySettings(buf);
			}
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.ITEM_TRANSMISSION, (client, handler, buf, responseSender) -> {
			SimpleTransmission transmission = SimpleTransmission.readFromBuf(buf);
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				client.world.addImportantParticle(new ItemTransmissionParticleEffect(transmission.getDestination(), transmission.getArrivalInTicks()), true, transmission.getOrigin().getX(), transmission.getOrigin().getY(), transmission.getOrigin().getZ(), 0.0D, 0.0D, 0.0D);
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.COLOR_TRANSMISSION, (client, handler, buf, responseSender) -> {
			ColoredTransmission transmission = ColoredTransmission.readFromBuf(buf);
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				client.world.addImportantParticle(new ColoredTransmissionParticleEffect(transmission.getDestination(), transmission.getArrivalInTicks(), transmission.getDyeColor()), true, transmission.getOrigin().getX(), transmission.getOrigin().getY(), transmission.getOrigin().getZ(), 0.0D, 0.0D, 0.0D);
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.EXPERIENCE_TRANSMISSION, (client, handler, buf, responseSender) -> {
			SimpleTransmission transmission = SimpleTransmission.readFromBuf(buf);
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				client.world.addImportantParticle(new ExperienceTransmissionParticleEffect(transmission.getDestination(), transmission.getArrivalInTicks()), true, transmission.getOrigin().getX(), transmission.getOrigin().getY(), transmission.getOrigin().getZ(), 0.0D, 0.0D, 0.0D);
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.BLOCK_POS_EVENT_TRANSMISSION, (client, handler, buf, responseSender) -> {
			SimpleTransmission transmission = SimpleTransmission.readFromBuf(buf);
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				client.world.addImportantParticle(new BlockPosEventTransmissionParticleEffect(transmission.getDestination(), transmission.getArrivalInTicks()), true, transmission.getOrigin().getX(), transmission.getOrigin().getY(), transmission.getOrigin().getZ(), 0.0D, 0.0D, 0.0D);
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.WIRELESS_REDSTONE_TRANSMISSION, (client, handler, buf, responseSender) -> {
			WirelessRedstoneTransmission transmission = WirelessRedstoneTransmission.readFromBuf(buf);
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				for (int i = 0; i < 10; i++) {
					client.world.addImportantParticle(new WirelessRedstoneTransmissionParticleEffect(transmission.getDestination(), transmission.getArrivalInTicks()), true, transmission.getOrigin().getX(), transmission.getOrigin().getY(), transmission.getOrigin().getZ(), 0.0D, 0.0D, 0.0D);
				}
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
				BossBarHud bossBarHud = client.inGameHud.getBossBarHud();
				Map<UUID, ClientBossBar> bossBars = ((BossBarHudAccessor) bossBarHud).getBossBars();
				if (bossBars.containsKey(bossBarUUID)) {
					ClientBossBar clientBossBar = bossBars.get(bossBarUUID);
					if (clientBossBar instanceof SpectrumClientBossBar spectrumClientBossBar) {
						spectrumClientBossBar.setSerpentMusic(hasSerpentMusic);
					}
				}
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.UPDATE_BLOCK_ENTITY_INK, (client, handler, buf, responseSender) -> {
			BlockPos blockPos = buf.readBlockPos();
			long colorTotal = buf.readLong();
			
			int colorEntries = buf.readInt();
			Map<InkColor, Long> colors = new HashMap<>();
			for (int i = 0; i < colorEntries; i++) {
				colors.put(InkColor.of(buf.readString()), buf.readLong());
			}
			
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				BlockEntity blockEntity = client.world.getBlockEntity(blockPos);
				if (blockEntity instanceof InkStorageBlockEntity inkStorageBlockEntity) {
					inkStorageBlockEntity.getEnergyStorage().setEnergy(colors, colorTotal);
				}
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.INK_COLOR_SELECTED, (client, handler, buf, responseSender) -> {
			ScreenHandler screenHandler = client.player.currentScreenHandler;
			if (screenHandler instanceof InkColorSelectedPacketReceiver inkColorSelectedPacketReceiver) {
				boolean isSelection = buf.readBoolean();
				
				InkColor color;
				if (isSelection) {
					String inkColorString = buf.readString();
					color = InkColor.of(inkColorString);
				} else {
					color = null;
				}
				inkColorSelectedPacketReceiver.onInkColorSelectedPacket(color);
			}
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_INK_EFFECT_PARTICLES, (client, handler, buf, responseSender) -> {
			InkColor inkColor = InkColor.of(buf.readString());
			double posX = buf.readDouble();
			double posY = buf.readDouble();
			double posZ = buf.readDouble();
			float potency = buf.readFloat();
			
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				InkSpellEffects.getEffect(inkColor).playEffects(client.world, new Vec3d(posX, posY, posZ), potency);
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_PRESENT_OPENING_PARTICLES, (client, handler, buf, responseSender) -> {
			BlockPos pos = buf.readBlockPos();
			int colorCount = buf.readInt();
			
			Map<DyeColor, Integer> colors = new HashMap<>();
			for (int i = 0; i < colorCount; i++) {
				DyeColor dyeColor = DyeColor.byId(buf.readByte());
				int amount = buf.readByte();
				colors.put(dyeColor, amount);
			}
			
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				PresentBlock.spawnParticles(client.world, pos, colors);
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_ASCENSION_APPLIED_EFFECTS, (client, handler, buf, responseSender) -> {
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				client.world.playSound(client.player.getBlockPos(), SpectrumSoundEvents.FADING_PLACED, SoundCategory.PLAYERS, 1.0F, 1.0F, false);
				client.getSoundManager().play(new DivinitySoundInstance());
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_DIVINITY_APPLIED_EFFECTS, (client, handler, buf, responseSender) -> {
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				ClientPlayerEntity player = client.player;
				client.particleManager.addEmitter(player, SpectrumParticleTypes.DIVINITY, 30);
				client.gameRenderer.showFloatingItem(SpectrumItems.DIVINATION_HEART.getDefaultStack());
				client.world.playSound(player.getBlockPos(), SpectrumSoundEvents.FAILING_PLACED, SoundCategory.PLAYERS, 1.0F, 1.0F, false);
				
				ParticleHelper.playParticleWithPatternAndVelocityClient(player.world, player.getPos(), SpectrumParticleTypes.WHITE_CRAFTING, ParticlePattern.SIXTEEN, 0.4);
				ParticleHelper.playParticleWithPatternAndVelocityClient(player.world, player.getPos(), SpectrumParticleTypes.RED_CRAFTING, ParticlePattern.SIXTEEN, 0.4);
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.MOONSTONE_BLAST, (client, handler, buf, responseSender) -> {
			ClientPlayerEntity player = client.player;
			
			double x = buf.readDouble();
			double y = buf.readDouble();
			double z = buf.readDouble();
			float power = buf.readFloat();
			double playerVelocityX = buf.readDouble();
			double playerVelocityY = buf.readDouble();
			double playerVelocityZ = buf.readDouble();

			client.execute(() -> {
				MoonstoneStrike.create(client.world, null, null, x, y, z, power);
				player.setVelocity(player.getVelocity().add(playerVelocityX, playerVelocityY, playerVelocityZ));
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.DISPLAY_HUD_MESSAGE, (client, handler, buf, responseSender) -> {
			Text text = buf.readText();
			boolean tinted = buf.readBoolean();

			client.execute(() -> {
				client.inGameHud.setOverlayMessage(text, tinted);
			});
		});

	}
	
}