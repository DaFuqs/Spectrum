package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.nbt.*;
import net.minecraft.network.syncher.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;

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
		setTrackedStack(stack.copy());
		super.setPickupItemStack(stack);
		this.entityData.set(TridentEntityAccessor.spectrum$getLoyalty(), (byte) SpectrumEnchantmentHelper.getLevel(level().registryAccess(), Enchantments.LOYALTY, stack));
		this.entityData.set(TridentEntityAccessor.spectrum$getEnchanted(), stack.hasFoil());
	}
	
	@Override
	protected SoundEvent getDefaultHitGroundSoundEvent() {
		return SpectrumSoundEvents.BIDENT_HIT_GROUND;
	}
	
	public ItemStack getTrackedStack() {
		return this.entityData.get(STACK);
	}
	
	public void setTrackedStack(ItemStack stack) {
		entityData.set(STACK, stack);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		this.entityData.set(STACK, CodecHelper.fromNbt(ItemStack.CODEC, nbt.get("item"), ItemStack.EMPTY));
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
	}
	
	@Override
	public AABB makeBoundingBox() {
		return super.makeBoundingBox();
	}
}
