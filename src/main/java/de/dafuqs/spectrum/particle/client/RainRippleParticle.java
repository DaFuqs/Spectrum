package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.particle.*;
import net.fabricmc.api.*;
import net.minecraft.client.color.world.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.*;
import net.minecraft.client.world.*;
import net.minecraft.particle.*;
import net.minecraft.util.math.*;
import org.joml.*;

import java.lang.Math;

public class RainRippleParticle extends SpriteBillboardParticle {

	private float width, lastWidth, alphaMult;

	public RainRippleParticle(ClientWorld clientWorld, double d, double e, double f, SpriteProvider spriteProvider) {
		super(clientWorld, d, e, f);
		setSprite(spriteProvider);
		gravityStrength = 0F;
		scale = 0.2F + random.nextFloat() * 0.1F;
		alphaMult = 0.75F + random.nextFloat() * 0.25F;
		alpha = alphaMult;
		var waterColor = ColorHelper.colorIntToVec(BiomeColors.getWaterColor(world, BlockPos.ofFloored(x, y, z)));
		red = waterColor.x;
		green = waterColor.y;
		blue = waterColor.z;
		maxAge = 13;
	}
	
	@Override
	public void tick() {
		lastWidth = width;
		width = Math.max(0.05F, age / 13F) + 0.1F;
		adjustAlpha();
		super.tick();
	}

	private void adjustAlpha() {
		alpha = MathHelper.clamp(Math.min(maxAge - age, 6) / 6F, 0, alphaMult);
		if (alpha < 0.01F) {
			markDead();
		}
	}
	
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		Vec3d vec3d = camera.getPos();
		float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());

		var effWidth = MathHelper.lerp(tickDelta, lastWidth, width);
		Vector3f[] vector3fs = new Vector3f[]{new Vector3f(-effWidth, 0, -effWidth), new Vector3f(-effWidth, 0, effWidth), new Vector3f(effWidth, 0, effWidth), new Vector3f(effWidth, 0, -effWidth)};
		float i = this.getSize(tickDelta);
		
		for (int j = 0; j < 4; ++j) {
			Vector3f vector3f = vector3fs[j];
			vector3f.mul(i);
			vector3f.add(f, g, h);
		}
		
		float k = this.getMinU();
		float l = this.getMaxU();
		float m = this.getMinV();
		float n = this.getMaxV();
		int o = this.getBrightness(tickDelta);
		vertexConsumer.vertex(vector3fs[0].x(), (double) vector3fs[0].y(), (double) vector3fs[0].z()).texture(l, n).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		vertexConsumer.vertex(vector3fs[1].x(), (double) vector3fs[1].y(), (double) vector3fs[1].z()).texture(l, m).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		vertexConsumer.vertex(vector3fs[2].x(), (double) vector3fs[2].y(), (double) vector3fs[2].z()).texture(k, m).color(this.red, this.green, this.blue, this.alpha).light(o).next();
		vertexConsumer.vertex(vector3fs[3].x(), (double) vector3fs[3].y(), (double) vector3fs[3].z()).texture(k, n).color(this.red, this.green, this.blue, this.alpha).light(o).next();
	}
	
	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;
		
		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		@Override
		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			return new RainRippleParticle(clientWorld, d, e, f, spriteProvider);
		}
	}
}
