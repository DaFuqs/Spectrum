package de.dafuqs.spectrum.particle.client;

import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.*;
import net.neoforged.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class LargePrimordialSmokeParticle extends TextureSheetParticle {
	
	LargePrimordialSmokeParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, boolean signal) {
		super(world, x, y, z);
		this.scale(3.0F);
		this.setSize(0.25F, 0.25F);
		if (signal) {
			this.lifetime = this.random.nextInt(50) + 280;
		} else {
			this.lifetime = this.random.nextInt(50) + 80;
		}
		
		this.gravity = 3.0E-6F;
		this.xd = velocityX;
		this.yd = velocityY + (double) (this.random.nextFloat() / 500.0F);
		this.zd = velocityZ;
	}
	
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ < this.lifetime && !(this.alpha <= 0.0F)) {
			this.xd += (this.random.nextFloat() / 5000.0F * (float) (this.random.nextBoolean() ? 1 : -1));
			this.zd += (this.random.nextFloat() / 5000.0F * (float) (this.random.nextBoolean() ? 1 : -1));
			this.yd -= this.gravity;
			this.move(this.xd, this.yd, this.zd);
			if (this.age >= this.lifetime - 60 && this.alpha > 0.01F) {
				this.alpha -= 0.015F;
			}
			
		} else {
			this.remove();
		}
	}
	
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class SignalSmokeFactory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;
		
		public SignalSmokeFactory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
			var campfireSmokeParticle = new LargePrimordialSmokeParticle(clientWorld, d, e, f, g, h, i, true);
			campfireSmokeParticle.setAlpha(0.95F);
			campfireSmokeParticle.pickSprite(this.spriteProvider);
			return campfireSmokeParticle;
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class CosySmokeFactory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;
		
		public CosySmokeFactory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
			var campfireSmokeParticle = new LargePrimordialSmokeParticle(clientWorld, d, e, f, g, h, i, false);
			campfireSmokeParticle.setAlpha(0.9F);
			campfireSmokeParticle.pickSprite(this.spriteProvider);
			return campfireSmokeParticle;
		}
	}
}
