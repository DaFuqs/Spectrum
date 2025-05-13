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

import static net.minecraft.util.math.MathHelper.lerp;

/**
 * I admit that this class is a mess
 * TODO: yes, this class is a mess. clean it up, pls
 * TODO: the mess grows
 */
public class DimensionRenderEffects {
	
	public static final float INTERP_TICKS = 80;
	public static final float[] FOG_DISTANCE_DEFAULT = new float[]{-2.25F, 1.5F};
	
	private static final Map<RegistryKey<Biome>, Float> DARKENING_MULTIPLIERS, FOG_DARKENING_MULTIPLIERS;
	private static final Map<RegistryKey<Biome>, float[]> FOG_DISTANCE_MULTIPLIERS;
	
	private static final Map<RegistryKey<Biome>, ColorGrading> COLOR_GRADING_DATA;
	private static final ColorGrading DEFAULT = new ColorGrading(1.0F, 0.0F, 65, 0.85F, 0.35F);
	private static final InterpolationQueue<float[]> GRADING_QUEUE = new InterpolationQueue<>();
	
	public static boolean isInDarkenedBiome, sleepAfflicted, forceFogEffects;
	public static int darkenTicks, darken, lastDarkenTicks, interpInterpTicks;
	
	public static float interpTarget, interp, lastInterpTarget, fogTarget = 1F, fogDarkness = 1F,
			lastFogTarget = 1F, nearTarget = 1F, near = 1F, lastNearTarget = 1F, farTarget = 1F, far = 1F, lastFarTarget = 1F,
			redTarget, red, lastRedTarget, greenTarget, green, lastGreenTarget, blueTarget, blue, lastBlueTarget, blendTarget, blend, lastBlendTarget;
	
	private static RegistryEntry<Biome> currentBiome;
	private static final MinecraftClient client = MinecraftClient.getInstance();
	private static boolean shouldUpdate, forceBiomeUpdate;
	
	// TODO: this should also invalidate the values when the world or spectated entity changed
	public static void clientTick(ClientWorld world, Entity entity, RegistryEntry<Biome> biome) {
		if (client.isPaused())
			return;
		
		lastDarkenTicks = darkenTicks;
		float sleepPotency = -1;
		@Nullable RegistryEntry<StatusEffect> sleepEffect = null;
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
			
			interpTarget = DARKENING_MULTIPLIERS.getOrDefault(biomeKey, 0F);
			fogTarget = FOG_DARKENING_MULTIPLIERS.getOrDefault(biomeKey, 1F);
			var targets = FOG_DISTANCE_MULTIPLIERS.getOrDefault(biomeKey, FOG_DISTANCE_DEFAULT);
			nearTarget = targets[0];
			farTarget = targets[1];
			if (GRADING_QUEUE.ready()) {
				GRADING_QUEUE.set(COLOR_GRADING_DATA.getOrDefault(biomeKey, DEFAULT).asArray(), ColorGrading.GRADING_OUT.clone());
			}
			else {
				GRADING_QUEUE.accept(COLOR_GRADING_DATA.getOrDefault(biomeKey, DEFAULT).asArray());
			}
			
			interpInterpTicks = 0;
		}
		
		if (interpInterpTicks < INTERP_TICKS) {
			interpInterpTicks++;
		}
		
		var delta = (float) interpInterpTicks / INTERP_TICKS;
		interp = lerp(delta, lastInterpTarget, interpTarget);
		fogDarkness = lerp(delta, lastFogTarget, fogTarget);
		near = lerp(delta, lastNearTarget, nearTarget);
		far = lerp(delta, lastFarTarget, farTarget);
		red = lerp(delta, lastRedTarget, redTarget);
		green = lerp(delta, lastGreenTarget, greenTarget);
		blue = lerp(delta, lastBlueTarget, blueTarget);
		blend = lerp(delta, lastBlendTarget, blendTarget);
		if (GRADING_QUEUE.ready()) {
			ColorGrading.update(GRADING_QUEUE.last, GRADING_QUEUE.current, delta);
		}
		
		isInDarkenedBiome = DARKENING_MULTIPLIERS.containsKey(biome.getKey().orElse(null));
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
		
		double y = lerp(client.getRenderTickCounter().getTickDelta(false), client.cameraEntity.lastRenderY, client.cameraEntity.getY());
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
		
		var y = lerp(client.getRenderTickCounter().getTickDelta(false), client.cameraEntity.lastRenderY, client.cameraEntity.getY());
		float distance;
		
		if (y < -270) {
			distance = (float) MathHelper.clampedLerp(1F, 0.667F, (y + 270) / -12) * near;
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
		return lerp(MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(false), (float) DimensionRenderEffects.darkenTicks, DimensionRenderEffects.lastDarkenTicks) / INTERP_TICKS * getInterp();
	}
	
