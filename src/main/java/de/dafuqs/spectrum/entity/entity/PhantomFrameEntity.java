package de.dafuqs.spectrum.entity.entity;

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

public class PhantomFrameEntity extends ItemFrameEntity {

	public PhantomFrameEntity(EntityType<? extends ItemFrameEntity> entityType, World world) {
		super(entityType, world);
	}

	public PhantomFrameEntity(EntityType<? extends ItemFrameEntity> type, World world, BlockPos pos, Direction facing) {
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
	protected ItemStack getAsItemStack() {
		return new ItemStack(SpectrumItems.PHANTOM_FRAME);
	}

	@Override
	public void setHeldItemStack(ItemStack value, boolean update) {
		super.setHeldItemStack(value, update);
		if (update && this.isAlive() && !this.getWorld().isClient()) {
			SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) this.getWorld(), getPos(), ParticleTypes.END_ROD, 10, new Vec3d(0, 0, 0), new Vec3d(0.1, 0.1, 0.1));
			this.getWorld().playSoundFromEntity(null, this, SpectrumSoundEvents.CRAFTING_DING, SoundCategory.BLOCKS, 0.5F, 1.0F);
		}
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		boolean success = super.damage(source, amount);
		if (success && this.isAlive() && !this.getWorld().isClient()) {
			SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) this.getWorld(), getPos(), ParticleTypes.END_ROD, 10, new Vec3d(0, 0, 0), new Vec3d(0.1, 0.1, 0.1));
			this.getWorld().playSoundFromEntity(null, this, SpectrumSoundEvents.CRAFTING_DING, SoundCategory.BLOCKS, 0.5F, 1.0F);
		}
		return success;
	}
	
	public boolean isRedstonePowered() {
		return this.getWorld().getReceivedRedstonePower(this.getBlockPos()) > 0;
	}
	
	public boolean shouldRenderAtMaxLight() {
		return isRedstonePowered();
	}
	
}
