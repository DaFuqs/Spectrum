package de.dafuqs.spectrum.particle.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class LitParticle extends AbstractSlowingParticle {
	
	protected LitParticle(ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
		this.scale *= this.random.nextFloat() * 0.6F + 0.6F;
	}
	
	public int getBrightness(float tint) {
		return 16777215; // #FFFFFF
	}
	
	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_LIT;
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