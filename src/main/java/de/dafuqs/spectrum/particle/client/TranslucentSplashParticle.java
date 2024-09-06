package de.dafuqs.spectrum.particle.client;

import net.fabricmc.api.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.*;
import net.minecraft.particle.*;

@Environment(EnvType.CLIENT)

public class TranslucentSplashParticle extends RainSplashParticle {

	protected TranslucentSplashParticle(ClientWorld clientWorld, double d, double e, double f) {
		super(clientWorld, d, e, f);
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

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			RainSplashParticle rainSplashParticle = new TranslucentSplashParticle(clientWorld, d, e, f);
			rainSplashParticle.setSprite(this.spriteProvider);
			return rainSplashParticle;
		}
	}
}
