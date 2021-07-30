package de.dafuqs.spectrum.particle.client;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

import java.util.Random;

public class ShootingStarParticle extends SpriteBillboardParticle {

	protected ShootingStarParticle(ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(clientWorld, x, y - 0.125D, z, velocityX, velocityY, velocityZ);
		this.collidesWithWorld = true;

		this.setBoundingBoxSpacing(0.01F, 0.01F);
		this.scale *= this.random.nextFloat() * 0.4F + 0.7F;
		this.maxAge = 120;
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public Particle createParticle(DefaultParticleType parameters, ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			Random random = clientWorld.getRandom();

			ShootingStarParticle particle = new ShootingStarParticle(clientWorld, x, y, z, velocityX * random.nextDouble(), (random.nextDouble() - 0.5) * 0.05, velocityZ * random.nextDouble());
			particle.setSprite(this.spriteProvider);
			return particle;
		}
	}

	@Override
	protected int getBrightness(float tint) {
		return 15728880;
	}

}