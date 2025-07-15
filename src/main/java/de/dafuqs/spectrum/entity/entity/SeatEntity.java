package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.nbt.*;
import net.minecraft.network.syncher.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.vehicle.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SeatEntity extends Entity {
	
	private static final EntityDataAccessor<Integer> EMPTY_TICKS = SynchedEntityData.defineId(SeatEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Optional<BlockState>> CUSHION = SynchedEntityData.defineId(SeatEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_STATE);
	private double offset = 0;
	
	public SeatEntity(EntityType<?> type, Level world) {
		super(type, world);
	}
	
	public SeatEntity(Level world, double offset) {
		super(SpectrumEntityTypes.SEAT, world);
		this.offset = offset;
	}
	
	@Override
	public void tick() {
		super.tick();
		
		var block = this.level().getBlockState(blockPosition()).getBlock();
		var cushion = getCushion();
		
		if (cushion.isEmpty()) {
			setRemoved(RemovalReason.DISCARDED);
			return;
		}
		
		var state = cushion.get();
		
		if (!state.is(block)) {
			var iter = BlockPos.withinManhattan(blockPosition(), 1, 1, 1);
			var fail = true;
			
			for (BlockPos pos : iter) {
				var check = this.level().getBlockState(pos).getBlock();
				if (state.is(check)) {
					absMoveTo(pos.getX() + 0.5, pos.getY() + offset, pos.getZ() + 0.5);
					fail = false;
					break;
				}
			}
			
			if (fail)
				incrementEmptyTicks();
		}
		
		if (getFirstPassenger() == null)
			incrementEmptyTicks();
		else if (state.is(block)) {
			setEmptyTicks(0);
		}
		
		if (getEmptyTicks() > 10) {
			setRemoved(RemovalReason.DISCARDED);
		}
	}
	
	public Optional<BlockState> getCushion() {
		return entityData.get(CUSHION);
	}
	
	public void setCushion(@NotNull BlockState state) {
		entityData.set(CUSHION, Optional.of(state));
	}
	
	public void setEmptyTicks(int ticks) {
		entityData.set(EMPTY_TICKS, ticks);
	}
	
	public int getEmptyTicks() {
		return entityData.get(EMPTY_TICKS);
	}
	
	public void incrementEmptyTicks() {
		setEmptyTicks(getEmptyTicks() + 1);
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(EMPTY_TICKS, 0);
		builder.define(CUSHION, Optional.empty());
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag nbt) {
		setEmptyTicks(nbt.getInt("emptyTicks"));
		
		var state = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), nbt.getCompound("BlockState"));
		entityData.set(CUSHION, Optional.ofNullable(state.isAir() ? null : state));
		
		offset = nbt.getDouble("offset");
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundTag nbt) {
		nbt.putInt("emptyTicks", getEmptyTicks());
		nbt.put("BlockState", NbtUtils.writeBlockState(entityData.get(CUSHION).orElse(Blocks.AIR.defaultBlockState())));
		nbt.putDouble("offset", offset);
	}
	
	@Override
	public boolean isInvulnerable() {
		return true;
	}
	
	@Override
	public void push(Entity entity) {
	}
	
	@Override
	public void move(MoverType movementType, Vec3 movement) {
		if (movementType != MoverType.PISTON)
			return;
		
		super.move(movementType, movement);
	}
	
	@Nullable
	private Vec3 locateSafeDismountingPos(Vec3 offset, LivingEntity passenger) {
		double x = this.getX() + offset.x;
		double y = this.getBoundingBox().minY + 0.5;
		double z = this.getZ() + offset.z;
		BlockPos.MutableBlockPos testPos = new BlockPos.MutableBlockPos();
		
		for (Pose pose : passenger.getDismountPoses()) {
			testPos.set(x, y, z);
			double maxHeight = this.getBoundingBox().maxY + 0.75;
			
			while (true) {
				double height = this.level().getBlockFloorHeight(testPos);
				if ((double) testPos.getY() + height > maxHeight) {
					break;
				}
				
				if (DismountHelper.isBlockFloorValid(height)) {
					AABB boundingBox = passenger.getLocalBoundsForPose(pose);
					Vec3 pos = new Vec3(x, (double) testPos.getY() + height, z);
					if (DismountHelper.canDismountTo(this.level(), passenger, boundingBox.move(pos))) {
						passenger.setPose(pose);
						return pos;
					}
				}
				
				testPos.move(Direction.UP);
				if (testPos.getY() >= maxHeight) {
					break;
				}
			}
		}
		
		return null;
	}
	
	@Override
	public void teleportTo(double destX, double destY, double destZ) {
	}
	
	public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
		Vec3 vec3d = getCollisionHorizontalEscapeVector(this.getBbWidth(), passenger.getBbWidth(), this.getYRot() + (passenger.getMainArm() == HumanoidArm.RIGHT ? 90.0F : -90.0F));
		Vec3 vec3d2 = this.locateSafeDismountingPos(vec3d, passenger);
		if (vec3d2 != null) {
			return vec3d2;
		} else {
			Vec3 vec3d3 = getCollisionHorizontalEscapeVector(this.getBbWidth(), passenger.getBbWidth(), this.getYRot() + (passenger.getMainArm() == HumanoidArm.LEFT ? 90.0F : -90.0F));
			Vec3 vec3d4 = this.locateSafeDismountingPos(vec3d3, passenger);
			return vec3d4 != null ? vec3d4 : this.position();
		}
	}
}
