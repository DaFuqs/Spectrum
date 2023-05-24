package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.helpers.ColorHelper;
import net.fabricmc.api.*;
import net.minecraft.client.render.*;
import net.minecraft.client.world.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.event.*;
import org.joml.*;

import java.util.function.*;

@Environment(EnvType.CLIENT)
public class ColoredTransmissionParticle extends TransmissionParticle {
	
	public ColoredTransmissionParticle(ClientWorld world, double x, double y, double z, PositionSource positionSource, int maxAge, DyeColor dyeColor) {
		super(world, x, y, z, positionSource, maxAge);

		Vector3f color = ColorHelper.getRGBVec(dyeColor);
		this.setColor(color.x(), color.y(), color.z());
	}
	
	@Override
	public void render(VertexConsumer vertexConsumer, Camera camera, float tickDelta, Consumer<Quaternionf> transforms) {
		Vec3d vec3d = camera.getPos();
		float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
		Vector3f vec3f = new Vector3f(0.5F, 0.5F, 0.5F);
		vec3f.normalize();
		// TODO - Used to have a True bool attached, needs testing to see if it renders correctly
		Quaternionf quaternion = new Quaternionf(vec3f.x, vec3f.y, vec3f.z, 0.0F);
		transforms.accept(quaternion);
		Vector3f vec3f2 = new Vector3f(-1.0F, -1.0F, 0.0F);
		vec3f2.rotate(quaternion);
		Vector3f[] vec3fs = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
		float i = this.getSize(tickDelta);
		
		if (this.angle == 0.0F) {
			quaternion = camera.getRotation();
		} else {
			quaternion = new Quaternionf(camera.getRotation());
			float z = MathHelper.lerp(tickDelta, this.prevAngle, this.angle);
			quaternion.mul(RotationAxis.POSITIVE_Z.rotationDegrees(z));
		}
		
		for (int j = 0; j < 4; ++j) {
			Vector3f vec = vec3fs[j];
			vec.rotate(quaternion);
			vec.mul(i);
			vec.add(f, g, h);
		}
		
		float k = this.getMinU();
		float l = this.getMaxU();
		float m = this.getMinV();
		float n = this.getMaxV();
		int o = this.getBrightness(tickDelta);
		vertexConsumer.vertex(vec3fs[0].x(), vec3fs[0].y(), vec3fs[0].z()).texture(l, n).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		vertexConsumer.vertex(vec3fs[1].x(), vec3fs[1].y(), vec3fs[1].z()).texture(l, m).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		vertexConsumer.vertex(vec3fs[2].x(), vec3fs[2].y(), vec3fs[2].z()).texture(k, m).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		vertexConsumer.vertex(vec3fs[3].x(), vec3fs[3].y(), vec3fs[3].z()).texture(k, n).color(this.red, this.green, this.blue, this.alpha).light(o).next();
	}
	
}