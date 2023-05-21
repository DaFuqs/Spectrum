package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.item.*;
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
	
	@Override
	public boolean isInvulnerableTo(DamageSource damageSource) {
		return damageSource.isFire() || super.isInvulnerableTo(damageSource);
	}
	
}
