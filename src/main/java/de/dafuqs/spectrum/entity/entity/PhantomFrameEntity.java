package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

public class PhantomFrameEntity extends ItemFrame {
	
	public PhantomFrameEntity(EntityType<? extends ItemFrame> entityType, Level world) {
		super(entityType, world);
	}
	
	public PhantomFrameEntity(EntityType<? extends ItemFrame> type, Level world, BlockPos pos, Direction facing) {
		super(type, world, pos, facing);
	}
	
	@Override
	public boolean isInvisible() {
		if (this.getItem().isEmpty()) {
			return super.isInvisible();
		} else {
			return true;
		}
	}
	
	@Override
	protected @NotNull ItemStack getFrameItemStack() {
		return new ItemStack(SpectrumItems.PHANTOM_FRAME.get());
	}
	
	@Override
	public void setItem(ItemStack value, boolean update) {
		super.setItem(value, update);
		if (update && this.isAlive() && !this.level().isClientSide()) {
			PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) this.level(), position(), ParticleTypes.END_ROD, 10, new Vec3(0, 0, 0), new Vec3(0.1, 0.1, 0.1));
			this.level().playSound(null, this, SpectrumSoundEvents.CRAFTING_DING, SoundSource.BLOCKS, 0.5F, 1.0F);
		}
	}
	
	@Override
	public boolean hurt(DamageSource source, float amount) {
		boolean success = super.hurt(source, amount);
		if (success && this.isAlive() && !this.level().isClientSide()) {
			PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) this.level(), position(), ParticleTypes.END_ROD, 10, new Vec3(0, 0, 0), new Vec3(0.1, 0.1, 0.1));
			this.level().playSound(null, this, SpectrumSoundEvents.CRAFTING_DING, SoundSource.BLOCKS, 0.5F, 1.0F);
		}
		return success;
	}
	
	public boolean isRedstonePowered() {
		return this.level().getBestNeighborSignal(this.blockPosition()) > 0;
	}
	
	public boolean shouldRenderAtMaxLight() {
		return isRedstonePowered();
	}
	
}
