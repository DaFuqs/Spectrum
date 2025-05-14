package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.particle.effect.*;
import net.fabricmc.api.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.util.*;
import net.minecraft.world.level.material.*;
import org.joml.*;

@Environment(EnvType.CLIENT)
public class ColoredBlockLeakParticle extends DripParticle {
	
	public ColoredBlockLeakParticle(ClientLevel world, double x, double y, double z, Fluid fluid) {
		super(world, x, y, z, fluid);
	}
	
	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleProvider<ColoredFallingSporeBlossomParticleEffect> {
		
		private final SpriteSet spriteProvider;
		
		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		@Override
		public TextureSheetParticle createParticle(ColoredFallingSporeBlossomParticleEffect parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			Vector3f color = parameters.getColor();
			int i = (int) (64.0F / Mth.randomBetween(world.getRandom(), 0.1F, 0.9F));
			DripParticle particle = new DripParticle.FallingParticle(world, x, y, z, Fluids.EMPTY, i);
			particle.gravity = 0.005F;
			particle.pickSprite(this.spriteProvider);
			particle.setColor(color.x, color.y, color.z);
			return particle;
		}
	}
	
}
