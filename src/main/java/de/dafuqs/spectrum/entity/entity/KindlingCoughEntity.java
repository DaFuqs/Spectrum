package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class KindlingCoughEntity extends ProjectileEntity {
	
	protected static final float DAMAGE = 10.0F;
	protected static final int FIRE_TICKS_ON_HIT = 30;
	
	public KindlingCoughEntity(EntityType<? extends KindlingCoughEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public KindlingCoughEntity(World world, LivingEntity owner) {
		this(SpectrumEntityTypes.KINDLING_COUGH, world);
		this.setOwner(owner);
		this.setPosition(owner.getX() - (owner.getWidth() + 1.0F) * 0.5 * MathHelper.sin(owner.bodyYaw * 0.017453292F), owner.getEyeY() - 0.1, owner.getZ() + (owner.getWidth() + 1.0F) * 0.5 * (double) MathHelper.cos(owner.bodyYaw * 0.017453292F));
	}
	
	@Override
	public void tick() {
		super.tick();
		Vec3d vec3d = this.getVelocity();
		HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
		this.onCollision(hitResult);
		double d = this.getX() + vec3d.x;
		double e = this.getY() + vec3d.y;
		double f = this.getZ() + vec3d.z;
		this.updateRotation();
		if (this.getWorld().getStatesInBox(this.getBoundingBox()).noneMatch(AbstractBlock.AbstractBlockState::isAir)) {
			this.discard();
		} else if (this.isInsideWaterOrBubbleColumn()) {
			this.discard();
		} else {
			this.setVelocity(vec3d.multiply(0.99));
			if (!this.hasNoGravity()) {
				this.setVelocity(this.getVelocity().add(0.0, -0.06, 0.0));
			}
			
			this.setPosition(d, e, f);
		}
	}
	
	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);
		
		Entity hitEntity = entityHitResult.getEntity();
		if (hitEntity instanceof LivingEntity livingEntity) {
			OnPrimordialFireComponent.addPrimordialFireTicks(livingEntity, FIRE_TICKS_ON_HIT);
		} else {
			hitEntity.setFireTicks(FIRE_TICKS_ON_HIT);
		}
		
		if (this.getOwner() instanceof LivingEntity owner) {
			hitEntity.damage(SpectrumDamageTypes.kindlingCough(this.getWorld(), owner), DAMAGE);
		}
	}
	
	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		
		if (!this.getWorld().isClient()) {
			PrimordialFireBlock.tryPlacePrimordialFire(this.getWorld(), blockHitResult.getBlockPos().offset(blockHitResult.getSide()), blockHitResult.getSide());
			this.discard();
		}
	}
	
	@Override
	protected void initDataTracker() {
	}
	
	@Override
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		double velX = packet.getVelocityX();
		double velY = packet.getVelocityY();
		double velZ = packet.getVelocityZ();
		
		for (int i = 0; i < 7; ++i) {
			double g = 0.4 + 0.1 * (double) i;
			this.getWorld().addParticle(SpectrumParticleTypes.PRIMORDIAL_FLAME, this.getX(), this.getY(), this.getZ(), velX * g, velY, velZ * g);
		}
		
		this.setVelocity(velX, velY, velZ);
	}
	
}
