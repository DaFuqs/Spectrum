package de.dafuqs.spectrum.entity.entity;

import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.util.Arm;
import net.minecraft.world.World;

import java.util.ArrayList;

public class LivingMarkerEntity extends LivingEntity {
	
	public LivingMarkerEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public double getAttributeValue(EntityAttribute attribute) {
		return 0;
	}
	
	@Override
	public Iterable<ItemStack> getArmorItems() {
		return new ArrayList<>();
	}
	
	@Override
	public ItemStack getEquippedStack(EquipmentSlot slot) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public void equipStack(EquipmentSlot slot, ItemStack stack) {
	
	}
	
	@Override
	public Arm getMainArm() {
		return Arm.LEFT;
	}
	
	public void tick() {
	}
	
	public void readCustomDataFromNbt(NbtCompound nbt) {
	}
	
	public void writeCustomDataToNbt(NbtCompound nbt) {
	}
	
	public Packet<?> createSpawnPacket() {
		throw new IllegalStateException("Living Markers should never be sent");
	}
	
	protected void addPassenger(Entity passenger) {
		passenger.stopRiding();
	}
	
	public PistonBehavior getPistonBehavior() {
		return PistonBehavior.IGNORE;
	}
	
}
