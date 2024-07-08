package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.item.*;
import net.minecraft.registry.tag.*;
import net.minecraft.world.*;

public class FireproofItemEntity extends ItemEntity {
	
	public FireproofItemEntity(EntityType<? extends ItemEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public FireproofItemEntity(World world, double x, double y, double z, ItemStack stack) {
		super(world, x, y, z, stack);
	}
	
	public FireproofItemEntity(World world, double x, double y, double z, ItemStack stack, double velocityX, double velocityY, double velocityZ) {
		this(SpectrumEntityTypes.FIREPROOF_ITEM, world);
		this.setPosition(x, y, z);
		this.setVelocity(velocityX, velocityY, velocityZ);
		this.setStack(stack);
	}
	
	private FireproofItemEntity(ItemEntity entity) {
		super(SpectrumEntityTypes.FIREPROOF_ITEM, entity.getWorld());
	}
	
	@Override
	public boolean isInvulnerableTo(DamageSource damageSource) {
		return damageSource.isIn(DamageTypeTags.IS_FIRE) || super.isInvulnerableTo(damageSource);
	}
	
	public ItemEntity copy() {
		return new FireproofItemEntity(this);
	}
	
	public static void scatter(World world, double x, double y, double z, ItemStack stack) {
		double d = EntityType.ITEM.getWidth();
		double e = 1.0 - d;
		double f = d / 2.0;
		double g = Math.floor(x) + world.random.nextDouble() * e + f;
		double h = Math.floor(y) + world.random.nextDouble() * e;
		double i = Math.floor(z) + world.random.nextDouble() * e + f;
		
		while(!stack.isEmpty()) {
			FireproofItemEntity itemEntity = new FireproofItemEntity(world, g, h, i, stack.split(world.random.nextInt(21) + 10));
			itemEntity.setVelocity(world.random.nextTriangular(0.0, 0.11485000171139836), world.random.nextTriangular(0.2, 0.11485000171139836), world.random.nextTriangular(0.0, 0.11485000171139836));
			world.spawnEntity(itemEntity);
		}
		
	}
	
}
