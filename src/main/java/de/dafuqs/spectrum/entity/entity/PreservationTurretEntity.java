package de.dafuqs.spectrum.entity.entity;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.ai.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
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
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import net.minecraft.world.event.listener.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public class PreservationTurretEntity extends GolemEntity implements Monster, VibrationListener.Callback {
	
	protected float DAMAGE = 4.0F;
	
	protected static final UUID COVERED_ARMOR_BONUS_ID = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF27F");
	protected static final UUID COVERED_TOUGHNESS_BONUS_ID = UUID.fromString("8ED24DFF-221F-4ADB-9DD2-7EA92574628C");
	protected static final EntityAttributeModifier COVERED_ARMOR_BONUS = new EntityAttributeModifier(COVERED_ARMOR_BONUS_ID, "Covered armor bonus", 20.0, EntityAttributeModifier.Operation.ADDITION);
	protected static final EntityAttributeModifier COVERED_TOUGHNESS_BONUS = new EntityAttributeModifier(COVERED_TOUGHNESS_BONUS_ID, "Covered toughness bonus", 6.0, EntityAttributeModifier.Operation.ADDITION);
	
	protected static final TrackedData<Direction> ATTACHED_FACE = DataTracker.registerData(PreservationTurretEntity.class, TrackedDataHandlerRegistry.FACING);
	protected static final TrackedData<Byte> PEEK_AMOUNT = DataTracker.registerData(PreservationTurretEntity.class, TrackedDataHandlerRegistry.BYTE);
	
	protected static final Vec3f SOUTH_VECTOR = Util.make(() -> {
		Vec3i vec3i = Direction.SOUTH.getVector();
		return new Vec3f(vec3i.getX(), vec3i.getY(), vec3i.getZ());
	});
	
	private final TargetPredicate TARGET_PREDICATE = TargetPredicate.createAttackable();
	
	protected final EntityGameEventHandler<VibrationListener> gameEventHandler = new EntityGameEventHandler<>(new VibrationListener(new EntityPositionSource(this, this.getStandingEyeHeight()), 16, this, (VibrationListener.Vibration) null, 0.0F, 0));
	
	protected float prevOpenProgress;
	protected float openProgress;
	protected @Nullable BlockPos prevAttachedBlock;
	
	public PreservationTurretEntity(EntityType<? extends PreservationTurretEntity> entityType, World world) {
		super(entityType, world);
		this.experiencePoints = 12;
		this.lookControl = new TurretLookControl(this);
	}
	
	@Override
	protected void initGoals() {
		this.goalSelector.add(1, new RatatatataGoal());
		this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 16.0F, 0.04F, true));
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
		return SpectrumSoundEvents.ENTITY_PRESERVATION_TURRET_AMBIENT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return SpectrumSoundEvents.ENTITY_PRESERVATION_TURRET_DEATH;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return this.isClosed() ? SpectrumSoundEvents.ENTITY_PRESERVATION_TURRET_HURT_CLOSED : SpectrumSoundEvents.ENTITY_PRESERVATION_TURRET_HURT;
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
		return new FixedBodyControl(this);
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putByte("AttachFace", (byte) this.getAttachedFace().getId());
		nbt.putByte("Peek", this.dataTracker.get(PEEK_AMOUNT));
		
		DataResult<NbtElement> dataResult = VibrationListener.createCodec(this).encodeStart(NbtOps.INSTANCE, this.gameEventHandler.getListener());
		dataResult.result().ifPresent((nbtElement) -> nbt.put("listener", nbtElement));
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.setAttachedFace(Direction.byId(nbt.getByte("AttachFace")));
		this.dataTracker.set(PEEK_AMOUNT, nbt.getByte("Peek"));
		
		if (nbt.contains("listener", NbtElement.COMPOUND_TYPE)) {
			DataResult<VibrationListener> result = VibrationListener.createCodec(this).parse(new Dynamic<>(NbtOps.INSTANCE, nbt.getCompound("listener")));
			result.result().ifPresent((vibrationListener) -> this.gameEventHandler.setListener(vibrationListener, this.world));
		}
	}
	
	@Override
	public void playAmbientSound() {
		if (this.getPeekAmount() > 0) {
			super.playAmbientSound();
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (this.world instanceof ServerWorld serverWorld) {
			(this.gameEventHandler.getListener()).tick(serverWorld);
		}
		
		if (!this.world.isClient && !this.hasVehicle() && !this.canStay(this.getBlockPos(), this.getAttachedFace())) {
			Direction direction = this.findAttachSide(this.getBlockPos());
			if (direction != null) {
				this.setAttachedFace(direction);
			}
		}
		
		this.tickOpenProgress();
	}
	
	@Override
	public void updateEventHandler(BiConsumer<EntityGameEventHandler<?>, ServerWorld> callback) {
		if (this.world instanceof ServerWorld serverWorld) {
			callback.accept(this.gameEventHandler, serverWorld);
		}
	}
	
	private void tickOpenProgress() {
		this.prevOpenProgress = this.openProgress;
		float peekAmount = (float) this.getPeekAmount() * 0.01F;
		if (this.openProgress != peekAmount) {
			if (this.openProgress > peekAmount) {
				this.openProgress = MathHelper.clamp(this.openProgress - 0.05F, peekAmount, 1.0F);
			} else {
				this.openProgress = MathHelper.clamp(this.openProgress + 0.05F, 0.0F, peekAmount);
			}
		}
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
				return this.world.isSpaceEmpty(this, getBoundingBox());
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
				this.playSound(SpectrumSoundEvents.ENTITY_PRESERVATION_TURRET_CLOSE, 1.0F, 1.0F);
				this.emitGameEvent(GameEvent.CONTAINER_CLOSE);
			} else {
				this.playSound(SpectrumSoundEvents.ENTITY_PRESERVATION_TURRET_OPEN, 1.0F, 1.0F);
				this.emitGameEvent(GameEvent.CONTAINER_OPEN);
			}
		}
		
		this.dataTracker.set(PEEK_AMOUNT, (byte) peekAmount);
	}
	
	public float getOpenProgress(float delta) {
		return MathHelper.lerp(delta, this.prevOpenProgress, this.openProgress);
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
	public boolean canSee(Entity entity) {
		if (entity.world != this.world) {
			return false;
		}
		
		Vec3d thisEyePos = this.getEyePos();
		Vec3d entityEyePos = entity.getEyePos();
		double distance = entityEyePos.distanceTo(thisEyePos);
		
		// they only have a very limited vertical field of sight
		// a valid strategy would be to sneak to their top / bottom, since they can't shoot there
		return distance < 26
				&& (Math.abs(this.getEyeY() - entity.getEyeY()) < distance / 2 || Math.abs(this.getEyeY() - entity.getY()) < distance / 2)
				&& this.world.raycast(new RaycastContext(thisEyePos, entityEyePos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this)).getType() == net.minecraft.util.hit.HitResult.Type.MISS;
	}
	
	@Override
	public float getTargetingMargin() {
		return 0.0F;
	}
	
	@Override
	public boolean accepts(ServerWorld world, GameEventListener listener, BlockPos pos, GameEvent event, GameEvent.Emitter emitter) {
		return !this.isRemoved()
				&& !this.isDead()
				&& !this.isAiDisabled()
				&& world.getWorldBorder().contains(pos)
				&& this.world == world
				&& emitter.sourceEntity() instanceof LivingEntity livingEntity
				&& this.isValidTarget(livingEntity);
	}
	
	@Contract("null->false")
	public boolean isValidTarget(@Nullable Entity entity) {
		return entity instanceof LivingEntity livingEntity
				&& this.world == entity.world
				&& EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(entity)
				&& !this.isTeammate(entity)
				&& livingEntity.getType() != EntityType.ARMOR_STAND
				&& livingEntity.getType() != SpectrumEntityTypes.PRESERVATION_TURRET
				&& !livingEntity.isInvulnerable()
				&& !livingEntity.isDead()
				&& this.world.getWorldBorder().contains(livingEntity.getBoundingBox());
	}
	
	@Override
	public void accept(ServerWorld world, GameEventListener listener, BlockPos pos, GameEvent event, @Nullable Entity entity, @Nullable Entity sourceEntity, float distance) {
		if (!this.isDead() && entity instanceof LivingEntity livingEntity && TARGET_PREDICATE.test(this, livingEntity)) {
			this.setTarget(livingEntity);
			PreservationTurretEntity.this.setPeekAmount(100);
		}
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
			Direction attachedDirection = PreservationTurretEntity.this.getAttachedFace().getOpposite();
			Vec3f southVectorCopy = PreservationTurretEntity.SOUTH_VECTOR.copy();
			southVectorCopy.rotate(attachedDirection.getRotationQuaternion());
			Vec3i vec3i = attachedDirection.getVector();
			Vec3f vec3f2 = new Vec3f(vec3i.getX(), vec3i.getY(), vec3i.getZ());
			vec3f2.cross(southVectorCopy);
			double xOffset = this.x - this.entity.getX();
			double yOffset = this.y - this.entity.getEyeY();
			double zOffset = this.z - this.entity.getZ();
			Vec3f vec3f3 = new Vec3f((float) xOffset, (float) yOffset, (float) zOffset);
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
		
		public RatatatataGoal() {
			this.setControls(EnumSet.of(Control.MOVE, Control.TARGET));
		}
		
		@Override
		public boolean canStart() {
			LivingEntity target = PreservationTurretEntity.this.getTarget();
			
			if (target != null) {
				PreservationTurretEntity.this.getLookControl().lookAt(target, 2.0F, 2.0F);
				
				if (!PreservationTurretEntity.this.canSee(target)) {
					PreservationTurretEntity.this.setTarget(null);
					return false;
				}
			}
			
			return target != null
					&& target.isAlive()
					&& PreservationTurretEntity.this.openProgress == 1.0 && TARGET_PREDICATE.test(PreservationTurretEntity.this, PreservationTurretEntity.this.getTarget());
		}
		
		@Override
		public boolean shouldContinue() {
			return super.shouldContinue() && TARGET_PREDICATE.test(PreservationTurretEntity.this, PreservationTurretEntity.this.getTarget());
		}
		
		@Override
		public void stop() {
			PreservationTurretEntity.this.setPeekAmount(0);
		}
		
		@Override
		public void tick() {
			if (PreservationTurretEntity.this.world.getDifficulty() != Difficulty.PEACEFUL) {
				LivingEntity target = PreservationTurretEntity.this.getTarget();
				if (target == null) {
					return;
				}
				
				if (!PreservationTurretEntity.this.canSee(target)) {
					return;
				}
				
				target.damage(EntityDamageSource.mob(PreservationTurretEntity.this), DAMAGE);
				PreservationTurretEntity.this.playSound(SpectrumSoundEvents.ENTITY_PRESERVATION_TURRET_SHOOT, 2.0F, 1.0F + 0.2F * (PreservationTurretEntity.this.random.nextFloat() - PreservationTurretEntity.this.random.nextFloat()));
				target.playSound(SpectrumSoundEvents.ENTITY_PRESERVATION_TURRET_SHOOT, 1.0F, 0.5F + 0.2F * (PreservationTurretEntity.this.random.nextFloat() - PreservationTurretEntity.this.random.nextFloat()));
				
				super.tick();
			}
		}
	}
	
}