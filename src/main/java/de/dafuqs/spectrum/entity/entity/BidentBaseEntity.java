package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.data.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.Box;
import net.minecraft.world.*;

public abstract class BidentBaseEntity extends TridentEntity {
	
	protected static final TrackedData<ItemStack> STACK = DataTracker.registerData(BidentBaseEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
	
	public BidentBaseEntity(EntityType<? extends TridentEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(STACK, Items.AIR.getDefaultStack());
	}
	
	public void setStack(ItemStack stack) {
		setTrackedStack(stack.copy());
		((TridentEntityAccessor) this).spectrum$setTridentStack(stack);
		this.dataTracker.set(TridentEntityAccessor.spectrum$getLoyalty(), (byte) EnchantmentHelper.getLoyalty(stack));
		this.dataTracker.set(TridentEntityAccessor.spectrum$getEnchanted(), stack.hasGlint());
	}
	
	@Override
	protected SoundEvent getHitSound() {
		return SpectrumSoundEvents.BIDENT_HIT_GROUND;
	}
	
	public ItemStack getTrackedStack() {
		return this.dataTracker.get(STACK);
	}

	public void setTrackedStack(ItemStack stack) {
		dataTracker.set(STACK, stack);
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.dataTracker.set(STACK, ItemStack.fromNbt(nbt.getCompound("Trident")));
	}

	@Override
	public Box calculateBoundingBox() {
		return super.calculateBoundingBox();
	}
}
