package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.particle.effect.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.world.level.material.*;
import net.neoforged.api.distmarker.*;
import org.joml.*;

@OnlyIn(Dist.CLIENT)
public class ColoredBlockLeakParticle extends DripParticle {
	
	public ColoredBlockLeakParticle(ClientLevel world, double x, double y, double z, Fluid fluid) {
		super(world, x, y, z, fluid);
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class Factory implements ParticleProvider<ColoredFallingSporeBlossomParticleEffect> {
		
		private final SpriteSet spriteProvider;
		
		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		@Override
		public TextureSheetParticle createParticle(ColoredFallingSporeBlossomParticleEffect parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			Vector3f color = parameters.getColor();
			DripParticle particle = new DripParticle.FallingParticle(world, x, y, z, Fluids.EMPTY);
			particle.gravity = 0.005F;
			particle.pickSprite(this.spriteProvider);
			particle.setColor(color.x, color.y, color.z);
			return particle;
		}
	}
	
}
