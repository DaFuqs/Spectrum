package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class MagicProjectileEntity extends ProjectileEntity {

	public MagicProjectileEntity(EntityType<? extends MagicProjectileEntity> type, World world) {
		super(type, world);
	}

	public MagicProjectileEntity(EntityType<? extends MagicProjectileEntity> type, double x, double y, double z, World world) {
		this(type, world);
		this.refreshPositionAndAngles(x, y, z, this.getYaw(), this.getPitch());
		this.refreshPosition();
	}

	@Override
	public void tick() {
		super.tick();

		boolean noClip = this.isNoClip();
		Vec3d thisVelocity = this.getVelocity();
		if (this.prevPitch == 0.0F && this.prevYaw == 0.0F) {
			double d = thisVelocity.horizontalLength();
			this.setYaw((float) (MathHelper.atan2(thisVelocity.x, thisVelocity.z) * 57.2957763671875D));
			this.setPitch((float) (MathHelper.atan2(thisVelocity.y, d) * 57.2957763671875D));
			this.prevYaw = this.getYaw();
			this.prevPitch = this.getPitch();
		}

		this.age();

		Vec3d vec3d2;
		Vec3d thisPos = this.getPos();
		vec3d2 = thisPos.add(thisVelocity);
		HitResult hitResult = this.world.raycast(new RaycastContext(thisPos, vec3d2, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
		if ((hitResult).getType() != HitResult.Type.MISS) {
			vec3d2 = (hitResult).getPos();
		}

		if (!this.isRemoved()) {
			EntityHitResult entityHitResult = this.getEntityCollision(thisPos, vec3d2);
			if (entityHitResult != null) {
				hitResult = entityHitResult;
			}

			if (hitResult.getType() == HitResult.Type.ENTITY) {
				Entity entity = ((EntityHitResult) hitResult).getEntity();
				Entity entity2 = this.getOwner();
				if (entity instanceof PlayerEntity && entity2 instanceof PlayerEntity && !((PlayerEntity) entity2).shouldDamagePlayer((PlayerEntity) entity)) {
					hitResult = null;
				}
			}

			if (hitResult != null && !noClip) {
				this.onCollision(hitResult);
				this.velocityDirty = true;
			}
		}

		thisVelocity = this.getVelocity();
		double velocityX = thisVelocity.x;
		double velocityY = thisVelocity.y;
		double velocityZ = thisVelocity.z;

		double h = this.getX() + velocityX;
		double j = this.getY() + velocityY;
		double k = this.getZ() + velocityZ;
		double l = thisVelocity.horizontalLength();
		if (noClip) {
			this.setYaw((float) (MathHelper.atan2(-velocityX, -velocityZ) * 57.2957763671875D));
		} else {
			this.setYaw((float) (MathHelper.atan2(velocityX, velocityZ) * 57.2957763671875D));
		}

		this.setPitch((float) (MathHelper.atan2(velocityY, l) * 57.2957763671875D));
		this.setPitch(updateRotation(this.prevPitch, this.getPitch()));
		this.setYaw(updateRotation(this.prevYaw, this.getYaw()));

		if (this.isTouchingWater()) {
			for (int o = 0; o < 4; ++o) {
				this.world.addParticle(ParticleTypes.BUBBLE, h - velocityX * 0.25D, j - velocityY * 0.25D, k - velocityZ * 0.25D, velocityX, velocityY, velocityZ);
			}
		}

		this.setPosition(h, j, k);
		this.checkBlockCollision();
	}

	protected void age() {
		++this.age;
		if (this.age >= 200) {
			this.discard();
		}

	}

	public boolean isNoClip() {
		if (!this.world.isClient) {
			return this.noClip;
		} else {
			return true;
		}
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		this.playSound(this.getHitSound(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
		this.discard();
	}

	protected SoundEvent getHitSound() {
		return SpectrumSoundEvents.INK_PROJECTILE_HIT;
	}

	@Nullable
	protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
		return ProjectileUtil.getEntityCollision(this.world, this, currentPosition, nextPosition, this.getBoundingBox().stretch(this.getVelocity()).expand(1.0D), this::canHit);
	}

}
