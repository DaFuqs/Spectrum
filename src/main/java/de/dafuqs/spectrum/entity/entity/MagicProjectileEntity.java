package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.*;
import net.minecraft.core.particles.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.*;

public abstract class MagicProjectileEntity extends Projectile {
	
	private static final double RAD_TO_DEG_CONSTANT = 180F / Math.PI;
	
	public MagicProjectileEntity(EntityType<? extends MagicProjectileEntity> type, Level world) {
		super(type, world);
	}
	
	public MagicProjectileEntity(EntityType<? extends MagicProjectileEntity> type, double x, double y, double z, Level world) {
		this(type, world);
		this.moveTo(x, y, z, this.getYRot(), this.getXRot());
		this.reapplyPosition();
	}
	
	public abstract void spawnImpactParticles();
	
	@Override
	public void tick() {
		super.tick();
		
		if (this.tickCount == 1 && level().isClientSide()) {
			MagicProjectileSoundInstance.startSoundInstance(this);
		}
		
		boolean noClip = this.isNoClip();
		Vec3 thisVelocity = this.getDeltaMovement();
		if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
			double d = thisVelocity.horizontalDistance();
			this.setYRot((float) (Mth.atan2(thisVelocity.x, thisVelocity.z) * RAD_TO_DEG_CONSTANT));
			this.setXRot((float) (Mth.atan2(thisVelocity.y, d) * RAD_TO_DEG_CONSTANT));
			this.yRotO = this.getYRot();
			this.xRotO = this.getXRot();
		}
		
		this.age();
		
		Vec3 vec3d2;
		Vec3 thisPos = this.position();
		vec3d2 = thisPos.add(thisVelocity);
		HitResult hitResult = this.level().clip(new ClipContext(thisPos, vec3d2, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
		if ((hitResult).getType() != HitResult.Type.MISS) {
			vec3d2 = (hitResult).getLocation();
		}
		
		if (!this.isRemoved()) {
			EntityHitResult entityHitResult = this.getEntityCollision(thisPos, vec3d2);
			if (entityHitResult != null) {
				hitResult = entityHitResult;
			}
			
			if (hitResult.getType() == HitResult.Type.ENTITY) {
				Entity entity = ((EntityHitResult) hitResult).getEntity();
				Entity entity2 = this.getOwner();
				if (entity instanceof Player && entity2 instanceof Player && !((Player) entity2).canHarmPlayer((Player) entity)) {
					hitResult = null;
				}
			}
			
			if (hitResult != null && !noClip) {
				this.onHit(hitResult);
				this.hasImpulse = true;
			}
		}
		
		thisVelocity = this.getDeltaMovement();
		double velocityX = thisVelocity.x;
		double velocityY = thisVelocity.y;
		double velocityZ = thisVelocity.z;
		
		double h = this.getX() + velocityX;
		double j = this.getY() + velocityY;
		double k = this.getZ() + velocityZ;
		double l = thisVelocity.horizontalDistance();
		if (noClip) {
			this.setYRot((float) (Mth.atan2(-velocityX, -velocityZ) * RAD_TO_DEG_CONSTANT));
		} else {
			this.setYRot((float) (Mth.atan2(velocityX, velocityZ) * RAD_TO_DEG_CONSTANT));
		}
		
		this.setXRot((float) (Mth.atan2(velocityY, l) * RAD_TO_DEG_CONSTANT));
		this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
		this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
		
		if (this.isInWater()) {
			for (int o = 0; o < 4; ++o) {
				this.level().addParticle(ParticleTypes.BUBBLE, h - velocityX * 0.25D, j - velocityY * 0.25D, k - velocityZ * 0.25D, velocityX, velocityY, velocityZ);
			}
		}
		
		this.setPos(h, j, k);
		this.checkInsideBlocks();
	}
	
	protected void age() {
		++this.tickCount;
		if (this.tickCount >= 200) {
			this.discard();
		}
		
	}
	
	public boolean isNoClip() {
		if (!this.level().isClientSide()) {
			return this.noPhysics;
		} else {
			return true;
		}
	}
	
	protected SoundEvent getHitSound() {
		return SpectrumSoundEvents.INK_PROJECTILE_HIT;
	}

	protected @Nullable EntityHitResult getEntityCollision(Vec3 currentPosition, Vec3 nextPosition) {
		return ProjectileUtil.getEntityHitResult(this.level(), this, currentPosition, nextPosition, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
	}
	
	public abstract InkColor getInkColor();
	
}
