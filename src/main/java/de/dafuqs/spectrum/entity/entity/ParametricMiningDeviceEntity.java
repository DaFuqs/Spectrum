package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.explosion.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.thrown.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.util.hit.*;
import net.minecraft.world.*;

public class ParametricMiningDeviceEntity extends ThrownItemEntity {
	
	public ParametricMiningDeviceEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public ParametricMiningDeviceEntity(World world, LivingEntity owner) {
		super(SpectrumEntityTypes.PARAMETRIC_MINING_DEVICE_ENTITY, owner, world);
	}
	
	public ParametricMiningDeviceEntity(World world, double x, double y, double z) {
		super(SpectrumEntityTypes.PARAMETRIC_MINING_DEVICE_ENTITY, x, y, z, world);
	}
	
	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		World world = this.getWorld();
		if (!world.isClient) {
			Entity owner = getOwner();
			PlayerEntity playerOwner = owner instanceof PlayerEntity player ? player : null;
			ModularExplosionDefinition.explode((ServerWorld) world, entityHitResult.getEntity().getBlockPos(), playerOwner, getStack());
		}
		world.sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
		
		remove(Entity.RemovalReason.DISCARDED);
	}
	
	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		World world = this.getWorld();
		if (!world.isClient) {
			Entity owner = getOwner();
			PlayerEntity playerOwner = owner instanceof PlayerEntity player ? player : null;
			ModularExplosionDefinition.explode((ServerWorld) world, blockHitResult.getBlockPos(), blockHitResult.getSide().getOpposite(), playerOwner, getStack());
		}
		world.sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
		
		remove(Entity.RemovalReason.DISCARDED);
	}
	
	@Override
	public void handleStatus(byte status) {
		var pos = getPos();
		
		if (status == 1) {
			for (int i = 0; i < 20; i++) {
				var particle = random.nextBoolean() ? SpectrumParticleTypes.PRIMORDIAL_SMOKE : SpectrumParticleTypes.PRIMORDIAL_FLAME;
				this.getWorld().addImportantParticle(particle, true, pos.getX(), pos.getY(), pos.getZ(), random.nextFloat() * 0.25 - 0.125, random.nextFloat() * 0.25 - 0.125, random.nextFloat() * 0.25 - 0.125);
			}
		} else if (status == 2) {
			var particles = 15 + random.nextInt(16);
			for (int i = 0; i < particles; i++) {
				var r = random.nextDouble() * 4;
				var orientation = Orientation.create(random.nextDouble() * Math.PI * 2, random.nextDouble() * Math.PI * 2);
				var particle = orientation.toVector(r).add(pos);
				this.getWorld().addParticle(SpectrumParticleTypes.PRIMORDIAL_SMOKE, particle.getX(), particle.getY(), particle.getZ(), 0, 0, 0);
			}
		}
	}
	
	@Override
	protected Item getDefaultItem() {
		return SpectrumBlocks.PARAMETRIC_MINING_DEVICE.asItem();
	}
	
}
