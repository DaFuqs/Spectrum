package de.dafuqs.spectrum.particle.client;

import net.fabricmc.api.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.*;
import net.minecraft.client.world.*;
import net.minecraft.particle.*;
import net.minecraft.util.math.*;

@Environment(EnvType.CLIENT)
public class LitParticle extends AbstractSlowingParticle {
	
	protected LitParticle(ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
		this.scale = (random.nextFloat() * 0.25F + 0.325F) / 4F;
		this.maxAge = (int) Math.round(random.nextTriangular(25, 15));
		this.alpha = 0;
	}

	@Override
	public void tick() {
		adjustAlpha();
		super.tick();
	}

	private void adjustAlpha() {
		if (age <= 3) {
			alpha = MathHelper.clamp(age / 3F, 0, 1F);
			return;
		}

		var fadeMarker = maxAge / 5 * 2;
		alpha = MathHelper.clamp(Math.min(maxAge - age, fadeMarker) / (float) fadeMarker, 0, 1F);

		if (alpha < 0.01F) {
			markDead();
		}
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
			LitParticle particle = new LitParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
			particle.setSprite(this.spriteProvider);
			return particle;
		}
	}
	
}