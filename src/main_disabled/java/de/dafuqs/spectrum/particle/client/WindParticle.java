package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.blocks.spirit_sallow.*;
import net.fabricmc.api.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.*;
import net.minecraft.util.*;
import net.minecraft.world.phys.*;

@Environment(EnvType.CLIENT)
public class WindParticle extends TextureSheetParticle {
	
	private static final WindStrength wind = new WindStrength();
	
	protected WindParticle(ClientLevel clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(clientWorld, x, y - 0.125D, z, velocityX, velocityY, velocityZ);
		
		this.hasPhysics = true;
		
		this.setSize(0.01F, 0.01F);
		this.quadSize *= this.random.nextFloat() * 0.4F + 0.7F;
		this.lifetime = 120;
	}
	
	@Override
	public void tick() {
		super.tick();
		
		Vec3 windStrength = wind.getWindStrength(level);
		this.xd += windStrength.x() * 0.004;
		this.yd += windStrength.y() * 0.001;
		this.zd += windStrength.z() * 0.004;
	}
	
	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}
	
	@Override
	protected int getLightColor(float tint) {
		return 15728880;
	}
	
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;
		
		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientLevel clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			RandomSource random = clientWorld.getRandom();
			
			WindParticle particle = new WindParticle(clientWorld, x, y, z, velocityX * random.nextDouble(), (random.nextDouble() - 0.5) * 0.05, velocityZ * random.nextDouble());
			particle.pickSprite(this.spriteProvider);
			return particle;
		}
	}
	
}
