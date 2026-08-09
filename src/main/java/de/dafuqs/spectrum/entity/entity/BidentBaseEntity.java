package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.network.syncher.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

public abstract class BidentBaseEntity extends ThrownTrident {
	
	protected static final EntityDataAccessor<ItemStack> STACK = SynchedEntityData.defineId(BidentBaseEntity.class, EntityDataSerializers.ITEM_STACK);
	
	public BidentBaseEntity(EntityType<? extends ThrownTrident> entityType, Level world) {
		super(entityType, world);
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(STACK, Items.AIR.getDefaultInstance());
	}
	
	@Override
	public void setPickupItemStack(ItemStack stack) {
		entityData.set(STACK, stack.copy());
		super.setPickupItemStack(stack);
	}
	
	@Override
	public ItemStack getDefaultPickupItem() {
		return entityData.get(STACK);
	}
	
	@Override
	protected SoundEvent getDefaultHitGroundSoundEvent() {
		return SpectrumSoundEvents.BIDENT_HIT_GROUND;
	}
	
}
