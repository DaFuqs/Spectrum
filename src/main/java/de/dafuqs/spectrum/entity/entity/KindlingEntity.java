package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.additionalentityattributes.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.data.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.loot.*;
import net.minecraft.loot.context.*;
import net.minecraft.nbt.*;
import net.minecraft.recipe.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class KindlingEntity extends HorseEntity implements RangedAttackMob, Angerable {
	
	protected static final Identifier CLIPPING_LOOT_TABLE = SpectrumCommon.locate("gameplay/kindling_clipping");
	protected static final Ingredient FOOD = Ingredient.fromTag(SpectrumItemTags.KINDLING_FOOD);
	
	private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(30, 59);
	private static final TrackedData<Integer> ANGER = DataTracker.registerData(KindlingEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> CLIPPED = DataTracker.registerData(KindlingEntity.class, TrackedDataHandlerRegistry.INTEGER);

	protected @Nullable UUID angryAt;
	
	public AnimationState standingAnimationState = new AnimationState();
	public AnimationState walkingAnimationState = new AnimationState();
	public AnimationState standingAngryAnimationState = new AnimationState();
	public AnimationState walkingAngryAnimationState = new AnimationState();
	public AnimationState glidingAnimationState = new AnimationState();
	
	public KindlingEntity(EntityType<? extends KindlingEntity> entityType, World world) {
		super(entityType, world);

		this.setPathfindingPenalty(PathNodeType.WATER, -0.75F);

		this.experiencePoints = 8;
	}
	
	public static DefaultAttributeContainer.Builder createKindlingAttributes() {
		return MobEntity.createMobAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0D)
				.add(EntityAttributes.GENERIC_ARMOR, 25.0D)
				.add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 12.0D)
				.add(AdditionalEntityAttributes.MAGIC_PROTECTION, 6.0D)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6D)
				.add(EntityAttributes.HORSE_JUMP_STRENGTH, 24.0D);
	}

	@Override
	protected void initAttributes(Random random) {
	}

	@Nullable
	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
		this.setPose(EntityPose.STANDING);
		return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new HorseBondWithPlayerGoal(this, 1.4));
		this.goalSelector.add(2, new ProjectileAttackGoal(this, 1.25, 40, 20.0F));
		this.goalSelector.add(3, new AnimalMateGoal(this, 1.0D));
		this.goalSelector.add(4, new TemptGoal(this, 1.25, FOOD, false));
		this.goalSelector.add(5, new FollowParentGoal(this, 1.1D));
		this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0D));
		this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.add(8, new LookAroundGoal(this));
		
		this.targetSelector.add(1, new CoughRevengeGoal(this));
		this.targetSelector.add(2, new UniversalAngerGoal<>(this, false));
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(ANGER, 0);
		this.dataTracker.startTracking(CLIPPED, 0);
	}
	
	@Override
	public void onTrackedDataSet(TrackedData<?> data) {
		if (POSE.equals(data)) {
			this.standingAnimationState.stop();
			this.walkingAnimationState.stop();
			this.standingAngryAnimationState.stop();
			this.walkingAngryAnimationState.stop();
			this.glidingAnimationState.stop();

			switch (this.getPose()) {
				case EMERGING -> this.standingAnimationState.start(this.age);
				case DIGGING -> this.walkingAnimationState.start(this.age);
				case ROARING -> this.standingAngryAnimationState.start(this.age);
				case SNIFFING -> this.walkingAngryAnimationState.start(this.age);
				case FALL_FLYING -> this.glidingAnimationState.start(this.age);
			}
		}
		super.onTrackedDataSet(data);
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		this.writeAngerToNbt(nbt);
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.readAngerFromNbt(this.getWorld(), nbt);
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return FOOD.test(stack);
	}

	@Nullable
	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
		return SpectrumEntityTypes.KINDLING.create(world);
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return SpectrumSoundEvents.ENTITY_KINDLING_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SpectrumSoundEvents.ENTITY_KINDLING_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return SpectrumSoundEvents.ENTITY_KINDLING_DEATH;
	}
