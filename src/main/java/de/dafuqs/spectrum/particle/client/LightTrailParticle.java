package de.dafuqs.spectrum.particle.client;

import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.*;
import net.minecraft.util.*;
import net.neoforged.api.distmarker.*;

public class LightTrailParticle extends SimpleAnimatedParticle {
	protected LightTrailParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteSet spriteProvider) {
		super(world, x, y, z, spriteProvider, 0);
		this.xd = velocityX;
		this.yd = velocityY;
		this.zd = velocityZ;
		this.quadSize = 0.2F;
		this.lifetime = 25;
		this.setSpriteFromAge(spriteProvider);
		setAlpha(0.8f);
		setFadeColor(0xa8baff);
	}
	
	@Override
	public void tick() {
		super.tick();
		var fadeProgress = Mth.clamp((age + Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false)) / lifetime, 0, 1);
		setAlpha(Mth.lerp(fadeProgress, 0.8F, 0F));
		quadSize = Mth.lerp(fadeProgress, 0.2F, 0.1F);
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;
		
		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		@Override
		public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
			return new LightTrailParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
		}
	}
}
