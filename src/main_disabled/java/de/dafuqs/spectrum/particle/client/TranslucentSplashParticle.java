package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.helpers.*;
import net.fabricmc.api.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;

@Environment(EnvType.CLIENT)

public class TranslucentSplashParticle extends WaterDropParticle {
	
	protected TranslucentSplashParticle(ClientLevel clientWorld, double d, double e, double f) {
		super(clientWorld, d, e, f);
		var waterColor = SpectrumColorHelper.colorIntToVec(BiomeColors.getAverageWaterColor(level, BlockPos.containing(x, y, z)));
		rCol = waterColor.x;
		gCol = waterColor.y;
		bCol = waterColor.z;
		quadSize *= 0.667F;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;
		
		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
			WaterDropParticle rainSplashParticle = new TranslucentSplashParticle(clientWorld, d, e, f);
			rainSplashParticle.pickSprite(this.spriteProvider);
			return rainSplashParticle;
		}
	}
}
