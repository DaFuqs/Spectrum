package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.deeper_down.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.util.*;
import net.minecraft.world.phys.*;
import net.neoforged.api.distmarker.*;

public class FallingAshParticle extends TextureSheetParticle {
	
	private static final Vec3 VERTICAL = new Vec3(0, 1, 0);
	private static final float GRAVITY = 0.15F;
	private static double targetVelocity = 0.215, ashScaleA = 20000, ashScaleB = 2200, ashScaleC = 200;
	private static Direction.Axis primaryAxis = Direction.Axis.X;
	private static Direction.Axis lastAxis = primaryAxis;
	private final float rotateFactor, lightness;
	private final int simInterval = SpectrumCommon.CONFIG.WindSimInterval, simOffset;
	private int slowTicks, axisTicks = 0;
	
	private static final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(); // to prevent us from having to create lots of BlockPos objects per (render) tick
	
	protected FallingAshParticle(ClientLevel clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteSet spriteProvider) {
		super(clientWorld, x, y, z);
		pickSprite(spriteProvider);
		this.xd = velocityX;
		this.yd = velocityY;
		this.zd = velocityZ;
		var random = clientWorld.getRandom();
		
		this.hasPhysics = true;
		this.gravity = GRAVITY;
		this.lifetime = 150 + random.nextInt(50);
		
		this.rotateFactor = ((float) Math.random() - 0.5F) * 0.002F;
		this.quadSize = (float) (0.06 + (random.nextDouble() / 14));
		this.lightness = random.nextFloat() * 0.6F + 0.4F;
		this.simOffset = random.nextInt(simInterval);
		setAlpha(0F);
	}
	
	@Override
	public void tick() {
		pos.set(x, y, z);
		
		var camPos = Minecraft.getInstance().getCameraEntity().position();
		var distance = Math.sqrt(camPos.distanceToSqr(x, y, z));
		if (distance > HowlingSpireEffects.getRenderRadius() - 1) {
			remove();
			return;
		}
		
		this.oRoll = this.roll;
		var water = !this.level.getFluidState(pos).isEmpty();
		var time = level.getGameTime() % 432000;
		
		if ((age + 2 < lifetime)
				&& level.getBiome(pos).is(SpectrumBiomes.HOWLING_SPIRES)) {
			age++;
		}
		
		if (lastAxis != primaryAxis) {
			lastAxis = primaryAxis;
			axisTicks = 0;
		}
		
		switch (primaryAxis) {
			case X -> {
				xd = Mth.clampedLerp(xd, adjustVelocity(xd, water), axisTicks / 20F);
				zd = Mth.clampedLerp(zd, getNonPrimaryVelocity(time), axisTicks / 20F);
			}
			case Z -> {
				zd = Mth.clampedLerp(zd, adjustVelocity(zd, water), axisTicks / 20F);
				xd = Mth.clampedLerp(xd, getNonPrimaryVelocity(time), axisTicks / 20F);
			}
		}
		
		if (axisTicks < 20) {
			axisTicks++;
		}
		
		if (Math.abs(xd) + Math.abs(zd) < 0.1) {
			if (slowTicks < 20)
				slowTicks++;
		} else {
			slowTicks = 0;
		}
		
		if (!this.onGround && !water) {
			this.roll += (float) (Math.PI * Math.sin(this.rotateFactor * this.age) / 2);
			
			if (verifySimConfig(time)) {
				adjustGravityForLift();
			}
		} else if (water) {
			this.yd /= 4;
			this.xd /= 4;
			this.zd /= 4;
			this.gravity = 0;
		} else {
			this.gravity = GRAVITY;
		}
		
		adjustAlpha(water);
		
		
		if (verifySimConfig(time) && Math.abs(xd) + Math.abs(zd) > 0.125) {
			applyAirflowTransforms();
		}
		super.tick();
	}
	
	private boolean verifySimConfig(long time) {
		return SpectrumCommon.CONFIG.WindSim && (time + simOffset) % simInterval == 0;
	}
	
