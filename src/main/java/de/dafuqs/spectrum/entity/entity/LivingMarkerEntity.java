package de.dafuqs.spectrum.entity.entity;

import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.protocol.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.material.*;

import java.util.*;

public class LivingMarkerEntity extends LivingEntity {
	
	public LivingMarkerEntity(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}
	
	@Override
	public void tick() {
	
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
	
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
	
	}
	
	@Override
	public double getAttributeValue(Holder<Attribute> attribute) {
		return 0D;
	}
	
	@Override
	public double getAttributeBaseValue(Holder<Attribute> attribute) {
		return 0D;
	}
	
	@Override
	protected boolean canAddPassenger(Entity passenger) {
		return false;
	}
	
	@Override
	protected boolean couldAcceptPassenger() {
		return false;
	}
	
	@Override
	public PushReaction getPistonPushReaction() {
		return PushReaction.IGNORE;
	}
	
	public boolean isIgnoringBlockTriggers() {
		return true;
	}
	
	@Override
	public Iterable<ItemStack> getArmorSlots() {
		return new ArrayList<>();
	}
	
	@Override
	public ItemStack getItemBySlot(EquipmentSlot slot) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
	
	}
	
	public void setPose(Pose pose) {
	
	}
	
	public Pose getPose() {
		return Pose.STANDING;
	}
	
	@Override
	public HumanoidArm getMainArm() {
		return HumanoidArm.LEFT;
	}
	
	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entityTrackerEntry) {
		throw new IllegalStateException("Living Markers should never be sent");
	}
	
	@Override
	protected void addPassenger(Entity passenger) {
		throw new IllegalStateException("Living Marker: should never addPassenger without checking couldAcceptPassenger()");
	}
	
}
