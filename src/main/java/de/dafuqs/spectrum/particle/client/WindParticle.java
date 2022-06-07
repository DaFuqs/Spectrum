package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.blocks.spirit_sallow.WindStrength;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class WindParticle extends SpriteBillboardParticle {
	
	private static final WindStrength wind = new WindStrength();
	
	protected WindParticle(ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(clientWorld, x, y - 0.125D, z, velocityX, velocityY, velocityZ);
		
		this.collidesWithWorld = true;
		
		this.setBoundingBoxSpacing(0.01F, 0.01F);
		this.scale *= this.random.nextFloat() * 0.4F + 0.7F;
		this.maxAge = 120;
	}
	
	@Override
	public void tick() {
		super.tick();
		
		Vec3d windStrength = wind.getWindStrength(world.getTime());
		this.velocityX += windStrength.getX() * 0.004;
		this.velocityY += windStrength.getY() * 0.001;
		this.velocityZ += windStrength.getZ() * 0.004;
	}
	
	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}
	
	@Override
	protected int getBrightness(float tint) {
		return 15728880;
	}
	
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;
		
		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		@Override
		public Particle createParticle(DefaultParticleType parameters, ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			Random random = clientWorld.getRandom();
			
			WindParticle particle = new WindParticle(clientWorld, x, y, z, velocityX * random.nextDouble(), (random.nextDouble() - 0.5) * 0.05, velocityZ * random.nextDouble());
			particle.setSprite(this.spriteProvider);
			return particle;
		}
	}
	
}