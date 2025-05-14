package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;

public class KindlingCoughEntity extends Projectile {
	
	protected static final float DAMAGE = 10.0F;
	protected static final int FIRE_TICKS_ON_HIT = 30;
	
	public KindlingCoughEntity(EntityType<? extends KindlingCoughEntity> entityType, Level world) {
		super(entityType, world);
	}
	
	public KindlingCoughEntity(Level world, LivingEntity owner) {
		this(SpectrumEntityTypes.KINDLING_COUGH, world);
		this.setOwner(owner);
		this.setPos(owner.getX() - (owner.getBbWidth() + 1.0F) * 0.5 * Mth.sin(owner.yBodyRot * 0.017453292F), owner.getEyeY() - 0.1, owner.getZ() + (owner.getBbWidth() + 1.0F) * 0.5 * (double) Mth.cos(owner.yBodyRot * 0.017453292F));
	}
	
	@Override
	public void tick() {
		super.tick();
		Vec3 vec3d = this.getDeltaMovement();
		HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
		this.onHit(hitResult);
		double d = this.getX() + vec3d.x;
		double e = this.getY() + vec3d.y;
		double f = this.getZ() + vec3d.z;
		this.updateRotation();
		if (this.level().getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
			this.discard();
		} else if (this.isInWaterOrBubble()) {
			this.discard();
		} else {
			this.setDeltaMovement(vec3d.scale(0.99));
			if (!this.isNoGravity()) {
				this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.06, 0.0));
			}
			
			this.setPos(d, e, f);
		}
	}
	
	@Override
	protected void onHitEntity(EntityHitResult entityHitResult) {
		super.onHitEntity(entityHitResult);
		
		Entity hitEntity = entityHitResult.getEntity();
		if (hitEntity instanceof LivingEntity livingEntity) {
			OnPrimordialFireComponent.addPrimordialFireTicks(livingEntity, FIRE_TICKS_ON_HIT);
		} else {
			hitEntity.setRemainingFireTicks(FIRE_TICKS_ON_HIT);
		}
		
		if (this.getOwner() instanceof LivingEntity owner) {
			hitEntity.hurt(SpectrumDamageTypes.kindlingCough(this.level(), owner), DAMAGE);
		}
	}
	
	@Override
	protected void onHitBlock(BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);
		
		if (!this.level().isClientSide()) {
			PrimordialFireBlock.tryPlacePrimordialFire(this.level(), blockHitResult.getBlockPos().relative(blockHitResult.getDirection()), blockHitResult.getDirection());
			this.discard();
		}
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
	}
	
	@Override
	public void recreateFromPacket(ClientboundAddEntityPacket packet) {
		super.recreateFromPacket(packet);
		double velX = packet.getXa();
		double velY = packet.getYa();
		double velZ = packet.getZa();
		
		for (int i = 0; i < 7; ++i) {
			double g = 0.4 + 0.1 * (double) i;
			this.level().addParticle(SpectrumParticleTypes.PRIMORDIAL_FLAME, this.getX(), this.getY(), this.getZ(), velX * g, velY, velZ * g);
		}
		
		this.setDeltaMovement(velX, velY, velZ);
	}
	
}
