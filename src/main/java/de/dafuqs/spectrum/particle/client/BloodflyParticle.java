package de.dafuqs.spectrum.particle.client;

import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.util.*;

public class BloodflyParticle extends TextureSheetParticle {
	
	private double lastVelX, lastVelZ;
	private int switchTicks = 10;
	private float r, g, b;
	
	protected BloodflyParticle(ClientLevel clientWorld, double d, double e, double f, double velocityX, double velocityY, double velocityZ, float scaleMultiplier, SpriteSet spriteProvider) {
		super(clientWorld, d, e, f, velocityX, velocityY, velocityZ);
		this.setSpriteFromAge(spriteProvider);
		this.xd = random.nextFloat() * 0.05F - 0.025F;
		this.yd = 0;
		this.zd = random.nextFloat() * 0.05F - 0.025F;
		var random = clientWorld.getRandom();
		this.gravity = random.nextFloat() * 0.04F - 0.02F;
		
		this.hasPhysics = true;
		this.lifetime = 60 + random.nextInt(20);
		
		var dist = random.nextFloat();
		if (dist < 0.725F) {
			r = 255;
			g = Mth.lerpInt(random.nextFloat(), 110, 175);
			b = Mth.lerpInt(random.nextFloat(), 60, 100);
		} else if (dist < 0.95F) {
			r = 170;
			g = Mth.lerpInt(random.nextFloat(), 200, 255);
			b = Mth.lerpInt(random.nextFloat(), 235, 255);
		} else {
			r = 255;
			g = 245;
			b = Mth.lerpInt(random.nextFloat(), 235, 250);
			
		}
		
		r /= 255F;
		g /= 255F;
		b /= 255F;
		
		this.quadSize = 0.01F + random.nextFloat() * 0.325F;
		quadSize *= scaleMultiplier;
		setColor(r, g, b);
		setAlpha(0F);
	}
	
	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		
		if (this.age++ >= this.lifetime) {
			this.remove();
			return;
		}
		
		if (switchTicks < 10)
			switchTicks++;
		
		var water = !this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z)).isEmpty();
		
		if (age % 11 == 0 && random.nextBoolean()) {
			switchTicks = 0;
			gravity = random.nextFloat() * 0.04F - 0.02F;
			lastVelX = xd;
			lastVelZ = zd;
			xd = random.nextFloat() * 0.05F - 0.025F;
			zd = random.nextFloat() * 0.05F - 0.025F;
		}
		
		var flutter = Math.sin(age / 8F) / 35F;
		
		var curVelX = Mth.lerp(switchTicks / 10F, lastVelX, xd);
		var curVelZ = Mth.lerp(switchTicks / 10F, lastVelZ, zd);
		
		if (this.onGround || water) {
			curVelX *= 0.7F;
			curVelZ *= 0.7F;
			gravity = random.nextFloat() * 0.03F;
		}
		
		this.yd -= 0.04 * (double) this.gravity;
		this.move(curVelX, this.yd + flutter, curVelZ);
		if (this.speedUpWhenYMotionIsBlocked && this.y == this.yo) {
			this.xd *= 1.1;
			this.zd *= 1.1;
		}
		
		adjustAlpha(water);
	}
	
	private void adjustAlpha(boolean water) {
		if (age <= 15) {
			alpha = Mth.clamp(age / 15F, 0, 1F);
			return;
		}
		
		var ageFade = Mth.clamp(Math.min(lifetime - age, 20) / 20F, 0, 1F);
		
		if (ageFade < 1) {
			alpha = Math.min(alpha, ageFade);
		} else if (onGround) {
			alpha = Mth.clamp(alpha - 0.02F, 0, 1F);
		} else if (water) {
			alpha = Mth.clamp(alpha - 0.02F, 0.5F, 1F);
		} else {
			alpha = Mth.clamp(alpha + 0.05F, 0F, 1F);
		}
		
		if (alpha < 0.01F) {
			remove();
		}
	}
	
	@Override
	public int getLightColor(float tint) {
		return LightTexture.FULL_BRIGHT;
	}
	
	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;
		
		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
			return new BloodflyParticle(clientWorld, d, e, f, 0.0, 0.0, 0.0, 1.0F, this.spriteProvider);
		}
	}
}
