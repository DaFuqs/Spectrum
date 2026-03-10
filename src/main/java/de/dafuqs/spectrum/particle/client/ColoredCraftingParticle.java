package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.particle.effect.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;
import org.joml.*;


public class ColoredCraftingParticle extends TextureSheetParticle {
	
	protected ColoredCraftingParticle(ClientLevel clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float red, float green, float blue) {
		super(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
		this.gravity = 0.0F;
		this.speedUpWhenYMotionIsBlocked = true;
		this.quadSize *= 0.75F;
		this.hasPhysics = false;
		
		this.rCol = red;
		this.gCol = green;
		this.bCol = blue;
		
		this.xd = velocityX;
		this.yd = velocityY;
		this.zd = velocityZ;
	}
	
	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	@Override
	public int getLightColor(float tint) {
		float f = ((float) this.age + tint) / (float) this.lifetime;
		f = Mth.clamp(f, 0.0F, 1.0F);
		int i = super.getLightColor(tint);
		int j = i & 255;
		int k = i >> 16 & 255;
		j += (int) (f * 15.0F * 16.0F);
		if (j > 240) {
			j = 240;
		}
		
		return j | k << 16;
	}
	
	
	public static class Factory implements ParticleProvider<ColoredCraftingParticleEffect> {
		
		private final SpriteSet spriteProvider;
		
		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		@Override
		public @Nullable Particle createParticle(ColoredCraftingParticleEffect parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			Vector3f color = parameters.getColor();
			ColoredCraftingParticle coloredCraftingParticle = new ColoredCraftingParticle(world, x, y, z, velocityX, velocityY, velocityZ, color.x, color.y, color.z);
			coloredCraftingParticle.setLifetime((int) (8.0D / (world.random.nextDouble() * 0.8D + 0.2D)));
			coloredCraftingParticle.pickSprite(this.spriteProvider);
			return coloredCraftingParticle;
		}
	}
	
}
