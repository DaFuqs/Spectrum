package de.dafuqs.spectrum.deeper_down;

import com.google.common.collect.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.client.*;
import net.minecraft.client.world.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.*;
import net.minecraft.util.math.*;
import net.minecraft.world.biome.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * I admit that this class is a mess
 */
public class DarknessEffects {
	
	public static final float INTERP_TICKS = 160;
	private static final Map<RegistryKey<Biome>, Float> INTERP_MULTIPLIERS, FOG_MULTIPLIERS;
	private static final Map<RegistryKey<Biome>, float[]> TRANS_MULTIPLIERS;
	public static boolean isInDarkenedBiome, sleepAfflicted, forceFogEffects;
	public static int darkenTicks, darken, lastDarkenTicks, interpInterpTicks;
	public static float interpTarget, interp, lastInterpTarget, fogTarget = 1F, fogDarkness = 1F,
			lastFogTarget = 1F, nearTarget = 1F, near = 1F, lastNearTarget = 1F, farTarget = 1F, far = 1F, lastFarTarget = 1F,
			redTarget, red, lastRedTarget, greenTarget, green, lastGreenTarget, blueTarget, blue, lastBlueTarget, blendTarget, blend, lastBlendTarget;
	private static RegistryEntry<Biome> currentBiome;
	private static final MinecraftClient client = MinecraftClient.getInstance();
	private static boolean shouldUpdate, forceBiomeUpdate;
	
	// TODO: this should probably also invalidate the values when the world or spectated entity changed
	public static void clientTick(ClientWorld world, Entity entity, RegistryEntry<Biome> biome) {
		if (client.isPaused())
			return;
		
		lastDarkenTicks = darkenTicks;
		float sleepPotency = -1;
		@Nullable StatusEffect sleepEffect = null;
		if (entity instanceof LivingEntity livingEntity) {
			sleepPotency = SleepStatusEffect.getSleepScaling(livingEntity);
			sleepEffect = SleepStatusEffect.getStrongestSleepEffect(livingEntity);
		}
		
		
		if (shouldUpdate) {
			var targets = MathHelper.clamp(sleepPotency / 2F, 0, 1);
			interpInterpTicks = 0;
			shouldUpdate = false;
			updateTargets();
			
			if (sleepEffect == SpectrumStatusEffects.FATAL_SLUMBER) {
				sleepAfflicted = true;
				
				blendTarget = 1F;
				interpTarget = 1F;
				redTarget = 14 / 255F;
				greenTarget = 4 / 255F;
				blueTarget = 27 / 255F;
				nearTarget = -10F;
				farTarget = 0.25F;
				forceFogEffects = true;
			} else if (sleepEffect == SpectrumStatusEffects.ETERNAL_SLUMBER) {
				sleepAfflicted = true;
				
				blendTarget = targets;
				interpTarget = targets;
				redTarget = 73 / 255F;
				greenTarget = 36 / 255F;
				blueTarget = 115 / 255F;
				nearTarget = -2F;
				farTarget = 0.9F;
				forceFogEffects = true;
			} else if (sleepEffect == SpectrumStatusEffects.SOMNOLENCE) {
				sleepAfflicted = true;
				
				blendTarget = targets;
				interpTarget = targets;
				redTarget = 195 / 255F;
				greenTarget = 95 / 255F;
				blueTarget = 238 / 255F;
				nearTarget = -5F;
				forceFogEffects = true;
			} else {
				sleepAfflicted = false;
				
				blendTarget = 0;
				redTarget = 0;
				greenTarget = 0;
				blueTarget = 0;
				currentBiome = null;
				forceFogEffects = false;
				forceBiomeUpdate = true;
			}
		} else if (currentBiome == null || !currentBiome.getKey().equals(biome.getKey())) {
			if (forceBiomeUpdate)
				forceBiomeUpdate = false;

			var biomeKey = biome.getKey().orElse(null);
			currentBiome = biome;
			updateTargets();
			
			interpTarget = INTERP_MULTIPLIERS.getOrDefault(biomeKey, 0F);
			fogTarget = FOG_MULTIPLIERS.getOrDefault(biomeKey, 1F);
			var targets = TRANS_MULTIPLIERS.getOrDefault(biomeKey, new float[]{1F, 1F});
			nearTarget = targets[0];
			farTarget = targets[1];
			interpInterpTicks = 0;
		}
		
		if (interpInterpTicks < Math.round(INTERP_TICKS / 1.5F)) {
			interpInterpTicks++;
		}
		
		interp = MathHelper.lerp((float) interpInterpTicks / Math.round(INTERP_TICKS / 1.5F), lastInterpTarget, interpTarget);
		fogDarkness = MathHelper.lerp((float) interpInterpTicks / Math.round(INTERP_TICKS / 1.5F), lastFogTarget, fogTarget);
		near = MathHelper.lerp((float) interpInterpTicks / Math.round(INTERP_TICKS / 1.5F), lastNearTarget, nearTarget);
		far = MathHelper.lerp((float) interpInterpTicks / Math.round(INTERP_TICKS / 1.5F), lastFarTarget, farTarget);
		red = MathHelper.lerp((float) interpInterpTicks / Math.round(INTERP_TICKS / 1.5F), lastRedTarget, redTarget);
		green = MathHelper.lerp((float) interpInterpTicks / Math.round(INTERP_TICKS / 1.5F), lastGreenTarget, greenTarget);
		blue = MathHelper.lerp((float) interpInterpTicks / Math.round(INTERP_TICKS / 1.5F), lastBlueTarget, blueTarget);
		blend = MathHelper.lerp((float) interpInterpTicks / Math.round(INTERP_TICKS / 1.5F), lastBlendTarget, blendTarget);
		
		
		isInDarkenedBiome = INTERP_MULTIPLIERS.containsKey(biome.getKey().orElse(null));
		if (isInDarkenedBiome || sleepAfflicted) {
			if (darkenTicks < INTERP_TICKS) {
				darkenTicks++;
			} else if (darkenTicks > INTERP_TICKS) {
				darkenTicks--;
			}
		} else if (darkenTicks > 0) {
			darkenTicks--;
		}
	}
	
