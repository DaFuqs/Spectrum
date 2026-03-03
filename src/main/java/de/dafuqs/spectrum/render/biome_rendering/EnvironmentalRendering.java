package de.dafuqs.spectrum.render.biome_rendering;

import com.mojang.datafixers.util.*;
import de.dafuqs.spectrum.data_loaders.client.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Cursor3D;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import org.joml.*;

import java.lang.Math;
import java.util.function.Supplier;

public class EnvironmentalRendering {
	
	private static final InterpolationMemory<float[]> GRADING_QUEUE = new InterpolationMemory<>();
	private static final InterpolationMemory<float[]> ENVIRONMENTAL_RENDERING_QUEUE = new InterpolationMemory<>();
	private static final InterpolationMemory<EnvironmentalDataOverride> OVERRIDE_QUEUE = new InterpolationMemory<>();
	
	private static final Minecraft client = Minecraft.getInstance();
	private static final Supplier<Float> delta = () -> client.getTimer().getGameTimeDeltaPartialTick(false);
	private static long envLoop, overLoop, over;
	private static boolean overActive;
	
	public static void tick(Entity entity) {
		var blending = client.options.biomeBlendRadius().get();
		var level = client.level;
		
		envLoop = level.getGameTime() % 3;
		overLoop = level.getGameTime() % 4;
		
		if (envLoop == 0)
			updateBiomeData(entity.blockPosition(), blending);
		
		updateOverrides(entity);
		if (GRADING_QUEUE.ready())
			ColorGrading.update(GRADING_QUEUE.last(), GRADING_QUEUE.current(), (envLoop + delta.get()) / 3F);
	}
	
	private static void updateOverrides(Entity entity) {
		if (overActive && over < 20) {
			over++;
		} else if (!overActive && over > 0) {
			over--;
		}
		
		if (overLoop != 0)
			return;
		
		EnvironmentalDataOverride current = EnvironmentalDataOverride.get(entity);
		overActive = current != EnvironmentalDataOverride.INACTIVE;
		
		if (!overActive && over > 0)
			return; // delay flushing the queue until the fadeout is done
		
		OVERRIDE_QUEUE.accept(current);
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
		
		float delta = overDelta();
		float[] override = processOverrides().dataOverride().asArray();
		interpolated[0] += Math.clamp(Mth.lerp(delta, 0, override[0]), -1, 1);
		interpolated[1] += Mth.lerp(delta, 0, override[1]);
		interpolated[2] += Mth.lerp(delta, 0, override[2]);
		interpolated[3] += Mth.lerp(delta, 0, override[3]);
		
		interpolated[0] = Math.clamp(interpolated[0], -0.1F, 1);
		interpolated[1] = Math.clamp(interpolated[1], 0, 1);
		interpolated[2] = Math.max(interpolated[2], -10F);
		interpolated[3] = Math.max(interpolated[3], 0.125F);
		
		return EnvironmentalData.fromArray(interpolated);
	}
	
	public static void applyColor(float[] out) {
		EnvironmentalDataOverride environmentalDataOverride = processOverrides();
		Vector3f color = environmentalDataOverride.color().colorMod();
		float blend = environmentalDataOverride.color().blend();
		float delta = overDelta();
		
		out[0] = Mth.lerp(delta * blend, out[0], color.x);
		out[1] = Mth.lerp(delta * blend, out[1], color.y);
		out[2] = Mth.lerp(delta * blend, out[2], color.z);
	}
	
	private static float overDelta() {
		float mutation = overActive ? over + delta.get() : over - delta.get();
		return Math.clamp(mutation / 20F, 0, 1);
	}
	
	private static EnvironmentalDataOverride processOverrides() {
		if (!OVERRIDE_QUEUE.ready()) {
			return EnvironmentalDataOverride.INACTIVE;
		}
		
		float[] cur = OVERRIDE_QUEUE.current().asArray();
		float[] last = OVERRIDE_QUEUE.last().asArray();
		
		float[] interpolated = new float[8];
		for (int i = 0; i < cur.length; i++) {
			interpolated[i] = Mth.lerp((overLoop + delta.get()) / 4F, last[i], cur[i]);
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
		
		if (client.level.dimension().equals(SpectrumDimensions.DIMENSION_KEY))
			return RenderState.ULTRA_DARK;
		
		if (overActive || over > 0)
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
		private T current, last;
		
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
		
		public T current() {
			return current;
		}
		
		public T last() {
			return last;
		}
		
		public boolean ready() {
			return current != null && last != null;
		}
	}
	
}