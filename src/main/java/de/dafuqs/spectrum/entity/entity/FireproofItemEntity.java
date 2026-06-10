package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import net.minecraft.tags.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

public class FireproofItemEntity extends ItemEntity {
	
	public FireproofItemEntity(EntityType<? extends ItemEntity> entityType, Level world) {
		super(entityType, world);
	}
	
	public FireproofItemEntity(Level world, double x, double y, double z, ItemStack stack) {
		super(world, x, y, z, stack);
	}
	
	public FireproofItemEntity(Level world, double x, double y, double z, ItemStack stack, double velocityX, double velocityY, double velocityZ) {
		this(SpectrumEntityTypes.FIREPROOF_ITEM, world);
		this.setPos(x, y, z);
		this.setDeltaMovement(velocityX, velocityY, velocityZ);
		this.setItem(stack);
	}
	
	@Override
	public boolean isInvulnerableTo(DamageSource damageSource) {
		return damageSource.is(DamageTypeTags.IS_FIRE) || super.isInvulnerableTo(damageSource);
	}
	
	public static void scatter(Level world, double x, double y, double z, ItemStack stack) {
		double d = EntityType.ITEM.getWidth();
		double e = 1.0 - d;
		double f = d / 2.0;
		double g = Math.floor(x) + world.getRandom().nextDouble() * e + f;
		double h = Math.floor(y) + world.getRandom().nextDouble() * e;
		double i = Math.floor(z) + world.getRandom().nextDouble() * e + f;
		
		while(!stack.isEmpty()) {
			FireproofItemEntity itemEntity = new FireproofItemEntity(world, g, h, i, stack.split(world.getRandom().nextInt(21) + 10));
			itemEntity.setDeltaMovement(world.getRandom().triangle(0.0, 0.11485000171139836), world.getRandom().triangle(0.2, 0.11485000171139836), world.getRandom().triangle(0.0, 0.11485000171139836));
			world.addFreshEntity(itemEntity);
		}
		
	}
	
}
