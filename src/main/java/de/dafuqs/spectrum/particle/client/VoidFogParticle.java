package de.dafuqs.spectrum.particle.client;

import net.fabricmc.api.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.*;
import net.minecraft.util.*;

@Environment(EnvType.CLIENT)
public class VoidFogParticle extends TextureSheetParticle {
	
	protected VoidFogParticle(ClientLevel clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
		this.hasPhysics = true;
		
		this.setSize(0.01F, 0.01F);
		this.quadSize *= this.random.nextFloat() * 0.2F + 0.1F;
		this.lifetime = 100 + (int) (this.random.nextFloat() * 20);
		
		this.xd = 0;
		this.yd = Math.random() * 0.05D + 0.05;
		this.zd = 0;
	}
	
	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}
	
	@Override
	protected int getLightColor(float tint) {
		return 0;
	}
	
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;
		
		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientLevel clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			RandomSource random = clientWorld.getRandom();
			
			VoidFogParticle particle = new VoidFogParticle(clientWorld, x, y, z, 0, (random.nextDouble() - 0.5) * 0.05, 0);
			particle.pickSprite(this.spriteProvider);
			return particle;
		}
	}
	
}
