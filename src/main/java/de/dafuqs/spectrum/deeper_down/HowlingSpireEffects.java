package de.dafuqs.spectrum.deeper_down;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.client.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.biome.*;

@Environment(EnvType.CLIENT)
public class HowlingSpireEffects {
	private static boolean initialized = false;
	
	private static final long ASH_UPDATE_INTERVAL = 1600;
	private static final double BASE_ASH_VELOCITY = 0.25;
	private static double targetAshVelocity = 0.215, lastAshVelocity = 0.215, ashScaleA = 20000, ashScaleB = 2200, ashScaleC = 200;
	private static int ashSwitchTicks = 50, ashSpawns, spireTicks;
	private static Direction.Axis ashAxis = Direction.Axis.X;
	private static Minecraft client = Minecraft.getInstance();
	
	public static void clientTick(ClientLevel world, Entity cameraEntity, Holder<Biome> biome) {
		if (client.isPaused()) return;
		BiomeAttenuatingSoundInstance.update(biome);
		
		boolean inHowlingSpires = biome.is(SpectrumBiomes.HOWLING_SPIRES);
		if (inHowlingSpires) {
			if (spireTicks < 60) {
				spireTicks++;
			}
		} else if (spireTicks > 0) {
			spireTicks--;
		}
		
		var time = world.getGameTime();
		var random = world.getRandom();
		
		var ashVelocity = targetAshVelocity;
		if (ashSwitchTicks < 50) {
			ashVelocity = Mth.clampedLerp(lastAshVelocity, targetAshVelocity, ashSwitchTicks);
			ashSwitchTicks++;
		}
		
		if (time % ASH_UPDATE_INTERVAL == 0 || !initialized) {
			updateAshEffects(random);
			
			FallingAshParticle.setTargetVelocity(ashVelocity);
			FallingAshParticle.setPrimaryAxis(ashAxis);
			FallingAshParticle.setAshScaleA(ashScaleA);
			FallingAshParticle.setAshScaleB(ashScaleB);
			FallingAshParticle.setAshScaleC(ashScaleC);
		}
		
		if (inHowlingSpires) {
			var maxAsh = ashSpawns / (SpectrumCommon.CONFIG.ReducedParticles ? 2 : 1);
			spawnHowlingSpiresAsh(cameraEntity, maxAsh, random, world, biome);
		}
		
		initialized = true;
	}
	
	private static void updateAshEffects(RandomSource random) {
		ashSpawns = random.nextIntBetweenInclusive(5, 7);
		
		if (random.nextFloat() < 0.125) {
			targetAshVelocity = Mth.clamp(targetAshVelocity + random.nextFloat() * 0.05 - 0.025, 0.025, 0.75);
			return;
		}
		
		if (random.nextFloat() < 0.95F)
			return;
		
		ashScaleA = 500 * (random.nextDouble() + 1) * (random.nextDouble() * 20) + 1000;
		ashScaleB = 100 * (random.nextDouble() + 1) * (random.nextDouble() * 10) + 250;
		ashScaleC = 20 * (random.nextDouble() + 1) * (random.nextDouble() * 5) + 100;
		
		var newAxis = random.nextBoolean() ? Direction.Axis.X : Direction.Axis.Z;
		if (newAxis == ashAxis)
			return;
		
		targetAshVelocity = BASE_ASH_VELOCITY * (random.nextDouble() * 0.5 + 0.5) * (random.nextBoolean() ? -1 : 1);
		ashAxis = newAxis;
	}
	
	private static void spawnHowlingSpiresAsh(Entity cameraEntity, int maxAsh, RandomSource random, ClientLevel clientWorld, Holder<Biome> biome) {
		var camera = cameraEntity.position();
		var renderDistance = getRenderRadius();
		var maxSpawnDistance = Math.min(96, renderDistance) * 2;

		for (int i = 0; i < maxAsh; i++) {
			var x = camera.x() + random.nextInt(maxSpawnDistance) - maxSpawnDistance / 2F;
			var y = camera.y() + random.nextInt(64) - 32;
			var z = camera.z() + random.nextInt(maxSpawnDistance) - maxSpawnDistance / 2F;
			var pos = new BlockPos((int) x, (int) y, (int) z);
			
			if (clientWorld.getBlockState(pos).isAir()) {
				clientWorld.addParticle(SpectrumParticleTypes.FALLING_ASH, x, y, z, 0, 0, 0);
			}
		}

		maxSpawnDistance /= 2;

		for (int i = 0; i < maxAsh; i++) {
			var x = camera.x() + random.nextInt(maxSpawnDistance) - maxSpawnDistance / 2F;
			var y = camera.y() + random.nextInt(29) - 8;
			var z = camera.z() + random.nextInt(maxSpawnDistance) - maxSpawnDistance / 2F;
			var pos = new BlockPos((int) x, (int) y, (int) z);

			if (clientWorld.getBlockState(pos).isAir()) {
				clientWorld.addParticle(SpectrumParticleTypes.FALLING_ASH, x, y, z, 0, 0, 0);
			}
		}
	}

	public static int getRenderRadius() {
		return (client.options.renderDistance().get() + 1) * 16;
	}
	
}
