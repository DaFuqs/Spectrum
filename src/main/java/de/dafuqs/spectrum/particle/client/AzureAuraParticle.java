package de.dafuqs.spectrum.particle.client;

import net.fabricmc.api.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.*;
import net.minecraft.client.world.*;
import net.minecraft.particle.*;
import net.minecraft.util.math.*;
import org.joml.*;

import java.lang.Math;

@Environment(EnvType.CLIENT)
public class AzureAuraParticle extends AbstractSlowingParticle {
	
	private final float alphaMult;
	private float length;
	
	protected AzureAuraParticle(ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
		this.maxAge = 160 + random.nextInt(140);
		this.scale = 0.3F;
		var thisLength = 2 + random.nextFloat() * 2;
		this.scale += thisLength / 14F;
		this.scale *= random.nextFloat() * 0.75F + 0.25F;
		this.length = thisLength * (random.nextFloat() * 0.75F + 0.25F);
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
		var ageFade = MathHelper.clamp(Math.min(maxAge - age, fadeMarker) / (float) fadeMarker, 0, alphaMult);
		
		if (ageFade < alphaMult) {
			alpha = Math.min(alpha, ageFade);
		} else if (!world.getBlockState(pos).isTransparent(world, pos)) {
			alpha = MathHelper.clamp(alpha - 0.06F, alphaMult / 10, alphaMult);
		} else {
			alpha = MathHelper.clamp(alpha + 0.0325F, 0F, alphaMult);
		}
		
		
		if (alpha < 0.01F) {
			markDead();
		}
	}
	
	// Mildly cursed
	// Dafuqs: Update: Majorly cursed
	// Mostly uncursed by Pizzer
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		final Vec3d cameraPos = camera.getPos();
		final float xOff = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - cameraPos.getX());
		final float yOff = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - cameraPos.getY());
		final float zOff = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - cameraPos.getZ());
		final float size = this.getSize(tickDelta);

		final float rot = (float) MathHelper.atan2(xOff, zOff);
		final float sin = org.joml.Math.sin(rot);
		final float cos = org.joml.Math.cosFromSin(sin, rot);

		final float negX = Math.fma(-cos,    size, xOff);
		final float posX = Math.fma( cos,    size, xOff);

		final float negY = Math.fma(-length, size, yOff);
		final float posY = Math.fma( length, size, yOff);

		final float negZ = Math.fma(-sin,    size, zOff);
		final float posZ = Math.fma( sin,    size, zOff);

		final float minU = this.getMinU();
		final float maxU = this.getMaxU();
		final float minV = this.getMinV();
		final float maxV = this.getMaxV();
		final int   brightness = this.getBrightness(tickDelta);
		vertexConsumer.vertex(negX, negY, posZ).texture(maxU, maxV).color(this.red, this.green, this.blue, 0).light(brightness).next();
		vertexConsumer.vertex(negX, posY, posZ).texture(maxU, minV).color(this.red, this.green, this.blue, this.alpha).light(brightness).next();
		vertexConsumer.vertex(posX, posY, negZ).texture(minU, minV).color(this.red, this.green, this.blue, this.alpha).light(brightness).next();
		vertexConsumer.vertex(posX, negY, negZ).texture(minU, maxV).color(this.red, this.green, this.blue, 0).light(brightness).next();
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