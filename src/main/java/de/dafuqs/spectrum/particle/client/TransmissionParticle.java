package de.dafuqs.spectrum.particle.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.event.PositionSource;

import java.util.Optional;
import java.util.function.Consumer;

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
		this.render(vertexConsumer, camera, tickDelta, (quaternion) -> {
			quaternion.hamiltonProduct(Vec3f.POSITIVE_Y.getRadialQuaternion(g));
			quaternion.hamiltonProduct(Vec3f.POSITIVE_X.getRadialQuaternion(-h));
			quaternion.hamiltonProduct(Vec3f.POSITIVE_Y.getRadialQuaternion(f));
		});
		this.render(vertexConsumer, camera, tickDelta, (quaternion) -> {
			quaternion.hamiltonProduct(Vec3f.POSITIVE_Y.getRadialQuaternion(3 * h + g));
			quaternion.hamiltonProduct(Vec3f.POSITIVE_X.getRadialQuaternion(h));
			quaternion.hamiltonProduct(Vec3f.POSITIVE_Y.getRadialQuaternion(f));
		});
	}
	
	public void render(VertexConsumer vertexConsumer, Camera camera, float tickDelta, Consumer<Quaternion> transforms) {
		Vec3d vec3d = camera.getPos();
		float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
		Vec3f vec3f = new Vec3f(0.5F, 0.5F, 0.5F);
		vec3f.normalize();
		Quaternion quaternion = new Quaternion(vec3f, 0.0F, true);
		transforms.accept(quaternion);
		Vec3f vec3f2 = new Vec3f(-1.0F, -1.0F, 0.0F);
		vec3f2.rotate(quaternion);
		Vec3f[] vec3fs = new Vec3f[]{new Vec3f(-1.0F, -1.0F, 0.0F), new Vec3f(-1.0F, 1.0F, 0.0F), new Vec3f(1.0F, 1.0F, 0.0F), new Vec3f(1.0F, -1.0F, 0.0F)};
		float i = this.getSize(tickDelta);
		
		for (int j = 0; j < 4; ++j) {
			Vec3f vec3f3 = vec3fs[j];
			vec3f3.rotate(quaternion);
			vec3f3.scale(i);
			vec3f3.add(f, g, h);
		}
		
		float k = this.getMinU();
		float l = this.getMaxU();
		float m = this.getMinV();
		float n = this.getMaxV();
		int o = this.getBrightness(tickDelta);
		vertexConsumer.vertex(vec3fs[0].getX(), vec3fs[0].getY(), vec3fs[0].getZ()).texture(l, n).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		vertexConsumer.vertex(vec3fs[1].getX(), vec3fs[1].getY(), vec3fs[1].getZ()).texture(l, m).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		vertexConsumer.vertex(vec3fs[2].getX(), vec3fs[2].getY(), vec3fs[2].getZ()).texture(k, m).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		vertexConsumer.vertex(vec3fs[3].getX(), vec3fs[3].getY(), vec3fs[3].getZ()).texture(k, n).color(this.red, this.green, this.blue, this.alpha).light(o).next();
	}
	
	@Override
	public int getBrightness(float tint) {
		return 240;
	}
	
	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}
	
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
