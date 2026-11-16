package de.dafuqs.spectrum.blocks.ink.gen;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;

import java.util.Random;

public class VantaclastBlockEntityRenderer<T extends VantaclastBlockEntity> implements BlockEntityRenderer<T> {

	private static final float MESH_BASE_Y = 0.5F;
	private static final float MESH_HALF_SIZE = 0.2F;
	private static final float MESH_BASE_HEIGHT = -0.05F;
	private static final float MESH_WAVE_AMPLITUDE = 0.05F;
	private static final double MESH_WAVE_X_TIME_SCALE = 31.0;
	private static final double MESH_WAVE_Z_TIME_SCALE = 39.0;
	private static final float MESH_WAVE_X_SPATIAL_SCALE = 35.0F;
	private static final float MESH_WAVE_Z_SPATIAL_SCALE = 31.0F;
	private static final int MESH_SUBDIVISIONS = 24;
	
	private static final int SPIKE_COUNT = 5;
	private static final float SPIKE_MIN_RADIUS = 0.045F;
	private static final float SPIKE_MAX_RADIUS = 0.07F;
	private static final float SPIKE_MIN_HEIGHT = 0.52F;
	private static final float SPIKE_MAX_HEIGHT = 0.85F;
	private static final float SPIKE_HEIGHT_VARIANCE = 0.3F;
	private static final float[] SPIKE_BOUNCE_SCALES = {0.75F, 0.5F, 0.25F};
	private static final int SPIKE_BOUNCE_DURATION = 12;
	private static final double SPIKE_INITIAL_RISE_DURATION = 1.0;
	private static final double SPIKE_INITIAL_SINK_MIN_DURATION = 0.55;
	private static final double SPIKE_INITIAL_SINK_DURATION_VARIANCE = 0.8;
	private static final double SPIKE_EASE_MIN = 0.85;
	private static final double SPIKE_EASE_VARIANCE = 0.3;
	
