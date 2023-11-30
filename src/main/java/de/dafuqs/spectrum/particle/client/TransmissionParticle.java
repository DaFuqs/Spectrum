package de.dafuqs.spectrum.particle.client;

import net.fabricmc.api.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.*;
import net.minecraft.client.world.*;
import net.minecraft.util.math.*;
import net.minecraft.world.event.*;
import org.joml.*;

@Environment(EnvType.CLIENT)
public class TransmissionParticle extends VibrationParticle {

	public TransmissionParticle(ClientWorld world, double x, double y, double z, PositionSource positionSource, int maxAge) {
		super(world, x, y, z, positionSource, maxAge);
		this.scale = 0.175F;
		this.alpha = 0.8F;
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		final Vec3d cameraPos = camera.getPos();
		final float x = (float) (MathHelper.lerp(tickDelta, prevPosX, this.x) - cameraPos.getX());
		final float y = (float) (MathHelper.lerp(tickDelta, prevPosY, this.y) - cameraPos.getY());
		final float z = (float) (MathHelper.lerp(tickDelta, prevPosZ, this.z) - cameraPos.getZ());
		final int light = getBrightness(tickDelta);

		final Quaternionf quaternion = camera.getRotation();
		final Vector3f[] vec3fs = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
		final float size = getSize(tickDelta);

		for (int k = 0; k < 4; ++k) {
			final Vector3f vec2 = vec3fs[k];
			vec2.rotate(quaternion);
			vec2.mul(size);
			vec2.add(x, y, z);
		}

		final float minU = getMinU();
		final float maxU = getMaxU();
		final float minV = getMinV();
		final float maxV = getMaxV();
		vertexConsumer.vertex(vec3fs[0].x(), vec3fs[0].y(), vec3fs[0].z()).texture(maxU, maxV).color(red, green, blue, alpha).light(light).next();
		vertexConsumer.vertex(vec3fs[1].x(), vec3fs[1].y(), vec3fs[1].z()).texture(maxU, minV).color(red, green, blue, alpha).light(light).next();
		vertexConsumer.vertex(vec3fs[2].x(), vec3fs[2].y(), vec3fs[2].z()).texture(minU, minV).color(red, green, blue, alpha).light(light).next();
		vertexConsumer.vertex(vec3fs[3].x(), vec3fs[3].y(), vec3fs[3].z()).texture(minU, maxV).color(red, green, blue, alpha).light(light).next();
	}
	
}