	private void adjustGravityForLift() {
		var height = 0F;
		var groundFound = false;
		for (; height < 20; ++height) {
			pos.move(Direction.DOWN);
			if (!level.getFluidState(pos).isEmpty()) {
				gravity = 0F;
				return;
			} else if (level.getBlockState(pos).isFaceSturdy(level, pos, Direction.UP)) {
				groundFound = true;
				break;
			}
		}
		
		height += (float) (y - (int) y);
		
		if (!groundFound) {
			gravity = GRAVITY * 2F * (1F + (1 - lightness));
			return;
		}
		
		var heightFactor = height / 14F;
		
		if (height < 4) {
			gravity = GRAVITY - GRAVITY * (1F - heightFactor * 2) * 2 * lightness;
			return;
		}
		
		if (height > 14F) {
			gravity = GRAVITY * (1 - ((height - 14) / 7)) * 0.225F;
			return;
		}
		
		gravity = GRAVITY * heightFactor;
	}
	
	private void applyAirflowTransforms() {
		var velocity = new Vec3(xd, yd, zd);
		var direction = velocity.normalize();
		var movementNormal = direction.cross(VERTICAL);
		
		for (int i = 0; i <= 6; i++) {
			var deflection = -0.0125F * (1 - (i / 24F)) * lightness * simInterval;
			var shift = velocity.scale(i).add(x, y, z);
			var maxDist = 6 - i;
			for (int orthogonal = 1; orthogonal <= maxDist; orthogonal++) {
				var leftShift = movementNormal.scale(orthogonal).add(shift);
				var rightShift = movementNormal.scale(-orthogonal).add(shift);
				var leftPos = new BlockPos((int) leftShift.x, (int) leftShift.y, (int) leftShift.z);
				var rightPos = new BlockPos((int) rightShift.x, (int) rightShift.y, (int) rightShift.z);
				
				if (level.getBlockState(leftPos).isRedstoneConductor(level, leftPos)) {
					var collisionDirection = leftShift.subtract(x, y, z).normalize();
					xd += collisionDirection.x * deflection;
					zd += collisionDirection.z * deflection;
				}
				if (level.getBlockState(rightPos).isRedstoneConductor(level, rightPos)) {
					var collisionDirection = rightShift.subtract(x, y, z).normalize();
					xd += collisionDirection.x * deflection;
					zd += collisionDirection.z * deflection;
				}
			}
		}
	}
	
	private static double getNonPrimaryVelocity(long time) {
		var scale = Math.sin(time / ashScaleA + ashScaleB);
		scale = scale * (Math.cos(time / ashScaleB + ashScaleC) / 2);
		scale = scale * (Math.cos(time / ashScaleC + ashScaleA) / 4);
		scale *= 2;
		return scale;
	}
	
	private void adjustAlpha(boolean water) {
		if (age <= 20) {
			alpha = Mth.clamp(age / 20F, 0, 1F);
			return;
		}
		
		var ageFade = Mth.clamp(Math.min(lifetime - age, 40) / 40F, 0, 1F);
		
		if (ageFade < 1) {
			alpha = Math.min(alpha, ageFade);
		} else if (onGround || slowTicks == 20) {
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
	
	private double adjustVelocity(double velocity, boolean water) {
		if (water)
			return velocity / 1.5;
		
		if (velocity != targetVelocity && velocity >= targetVelocity - 0.15 && velocity <= targetVelocity + 0.15) {
			velocity = targetVelocity;
		} else if (velocity > targetVelocity) {
			velocity = velocity - 0.125;
		} else if (velocity < targetVelocity) {
			velocity = velocity + 0.0334;
		}
		
		return velocity;
	}
	
	public static void setTargetVelocity(double targetVelocity) {
		FallingAshParticle.targetVelocity = targetVelocity;
	}
	
	public static void setPrimaryAxis(Direction.Axis primaryAxis) {
		FallingAshParticle.primaryAxis = primaryAxis;
	}
	
	public static void setAshScaleA(double ashScaleA) {
		FallingAshParticle.ashScaleA = ashScaleA;
	}
	
	public static void setAshScaleB(double ashScaleB) {
		FallingAshParticle.ashScaleB = ashScaleB;
	}
	
	public static void setAshScaleC(double ashScaleC) {
		FallingAshParticle.ashScaleC = ashScaleC;
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
		
		@Override
		public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
			return new FallingAshParticle(clientWorld, d, e, f, g, h, i, spriteProvider);
		}
	}
}