	@SuppressWarnings("unused")
	public VantaclastBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
	}
	
	@Override
	public void render(VantaclastBlockEntity blockEntity, float tickDelta, PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int light, int overlay) {
		Minecraft client = Minecraft.getInstance();
		
		// render the disturbed ink surface
		poseStack.pushPose();
		poseStack.translate(0.5F, MESH_BASE_Y, 0.5F);
		renderMesh(blockEntity, tickDelta, poseStack, vertexConsumerProvider);
		poseStack.popPose();
		
		// floating in air
		ItemStack stack = blockEntity.getItem(0);
		if (!stack.isEmpty()) {
			poseStack.pushPose();
			
			float time = (float) (blockEntity.getLevel().getGameTime() % 50000.0 + tickDelta);
			double height = Math.sin(time / 8.0) * (1.0 / 6.0);
			
			poseStack.translate(0.5F, 2.5 + height, 0.5F);
			poseStack.mulPose(client.getBlockEntityRenderDispatcher().camera.rotation());
			poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
			Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, poseStack, vertexConsumerProvider, blockEntity.getLevel(), 0);
			poseStack.popPose();
		}
	}

	private void renderMesh(VantaclastBlockEntity blockEntity, float tickDelta, PoseStack poseStack, MultiBufferSource vertexConsumerProvider) {
		long eventTick = blockEntity.getLastConvertedBlockTick();
		long gameTime = blockEntity.getLevel().getGameTime();
		double time = gameTime + tickDelta;

		float[] spikeX = new float[SPIKE_COUNT];
		float[] spikeZ = new float[SPIKE_COUNT];
		float[] spikeRadii = new float[SPIKE_COUNT];
		float[] spikeHeights = new float[SPIKE_COUNT];
		if (eventTick >= 0L) {
			double elapsed = time - eventTick;
			// we need a deterministic random here, so not using the level random
			Random random = new Random(blockEntity.getBlockPos().asLong() ^ Long.rotateLeft(eventTick, 17));
			for (int i = 0; i < SPIKE_COUNT; i++) {
				float spikeCenterLimit = MESH_HALF_SIZE - SPIKE_MAX_RADIUS;
				spikeX[i] = (random.nextFloat() * 2.0F - 1.0F) * spikeCenterLimit;
				spikeZ[i] = (random.nextFloat() * 2.0F - 1.0F) * spikeCenterLimit;
				spikeRadii[i] = SPIKE_MIN_RADIUS + random.nextFloat() * (SPIKE_MAX_RADIUS - SPIKE_MIN_RADIUS);
				float baseHeight = SPIKE_MIN_HEIGHT + random.nextFloat() * (SPIKE_MAX_HEIGHT - SPIKE_MIN_HEIGHT);
				spikeHeights[i] = elapsed < 0.0 ? 0.0F : getSpikeHeight(elapsed, baseHeight, random);
			}
		}

		VertexConsumer vertices = vertexConsumerProvider.getBuffer(RenderType.debugQuads());
		var matrix = poseStack.last().pose();
		float step = MESH_HALF_SIZE * 2.0F / MESH_SUBDIVISIONS;
		for (int x = 0; x < MESH_SUBDIVISIONS; x++) {
			float x0 = -MESH_HALF_SIZE + x * step;
			float x1 = x0 + step;
			for (int z = 0; z < MESH_SUBDIVISIONS; z++) {
				float z0 = -MESH_HALF_SIZE + z * step;
				float z1 = z0 + step;

				addMeshVertex(vertices, matrix, x0, z0, time, spikeX, spikeZ, spikeRadii, spikeHeights);
				addMeshVertex(vertices, matrix, x0, z1, time, spikeX, spikeZ, spikeRadii, spikeHeights);
				addMeshVertex(vertices, matrix, x1, z1, time, spikeX, spikeZ, spikeRadii, spikeHeights);
				addMeshVertex(vertices, matrix, x1, z0, time, spikeX, spikeZ, spikeRadii, spikeHeights);
			}
		}
	}

	private void addMeshVertex(VertexConsumer vertices, org.joml.Matrix4f matrix, float x, float z, double time, float[] spikeX, float[] spikeZ, float[] spikeRadii, float[] spikeHeights) {
		double wave = Math.sin(time / MESH_WAVE_X_TIME_SCALE + x * MESH_WAVE_X_SPATIAL_SCALE) * Math.cos(time / MESH_WAVE_Z_TIME_SCALE + z * MESH_WAVE_Z_SPATIAL_SCALE);
		float height = MESH_BASE_HEIGHT + (float) wave * MESH_WAVE_AMPLITUDE;
		for (int i = 0; i < SPIKE_COUNT; i++) {
			if (spikeHeights[i] <= 0.0F) {
				continue;
			}
			float radius = spikeRadii[i];
			float dx = x - spikeX[i];
			float dz = z - spikeZ[i];
			float influence = (float) Math.exp(-(dx * dx + dz * dz) / (radius * radius));
			height += spikeHeights[i] * influence;
		}
		vertices.addVertex(matrix, x, height, z).setColor(0.0F, 0.0F, 0.0F, 1.0F);
	}

	private float getSpikeHeight(double elapsed, float baseHeight, Random random) {
		float targetHeight = baseHeight * (1.0F - SPIKE_HEIGHT_VARIANCE / 2.0F + random.nextFloat() * SPIKE_HEIGHT_VARIANCE);
		double initialSinkDuration = SPIKE_INITIAL_SINK_MIN_DURATION + random.nextDouble() * SPIKE_INITIAL_SINK_DURATION_VARIANCE;
		double initialEase = SPIKE_EASE_MIN + random.nextDouble() * SPIKE_EASE_VARIANCE;
		double initialSinkEase = SPIKE_EASE_MIN + random.nextDouble() * SPIKE_EASE_VARIANCE;

		if (elapsed <= SPIKE_INITIAL_RISE_DURATION) {
			return (float) (targetHeight * Math.pow(ease(elapsed / SPIKE_INITIAL_RISE_DURATION), initialEase));
		}
		if (elapsed < SPIKE_INITIAL_RISE_DURATION + initialSinkDuration) {
			double sinkProgress = (elapsed - SPIKE_INITIAL_RISE_DURATION) / initialSinkDuration;
			return (float) (targetHeight * (1.0 - Math.pow(ease(sinkProgress), initialSinkEase)));
		}

		double bounceElapsed = elapsed - SPIKE_INITIAL_RISE_DURATION - initialSinkDuration;
		double baseDuration = Math.max(1, SPIKE_BOUNCE_DURATION);
		for (float bounceScale : SPIKE_BOUNCE_SCALES) {
			double duration = baseDuration * (0.8 + random.nextDouble() * 0.4);
			double riseFraction = 0.42 + random.nextDouble() * 0.16;
			double riseEase = SPIKE_EASE_MIN + random.nextDouble() * SPIKE_EASE_VARIANCE;
			double fallEase = SPIKE_EASE_MIN + random.nextDouble() * SPIKE_EASE_VARIANCE;
			if (bounceElapsed < duration) {
				double progress = bounceElapsed / duration;
				double bounceHeight = progress < riseFraction
						? Math.pow(ease(progress / riseFraction), riseEase)
						: 1.0 - Math.pow(ease((progress - riseFraction) / (1.0 - riseFraction)), fallEase);
				return (float) (targetHeight * bounceScale * bounceHeight);
			}
			bounceElapsed -= duration;
		}
		return 0.0F;
	}

	private double ease(double progress) {
		double clampedProgress = Mth.clamp(progress, 0.0, 1.0);
		return clampedProgress * clampedProgress * (3.0 - 2.0 * clampedProgress);
	}

}
