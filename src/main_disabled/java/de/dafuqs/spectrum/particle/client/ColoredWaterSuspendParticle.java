package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.particle.effect.*;
import net.fabricmc.api.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.*;
import net.minecraft.util.*;
import org.joml.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class ColoredWaterSuspendParticle extends SuspendedParticle {
	
	public ColoredWaterSuspendParticle(ClientLevel world, SpriteSet spriteProvider, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(world, spriteProvider, x, y, z, velocityX, velocityY, velocityZ);
	}
	
	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleProvider<ColoredSporeBlossomAirParticleEffect> {
		
		private final SpriteSet spriteProvider;
		
		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		@Override
		public TextureSheetParticle createParticle(ColoredSporeBlossomAirParticleEffect parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			Vector3f color = parameters.getColor();
			
			SuspendedParticle particle = new ColoredWaterSuspendParticle(world, this.spriteProvider, x, y, z, 0.0, -0.800000011920929, 0.0) {
				public Optional<ParticleGroup> getParticleGroup() {
					return Optional.of(ParticleGroup.SPORE_BLOSSOM);
				}
			};
			particle.setLifetime(Mth.randomBetweenInclusive(world.random, 500, 1000));
			particle.gravity = 0.01F;
			particle.setColor(color.x, color.y, color.z);
			return particle;
		}
	}
	
}
