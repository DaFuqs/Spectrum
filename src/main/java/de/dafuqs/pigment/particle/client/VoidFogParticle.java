package de.dafuqs.pigment.particle.client;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

import java.util.Random;

public class VoidFogParticle extends SpriteBillboardParticle {

	protected VoidFogParticle(ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(clientWorld, x, y - 0.125D, z, velocityX, velocityY, velocityZ);
		this.collidesWithWorld = true;

		this.setBoundingBoxSpacing(0.01F, 0.01F);
		this.scale *= this.random.nextFloat() * 0.2F + 0.1F;
		this.maxAge = 120;

		this.velocityX = 0;
		this.velocityY = Math.random() * 0.05D + 0.05;
		this.velocityZ = 0;
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

			VoidFogParticle particle = new VoidFogParticle(clientWorld, x, y, z, 0, (random.nextDouble() - 0.5) * 0.05, 0);
			particle.setSprite(this.spriteProvider);
			return particle;
		}
	}

	@Override
	protected int getBrightness(float tint) {
		return 15728880;
	}

}