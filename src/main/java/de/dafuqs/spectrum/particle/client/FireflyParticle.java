package de.dafuqs.spectrum.particle.client;

import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.util.*;
import net.neoforged.api.distmarker.*;

public class FireflyParticle extends TextureSheetParticle {
	
	private double lastVelX, lastVelZ;
	private int switchTicks = 10, blinkTicks = 11;
	private float r, g, b;
	
	protected FireflyParticle(ClientLevel clientWorld, double d, double e, double f, double velocityX, double velocityY, double velocityZ, float scaleMultiplier, SpriteSet spriteProvider) {
		super(clientWorld, d, e, f, velocityX, velocityY, velocityZ);
		this.setSpriteFromAge(spriteProvider);
		this.xd = random.nextFloat() * 0.1F - 0.05F;
		this.yd = 0;
		this.zd = random.nextFloat() * 0.1F - 0.05F;
		var random = clientWorld.getRandom();
		this.gravity = random.nextFloat() * 0.075F - 0.0375F;
		
		this.hasPhysics = true;
		this.lifetime = 40 + random.nextInt(10);
		
		r = 255;
		g = 232;
		b = 173;
		var edit = random.nextInt(3);
		
		if (edit == 0) {
			r = Mth.lerpInt(random.nextFloat(), 255, 190);
			g *= 0.9F;
			b *= 0.9F;
		} else if (edit == 1) {
			r *= 0.9F;
			g = Mth.lerpInt(random.nextFloat(), 232, 203);
			b *= 0.9F;
		} else {
			r *= 0.9F;
			g *= 0.9F;
			b = Mth.lerpInt(random.nextFloat(), 232, 230);
		}
		
		r /= 255F;
		g /= 255F;
		b /= 255F;
		
		this.quadSize = 0.025F + random.nextFloat() * 0.375F;
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
		
		if (blinkTicks <= 10) {
			var delta = Math.abs(blinkTicks) / 10F;
			
			setColor(
					(Mth.lerp(delta, 0.25F * r, r)),
					(Mth.lerp(delta, 0.25F * g, g)),
					(Mth.lerp(delta, 0.25F * b, b))
			);
			
			blinkTicks++;
			alpha = Math.min(1F, delta + 0.5F);
		}
		
		if (switchTicks < 10)
			switchTicks++;
		
		var water = !this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z)).isEmpty();
		
		if (age % 10 == 0 && random.nextBoolean()) {
			switchTicks = 0;
			gravity = random.nextFloat() * 0.075F - 0.0375F;
			lastVelX = xd;
			lastVelZ = zd;
			xd = random.nextFloat() * 0.1F - 0.05F;
			zd = random.nextFloat() * 0.1F - 0.05F;
		}
		
		if (age % 13 == 0 && random.nextFloat() < 0.334F) {
			blinkTicks = -10;
		}
		
		var flutter = Math.sin(age / 17F) / 18F;
		
		var curVelX = Mth.lerp(switchTicks / 10F, lastVelX, xd);
		var curVelZ = Mth.lerp(switchTicks / 10F, lastVelZ, zd);
		
		if (this.onGround || water) {
			curVelX *= 0.7F;
			curVelZ *= 0.7F;
			gravity = random.nextFloat() * 0.1334F;
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
		if (age <= 5) {
			alpha = Mth.clamp(age / 5F, 0, 1F);
			return;
		}
		
		var ageFade = Mth.clamp(Math.min(lifetime - age, 5) / 5F, 0, 1F);
		
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
	
	@OnlyIn(Dist.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;
		
		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
			return new FireflyParticle(clientWorld, d, e, f, 0.0, 0.0, 0.0, 1.0F, this.spriteProvider);
		}
	}
}
