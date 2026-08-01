package de.dafuqs.spectrum.render.biome_rendering;

import com.mojang.datafixers.util.*;
import de.dafuqs.spectrum.data_loaders.client.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.biome.*;
import net.neoforged.neoforge.client.event.*;
import org.joml.*;
import org.jspecify.annotations.*;

import java.lang.Math;
import java.util.function.*;

public class EnvironmentalRendering {
	
	private static final InterpolationMemory<float[]> GRADING_QUEUE = new InterpolationMemory<>();
	private static final InterpolationMemory<float[]> ENVIRONMENTAL_RENDERING_QUEUE = new InterpolationMemory<>();
	private static final InterpolationMemory<EnvironmentalDataOverride> OVERRIDE_QUEUE = new InterpolationMemory<>();
	
	private static final Minecraft client = Minecraft.getInstance();
	private static final Supplier<Float> delta = () -> client.getTimer().getGameTimeDeltaPartialTick(false);
	private static long envLoop, overrideLoop, overrideActiveTicks;
	private static boolean overrideActive;
	
	public static void tick(Entity entity) {
		var blending = client.options.biomeBlendRadius().get();
		var level = client.level;
		
		envLoop = level.getGameTime() % 3;
		overrideLoop = level.getGameTime() % 4;
		
		if (envLoop == 0)
			updateBiomeData(entity.blockPosition(), blending);
		
		updateOverrides(entity);
		if (GRADING_QUEUE.ready())
			ColorGrading.update(GRADING_QUEUE.last(), GRADING_QUEUE.current(), (envLoop + delta.get()) / 3F);
	}
	
	private static void updateOverrides(Entity entity) {
		if (overrideActive && overrideActiveTicks < 20) {
			overrideActiveTicks++;
		} else if (!overrideActive && overrideActiveTicks > 0) {
			overrideActiveTicks--;
		}
		
		if (overrideLoop != 0)
			return;
		
		EnvironmentalDataOverride override = EnvironmentalDataOverride.get(entity);
		overrideActive = override != EnvironmentalDataOverride.INACTIVE;
		
		if (!overrideActive && overrideActiveTicks > 0)
			return; // delay flushing the queue until the fadeout is done
		
		OVERRIDE_QUEUE.accept(override);
	}
	
	private static void updateBiomeData(BlockPos center, int blendingRadius) {
		if (blendingRadius <= 0) {
			ResourceKey<Biome> biome = getBiomeAtPos(center);
			Pair<ColorGrading, EnvironmentalData> biomeRenderingData = BiomeRenderingDataLoader.get(biome);
			GRADING_QUEUE.accept(biomeRenderingData.getFirst().asArray());
			processAndAcceptEnvironmentalData(center, biomeRenderingData.getSecond().asArray());
			return;
		}
		
		InterpolationStack environmentalRenderingStack = new InterpolationStack(4);
		InterpolationStack gradingStack = new InterpolationStack(5);
		
		Cursor3D cursor = new Cursor3D(
				center.getX() - blendingRadius,
				center.getY() - blendingRadius,
				center.getZ() - blendingRadius,
				center.getX() + blendingRadius,
				center.getY() + blendingRadius,
				center.getZ() + blendingRadius
		);
		
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		while (cursor.advance()) {
			pos.set(cursor.nextX(), cursor.nextY(), cursor.nextZ());
			
			ResourceKey<Biome> biome = getBiomeAtPos(pos);
			Pair<ColorGrading, EnvironmentalData> biomeRenderingData = BiomeRenderingDataLoader.get(biome);
			gradingStack.insert(biomeRenderingData.getFirst().asArray());
			environmentalRenderingStack.insert(biomeRenderingData.getSecond().asArray());
		}
		
		processAndAcceptEnvironmentalData(center, environmentalRenderingStack.get());
		GRADING_QUEUE.accept(gradingStack.get());
	}
	
	private static void processAndAcceptEnvironmentalData(BlockPos ref, float[] env) {
		float depthDarkening = 0F;
		int topSpace = client.level.getMaxBuildHeight() - ref.getY();
		depthDarkening += Mth.clampedLerp(0.334F, 0F, topSpace / 48F);
		
		int bottomSpace = ref.getY() - client.level.getMinBuildHeight();
		depthDarkening += Mth.clampedLerp(0.667F, 0F, bottomSpace / 64F);
		float depthFog = Mth.clampedLerp(0.337F, 1F, bottomSpace / 48F);
		
		env[0] = Math.clamp(env[0] + depthDarkening / 2F, 0, 1);
		env[1] = Math.clamp(env[1] - depthDarkening / 3F, 0.01F, 1);
		env[2] -= (1 - depthFog) * 2;
		env[3] *= depthFog;
		env[2] = Math.min(env[2], env[3]);
		
		ENVIRONMENTAL_RENDERING_QUEUE.accept(env);
	}
	
