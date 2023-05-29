package de.dafuqs.spectrum.recipe.fusion_shrine;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.particle.*;
import net.minecraft.server.*;
import net.minecraft.server.command.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.explosion.*;
import net.minecraft.world.level.*;

import java.util.*;

/**
 * Effects that are played when crafting with the fusion shrine
 */
public interface FusionShrineRecipeWorldEffect {
	
	Map<String, FusionShrineRecipeWorldEffect> TYPES = new HashMap<>();
	
	FusionShrineRecipeWorldEffect NOTHING = register("nothing", new SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			
		}
	});
	FusionShrineRecipeWorldEffect WEATHER_CLEAR = register("weather_clear", new SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			ServerWorldProperties serverWorldProperties = ((ServerWorldProperties) world.getLevelProperties());
			serverWorldProperties.setRainTime(0);
			serverWorldProperties.setRaining(false);
			serverWorldProperties.setThunderTime(0);
			serverWorldProperties.setThundering(false);
		}
	});
	FusionShrineRecipeWorldEffect WEATHER_RAIN = register("weather_rain", new SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			ServerWorldProperties serverWorldProperties = ((ServerWorldProperties) world.getLevelProperties());
			serverWorldProperties.setRainTime(MathHelper.nextBetween(world.random, 12000, 18000));
			serverWorldProperties.setRaining(true);
			serverWorldProperties.setThunderTime(0);
			serverWorldProperties.setThundering(false);
			
			world.playSound(null, pos.up(), SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.8F, 0.9F + world.random.nextFloat() * 0.2F);
		}
	});
	FusionShrineRecipeWorldEffect WEATHER_THUNDER = register("weather_thunder", new SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			ServerWorldProperties serverWorldProperties = ((ServerWorldProperties) world.getLevelProperties());
			serverWorldProperties.setRainTime(MathHelper.nextBetween(world.random, 12000, 24000));
			serverWorldProperties.setRaining(true);
			serverWorldProperties.setThunderTime(MathHelper.nextBetween(world.random, 3600, 15600));
			serverWorldProperties.setThundering(false);
			
			world.playSound(null, pos.up(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 0.8F, 0.9F + world.random.nextFloat() * 0.2F);
		}
	});
	FusionShrineRecipeWorldEffect WEATHER_RAIN_SHORT = register("weather_rain_short", new SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			ServerWorldProperties serverWorldProperties = ((ServerWorldProperties) world.getLevelProperties());
			serverWorldProperties.setRainTime(MathHelper.nextBetween(world.random, 4000, 6000));
			serverWorldProperties.setRaining(true);
			serverWorldProperties.setThunderTime(0);
			serverWorldProperties.setThundering(false);
			
			world.playSound(null, pos.up(), SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.8F, 0.9F + world.random.nextFloat() * 0.2F);
		}
	});
	FusionShrineRecipeWorldEffect WEATHER_THUNDER_SHORT = register("weather_thunder_short", new SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			ServerWorldProperties serverWorldProperties = ((ServerWorldProperties) world.getLevelProperties());
			serverWorldProperties.setRainTime(MathHelper.nextBetween(world.random, 4000, 6000));
			serverWorldProperties.setRaining(true);
			serverWorldProperties.setThunderTime(MathHelper.nextBetween(world.random, 3000, 4000));
			serverWorldProperties.setThundering(false);
			
			world.playSound(null, pos.up(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 0.8F, 0.9F + world.random.nextFloat() * 0.2F);
		}
	});
	FusionShrineRecipeWorldEffect LIGHTNING_ON_SHRINE = register("lightning_on_shrine", new SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
			if (lightningEntity != null) {
				lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(pos));
				lightningEntity.setCosmetic(true);
				world.spawnEntity(lightningEntity);
			}
		}
	});
	FusionShrineRecipeWorldEffect LIGHTNING_AROUND_SHRINE = register("lightning_around_shrine", new EveryTickRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			if (world.getRandom().nextFloat() < 0.05F) {
				int randomX = pos.getX() + 12 - world.getRandom().nextInt(24);
				int randomZ = pos.getZ() + 12 - world.getRandom().nextInt(24);
				
				BlockPos randomTopPos = new BlockPos(randomX, world.getTopY(Heightmap.Type.WORLD_SURFACE, randomX, randomZ), randomZ);
				LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
				if (lightningEntity != null) {
					lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(randomTopPos));
					lightningEntity.setCosmetic(false);
					world.spawnEntity(lightningEntity);
				}
			}
		}
	});
	FusionShrineRecipeWorldEffect VISUAL_EXPLOSIONS_ON_SHRINE = register("visual_explosions_on_shrine", new EveryTickRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			if (world.getRandom().nextFloat() < 0.1) {
				world.playSound(null, pos.up(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.5F, 0.8F + world.random.nextFloat() * 0.4F);
				SpectrumS2CPacketSender.playParticles(world, pos.up(), ParticleTypes.EXPLOSION, 1);
			}
		}
	});
	FusionShrineRecipeWorldEffect SINGLE_VISUAL_EXPLOSION_ON_SHRINE = register("single_visual_explosion_on_shrine", new SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			world.playSound(null, pos.up(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.4F);
			SpectrumS2CPacketSender.playParticles(world, pos, ParticleTypes.EXPLOSION, 1);
		}
	});
	FusionShrineRecipeWorldEffect MAYBE_PLACE_MIDNIGHT_SOLUTION = register("maybe_place_midnight_solution", new EveryTickRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			if (world.getRandom().nextFloat() < 0.05F) {
				Optional<BlockPos> targetPos = Support.getNexReplaceableBlockPosUpDown(world, pos.add(5 - world.getRandom().nextInt(10), 1, 5 - world.getRandom().nextInt(10)), 5);
				if (targetPos.isPresent()) {
					world.setBlockState(targetPos.get(), SpectrumBlocks.MIDNIGHT_SOLUTION.getDefaultState());
					MidnightSolutionFluidBlock.playExtinguishSound(world, targetPos.get());
				}
			}
		}
	});
	FusionShrineRecipeWorldEffect PLACE_MIDNIGHT_SOLUTION = register("place_midnight_solution", new EveryTickRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			Optional<BlockPos> targetPos = Support.getNexReplaceableBlockPosUpDown(world, pos.add(5 - world.getRandom().nextInt(10), 1, 5 - world.getRandom().nextInt(10)), 5);
			if (targetPos.isPresent()) {
				world.setBlockState(targetPos.get(), SpectrumBlocks.MIDNIGHT_SOLUTION.getDefaultState());
				MidnightSolutionFluidBlock.playExtinguishSound(world, targetPos.get());
			}
		}
	});
	FusionShrineRecipeWorldEffect EXPLOSIONS_AROUND_SHRINE = register("explosions_around_shrine", new EveryTickRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			if (world.getRandom().nextFloat() < 0.1) {
				float randomX = pos.getX() + 0.5F + 10 - world.getRandom().nextInt(20);
				float randomY = pos.getY() + 0.5F + 1 - world.getRandom().nextInt(3);
				float randomZ = pos.getZ() + 0.5F + 10 - world.getRandom().nextInt(20);
				world.createExplosion(null, randomX, randomY, randomZ, 4, Explosion.DestructionType.NONE);
			}
		}
	});
	FusionShrineRecipeWorldEffect EXPLOSIONS_AND_LIGHTNING_AROUND_SHRINE = register("explosions_and_lightning_around_shrine", new EveryTickRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			if (world.getRandom().nextFloat() < 0.1) {
				float randomX = pos.getX() + 0.5F + 10 - world.getRandom().nextInt(20);
				float randomY = pos.getY() + 0.5F + 1 - world.getRandom().nextInt(3);
				float randomZ = pos.getZ() + 0.5F + 10 - world.getRandom().nextInt(20);
				world.createExplosion(null, randomX, randomY, randomZ, 4, Explosion.DestructionType.NONE);
			}
			if (world.getRandom().nextFloat() < 0.05F) {
				int randomX = pos.getX() + 12 - world.getRandom().nextInt(24);
				int randomZ = pos.getZ() + 12 - world.getRandom().nextInt(24);
				
				BlockPos randomTopPos = new BlockPos(randomX, world.getTopY(Heightmap.Type.WORLD_SURFACE, randomX, randomZ), randomZ);
				LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
				if (lightningEntity != null) {
					lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(randomTopPos));
					lightningEntity.setCosmetic(false);
					world.spawnEntity(lightningEntity);
				}
			}
		}
	});
	FusionShrineRecipeWorldEffect PLAY_GLASS_BREAKING_SOUND = register("play_glass_breaking_sound", new SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			world.playSound(null, pos.up(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
	});
	FusionShrineRecipeWorldEffect RIDICULOUSLY_SQUEAKY_FART = register("ridiculously_squeaky_fart", new SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			world.playSound(null, pos.up(), SpectrumSoundEvents.SQUEAKER, SoundCategory.BLOCKS, 1.4F, 1.2F + world.random.nextFloat() * 0.4F);
		}
	});
	
	static FusionShrineRecipeWorldEffect register(String id, FusionShrineRecipeWorldEffect effect) {
		TYPES.put(id, effect);
		return effect;
	}
	
	static FusionShrineRecipeWorldEffect fromString(String string) {
		if (string == null || string.isBlank()) {
			return NOTHING;
		}
		if (string.startsWith("/")) {
			return new CommandRecipeWorldEffect(string);
		}
		
		FusionShrineRecipeWorldEffect effect = TYPES.get(string);
		if (effect == null) {
			SpectrumCommon.logError("Unknown fusion shrine world effect '" + string + "'. Will be ignored.");
			return NOTHING;
		}
		return effect;
	}
	
	/**
	 * True for all effects that should just play once.
	 * Otherwise, it will be triggered each tick of the recipe
	 */
	boolean isOneTimeEffect();
	
	void trigger(ServerWorld world, BlockPos pos);
	
	abstract class EveryTickRecipeWorldEffect implements FusionShrineRecipeWorldEffect {
		
		public EveryTickRecipeWorldEffect() {
		}
		
		@Override
		public boolean isOneTimeEffect() {
			return false;
		}
		
	}
	
	abstract class SingleTimeRecipeWorldEffect implements FusionShrineRecipeWorldEffect {
		
		public SingleTimeRecipeWorldEffect() {
		}
		
		@Override
		public boolean isOneTimeEffect() {
			return true;
		}
		
	}
	
	class CommandRecipeWorldEffect implements FusionShrineRecipeWorldEffect, CommandOutput {
		
		protected final String command;
		
		public CommandRecipeWorldEffect(String command) {
			this.command = command;
		}
		
		public static CommandRecipeWorldEffect fromJson(JsonObject json) {
			return new CommandRecipeWorldEffect(json.getAsString());
		}
		
		@Override
		public boolean isOneTimeEffect() {
			return false;
		}
		
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			MinecraftServer minecraftServer = world.getServer();
			ServerCommandSource serverCommandSource = new ServerCommandSource(this, Vec3d.ofCenter(pos), Vec2f.ZERO, world, 2, "FusionShrine", world.getBlockState(pos).getBlock().getName(), minecraftServer, null);
			minecraftServer.getCommandManager().executeWithPrefix(serverCommandSource, command);
		}
		
		@Override
		public void sendMessage(Text message) {
			
		}
		
		@Override
		public boolean shouldReceiveFeedback() {
			return false;
		}
		
		@Override
		public boolean shouldTrackOutput() {
			return false;
		}
		
		@Override
		public boolean shouldBroadcastConsoleToOps() {
			return false;
		}
	}
	
}
