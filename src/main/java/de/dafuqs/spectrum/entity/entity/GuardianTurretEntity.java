package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.data.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.nbt.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GuardianTurretEntity extends GolemEntity implements Monster {
	
	protected static final UUID COVERED_ARMOR_BONUS_ID = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF27F");
	protected static final UUID COVERED_TOUGHNESS_BONUS_ID = UUID.fromString("8ED24DFF-221F-4ADB-9DD2-7EA92574628C");
	protected static final EntityAttributeModifier COVERED_ARMOR_BONUS = new EntityAttributeModifier(COVERED_ARMOR_BONUS_ID, "Covered armor bonus", 20.0, EntityAttributeModifier.Operation.ADDITION);
	protected static final EntityAttributeModifier COVERED_TOUGHNESS_BONUS = new EntityAttributeModifier(COVERED_TOUGHNESS_BONUS_ID, "Covered toughness bonus", 6.0, EntityAttributeModifier.Operation.ADDITION);
	
	protected static final TrackedData<Direction> ATTACHED_FACE = DataTracker.registerData(GuardianTurretEntity.class, TrackedDataHandlerRegistry.FACING);
	protected static final TrackedData<Byte> PEEK_AMOUNT = DataTracker.registerData(GuardianTurretEntity.class, TrackedDataHandlerRegistry.BYTE);
	protected static final Vec3f SOUTH_VECTOR = Util.make(() -> {
		Vec3i vec3i = Direction.SOUTH.getVector();
		return new Vec3f((float) vec3i.getX(), (float) vec3i.getY(), (float) vec3i.getZ());
	});
	
	protected float prevOpenProgress;
	protected float openProgress;
	protected @Nullable BlockPos prevAttachedBlock;
	
	public GuardianTurretEntity(EntityType<? extends GuardianTurretEntity> entityType, World world) {
		super(entityType, world);
		this.experiencePoints = 16;
		this.lookControl = new TurretLookControl(this);
	}
	
	@Override
	protected void initGoals() {
		this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 16.0F, 0.02F, true));
		this.goalSelector.add(4, new RatatatataGoal());
		this.goalSelector.add(7, new PeekGoal());
		this.goalSelector.add(8, new LookAroundGoal(this));
		this.targetSelector.add(1, new RevengeGoal(this, this.getClass()).setGroupRevenge());
		this.targetSelector.add(2, new TargetPlayerGoal(this));
		this.targetSelector.add(3, new TargetOtherTeamGoal(this));
	}
	
	@Override
	protected Entity.MoveEffect getMoveEffect() {
		return Entity.MoveEffect.NONE;
	}
	
	@Override
	public SoundCategory getSoundCategory() {
		return SoundCategory.HOSTILE;
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return SpectrumSoundEvents.ENTITY_GUARDIAN_TURRET_AMBIENT;
	}
	
	@Override
	public void playAmbientSound() {
		if (!this.isClosed()) {
			super.playAmbientSound();
		}
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return SpectrumSoundEvents.ENTITY_GUARDIAN_TURRET_DEATH;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return this.isClosed() ? SpectrumSoundEvents.ENTITY_GUARDIAN_TURRET_HURT_CLOSED : SpectrumSoundEvents.ENTITY_GUARDIAN_TURRET_HURT;
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(ATTACHED_FACE, Direction.DOWN);
		this.dataTracker.startTracking(PEEK_AMOUNT, (byte) 0);
	}
	
	public static DefaultAttributeContainer.Builder createGuardianTurretAttributes() {
		return MobEntity.createMobAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 60.0);
	}
	
	@Override
	protected BodyControl createBodyControl() {
		return new TurretBodyControl(this);
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.setAttachedFace(Direction.byId(nbt.getByte("AttachFace")));
		this.dataTracker.set(PEEK_AMOUNT, nbt.getByte("Peek"));
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putByte("AttachFace", (byte) this.getAttachedFace().getId());
		nbt.putByte("Peek", this.dataTracker.get(PEEK_AMOUNT));
	}
	
	@Override
	public void tick() {
		super.tick();
		if (!this.world.isClient && !this.hasVehicle() && !this.canStay(this.getBlockPos(), this.getAttachedFace())) {
			this.tryAttachOrTeleport();
		}
		
		if (this.tickOpenProgress()) {
			this.moveEntities();
		}
	}
	
	private void tryAttachOrTeleport() {
		Direction direction = this.findAttachSide(this.getBlockPos());
		if (direction != null) {
			this.setAttachedFace(direction);
		} else {
			// this.tryTeleport();
		}
		
	}
	
	@Override
	protected Box calculateBoundingBox() {
		float f = getExtraLength(this.openProgress);
		Direction direction = this.getAttachedFace().getOpposite();
		float g = this.getType().getWidth() / 2.0F;
		return calculateBoundingBox(direction, f).offset(this.getX() - (double) g, this.getY(), this.getZ() - (double) g);
	}
	
	private static float getExtraLength(float openProgress) {
		return 0.5F - MathHelper.sin((0.5F + openProgress) * 3.1415927F) * 0.5F;
	}
	
	private boolean tickOpenProgress() {
		this.prevOpenProgress = this.openProgress;
		float peekAmount = (float) this.getPeekAmount() * 0.01F;
		if (this.openProgress == peekAmount) {
			return false;
		} else {
			if (this.openProgress > peekAmount) {
				this.openProgress = MathHelper.clamp(this.openProgress - 0.05F, peekAmount, 1.0F);
			} else {
				this.openProgress = MathHelper.clamp(this.openProgress + 0.05F, 0.0F, peekAmount);
			}
			
			return true;
		}
	}
	
	private void moveEntities() {
		this.refreshPosition();
		float extraLength = getExtraLength(this.openProgress);
		float previousExtraLength = getExtraLength(this.prevOpenProgress);
		Direction direction = this.getAttachedFace().getOpposite();
		float extraLengthDif = extraLength - previousExtraLength;
		if (!(extraLengthDif <= 0.0F)) {
			List<Entity> entitiesOnTop = this.world.getOtherEntities(this, calculateBoundingBox(direction, previousExtraLength, extraLength).offset(this.getX() - 0.5, this.getY(), this.getZ() - 0.5), EntityPredicates.EXCEPT_SPECTATOR.and((entity) -> !entity.isConnectedThroughVehicle(this)));
			for (Entity entity : entitiesOnTop) {
				if (!(entity instanceof GuardianTurretEntity) && !entity.noClip) {
					entity.move(MovementType.SHULKER, new Vec3d((extraLengthDif * (float) direction.getOffsetX()), (extraLengthDif * (float) direction.getOffsetY()), (extraLengthDif * (float) direction.getOffsetZ())));
				}
			}
		}
	}
	
	public static Box calculateBoundingBox(Direction direction, float extraLength) {
		return calculateBoundingBox(direction, -1.0F, extraLength);
	}
	
	public static Box calculateBoundingBox(Direction direction, float prevExtraLength, float extraLength) {
		double d = Math.max(prevExtraLength, extraLength);
		double e = Math.min(prevExtraLength, extraLength);
		return (new Box(BlockPos.ORIGIN)).stretch((double) direction.getOffsetX() * d, (double) direction.getOffsetY() * d, (double) direction.getOffsetZ() * d).shrink((double) (-direction.getOffsetX()) * (1.0 + e), (double) (-direction.getOffsetY()) * (1.0 + e), (double) (-direction.getOffsetZ()) * (1.0 + e));
	}
	
	@Override
	public double getHeightOffset() {
		EntityType<?> vehicleType = this.getVehicle().getType();
		return vehicleType != EntityType.BOAT && vehicleType != EntityType.MINECART ? super.getHeightOffset() : 0.1875 - this.getVehicle().getMountedHeightOffset();
	}
	
	@Override
	public boolean startRiding(Entity entity, boolean force) {
		if (this.world.isClient()) {
			this.prevAttachedBlock = null;
		}
		
		this.setAttachedFace(Direction.DOWN);
		return super.startRiding(entity, force);
	}
	
	@Override
	public void stopRiding() {
		super.stopRiding();
		if (this.world.isClient) {
			this.prevAttachedBlock = this.getBlockPos();
		}
		
		this.prevBodyYaw = 0.0F;
		this.bodyYaw = 0.0F;
	}
	
	@Nullable
	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
		this.setYaw(0.0F);
		this.headYaw = this.getYaw();
		this.resetPosition();
		return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
	}
	
	@Override
	public Vec3d getVelocity() {
		return Vec3d.ZERO;
	}
	
	@Override
	public void setVelocity(Vec3d velocity) {
	}
	
	@Override
	public void setPosition(double x, double y, double z) {
		BlockPos blockPos = this.getBlockPos();
		if (this.hasVehicle()) {
			super.setPosition(x, y, z);
		} else {
			super.setPosition(MathHelper.floor(x) + 0.5, MathHelper.floor(y + 0.5), MathHelper.floor(z) + 0.5);
		}
		
		if (this.age != 0) {
			BlockPos blockPos2 = this.getBlockPos();
			if (!blockPos2.equals(blockPos)) {
				this.dataTracker.set(PEEK_AMOUNT, (byte) 0);
				this.velocityDirty = true;
				if (this.world.isClient && !this.hasVehicle() && !blockPos2.equals(this.prevAttachedBlock)) {
					this.prevAttachedBlock = blockPos;
					this.lastRenderX = this.getX();
					this.lastRenderY = this.getY();
					this.lastRenderZ = this.getZ();
				}
			}
			
		}
	}
	
	@Nullable
	protected Direction findAttachSide(BlockPos pos) {
		for (Direction direction : Direction.values()) {
			if (this.canStay(pos, direction)) {
				return direction;
			}
		}
		return null;
	}
	
	boolean canStay(BlockPos pos, Direction direction) {
		if (this.isInvalidPosition(pos)) {
			return false;
		} else {
			Direction direction2 = direction.getOpposite();
			if (!this.world.isDirectionSolid(pos.offset(direction), this, direction2)) {
				return false;
			} else {
				Box box = calculateBoundingBox(direction2, 1.0F).offset(pos).contract(1.0E-6);
				return this.world.isSpaceEmpty(this, box);
			}
		}
	}
	
	private boolean isInvalidPosition(BlockPos pos) {
		BlockState blockState = this.world.getBlockState(pos);
		if (blockState.isAir()) {
			return false;
		} else {
			boolean bl = blockState.isOf(Blocks.MOVING_PISTON) && pos.equals(this.getBlockPos());
			return !bl;
		}
	}
	
	@Override
	public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
		this.bodyTrackingIncrements = 0;
		this.setPosition(x, y, z);
		this.setRotation(yaw, pitch);
	}
	
	@Override
	public boolean damage(DamageSource source, float amount) {
		if (this.isClosed() && source.getSource() instanceof PersistentProjectileEntity) {
			return false;
		}
		return super.damage(source, amount);
	}
	
	private boolean isClosed() {
		return this.getPeekAmount() == 0;
	}
	
	@Override
	public boolean isCollidable() {
		return this.isAlive();
	}
	
	public Direction getAttachedFace() {
		return this.dataTracker.get(ATTACHED_FACE);
	}
	
	private void setAttachedFace(Direction face) {
		this.dataTracker.set(ATTACHED_FACE, face);
	}
	
	@Override
	public void onTrackedDataSet(TrackedData<?> data) {
		if (ATTACHED_FACE.equals(data)) {
			this.setBoundingBox(this.calculateBoundingBox());
		}
		
		super.onTrackedDataSet(data);
	}
	
	private int getPeekAmount() {
		return this.dataTracker.get(PEEK_AMOUNT);
	}
	
	void setPeekAmount(int peekAmount) {
		if (!this.world.isClient) {
			this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).removeModifier(COVERED_ARMOR_BONUS);
			this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).removeModifier(COVERED_TOUGHNESS_BONUS);
			if (peekAmount == 0) {
				this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).addPersistentModifier(COVERED_ARMOR_BONUS);
				this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).addPersistentModifier(COVERED_TOUGHNESS_BONUS);
				this.playSound(SpectrumSoundEvents.ENTITY_GUARDIAN_TURRET_CLOSE, 1.0F, 1.0F);
				this.emitGameEvent(GameEvent.CONTAINER_CLOSE);
			} else {
				this.playSound(SpectrumSoundEvents.ENTITY_GUARDIAN_TURRET_OPEN, 1.0F, 1.0F);
				this.emitGameEvent(GameEvent.CONTAINER_OPEN);
			}
		}
		
		this.dataTracker.set(PEEK_AMOUNT, (byte) peekAmount);
	}
	
	public float getOpenProgress(float delta) {
		return MathHelper.lerp(delta, this.prevOpenProgress, this.openProgress);
	}
	
	@Override
	protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
		return 0.5F;
	}
	
	@Override
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		this.bodyYaw = 0.0F;
		this.prevBodyYaw = 0.0F;
	}
	
	@Override
	public int getMaxLookPitchChange() {
		return 180;
	}
	
	@Override
	public int getMaxHeadRotation() {
		return 180;
	}
	
	@Override
	public void pushAwayFrom(Entity entity) {
	}
	
	@Override
	public float getTargetingMargin() {
		return 0.0F;
	}
	
	private class TurretLookControl extends LookControl {
		public TurretLookControl(MobEntity entity) {
			super(entity);
		}
		
		@Override
		protected void clampHeadYaw() {
		}
		
		@Override
		protected Optional<Float> getTargetYaw() {
			Direction attachedDirection = GuardianTurretEntity.this.getAttachedFace().getOpposite();
			Vec3f southVectorCopy = GuardianTurretEntity.SOUTH_VECTOR.copy();
			southVectorCopy.rotate(attachedDirection.getRotationQuaternion());
			Vec3i vec3i = attachedDirection.getVector();
			Vec3f vec3f2 = new Vec3f(vec3i.getX(), vec3i.getY(), vec3i.getZ());
			vec3f2.cross(southVectorCopy);
			double d = this.x - this.entity.getX();
			double e = this.y - this.entity.getEyeY();
			double f = this.z - this.entity.getZ();
			Vec3f vec3f3 = new Vec3f((float) d, (float) e, (float) f);
			float g = vec3f2.dot(vec3f3);
			float h = southVectorCopy.dot(vec3f3);
			return !(Math.abs(g) > 1.0E-5F) && !(Math.abs(h) > 1.0E-5F) ? Optional.empty() : Optional.of((float) (MathHelper.atan2((-g), h) * 57.2957763671875));
		}
		
		@Override
		protected Optional<Float> getTargetPitch() {
			return Optional.of(0.0F);
		}
	}
	
	/**
	 * This does the ouch
	 */
	private class RatatatataGoal extends Goal {
		private int counter;
		
		public RatatatataGoal() {
			this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
		}
		
		@Override
		public boolean canStart() {
			LivingEntity livingEntity = GuardianTurretEntity.this.getTarget();
			if (livingEntity != null && livingEntity.isAlive()) {
				return GuardianTurretEntity.this.world.getDifficulty() != Difficulty.PEACEFUL;
			} else {
				return false;
			}
		}
		
		@Override
		public void start() {
			this.counter = 20;
			GuardianTurretEntity.this.setPeekAmount(100);
		}
		
		@Override
		public void stop() {
			GuardianTurretEntity.this.setPeekAmount(0);
		}
		
		@Override
		public boolean shouldRunEveryTick() {
			return true;
		}
		
		@Override
		public void tick() {
			if (GuardianTurretEntity.this.world.getDifficulty() != Difficulty.PEACEFUL) {
				--this.counter;
				LivingEntity target = GuardianTurretEntity.this.getTarget();
				if (target != null) {
					GuardianTurretEntity.this.getLookControl().lookAt(target, 180.0F, 180.0F);
					double squaredDistanceToTarget = GuardianTurretEntity.this.squaredDistanceTo(target);
					if (squaredDistanceToTarget < 400.0) {
						if (this.counter <= 0) {
							this.counter = 20 + GuardianTurretEntity.this.random.nextInt(10) * 20 / 2;
							target.damage(EntityDamageSource.mob(GuardianTurretEntity.this), 4F);
							GuardianTurretEntity.this.playSound(SpectrumSoundEvents.ENCHANTER_DING, 2.0F, 1.0F + 0.2F * (GuardianTurretEntity.this.random.nextFloat() - GuardianTurretEntity.this.random.nextFloat()));
							target.playSound(SpectrumSoundEvents.ENCHANTER_DING, 1.0F, 0.5F + 0.2F * (GuardianTurretEntity.this.random.nextFloat() - GuardianTurretEntity.this.random.nextFloat()));
						}
					} else {
						GuardianTurretEntity.this.setTarget(null);
					}
					
					super.tick();
				}
			}
		}
	}
	
	private class PeekGoal extends Goal {
		private int counter;
		
		PeekGoal() {
		}
		
		@Override
		public boolean canStart() {
			return GuardianTurretEntity.this.getTarget() == null && GuardianTurretEntity.this.random.nextInt(toGoalTicks(40)) == 0 && GuardianTurretEntity.this.canStay(GuardianTurretEntity.this.getBlockPos(), GuardianTurretEntity.this.getAttachedFace());
		}
		
		@Override
		public boolean shouldContinue() {
			return GuardianTurretEntity.this.getTarget() == null && this.counter > 0;
		}
		
		@Override
		public void start() {
			this.counter = this.getTickCount(20 * (1 + GuardianTurretEntity.this.random.nextInt(3)));
			GuardianTurretEntity.this.setPeekAmount(30);
		}
		
		@Override
		public void stop() {
			if (GuardianTurretEntity.this.getTarget() == null) {
				GuardianTurretEntity.this.setPeekAmount(0);
			}
			
		}
		
		@Override
		public void tick() {
			--this.counter;
		}
	}
	
	/**
	 * A hostile target goal on players.
	 */
	private class TargetPlayerGoal extends ActiveTargetGoal<PlayerEntity> {
		public TargetPlayerGoal(GuardianTurretEntity turret) {
			super(turret, PlayerEntity.class, true);
		}
		
		@Override
		public boolean canStart() {
			return GuardianTurretEntity.this.world.getDifficulty() != Difficulty.PEACEFUL && super.canStart();
		}
		
		@Override
		protected Box getSearchBox(double distance) {
			Direction direction = ((GuardianTurretEntity) this.mob).getAttachedFace();
			if (direction.getAxis() == Direction.Axis.X) {
				return this.mob.getBoundingBox().expand(4.0, distance, distance);
			} else {
				return direction.getAxis() == Direction.Axis.Z ? this.mob.getBoundingBox().expand(distance, distance, 4.0) : this.mob.getBoundingBox().expand(distance, 4.0, distance);
			}
		}
	}
	
	/**
	 * A target goal on other teams' entities if this turret belongs to a team.
	 */
	private static class TargetOtherTeamGoal extends ActiveTargetGoal<LivingEntity> {
		public TargetOtherTeamGoal(GuardianTurretEntity turret) {
			super(turret, LivingEntity.class, 10, true, false, (entity) -> entity instanceof Monster);
		}
		
		@Override
		public boolean canStart() {
			return this.mob.getScoreboardTeam() != null && super.canStart();
		}
		
		@Override
		protected Box getSearchBox(double distance) {
			Direction direction = ((GuardianTurretEntity) this.mob).getAttachedFace();
			if (direction.getAxis() == Direction.Axis.X) {
				return this.mob.getBoundingBox().expand(4.0, distance, distance);
			} else {
				return direction.getAxis() == Direction.Axis.Z ? this.mob.getBoundingBox().expand(distance, distance, 4.0) : this.mob.getBoundingBox().expand(distance, 4.0, distance);
			}
		}
	}
	
	private static class TurretBodyControl extends BodyControl {
		public TurretBodyControl(MobEntity mobEntity) {
			super(mobEntity);
		}
		
		@Override
		public void tick() {
		}
	}
	
}