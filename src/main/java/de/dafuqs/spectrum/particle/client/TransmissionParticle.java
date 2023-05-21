package de.dafuqs.spectrum.particle.client;

import net.fabricmc.api.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.*;
import net.minecraft.client.world.*;
import net.minecraft.util.math.*;
import net.minecraft.world.event.*;
import org.joml.*;

import java.util.*;
import java.util.function.*;

@Environment(EnvType.CLIENT)
public class TransmissionParticle extends SpriteBillboardParticle {
	
	private final PositionSource positionSource;
	private float current;
	private float last;
	
	public TransmissionParticle(ClientWorld world, double x, double y, double z, PositionSource positionSource, int maxAge) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
		this.scale = 0.175F;
		this.alpha = 0.8F;
		this.positionSource = positionSource;
		this.maxAge = maxAge;
	}
	
	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		float f = MathHelper.sin(((float) this.age + tickDelta - 6.2831855F) * 0.05F) * 2.0F;
		float g = MathHelper.lerp(tickDelta, this.last, this.current);
		float h = 1.0472F;
		// TODO - Used to be hamiltonProduct, needs testing to ensure this is correct conversion
		this.render(vertexConsumer, camera, tickDelta, (quaternion) -> {
			quaternion.mul(RotationAxis.POSITIVE_Y.rotationDegrees(g));
			quaternion.mul(RotationAxis.POSITIVE_X.rotationDegrees(-h));
			quaternion.mul(RotationAxis.POSITIVE_Y.rotationDegrees(f));
		});
		// TODO - Used to be hamiltonProduct, needs testing to ensure this is correct conversion
		this.render(vertexConsumer, camera, tickDelta, (quaternion) -> {
			quaternion.mul(RotationAxis.POSITIVE_Y.rotationDegrees(3 * h + g));
			quaternion.mul(RotationAxis.POSITIVE_X.rotationDegrees(h));
			quaternion.mul(RotationAxis.POSITIVE_Y.rotationDegrees(f));
		});
	}
	
	public void render(VertexConsumer vertexConsumer, Camera camera, float tickDelta, Consumer<Quaternionf> transforms) {
		Vec3d vec3d = camera.getPos();
		float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
		Vector3f vec3f = new Vector3f(0.5F, 0.5F, 0.5F);
		vec3f.normalize();
		Quaternionf quaternion = new Quaternionf(vec3f.x, vec3f.y, vec3f.z, 0.0f);
		transforms.accept(quaternion);
		Vector3f[] vec3fs = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
		float i = this.getSize(tickDelta);
		
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
	
	@Override
	public int getBrightness(float tint) {
		return 240;
	}
	
	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	@Override
	public void tick() {
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;
		if (this.age++ >= this.maxAge) {
			this.markDead();
		} else {
			Optional<Vec3d> optional = this.positionSource.getPos(this.world);
			if (optional.isEmpty()) {
				this.markDead();
			} else {
				int i = this.maxAge - this.age;
				double d = 1.0D / (double) i;
				Vec3d vec3d = optional.get();
				this.x = MathHelper.lerp(d, this.x, vec3d.getX());
				this.y = MathHelper.lerp(d, this.y, vec3d.getY());
				this.z = MathHelper.lerp(d, this.z, vec3d.getZ());
				this.last = this.current;
				this.current = (float) MathHelper.atan2(this.x - vec3d.getX(), this.z - vec3d.getZ());
			}
		}
	}
	
}
