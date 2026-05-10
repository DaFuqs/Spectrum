package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.particle.effect.*;
import net.fabricmc.api.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.*;
import net.minecraft.util.*;
import org.joml.*;

@Environment(EnvType.CLIENT)
public class FixedVelocityParticle extends TextureSheetParticle {
	
	protected FixedVelocityParticle(ClientLevel clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
		this.gravity = 0.0F;
		this.speedUpWhenYMotionIsBlocked = true;
		this.quadSize *= 0.75F;
		this.hasPhysics = false;

		// override the vanilla velocity randomization
		this.xd = velocityX;
		this.yd = velocityY;
		this.zd = velocityZ;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	@Override
	public int getLightColor(float tint) {
		float f = ((float) this.age + tint) / (float) this.lifetime;
		f = Mth.clamp(f, 0.0F, 1.0F);
		int i = super.getLightColor(tint);
		int j = i & 255;
		int k = i >> 16 & 255;
		j += (int) (f * 15.0F * 16.0F);
		if (j > 240) {
			j = 240;
		}
		
		return j | k << 16;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		
		private final SpriteSet spriteProvider;
		
		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		@Override
		public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			FixedVelocityParticle craftingParticle = new FixedVelocityParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
			craftingParticle.setLifetime((int) (8.0D / (clientWorld.getRandom().nextDouble() * 0.8D + 0.2D)));
			craftingParticle.pickSprite(this.spriteProvider);
			return craftingParticle;
		}
	}
	
	@Environment(EnvType.CLIENT)
	public static class ColoredFluidRisingFactory implements ParticleProvider<ColoredFluidRisingParticleEffect> {
		
		private final SpriteSet spriteProvider;
		
		public ColoredFluidRisingFactory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		@Override
		public Particle createParticle(ColoredFluidRisingParticleEffect particleEffect, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			FixedVelocityParticle particle = new FixedVelocityParticle(world, x, y, z, velocityX, velocityY, velocityZ);
			particle.setLifetime((int) (8.0D / (world.getRandom().nextDouble() * 0.8D + 0.2D)));
			particle.pickSprite(this.spriteProvider);
			
			Vector3f color = particleEffect.getColor();
			particle.setColor(color.x, color.y, color.z);
			
			return particle;
		}
	}
	
	@Environment(EnvType.CLIENT)
	public static class ColoredSparkleRisingFactory implements ParticleProvider<ColoredSparkleRisingParticleEffect> {
		
		private final SpriteSet spriteProvider;
		
		public ColoredSparkleRisingFactory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		@Override
		public Particle createParticle(ColoredSparkleRisingParticleEffect particleEffect, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			FixedVelocityParticle particle = new FixedVelocityParticle(world, x, y, z, velocityX, velocityY, velocityZ);
			particle.setLifetime((int) (8.0D / (world.getRandom().nextDouble() * 0.8D + 0.2D)));
			particle.pickSprite(this.spriteProvider);
			
			Vector3f color = particleEffect.getColor();
			particle.setColor(color.x, color.y, color.z);
			
			return particle;
		}
	}

}
