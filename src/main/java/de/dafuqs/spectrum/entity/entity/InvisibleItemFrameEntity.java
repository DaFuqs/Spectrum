package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.registries.SpectrumItems;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class InvisibleItemFrameEntity extends ItemFrameEntity {
	
	public InvisibleItemFrameEntity(EntityType<? extends ItemFrameEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public InvisibleItemFrameEntity(World world, BlockPos pos, Direction facing) {
		this(SpectrumEntityTypes.INVISIBLE_ITEM_FRAME, world, pos, facing);
	}
	
	public InvisibleItemFrameEntity(EntityType<? extends ItemFrameEntity> type, World world, BlockPos pos, Direction facing) {
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
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
	}
	
	@Override
	protected ItemStack getAsItemStack() {
		return new ItemStack(SpectrumItems.INVISIBLE_ITEM_FRAME);
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
