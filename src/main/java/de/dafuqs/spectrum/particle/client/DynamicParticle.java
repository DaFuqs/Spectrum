package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.mixin.client.particle.*;
import net.minecraft.client.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.*;
import org.jetbrains.annotations.*;

@Environment(EnvType.CLIENT)
public class DynamicParticle extends SpriteBillboardParticle {
	
	protected boolean glowInTheDark;
	
	public DynamicParticle(ClientWorld clientWorld, double d, double e, double f, double velocityX, double velocityY, double velocityZ) {
		super(clientWorld, d, e, f, velocityX, velocityY, velocityZ);
		// Override the default random particle velocities again.
		// Not performant, but super() has to be called here :/
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.velocityZ = velocityZ;
		this.glowInTheDark = false;
	}
	
	public int getBrightness(float tint) {
		if (glowInTheDark) {
			return 16777215; // #FFFFFF
		} else {
			return super.getBrightness(tint);
		}
	}
	
	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}
	
	public void apply(@NotNull DynamicParticleEffect effect) {
		this.setSprite(sprite);
		this.setMaxAge(effect.lifetimeTicks);
		this.scale(effect.scale);
		this.setColor(effect.color.getX(), effect.color.getY(), effect.color.getZ());
		this.gravityStrength = effect.gravity;
		this.collidesWithWorld = effect.collisions;
		this.glowInTheDark = effect.glowInTheDark;
	}
	
	public static class Factory<P extends DynamicParticleEffect> implements ParticleFactory<P> {
		
		private final SpriteProvider spriteProvider;
		
		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		@Override
		public Particle createParticle(P parameters, ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			DynamicParticle particle = new DynamicParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
			
			SpriteProvider dynamicProvider = ((ParticleManagerAccessor) MinecraftClient.getInstance().particleManager).getSpriteAwareFactories().get(parameters.particleTypeIdentifier);
			if (dynamicProvider == null) {
				SpectrumCommon.logError("Trying to use a non-existent sprite provider for particle spawner particle: " + parameters.particleTypeIdentifier);
				particle.setSprite(spriteProvider);
			} else {
				particle.setSprite(dynamicProvider);
			}
			
			particle.apply(parameters);
			return particle;
		}
		
	}
	
}