	// this should really be a data loader
	static {
		var builder = ImmutableMap.<RegistryKey<Biome>, Float>builder();
		builder.put(SpectrumBiomes.BLACK_LANGAST, 0.7F);
		builder.put(SpectrumBiomes.DEEP_BARRENS, 0.325F);
		builder.put(SpectrumBiomes.DEEP_DRIPSTONE_CAVES, 0.1F);
		builder.put(SpectrumBiomes.NOXSHROOM_FOREST, 0.05F);
		DARKENING_MULTIPLIERS = builder.build();
		
		// Fog darkening,
		var fogBuilder = ImmutableMap.<RegistryKey<Biome>, Float>builder();
		fogBuilder.put(SpectrumBiomes.NOXSHROOM_FOREST, 0.125F);
		fogBuilder.put(SpectrumBiomes.RAZOR_EDGE, 0.65F);
		fogBuilder.put(SpectrumBiomes.DEEP_DRIPSTONE_CAVES, 0.25F);
		fogBuilder.put(SpectrumBiomes.DEEP_BARRENS, 0.55F);
		fogBuilder.put(SpectrumBiomes.BLACK_LANGAST, 0.0125F);
		FOG_DARKENING_MULTIPLIERS = fogBuilder.build();
		
		// These are percents of view distance (capped to 192 blocks for far)
		// Format is [near, far]. ...
		var transMultiplier = ImmutableMap.<RegistryKey<Biome>, float[]>builder();
		transMultiplier.put(SpectrumBiomes.NOXSHROOM_FOREST, new float[]{-3F, 1.5F});
		transMultiplier.put(SpectrumBiomes.HOWLING_SPIRES, new float[]{-5.25F, 1.25F});
		transMultiplier.put(SpectrumBiomes.DEEP_DRIPSTONE_CAVES, new float[]{-4F, 1.5F});
		transMultiplier.put(SpectrumBiomes.DEEP_BARRENS, new float[]{-5F, 0.5F});
		transMultiplier.put(SpectrumBiomes.BLACK_LANGAST, new float[]{-8F, 0.5F});
		transMultiplier.put(SpectrumBiomes.DRAGONROT_SWAMP, new float[]{-4F, 1F});
		FOG_DISTANCE_MULTIPLIERS = transMultiplier.build();
		
		var colorGradingBuilder = ImmutableMap.<RegistryKey<Biome>, ColorGrading>builder();
		colorGradingBuilder.put(SpectrumBiomes.NOXSHROOM_FOREST, new ColorGrading(1.05F, 0.015F, 80, 0.7F, 0.3125F));
		colorGradingBuilder.put(SpectrumBiomes.HOWLING_SPIRES, new ColorGrading(1.0F, 0.0F, 60, 0.9F, 0.425F));
		colorGradingBuilder.put(SpectrumBiomes.DEEP_DRIPSTONE_CAVES, new ColorGrading(1.0F, 0.02F, 60, 0.8F, 0.3F));
		colorGradingBuilder.put(SpectrumBiomes.DEEP_BARRENS, new ColorGrading(0.5F, 0.0F, 55, 0.7F, 0.2F));
		colorGradingBuilder.put(SpectrumBiomes.BLACK_LANGAST, new ColorGrading(0.5F, 0.0F, 60, 1.0F, 0.1F));
		colorGradingBuilder.put(SpectrumBiomes.DRAGONROT_SWAMP, new ColorGrading(0.7F, 0.05F, 105, 0.75F, 0.3F));
		COLOR_GRADING_DATA = colorGradingBuilder.build();
	}
	
	public record ColorGrading(float saturation, float rubedo, float colorTemperature, float threshold, float bloom) {
		public static final float[] GRADING_OUT = new float[5];
		
		private static void update(float[] old, float[] current, float delta) {
			for (int i = 0; i < 5; i++) {
				GRADING_OUT[i] =lerp(delta, old[i], current[i]);
			}
		}
		
		private float[] asArray() {
			return new float[]{
					saturation, rubedo, colorTemperature, threshold, bloom
			};
		}
	}
	
	//I will migrate shit to this someday trust me
	private static class InterpolationQueue<T> {
		private T current, last;
		
		public void accept(T newHead) {
			if (!ready()) {
				initialize(newHead);
				return;
			}
			
			current = newHead;
		}
		
		public void set(T current, T last) {
			accept(current);
			this.last = last;
		}
		
		public void initialize(T value) {
			last = value;
			current = value;
		}
		
		public boolean ready() {
			return current != null && last != null;
		}
	}
}
