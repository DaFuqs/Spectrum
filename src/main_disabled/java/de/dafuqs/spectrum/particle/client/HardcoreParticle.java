package de.dafuqs.spectrum.particle.client;

import net.fabricmc.api.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.*;

@Environment(EnvType.CLIENT)
public class HardcoreParticle extends SimpleAnimatedParticle {
	
	HardcoreParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteSet spriteProvider) {
		super(world, x, y, z, spriteProvider, 1.25F);
		this.friction = 0.6F;
		this.xd = velocityX;
		this.yd = velocityY;
		this.zd = velocityZ;
		this.quadSize *= 0.75F;
		this.lifetime = 60 + this.random.nextInt(12);
		this.setSpriteFromAge(spriteProvider);
		if (this.random.nextInt(4) == 0) {
			this.setColor(0.0F, 0.0F, 0.0F);
		} else {
			this.setColor(0.2F + this.random.nextFloat() * 0.5F, this.random.nextFloat() * 0.2F, this.random.nextFloat() * 0.2F);
		}
		
	}
	
	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;
		
		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		@Override
		public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
			return new HardcoreParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
		}
	}
}
