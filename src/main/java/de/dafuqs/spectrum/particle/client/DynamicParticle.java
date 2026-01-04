package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.mixin.client.accessors.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.*;
import net.minecraft.core.registries.*;
import org.jetbrains.annotations.*;


public class DynamicParticle extends TextureSheetParticle {
	
	protected boolean glowInTheDark = false;
	
	public DynamicParticle(ClientLevel clientWorld, double d, double e, double f, double velocityX, double velocityY, double velocityZ) {
		super(clientWorld, d, e, f, velocityX, velocityY, velocityZ);
		// Override the default random particle velocities again.
		// Not performant, but super() has to be called here :/
		this.xd = velocityX;
		this.yd = velocityY;
		this.zd = velocityZ;
	}
	
	@Override
	public int getLightColor(float tint) {
		if (glowInTheDark) {
			return LightTexture.FULL_BRIGHT;
		} else {
			return super.getLightColor(tint);
		}
	}
	
	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}
	
	public void apply(@NotNull DynamicParticleEffect effect) {
		this.setSprite(sprite);
		this.setLifetime(effect.lifetimeTicks());
		this.scale(effect.scale());
		this.setColor(effect.color().x(), effect.color().y(), effect.color().z());
		this.gravity = effect.gravity();
		this.hasPhysics = effect.collisions();
		this.glowInTheDark = effect.glowing();
	}
	
	public static class Factory<P extends DynamicParticleEffect> implements ParticleProvider<P> {
		
		private final SpriteSet spriteProvider;
		
		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		@Override
		public Particle createParticle(P parameters, ClientLevel clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			Minecraft client = Minecraft.getInstance();
			DynamicParticle particle = new DynamicParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
			
			var particleTypeIdentifier = BuiltInRegistries.PARTICLE_TYPE.getKey(parameters.particleType());
			SpriteSet dynamicProvider = ((ParticleManagerAccessor) client.particleEngine).getSpriteSets().get(particleTypeIdentifier);
			if (dynamicProvider == null) {
				SpectrumCommon.logError("Trying to use a non-existent sprite provider for particle spawner particle: " + particleTypeIdentifier);
				particle.pickSprite(spriteProvider);
			} else {
				particle.pickSprite(dynamicProvider);
			}
			
			particle.apply(parameters);
			return particle;
		}
		
	}
	
}
