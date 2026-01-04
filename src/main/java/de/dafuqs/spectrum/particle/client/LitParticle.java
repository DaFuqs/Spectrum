package de.dafuqs.spectrum.particle.client;

import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.*;
import net.minecraft.core.particles.*;
import net.minecraft.util.*;


public class LitParticle extends RisingParticle {
	
	protected LitParticle(ClientLevel clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
		this.quadSize = (random.nextFloat() * 0.25F + 0.325F) / 4F;
		this.lifetime = (int) Math.round(random.triangle(25, 15));
		this.alpha = 0;
	}
	
	@Override
	public void tick() {
		adjustAlpha();
		super.tick();
	}
	
	private void adjustAlpha() {
		if (age <= 3) {
			alpha = Mth.clamp(age / 3F, 0, 1F);
			return;
		}
		
		var fadeMarker = lifetime / 5 * 2;
		alpha = Mth.clamp(Math.min(lifetime - age, fadeMarker) / (float) fadeMarker, 0, 1F);
		
		if (alpha < 0.01F) {
			remove();
		}
	}
	
	@Override
	public int getLightColor(float tint) {
		return LightTexture.FULL_BRIGHT;
	}
	
	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		
		private final SpriteSet spriteProvider;
		
		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientLevel clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			LitParticle particle = new LitParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
			particle.pickSprite(this.spriteProvider);
			return particle;
		}
	}
	
}