	private static void updateTargets() {
		lastInterpTarget = interp;
		lastFogTarget = fogDarkness;
		lastNearTarget = near;
		lastFarTarget = far;
		lastRedTarget = red;
		lastGreenTarget = green;
		lastBlueTarget = blue;
		lastBlendTarget = blend;
	}

	public static void markForEffectUpdate() {
		shouldUpdate = true;
	}

	public static float getInterp() {
		if (client.cameraEntity == null)
			return interp;
		
		var y = MathHelper.lerp(client.getTickDelta(), client.cameraEntity.lastRenderY, client.cameraEntity.getY());
		float adjustedInterp;
		
		//entrance darkening
		if (y > -116) {
			adjustedInterp = (float) MathHelper.clampedLerp(0.175F, interp, (y + 64) / -52F);
		}
		//depth darkening
		else if (y < -256) {
			adjustedInterp = (float) Math.max(interp, Math.min(0.825F, interp + (y + 256) / -256F));
		} else {
			adjustedInterp = interp;
		}
		
		return adjustedInterp;
	}
	
	public static float getNear(float start) {
		if (client.cameraEntity == null)
			return near;
		
		var y = MathHelper.lerp(client.getTickDelta(), client.cameraEntity.lastRenderY, client.cameraEntity.getY());
		float distance;
		
		if (y < -272) {
			distance = (float) MathHelper.clampedLerp(0F, 1.334F, (y + 272) / -48) * near;
		} else {
			distance = near;
		}
		
		return distance * start;
	}
	
	public static float getFar(float end) {
		if (client.cameraEntity == null)
			return far;
		
		return far * end;
	}
	
	public static float getDarknessInterpolation() {
		return MathHelper.lerp(MinecraftClient.getInstance().getTickDelta(), (float) DarknessEffects.darkenTicks, DarknessEffects.lastDarkenTicks) / INTERP_TICKS * getInterp();
	}
	
	static {
		var builder = ImmutableMap.<RegistryKey<Biome>, Float>builder();
		builder.put(SpectrumBiomes.BLACK_LANGAST, 0.7F);
		builder.put(SpectrumBiomes.DEEP_BARRENS, 0.325F);
		builder.put(SpectrumBiomes.DEEP_DRIPSTONE_CAVES, 0.1F);
		builder.put(SpectrumBiomes.NOXSHROOM_FOREST, 0.05F);
		INTERP_MULTIPLIERS = builder.build();
		
		var fogBuilder = ImmutableMap.<RegistryKey<Biome>, Float>builder();
		fogBuilder.put(SpectrumBiomes.NOXSHROOM_FOREST, 0.125F);
		fogBuilder.put(SpectrumBiomes.DEEP_DRIPSTONE_CAVES, 0.25F);
		fogBuilder.put(SpectrumBiomes.DEEP_BARRENS, 0.55F);
		fogBuilder.put(SpectrumBiomes.BLACK_LANGAST, 0.0125F);
		FOG_MULTIPLIERS = fogBuilder.build();
		
		var transMultiplier = ImmutableMap.<RegistryKey<Biome>, float[]>builder();
		transMultiplier.put(SpectrumBiomes.NOXSHROOM_FOREST, new float[]{1.25F, 1F});
		transMultiplier.put(SpectrumBiomes.HOWLING_SPIRES, new float[]{-10F, 0.85F});
		transMultiplier.put(SpectrumBiomes.DEEP_BARRENS, new float[]{-1F, 0.5F});
		transMultiplier.put(SpectrumBiomes.BLACK_LANGAST, new float[]{0.8F, 0.8F});
		transMultiplier.put(SpectrumBiomes.DRAGONROT_SWAMP, new float[]{-5F, 0.8F});
		TRANS_MULTIPLIERS = transMultiplier.build();
	}
}