@Override
	protected SoundEvent getAngrySound() {
		return SpectrumSoundEvents.ENTITY_KINDLING_ANGRY;
	}

	@Override
	protected void playJumpSound() {
		this.playSound(SpectrumSoundEvents.ENTITY_KINDLING_JUMP, 0.4F, 1.0F);
	}

	@Override
	public boolean isInAir() {
		return !this.isOnGround();
	}
	
	@Override
	public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
		return false;
	}
	
	@Override
	protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
		return 0.6F * dimensions.height;
	}
	
	@Override
	protected void mobTick() {
		super.mobTick();

		if (!this.getWorld().isClient()) {
			this.tickAngerLogic((ServerWorld) this.getWorld(), false);
			this.setClipped(this.getClipTime() - 1);
		}
		if (this.age % 600 == 0) {
			this.heal(1.0F);
		}

		if (this.fallDistance < 0.2) {
			boolean isMoving = moveControl.isMoving();
			if (getAngerTime() > 0) {
				this.setPose(isMoving ? EntityPose.EMERGING : EntityPose.ROARING);
			} else {
				this.setPose(isMoving ? EntityPose.SNIFFING : EntityPose.STANDING);
			}
		} else {
			this.setPose(EntityPose.FALL_FLYING);
		}
	}

	@Override
	public void tickMovement() {
		super.tickMovement();

		Vec3d vec3d = this.getVelocity();
		if (!this.isOnGround() && vec3d.y < 0.0) {
			this.setVelocity(vec3d.multiply(1.0, 0.6, 1.0));
		}
	}
	
	@Override
	protected boolean hasWings() {
		return true;
	}
	
	@Override
	protected void addFlapEffects() {

	}

	@Override
	public ActionResult interactMob(PlayerEntity player, Hand hand) {
		if (this.getAngerTime() > 0) {
			return ActionResult.success(this.getWorld().isClient());
		}
ItemStack handStack = player.getMainHandStack();
		if (!this.isClipped()) {
			if (handStack.isIn(ConventionalItemTags.SHEARS)) {
			handStack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));

			if (!this.getWorld().isClient()) {
					setTarget(player);
setAngryAt(player.getUuid());
					chooseRandomAngerTime();
this.playAngrySound();
				clipAndDrop();
				}

				return ActionResult.success(world.isClient);

			} else if (handStack.isIn(SpectrumItemTags.PEACHES) || handStack.isIn(SpectrumItemTags.EGGPLANTS)) {
				// 🍆 / 🍑 = 💘

				if (!this.getWorld().isClient) {
					handStack.decrement(1);
					this.world.sendEntityStatus(this, (byte) 7); // heart particles
this.playSoundIfNotSilent(SpectrumSoundEvents.ENTITY_KINDLING_LOVE);

					clipAndDrop();
			}

			return ActionResult.success(this.getWorld().isClient);
			}
		}

		return super.interactMob(player, hand);
	}
	
	private void clipAndDrop() {
		setClipped(4800); // 4 minutes
		for (ItemStack clippedStack : getClippedStacks((ServerWorld) world)) {
			dropStack(clippedStack, 0.3F);
		}
	}

	public List<ItemStack> getClippedStacks(ServerWorld world) {
		LootTable lootTable = world.getServer().getLootManager().getTable(CLIPPING_LOOT_TABLE);
		return lootTable.generateLoot(
				new LootContext.Builder(world)
						.parameter(LootContextParameters.THIS_ENTITY, KindlingEntity.this)
						.random(world.random)
						.build(LootContextTypes.BARTER));
	}

	protected void coughAt(LivingEntity target) {
		KindlingCoughEntity kindlingCoughEntity = new KindlingCoughEntity(this.getWorld(), this);
		double d = target.getX() - this.getX();
		double e = target.getBodyY(0.33F) - kindlingCoughEntity.getY();
		double f = target.getZ() - this.getZ();
		double g = Math.sqrt(d * d + f * f) * 0.2;
		kindlingCoughEntity.setVelocity(d, e + g, f, 1.5F, 10.0F);

		if (!this.isSilent()) {
			this.playSound(SpectrumSoundEvents.ENTITY_KINDLING_SHOOT, 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
		}
		
		this.getWorld().spawnEntity(kindlingCoughEntity);
	}
	
	public boolean isClipped() {
		return this.dataTracker.get(CLIPPED) > 0;
	}

	public int getClipTime() {
		return this.dataTracker.get(CLIPPED);
	}

	public void setClipped(int clipTime) {
		this.dataTracker.set(CLIPPED, clipTime);
	}

	@Override
	public boolean isAngry() {
		return this.getHorseFlag(32);
	}

	@Override
	public int getAngerTime() {
		return this.dataTracker.get(ANGER);
	}
	
	@Override
	public void setAngerTime(int angerTime) {
		this.dataTracker.set(ANGER, angerTime);
	}
	
	@Override
	public @Nullable UUID getAngryAt() {
		return this.angryAt;
	}
	
	@Override
	public void setAngryAt(@Nullable UUID angryAt) {
		this.angryAt = angryAt;
	}
	
	public
	@Override void chooseRandomAngerTime() {
		this.setAngerTime(ANGER_TIME_RANGE.get(this.random));
	}

	@Override
	public void attack(LivingEntity target, float pullProgress) {
		this.coughAt(target);
	}
	
	@Override
	public boolean eatsGrass() {
		return false;
	}
	
	@Override
	public boolean canBreedWith(AnimalEntity other) {
		return other != this && other instanceof KindlingEntity otherKindling && this.canBreed() && otherKindling.canBreed();
	}
	
	@Override
	public EntityView method_48926() {
		return this.getWorld();
	}

	protected class CoughRevengeGoal extends RevengeGoal {

		public CoughRevengeGoal(KindlingEntity kindling) {
			super(kindling);
		}
		
		@Override
		public boolean shouldContinue() {
			return KindlingEntity.this.hasAngerTime() && super.shouldContinue();
		}
		
		@Override
		protected void setMobEntityTarget(MobEntity mob, LivingEntity target) {
			if (mob instanceof BeeEntity && this.mob.canSee(target)) {
				mob.setTarget(target);
			}
		}
		
	}
	
}
