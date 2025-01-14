package de.dafuqs.spectrum.networking;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.fusion_shrine.*;
import de.dafuqs.spectrum.blocks.particle_spawner.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.blocks.present.*;
import de.dafuqs.spectrum.blocks.shooting_star.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.deeper_down.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.map.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.*;
import de.dafuqs.spectrum.spells.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.render.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.item.map.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.particle.*;
import net.minecraft.registry.*;
import net.minecraft.screen.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.dimension.*;
import org.joml.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class SpectrumS2CPacketReceiver {
	
	@SuppressWarnings("deprecation")
	public static void registerS2CReceivers() {
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_PARTICLE_WITH_RANDOM_OFFSET_AND_VELOCITY, (client, handler, buf, responseSender) -> {
			Vec3d position = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			ParticleType<?> particleType = Registries.PARTICLE_TYPE.get(buf.readIdentifier());
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
			ParticleType<?> particleType = Registries.PARTICLE_TYPE.get(buf.readIdentifier());
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
			ParticleType<?> particleType = Registries.PARTICLE_TYPE.get(buf.readIdentifier());
			VectorPattern pattern = VectorPattern.values()[buf.readInt()];
			double velocity = buf.readDouble();
			if (particleType instanceof ParticleEffect particleEffect) {
				client.execute(() -> {
					// Everything in this lambda is running on the render thread
					ParticleHelper.playParticleWithPatternAndVelocityClient(client.world, position, particleEffect, pattern, velocity);
				});
			}
		});

		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_PARTICLE_AROUND_BLOCK_SIDES, (client, handler, buf, responseSender) -> {
			int quantity = buf.readInt();
			Vec3d position = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			Vec3d velocity = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			ParticleType<?> particleType = Registries.PARTICLE_TYPE.get(buf.readIdentifier());
			var sideCount = buf.readInt();
			var sides = new Direction[sideCount];
			for (int i = 0; i < sideCount; i++) {
				sides[i] = Direction.values()[buf.readInt()];
			}
			
			if (particleType instanceof ParticleEffect particleEffect && client.world != null) {
				client.execute(() -> {
					ParticleHelper.playParticleAroundBlockSides(client.world, particleEffect, position, sides, quantity, velocity);
				});
			}
		});

		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_PARTICLE_AROUND_AREA, (client, handler, buf, responseSender) -> {
			int quantity = buf.readInt();
			double bonusYOffset = buf.readDouble();
			boolean triangular = buf.readBoolean();
			boolean solidSpawns = buf.readBoolean();
			Vec3d scale = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			Vec3d position = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			Vec3d velocity = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			ParticleType<?> particleType = Registries.PARTICLE_TYPE.get(buf.readIdentifier());
			
			if (particleType instanceof ParticleEffect particleEffect && client.world != null) {
				client.execute(() -> {
					ParticleHelper.playTriangulatedParticle(client.world, particleEffect, quantity, triangular, scale, bonusYOffset, solidSpawns, position, velocity);
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
					client.world.playSound(null, client.player.getBlockPos(), SpectrumSoundEvents.CELESTIAL_POCKET_WATCH_FLY_BY, SoundCategory.NEUTRAL, 0.15F, 1.0F);
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
			ShootingStar.Type shootingStarType = ShootingStar.Type.getType(buf.readInt());
			
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				ShootingStarEntity.playHitParticles(client.world, x, y, z, shootingStarType, 25);
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_FUSION_CRAFTING_IN_PROGRESS_PARTICLE_PACKET_ID, (client, handler, buf, responseSender) -> {
			BlockPos position = buf.readBlockPos();
			client.execute(() -> {
				BlockEntity blockEntity = client.world.getBlockEntity(position);
				if (blockEntity instanceof FusionShrineBlockEntity fusionShrineBlockEntity) {
					fusionShrineBlockEntity.spawnCraftingParticles();
				}
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_FUSION_CRAFTING_FINISHED_PARTICLE_PACKET_ID, (client, handler, buf, responseSender) -> {
			BlockPos position = buf.readBlockPos();
			DyeColor dyeColor = DyeColor.values()[buf.readInt()];
			client.execute(() -> {
				Vec3d sourcePos = new Vec3d(position.getX() + 0.5, position.getY() + 1, position.getZ() + 0.5);
				
				Vector3f color = ColorHelper.getRGBVec(dyeColor);
				float velocityModifier = 0.25F;
				for (Vec3d velocity : VectorPattern.SIXTEEN.getVectors()) {
					client.world.addParticle(
							new DynamicParticleEffect(SpectrumParticleTypes.WHITE_CRAFTING, 0.0F, color, 1.5F, 40, false, true),
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
				
				Vector3f colorVec1 = ColorHelper.colorIntToVec(color1);
				Vector3f colorVec2 = ColorHelper.colorIntToVec(color2);
				
				for (int i = 0; i < amount; i++) {
					int randomLifetime = 30 + random.nextInt(20);
					
					// color1
					client.world.addParticle(
							new DynamicParticleEffect(SpectrumParticleTypes.WHITE_CRAFTING, 0.5F, colorVec1, 1.0F, randomLifetime, false, true),
							position.getX() + 0.5, position.getY() + 0.5, position.getZ(),
							0.15 - random.nextFloat() * 0.3, random.nextFloat() * 0.15 + 0.1, 0.15 - random.nextFloat() * 0.3
					);
					
					// color2
					client.world.addParticle(
							new DynamicParticleEffect(SpectrumParticleTypes.WHITE_CRAFTING, 0.5F, colorVec2, 1.0F, randomLifetime, false, true),
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
			ParticleSpawnerConfiguration configuration = ParticleSpawnerConfiguration.fromBuf(buf);
			
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				if (client.world.getBlockEntity(pos) instanceof ParticleSpawnerBlockEntity particleSpawnerBlockEntity) {
					particleSpawnerBlockEntity.applySettings(configuration);
				}
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PASTEL_TRANSMISSION, (client, handler, buf, responseSender) -> {
			UUID networkUUID = buf.readUuid();
			int travelTime = buf.readInt();
			PastelTransmission transmission = PastelTransmission.fromPacket(buf);
			BlockPos spawnPos = transmission.getStartPos();
			int color = ColorHelper.getRandomColor(networkUUID.hashCode());
			
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				client.world.addParticle(new PastelTransmissionParticleEffect(transmission.getNodePositions(), transmission.getVariant().toStack(), travelTime, color), spawnPos.getX() + 0.5, spawnPos.getY() + 0.5, spawnPos.getZ() + 0.5, 0, 0, 0);
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.TYPED_TRANSMISSION, (client, handler, buf, responseSender) -> {
			TypedTransmission transmission = TypedTransmission.readFromBuf(buf);
			
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				switch (transmission.getVariant()) {
					case BLOCK_POS -> client.world.addImportantParticle(new BlockPosEventTransmissionParticleEffect(transmission.getDestination(), transmission.getArrivalInTicks()), true, transmission.getOrigin().getX(), transmission.getOrigin().getY(), transmission.getOrigin().getZ(), 0.0D, 0.0D, 0.0D);
					case ITEM -> client.world.addImportantParticle(new ItemTransmissionParticleEffect(transmission.getDestination(), transmission.getArrivalInTicks()), true, transmission.getOrigin().getX(), transmission.getOrigin().getY(), transmission.getOrigin().getZ(), 0.0D, 0.0D, 0.0D);
					case EXPERIENCE -> client.world.addImportantParticle(new ExperienceTransmissionParticleEffect(transmission.getDestination(), transmission.getArrivalInTicks()), true, transmission.getOrigin().getX(), transmission.getOrigin().getY(), transmission.getOrigin().getZ(), 0.0D, 0.0D, 0.0D);
					case HUMMINGSTONE -> client.world.addImportantParticle(new HummingstoneTransmissionParticleEffect(transmission.getDestination(), transmission.getArrivalInTicks()), true, transmission.getOrigin().getX(), transmission.getOrigin().getY(), transmission.getOrigin().getZ(), 0.0D, 0.0D, 0.0D);
					case REDSTONE -> client.world.addImportantParticle(new WirelessRedstoneTransmissionParticleEffect(transmission.getDestination(), transmission.getArrivalInTicks()), true, transmission.getOrigin().getX(), transmission.getOrigin().getY(), transmission.getOrigin().getZ(), 0.0D, 0.0D, 0.0D);
				}
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.COLOR_TRANSMISSION, (client, handler, buf, responseSender) -> {
			ColoredTransmission transmission = ColoredTransmission.readFromBuf(buf);
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				client.world.addImportantParticle(new ColoredTransmissionParticleEffect(transmission.getDestination(), transmission.getArrivalInTicks(), transmission.getDyeColor()), true, transmission.getOrigin().getX(), transmission.getOrigin().getY(), transmission.getOrigin().getZ(), 0.0D, 0.0D, 0.0D);
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
						SoundEvent soundEvent = Registries.SOUND_EVENT.get(soundEffectIdentifier);
						Block block = Registries.BLOCK.get(blockIdentifier);
						
						CraftingBlockSoundInstance.startSoundInstance(soundEvent, blockPos, block, maxDurationTicks);
					}
				});
			}
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_TAKE_OFF_BELT_SOUND_INSTANCE, (client, handler, buf, responseSender) -> client.execute(TakeOffBeltSoundInstance::startSoundInstance));
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.UPDATE_BLOCK_ENTITY_INK, (client, handler, buf, responseSender) -> {
			BlockPos blockPos = buf.readBlockPos();
			long colorTotal = buf.readLong();
			
			int colorEntries = buf.readInt();
			Map<InkColor, Long> colors = new HashMap<>();
			for (int i = 0; i < colorEntries; i++) {
				Optional<InkColor> optionalInkColor = InkColor.ofId(buf.readIdentifier());
				if (optionalInkColor.isPresent()) {
					colors.put(optionalInkColor.get(), buf.readLong());
				}
			}
			
			client.execute(() -> {
				// Everything in this lambda is running on the render thread
				BlockEntity blockEntity = client.world.getBlockEntity(blockPos);
				if (blockEntity instanceof InkStorageBlockEntity<?> inkStorageBlockEntity) {
					inkStorageBlockEntity.getEnergyStorage().setEnergy(colors, colorTotal);
				}
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.INK_COLOR_SELECTED, (client, handler, buf, responseSender) -> {
			ScreenHandler screenHandler = client.player.currentScreenHandler;
			if (screenHandler instanceof InkColorSelectedPacketReceiver inkColorSelectedPacketReceiver) {
				boolean isSelection = buf.readBoolean();
				
				InkColor color = null;
				if (isSelection) {
					Optional<InkColor> optionalInkColor = InkColor.ofId(buf.readIdentifier());
					if (optionalInkColor.isPresent()) {
						color = optionalInkColor.get();
					}
				}
				inkColorSelectedPacketReceiver.onInkColorSelectedPacket(color);
			}
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
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_ASCENSION_APPLIED_EFFECTS, (client, handler, buf, responseSender) -> client.execute(() -> {
			// Everything in this lambda is running on the render thread
			client.world.playSound(null, client.player.getBlockPos(), SpectrumSoundEvents.FADING_PLACED, SoundCategory.PLAYERS, 1.0F, 1.0F);
			client.getSoundManager().play(new DivinitySoundInstance());
		}));
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PLAY_DIVINITY_APPLIED_EFFECTS, (client, handler, buf, responseSender) -> client.execute(() -> {
			// Everything in this lambda is running on the render thread
			PlayerEntity player = client.player;
			client.particleManager.addEmitter(player, SpectrumParticleTypes.DIVINITY, 30);
			client.gameRenderer.showFloatingItem(SpectrumItems.DIVINATION_HEART.getDefaultStack());
			client.world.playSound(null, player.getBlockPos(), SpectrumSoundEvents.FAILING_PLACED, SoundCategory.PLAYERS, 1.0F, 1.0F);
			
			ParticleHelper.playParticleWithPatternAndVelocityClient(player.getWorld(), player.getPos(), SpectrumParticleTypes.WHITE_CRAFTING, VectorPattern.SIXTEEN, 0.4);
			ParticleHelper.playParticleWithPatternAndVelocityClient(player.getWorld(), player.getPos(), SpectrumParticleTypes.RED_CRAFTING, VectorPattern.SIXTEEN, 0.4);
		}));
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.MOONSTONE_BLAST, (client, handler, buf, responseSender) -> {
			PlayerEntity player = client.player;
			
			double x = buf.readDouble();
			double y = buf.readDouble();
			double z = buf.readDouble();
			float power = buf.readFloat();
			float knockback = buf.readFloat();
			double playerVelocityX = buf.readDouble();
			double playerVelocityY = buf.readDouble();
			double playerVelocityZ = buf.readDouble();
			
			client.execute(() -> {
				MoonstoneStrike.create(client.world, null, null, x, y, z, power, knockback);
				player.setVelocity(player.getVelocity().add(playerVelocityX, playerVelocityY, playerVelocityZ));
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.SYNC_ARTISANS_ATLAS, (client, handler, buf, responseSender) -> {
			String targetIdStr = buf.readString();
			Identifier targetId = targetIdStr.length() == 0 ? null : new Identifier(targetIdStr);

			MapUpdateS2CPacket packet = new MapUpdateS2CPacket(buf);

			client.execute(() -> {
				NetworkThreadUtils.forceMainThread(packet, handler, client);
				MapRenderer mapRenderer = client.gameRenderer.getMapRenderer();
				int i = packet.getId();
				String string = FilledMapItem.getMapName(i);

				if (client.world != null) {
					MapState mapState = client.world.getMapState(string);

					if (mapState == null) {
						mapState = new ArtisansAtlasState(packet.getScale(), packet.isLocked(), client.world.getRegistryKey());
						client.world.putClientsideMapState(string, mapState);
					}

					if (mapState instanceof ArtisansAtlasState artisansAtlasState) {
						artisansAtlasState.setTargetId(targetId);
						packet.apply(mapState);
						mapRenderer.updateTexture(i, mapState);
					}
				}
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.SYNC_MENTAL_PRESENCE, ((client, handler, buf, responseSender) -> {
			double value = buf.readDouble();

			client.execute(() -> {
				if (client.player != null) {
					MiscPlayerDataComponent.get(client.player).setLastSyncedSleepPotency(value);
					DarknessEffects.markForEffectUpdate();
				}
			});
		}));

		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.COMPACTING_CHEST_STATUS_UPDATE, (((client, handler, buf, responseSender) -> {
			var pos = buf.readBlockPos();
			var hasToCraft = buf.readBoolean();

			client.execute(() -> {
				var entity = client.world.getBlockEntity(pos, SpectrumBlockEntities.COMPACTING_CHEST);

				if (entity.isEmpty())
					return;

				entity.get().shouldCraft(hasToCraft);
			});
		})));

		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.RESTOCKING_CHEST_STATUS_UPDATE, (((client, handler, buf, responseSender) -> {
			var pos = buf.readBlockPos();
			var isFull = buf.readBoolean();
			var hasValidRecipes = buf.readBoolean();
			var outputCount = buf.readInt();
			final var cachedOutputs = new ArrayList<ItemStack>(4);
			for (int i = 0; i < outputCount; i++) {
				cachedOutputs.add(buf.readItemStack());
			}

			client.execute(() -> {
				var entity = client.world.getBlockEntity(pos, SpectrumBlockEntities.RESTOCKING_CHEST);

				if (entity.isEmpty())
					return;

				entity.get().updateState(isFull, hasValidRecipes, cachedOutputs);
			});
		})));

		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.BLACK_HOLE_CHEST_STATUS_UPDATE, (((client, handler, buf, responseSender) -> {
			var pos = buf.readBlockPos();
			var isFull = buf.readBoolean();
			var canStoreXP = buf.readBoolean();
			var xp = buf.readLong();
			var max = buf.readLong();

			client.execute(() -> {
				var entity = client.world.getBlockEntity(pos, SpectrumBlockEntities.BLACK_HOLE_CHEST);

				entity.ifPresent(chest -> {
					chest.setFull(isFull);
					chest.setHasXPStorage(canStoreXP);
					chest.setXPData(xp, max);
				});
			});
		})));

		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PASTEL_NODE_STATUS_UPDATE, ((((client, handler, buf, responseSender) -> {
			var trigger = buf.readBoolean();
			var nodeCount = buf.readInt();
			var positions = new ArrayList<BlockPos>(nodeCount);
			var times = new ArrayList<Integer>(nodeCount);

			for (int n = 0; n < nodeCount; n++) {
				positions.add(buf.readBlockPos());
				times.add(buf.readInt());
			}

			client.execute(() -> {
				for (int index = 0; index < positions.size(); index++) {
					var entity = client.world.getBlockEntity(positions.get(index));

					if (!(entity instanceof PastelNodeBlockEntity node))
						continue;

					node.setSpinTicks(times.get(index));

					if (trigger && node.isTriggerTransfer())
						node.markTriggered();
				}
			});
		}))));
		
		ClientPlayNetworking.registerGlobalReceiver(SpectrumS2CPackets.PASTEL_NETWORK_EDGE_SYNC, (client, handler, buf, responseSender) -> {
			var uuid = buf.readUuid();
			NbtCompound nbt = buf.readNbt();
			
			client.execute(() -> {
				Optional<? extends PastelNetwork> network = Pastel.getInstance(true).getNetwork(uuid);
				if (network.isPresent()) {
					network.get().setGraph(PastelNetwork.graphFromNbt(nbt));
				} else {
					PastelNetwork pn = Pastel.getClientInstance().createNetwork(client.world, uuid);
					pn.setGraph(PastelNetwork.graphFromNbt(nbt));
				}
			});
		});
    }
	
}