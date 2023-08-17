package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.decoration.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class InvisibleGlowItemFrameEntity extends ItemFrameEntity {
	
	public InvisibleGlowItemFrameEntity(EntityType<? extends ItemFrameEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public InvisibleGlowItemFrameEntity(World world, BlockPos pos, Direction facing) {
		this(SpectrumEntityTypes.INVISIBLE_GLOW_ITEM_FRAME, world, pos, facing);
	}
	
	public InvisibleGlowItemFrameEntity(EntityType<? extends ItemFrameEntity> type, World world, BlockPos pos, Direction facing) {
		super(type, world, pos, facing);
	}
	
	@Override
	public boolean isInvisible() {
		if (this.getHeldItemStack().isEmpty()) {
			return super.isInvisible();
		} else {
			return true;
		}
	}
	
	@Override
	public SoundEvent getRemoveItemSound() {
		return SoundEvents.ENTITY_GLOW_ITEM_FRAME_REMOVE_ITEM;
	}
	
	@Override
	public SoundEvent getBreakSound() {
		return SoundEvents.ENTITY_GLOW_ITEM_FRAME_BREAK;
	}
	
	@Override
	public SoundEvent getPlaceSound() {
		return SoundEvents.ENTITY_GLOW_ITEM_FRAME_PLACE;
	}
	
	@Override
	public SoundEvent getAddItemSound() {
		return SoundEvents.ENTITY_GLOW_ITEM_FRAME_ADD_ITEM;
	}
	
	@Override
	public SoundEvent getRotateItemSound() {
		return SoundEvents.ENTITY_GLOW_ITEM_FRAME_ROTATE_ITEM;
	}
	
	@Override
	protected ItemStack getAsItemStack() {
		return new ItemStack(SpectrumItems.INVISIBLE_GLOW_ITEM_FRAME);
	}
	
	@Override
	public void setHeldItemStack(ItemStack value, boolean update) {
		super.setHeldItemStack(value, update);
		if (update && !this.world.isClient) {
			SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) world, getPos(), ParticleTypes.END_ROD, 10, new Vec3d(0, 0, 0), new Vec3d(0.1, 0.1, 0.1));
			world.playSoundFromEntity(null, this, SpectrumSoundEvents.ENCHANTER_DING, SoundCategory.BLOCKS, 0.5F, 1.0F);
		}
	}
	
	@Override
	public boolean damage(DamageSource source, float amount) {
		boolean success = super.damage(source, amount);
		if (success && !this.world.isClient) {
			SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) world, getPos(), ParticleTypes.END_ROD, 10, new Vec3d(0, 0, 0), new Vec3d(0.1, 0.1, 0.1));
			world.playSoundFromEntity(null, this, SpectrumSoundEvents.ENCHANTER_DING, SoundCategory.BLOCKS, 0.5F, 1.0F);
		}
		return success;
	}
	
}
