package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.particle.effect.Transphere;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.*;

import java.util.Optional;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class TransphereParticle extends SpriteBillboardParticle {
	
	private final Transphere transphere;
	private float field_28250;
	private float field_28248;
	
	public TransphereParticle(ClientWorld clientWorld, Transphere transphere, int i) {
		super(clientWorld, ((float) transphere.getOrigin().getX() + 0.5F), ((float) transphere.getOrigin().getY() + 0.5F), ((float) transphere.getOrigin().getZ() + 0.5F), 0.0D, 0.0D, 0.0D);
		this.scale = 0.2F;
		this.transphere = transphere;
		
		Vec3f rgb = ColorHelper.getVec(transphere.getDyeColor());
		this.setColor(rgb.getX(), rgb.getY(), rgb.getZ());
		this.maxAge = i;
	}
	
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		float f = MathHelper.sin(((float) this.age + tickDelta - 6.2831855F) * 0.05F) * 2.0F;
		float g = MathHelper.lerp(tickDelta, this.field_28248, this.field_28250);
		float h = 1.0472F;
		this.method_33078(vertexConsumer, camera, tickDelta, (quaternion) -> {
			quaternion.hamiltonProduct(Vec3f.POSITIVE_Y.getRadialQuaternion(g));
			quaternion.hamiltonProduct(Vec3f.POSITIVE_X.getRadialQuaternion(-h));
			quaternion.hamiltonProduct(Vec3f.POSITIVE_Y.getRadialQuaternion(f));
		});
		this.method_33078(vertexConsumer, camera, tickDelta, (quaternion) -> {
			quaternion.hamiltonProduct(Vec3f.POSITIVE_Y.getRadialQuaternion(-3.1415927F + g));
			quaternion.hamiltonProduct(Vec3f.POSITIVE_X.getRadialQuaternion(h));
			quaternion.hamiltonProduct(Vec3f.POSITIVE_Y.getRadialQuaternion(f));
		});
	}
	
	private void method_33078(VertexConsumer vertexConsumer, Camera camera, float tickDelta, Consumer<Quaternion> consumer) {
		Vec3d vec3d = camera.getPos();
		float g = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float h = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float i = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
		Vec3f vec3f = new Vec3f(0.5F, 0.5F, 0.5F);
		vec3f.normalize();
		Quaternion quaternion = new Quaternion(vec3f, 0.0F, true);
		consumer.accept(quaternion);
		Vec3f vec3f2 = new Vec3f(-1.0F, -1.0F, 0.0F);
		vec3f2.rotate(quaternion);
		Vec3f[] vec3fs = new Vec3f[]{new Vec3f(-1.0F, -1.0F, 0.0F), new Vec3f(-1.0F, 1.0F, 0.0F), new Vec3f(1.0F, 1.0F, 0.0F), new Vec3f(1.0F, -1.0F, 0.0F)};
		float j = this.getSize(tickDelta);
		
		if (this.angle == 0.0F) {
			quaternion = camera.getRotation();
		} else {
			quaternion = new Quaternion(camera.getRotation());
			float z = MathHelper.lerp(tickDelta, this.prevAngle, this.angle);
			quaternion.hamiltonProduct(Vec3f.POSITIVE_Z.getRadialQuaternion(z));
		}
		
		for (int k = 0; k < 4; ++k) {
			Vec3f vec3f3 = vec3fs[k];
			vec3f3.rotate(quaternion);
			vec3f3.scale(j);
			vec3f3.add(g, h, i);
		}
		
		float l = this.getMinU();
		float m = this.getMaxU();
		float n = this.getMinV();
		float o = this.getMaxV();
		int p = this.getBrightness(tickDelta);
		vertexConsumer.vertex(vec3fs[0].getX(), vec3fs[0].getY(), vec3fs[0].getZ()).texture(m, o).color(this.red, this.green, this.blue, this.alpha).light(p).next();
		vertexConsumer.vertex(vec3fs[1].getX(), vec3fs[1].getY(), vec3fs[1].getZ()).texture(m, n).color(this.red, this.green, this.blue, this.alpha).light(p).next();
		vertexConsumer.vertex(vec3fs[2].getX(), vec3fs[2].getY(), vec3fs[2].getZ()).texture(l, n).color(this.red, this.green, this.blue, this.alpha).light(p).next();
		vertexConsumer.vertex(vec3fs[3].getX(), vec3fs[3].getY(), vec3fs[3].getZ()).texture(l, o).color(this.red, this.green, this.blue, this.alpha).light(p).next();
	}
	
	public int getBrightness(float tint) {
		return 240;
	}
	
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	public void tick() {
		super.tick();
		Optional<BlockPos> optional = this.transphere.getDestination().getPos(this.world);
		if (optional.isEmpty()) {
			this.markDead();
		} else {
			double d = (double) this.age / (double) this.maxAge;
			BlockPos blockPos = this.transphere.getOrigin();
			BlockPos blockPos2 = optional.get();
			this.x = MathHelper.lerp(d, (double) blockPos.getX() + 0.5D, (double) blockPos2.getX() + 0.5D);
			this.y = MathHelper.lerp(d, (double) blockPos.getY() + 0.5D, (double) blockPos2.getY() + 0.5D);
			this.z = MathHelper.lerp(d, (double) blockPos.getZ() + 0.5D, (double) blockPos2.getZ() + 0.5D);
			this.field_28248 = this.field_28250;
			this.field_28250 = (float) MathHelper.atan2(this.x - (double) blockPos2.getX(), this.z - (double) blockPos2.getZ());
		}
	}
	
}
