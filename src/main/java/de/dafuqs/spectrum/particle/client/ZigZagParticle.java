package de.dafuqs.spectrum.particle.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;

public class ZigZagParticle extends SpriteBillboardParticle {
	
	protected ZigZagParticle(ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
		
		this.gravityStrength = 0.0F;
		this.field_28787 = true;
		this.scale *= 0.75F;
		this.collidesWithWorld = false;
	}
	
	@Override
	public void tick() {
		super.tick();
		
		// randomize the current velocity for sharp turns
		if (age % 8 == 0) {
			//setVelocity(0.06 - random.nextFloat() * 0.12, 0.06 - random.nextFloat() * 0.12, 0.06 - random.nextFloat() * 0.12);
			
			switch (world.random.nextInt(6)) {
				case 1: {
					setVelocity(velocityX, velocityZ, velocityY);
				}
				case 2: {
					setVelocity(velocityZ, velocityY, velocityX);
				}
				case 3: {
					setVelocity(velocityZ, velocityX, velocityY);
				}
				case 4: {
					setVelocity(velocityY, velocityX, velocityZ);
				}
				default: {
					setVelocity(velocityY, velocityZ, velocityX);
				}
			}
		}
	}
	
	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	public int getBrightness(float tint) {
		float f = ((float) this.age + tint) / (float) this.maxAge;
		f = MathHelper.clamp(f, 0.0F, 1.0F);
		int i = super.getBrightness(tint);
		int j = i & 255;
		int k = i >> 16 & 255;
		j += (int) (f * 15.0F * 16.0F);
		if (j > 240) {
			j = 240;
		}
		
		return j | k << 16;
	}
	
	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		
		private final SpriteProvider spriteProvider;
		
		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			ZigZagParticle craftingParticle = new ZigZagParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
			craftingParticle.setMaxAge((int) (8.0D / (clientWorld.random.nextDouble() * 0.8D + 0.2D)));
			craftingParticle.setSprite(this.spriteProvider);
			return craftingParticle;
		}
	}
	
}
