package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.particle.effect.*;
import net.fabricmc.api.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import org.jetbrains.annotations.*;
import org.joml.*;

@Environment(EnvType.CLIENT)
public class ColoredExplosionParticle extends HugeExplosionParticle {
	
	protected ColoredExplosionParticle(ClientLevel world, double x, double y, double z, double d, SpriteSet spriteProvider, float red, float green, float blue) {
		super(world, x, y, z, d, spriteProvider);
		
		this.rCol = red;
		this.gCol = green;
		this.bCol = blue;
	}
	
	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleProvider<ColoredExplosionParticleEffect> {
		
		private final SpriteSet spriteProvider;
		
		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		@Override
		public @Nullable Particle createParticle(ColoredExplosionParticleEffect parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			Vector3f color = parameters.getColor();
			return new ColoredExplosionParticle(world, x, y, z, velocityX, this.spriteProvider, color.x, color.y, color.z);
		}
	}
	
}
