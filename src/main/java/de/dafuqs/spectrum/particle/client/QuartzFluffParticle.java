package de.dafuqs.spectrum.particle.client;

import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.*;
import net.minecraft.util.*;

public class QuartzFluffParticle extends BaseAshSmokeParticle {
	
	protected QuartzFluffParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float scaleMultiplier, SpriteSet spriteProvider) {
		super(world, x, y, z, 0.0725F, -0.1F, 0.0725F, velocityX, velocityY, velocityZ, scaleMultiplier, spriteProvider, 1F, 0, 0.08F, false);
		alpha = 0;
		this.lifetime = 15 + world.getRandom().nextInt(16);
		this.quadSize = (0.25F + random.nextFloat() * 0.5F) * 0.25F;
		this.rCol = 1F;
		this.gCol = 0.975F;
		this.bCol = 0.9125F;
	}
	
	@Override
	public void tick() {
		super.tick();
		adjustAlpha();
	}
	
	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	private void adjustAlpha() {
		if (age <= 7) {
			alpha = Mth.clamp(age / 7F, 0, 1F);
			return;
		}
		
		var ageFade = Mth.clamp(Math.min(lifetime - age, 7) / 7F, 0, 1F);
		
		if (ageFade < 1) {
			alpha = Math.min(alpha, ageFade);
		} else if (onGround) {
			alpha = Mth.clamp(alpha - 0.02F, 0, 1F);
		} else {
			alpha = Mth.clamp(alpha + 0.05F, 0F, 1F);
		}
		
		if (alpha < 0.01F) {
			remove();
		}
	}
	
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;
		
		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
			return new QuartzFluffParticle(clientWorld, d, e, f, 0.0, 0.0, 0.0, 1.0F, this.spriteProvider);
		}
	}
}