	public static EnvironmentalData getCurrentEnvironmentalData() {
		if (!ENVIRONMENTAL_RENDERING_QUEUE.ready()) {
			return EnvironmentalData.NOOP;
		}
		
		float[] interpolated = new float[4];
		for (int i = 0; i < interpolated.length; i++) {
			interpolated[i] = Mth.lerp((envLoop + delta.get()) / 3F, ENVIRONMENTAL_RENDERING_QUEUE.last()[i], ENVIRONMENTAL_RENDERING_QUEUE.current()[i]);
		}
		
		float overrideDelta = overrideDelta();
		float[] override = processOverrides().dataOverride().asArray();
		interpolated[0] = Mth.lerp(overrideDelta, interpolated[0], override[0]);
		interpolated[1] = Mth.lerp(overrideDelta, interpolated[1], override[1]);
		interpolated[2] = Mth.lerp(overrideDelta, interpolated[2], override[2]);
		interpolated[3] = Mth.lerp(overrideDelta, interpolated[3], override[3]);
		
		return EnvironmentalData.fromArray(interpolated);
	}
	
	public static void applyColor(ViewportEvent.ComputeFogColor event, EnvironmentalData environmentalData) {
		EnvironmentalDataOverride environmentalDataOverride = processOverrides();
		Vector3f color = environmentalDataOverride.color().color();
		float blend = environmentalDataOverride.color().blend();
		float overrideDelta = overrideDelta();
		
		float brightnessMultiplier = environmentalData.fogBrightnessMultiplier();
		float red = event.getRed() * brightnessMultiplier;
		float green = event.getGreen() * brightnessMultiplier;
		float blue = event.getBlue() * brightnessMultiplier;
		
		event.setRed(Mth.lerp(overrideDelta * blend, red, color.x));
		event.setGreen(Mth.lerp(overrideDelta * blend, green, color.y));
		event.setBlue(Mth.lerp(overrideDelta * blend, blue, color.z));
	}
	
	private static float overrideDelta() {
		float mutation = overrideActive ? overrideActiveTicks + delta.get() : overrideActiveTicks - delta.get();
		return Math.clamp(mutation / 20F, 0, 1);
	}
	
	private static EnvironmentalDataOverride processOverrides() {
		if (!OVERRIDE_QUEUE.ready()) {
			return EnvironmentalDataOverride.INACTIVE;
		}
		
		float[] currentOverride = OVERRIDE_QUEUE.current().asArray();
		float[] lastOverride = OVERRIDE_QUEUE.last().asArray();
		
		float[] interpolated = new float[8];
		for (int i = 0; i < currentOverride.length; i++) {
			interpolated[i] = Mth.lerp((overrideLoop + delta.get()) / 4F, lastOverride[i], currentOverride[i]);
		}
		
		return EnvironmentalDataOverride.fromArray(interpolated);
	}
	
	private static ResourceKey<Biome> getBiomeAtPos(BlockPos pos) {
		return client.level.getBiome(pos).getKey();
	}
	
	private static class InterpolationStack {
		
		private final float[] stack;
		private int count;
		
		public InterpolationStack(int size) {
			stack = new float[size];
		}
		
		public void insert(float[] inset) {
			for (int i = 0; i < inset.length; i++) {
				stack[i] += inset[i];
			}
			count++;
		}
		
		public float[] get() {
			float[] result = new float[stack.length];
			
			for (int i = 0; i < stack.length; i++) {
				result[i] = stack[i] / count;
			}
			
			return result;
		}
	}
	
	public static RenderState getRenderState() {
		if (client.level == null)
			return RenderState.INACTIVE;
		
		if (client.level.dimension().equals(SpectrumDimensionKeys.DIMENSION_KEY))
			return RenderState.ULTRA_DARK;
		
		if (overrideActive || overrideActiveTicks > 0)
			return RenderState.ACTIVE;
		
		return RenderState.INACTIVE;
	}
	
	public enum RenderState {
		INACTIVE(false, false),
		ACTIVE(true, false),
		ULTRA_DARK(true, true);
		
		private final boolean active;
		private final boolean ultradark;
		
		RenderState(boolean active, boolean ultradark) {
			this.active = active;
			this.ultradark = ultradark;
		}
		
		public boolean active() {
			return active;
		}
		
		public boolean ultradark() {
			return ultradark;
		}
	}
	
	static class InterpolationMemory<T> {
		private @Nullable T current, last;
		
		public void accept(T newHead) {
			if (!ready()) {
				initialize(newHead);
				return;
			}
			
			last = current;
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
		
		public @Nullable T current() {
			return current;
		}
		
		public @Nullable T last() {
			return last;
		}
		
		public boolean ready() {
			return current != null && last != null;
		}
	}
	
}