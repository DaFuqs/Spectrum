package de.dafuqs.spectrum.entity.entity;

import net.minecraft.block.piston.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import java.util.*;

public class LivingMarkerEntity extends LivingEntity {
	
	public LivingMarkerEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
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
	
	@Override
	public void tick() {
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
	}
	
	@Override
	public Packet<ClientPlayPacketListener> createSpawnPacket() {
		throw new IllegalStateException("Living Markers should never be sent");
	}
	
	@Override
	protected void addPassenger(Entity passenger) {
		passenger.stopRiding();
	}
	
	@Override
	public PistonBehavior getPistonBehavior() {
		return PistonBehavior.IGNORE;
	}
	
}
