package de.dafuqs.spectrum.particle.client;

import net.fabricmc.api.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.*;
import net.minecraft.util.*;

public class ZigZagParticle extends TextureSheetParticle {
	
	protected ZigZagParticle(ClientLevel clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
		
		this.gravity = 0.0F;
		this.speedUpWhenYMotionIsBlocked = true;
		this.quadSize *= 0.75F;
		this.hasPhysics = false;
	}
	
	@Override
	public void tick() {
		super.tick();
		
		// randomize the current velocity for sharp turns
		if (age % 8 == 0) {
			//setVelocity(0.06 - random.nextFloat() * 0.12, 0.06 - random.nextFloat() * 0.12, 0.06 - random.nextFloat() * 0.12);
			
			switch (level.random.nextInt(6)) {
				case 1: {
					setParticleSpeed(xd, zd, yd);
				}
				case 2: {
					setParticleSpeed(zd, yd, xd);
				}
				case 3: {
					setParticleSpeed(zd, xd, yd);
				}
				case 4: {
					setParticleSpeed(yd, xd, zd);
				}
				default: {
					setParticleSpeed(yd, zd, xd);
				}
			}
		}
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
			ZigZagParticle craftingParticle = new ZigZagParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
			craftingParticle.setLifetime((int) (8.0D / (clientWorld.random.nextDouble() * 0.8D + 0.2D)));
			craftingParticle.pickSprite(this.spriteProvider);
			return craftingParticle;
		}
	}
	
}
