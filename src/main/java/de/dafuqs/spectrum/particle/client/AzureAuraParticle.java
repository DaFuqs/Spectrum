package de.dafuqs.spectrum.particle.client;

import net.fabricmc.api.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.*;
import net.minecraft.client.world.*;
import net.minecraft.particle.*;
import net.minecraft.util.math.*;
import org.joml.*;

import java.lang.*;
import java.lang.Math;

@Environment(EnvType.CLIENT)
public class AzureAuraParticle extends AbstractSlowingParticle {

	private final float alphaMult;
	private float length;

	protected AzureAuraParticle(ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
		this.maxAge = 160 + random.nextInt(140);
		this.scale = 0.3F;
		this.length = 2 + random.nextFloat() * 2;
		this.scale += length / 14F;
		this.scale *= random.nextFloat() * 0.75F + 0.25F;
		this.length *= random.nextFloat() * 0.75F + 0.25F;
		this.velocityY += this.length / 100;
		this.alpha = 0;
		this.collidesWithWorld = false;

		this.alphaMult = random.nextFloat() * 0.5F + 0.5F;

		this.blue = 1F;
		this.red = 0.15F * random.nextFloat();
		this.green = 0.3F + random.nextFloat() * 0.55F;
		this.velocityMultiplier = 1;
	}

	@Override
	public void tick() {
		adjustAlpha();
		super.tick();
	}

	private void adjustAlpha() {
		var pos = BlockPos.ofFloored(x, y, z);
		if (age <= 20) {
			alpha = MathHelper.clamp(age / 20F, 0, alphaMult);
			return;
		}

		var fadeMarker = Math.min(maxAge / 5 * 2, 40);
		var ageFade = MathHelper.clamp(Math.min(maxAge - age, fadeMarker) / (float) fadeMarker, 0, alphaMult);;

		if (ageFade < alphaMult) {
			alpha = Math.min(alpha, ageFade);
		}
		else if (!world.getBlockState(pos).isTransparent(world, pos)) {
			alpha = MathHelper.clamp(alpha - 0.06F, alphaMult / 10, alphaMult);
		}
		else {
			alpha = MathHelper.clamp(alpha + 0.0325F, 0F, alphaMult);
		}


		if (alpha < 0.01F) {
			markDead();
		}
	}

	// Mildly cursed
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		Vec3d vec3d = camera.getPos();
		float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
		var xOffset = x - camera.getPos().x;
		var zOffset = z - camera.getPos().z;

		Quaternionf quaternionf = RotationAxis.POSITIVE_Y.rotation((float) MathHelper.atan2(xOffset, zOffset));

		Vector3f[] vector3fs = new Vector3f[]{new Vector3f(-1.0F, -length, 0.0F), new Vector3f(-1.0F, length, 0.0F), new Vector3f(1.0F, length, 0.0F), new Vector3f(1.0F, -length, 0.0F)};
		float i = this.getSize(tickDelta);

		for (int j = 0; j < 4; ++j) {
			Vector3f vector3f = vector3fs[j];
			vector3f.rotate(quaternionf);
			vector3f.mul(i);
			vector3f.add(f, g, h);
		}

		float k = this.getMinU();
		float l = this.getMaxU();
		float m = this.getMinV();
		float n = this.getMaxV();
		int o = this.getBrightness(tickDelta);
		vertexConsumer.vertex(vector3fs[0].x(), (double) vector3fs[0].y(), (double) vector3fs[0].z()).texture(l, n).color(this.red, this.green, this.blue, 0).light(o).next();
		vertexConsumer.vertex(vector3fs[1].x(), (double) vector3fs[1].y(), (double) vector3fs[1].z()).texture(l, m).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		vertexConsumer.vertex(vector3fs[2].x(), (double) vector3fs[2].y(), (double) vector3fs[2].z()).texture(k, m).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		vertexConsumer.vertex(vector3fs[3].x(), (double) vector3fs[3].y(), (double) vector3fs[3].z()).texture(k, n).color(this.red, this.green, this.blue, 0).light(o).next();
	}
	
	@Override
	public int getBrightness(float tint) {
		return LightmapTextureManager.MAX_LIGHT_COORDINATE;
	}
	
	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		
		private final SpriteProvider spriteProvider;
		
		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		@Override
		public Particle createParticle(DefaultParticleType parameters, ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			AzureAuraParticle particle = new AzureAuraParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
			particle.setSprite(this.spriteProvider);
			return particle;
		}
	}
	
}