package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.additionalentityattributes.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.data.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.loot.*;
import net.minecraft.loot.context.*;
import net.minecraft.nbt.*;
import net.minecraft.particle.*;
import net.minecraft.recipe.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class KindlingEntity extends HorseEntity implements RangedAttackMob, Angerable {
	
	protected static final Identifier CLIPPING_LOOT_TABLE = SpectrumCommon.locate("gameplay/kindling_clipping");
	protected static final Ingredient FOOD = Ingredient.fromTag(SpectrumItemTags.KINDLING_FOOD);
	
	private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(30, 59);
	private static final TrackedData<Integer> ANGER = DataTracker.registerData(KindlingEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> CLIPPED = DataTracker.registerData(KindlingEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> CHILL = DataTracker.registerData(KindlingEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Boolean> PLAYING = DataTracker.registerData(KindlingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> INCITED = DataTracker.registerData(KindlingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
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
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 25F)
				.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.5F)
				.add(EntityAttributes.HORSE_JUMP_STRENGTH, 12.0D);
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
		this.goalSelector.add(2, new PounceAtTargetGoal(this, 0.2F));
		this.goalSelector.add(3, new MeleeChaseGoal(this));
		this.goalSelector.add(4, new CancellableProjectileAttackGoal(this, 1.25, 40, 20.0F));
		this.goalSelector.add(5, new AnimalMateGoal(this, 1.0D));
		this.goalSelector.add(6, new PlayRoughGoal(this));
		this.goalSelector.add(7, new TemptGoal(this, 1.25, FOOD, false));
		this.goalSelector.add(8, new FollowParentGoal(this, 1.1D));
		this.goalSelector.add(9, new WanderAroundFarGoal(this, 1.0D));
		this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.add(11, new LookAroundGoal(this));
		
		this.targetSelector.add(1, new CoughRevengeGoal(this));
		this.targetSelector.add(2, new FindPlayMateGoal<>(this, 4, 0.25F, HostileEntity.class));
		this.targetSelector.add(3, new FindPlayMateGoal<>(this, 10, 1F, KindlingEntity.class));
		this.targetSelector.add(4, new FindPlayMateGoal<>(this, 40, 4F, PlayerEntity.class));
		this.targetSelector.add(5, new UniversalAngerGoal<>(this, false));
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(ANGER, 0);
		this.dataTracker.startTracking(CHILL, 40);
		this.dataTracker.startTracking(CLIPPED, 0);
		this.dataTracker.startTracking(PLAYING, false);
		this.dataTracker.startTracking(INCITED, false);
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
				case STANDING -> this.standingAnimationState.start(this.age);
				case DIGGING -> this.walkingAnimationState.start(this.age);
				case ROARING -> this.standingAngryAnimationState.start(this.age);
				case SNIFFING -> this.walkingAngryAnimationState.start(this.age);
				case FALL_FLYING -> this.glidingAnimationState.start(this.age);
				default -> {
				}
			}
		}
		super.onTrackedDataSet(data);
	}
	
	@Override
	public double getMountedHeightOffset() {
		return this.getHeight() - (this.isBaby() ? 0.2 : 0.15);
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		this.writeAngerToNbt(nbt);
		nbt.putInt("chillTime", getChillTime());
		nbt.putBoolean("playing", isPlaying());
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.readAngerFromNbt(this.getWorld(), nbt);
		setChillTime(nbt.getInt("chillTime"));
		setPlaying(nbt.getBoolean("playing"));
	}
	
	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return FOOD.test(stack);
	}
	
	@Nullable
	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
		HorseEntity baby = SpectrumEntityTypes.KINDLING.create(world);
		this.setChildAttributes(entity, baby);
		return baby;
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
	public boolean damage(DamageSource source, float amount) {
		if (source.getAttacker() instanceof KindlingEntity) {
			amount = 1;
			
			if (random.nextBoolean()) {
				setChillTime(0);
			}
		}
		
		if (amount > 1) {
			setPlaying(false);
		}
		
		thornsFlag = source.isOf(DamageTypes.THORNS);
		
		return super.damage(source, amount);
	}
	
	// makes it so Kindlings are not angered by thorns damage
	// since they play fight and may damage their owner
	// that would make them aggro otherwise
	boolean thornsFlag = false;
	
	@Override
	public void setAttacker(@Nullable LivingEntity attacker) {
		if(!thornsFlag) {
			super.setAttacker(attacker);
		}
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
			this.setChillTime(this.getChillTime() - 1);
		}
		if (this.age % 600 == 0) {
			this.heal(1.0F);
		}
	}
	
	@Override
	public void tickMovement() {
		super.tickMovement();
		
		Vec3d vec3d = this.getVelocity();
		if (!this.isOnGround() && vec3d.y < 0.0) {
			this.setVelocity(vec3d.multiply(1.0, 0.6, 1.0));
		}
		if (this.fallDistance < 0.2) {
			boolean isMoving = this.getX() - this.prevX != 0 || this.getZ() - this.prevZ != 0; // pretty ugly, but also triggers when being ridden
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
	protected boolean isFlappingWings() {
		return true;
	}
	
	@Override
	protected void addFlapEffects() {
		// TODO - Make the Kindling flap its wings? Maybe while jumping or passively
	}
	
	@Override
	public ActionResult interactMob(PlayerEntity player, Hand hand) {
		if(player.shouldCancelInteraction()) {
			return super.interactMob(player, hand);
		}
		
		if (this.getAngerTime() > 0) {
			return ActionResult.success(this.getWorld().isClient());
		}
		
		ItemStack handStack = player.getMainHandStack();
		if(!this.isBaby()) {
			if (!this.isClipped() && handStack.isIn(ConventionalItemTags.SHEARS)) {
				handStack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));
				
				if (!this.getWorld().isClient()) {
					setTarget(player);
					takeRevenge(player.getUuid());
					this.playAngrySound();
					clipAndDrop();
				}
				
				return ActionResult.success(this.getWorld().isClient());
				
			} else if (handStack.isIn(SpectrumItemTags.PEACHES) || handStack.isIn(SpectrumItemTags.EGGPLANTS)) {
				// 🍆 / 🍑 = 💘
				
				if (!this.getWorld().isClient()) {
					handStack.decrement(1);
					
					this.setTame(true);
					if (getOwnerUuid() == null && player instanceof ServerPlayerEntity serverPlayerEntity) {
						this.setOwnerUuid(player.getUuid());
						Criteria.TAME_ANIMAL.trigger(serverPlayerEntity, this);
					}
					
					this.lovePlayer(player);
					
					this.getWorld().sendEntityStatus(this, (byte) 7); // heart particles
					this.playSoundIfNotSilent(SpectrumSoundEvents.ENTITY_KINDLING_LOVE);
					
					clipAndDrop();
				}
				
				return ActionResult.success(this.getWorld().isClient());
			}
		}
		
		return super.interactMob(player, hand);
	}
	
	@Override
	protected boolean receiveFood(PlayerEntity player, ItemStack item) {
		boolean canEat = false;

		this.lovePlayer(player);
		
		if (this.getHealth() < this.getMaxHealth()) {
			this.heal(2.0F);
			canEat = true;
		}
		
		if (this.isBaby()) {
			this.getWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getParticleX(1.0), this.getRandomBodyY() + 0.5, this.getParticleZ(1.0), 0.0, 0.0, 0.0);
			if (!this.getWorld().isClient) {
				this.growUp(20);
			}
			
			canEat = true;
		}
		
		if ((canEat || !this.isTame()) && this.getTemper() < this.getMaxTemper()) {
			canEat = true;
			if (!this.getWorld().isClient) {
				this.addTemper(3);
			}
		}
		
		if (canEat) {
			//this.playEatingAnimation();
			this.emitGameEvent(GameEvent.EAT);
		}
		
		return canEat;
	}
	
	@Override
	public void tickAngerLogic(ServerWorld world, boolean angerPersistent) {
		LivingEntity livingEntity = this.getTarget();
		UUID uUID = this.getAngryAt();
		if ((livingEntity == null || livingEntity.isDead()) && uUID != null && world.getEntity(uUID) instanceof MobEntity) {
			this.stopAnger();
		} else {
			if (this.getAngerTime() > 0 && (livingEntity == null || livingEntity.getType() != EntityType.PLAYER || !angerPersistent)) {
				this.setAngerTime(this.getAngerTime() - 1);
				if (this.getAngerTime() == 0) {
					this.stopAnger();
				}
			}
			
		}
	}
	
	private void clipAndDrop() {
		setClipped(4800); // 4 minutes
		for (ItemStack clippedStack : getClippedStacks((ServerWorld) this.getWorld())) {
			dropStack(clippedStack, 0.3F);
		}
	}
	
	public List<ItemStack> getClippedStacks(ServerWorld world) {
		LootTable lootTable = world.getServer().getLootManager().getLootTable(CLIPPING_LOOT_TABLE);
		return lootTable.generateLoot(
				new LootContextParameterSet.Builder(world)
						.add(LootContextParameters.THIS_ENTITY, KindlingEntity.this)
						.build(LootContextTypes.BARTER)
		);
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
	
	public int getChillTime() {
		return this.dataTracker.get(CHILL);
	}
	
	public void setChillTime(int chillTime) {
		this.dataTracker.set(CHILL, chillTime);
	}
	
	public void setPlaying(boolean playing) {
		this.dataTracker.set(PLAYING, playing);
	}
	
	public boolean isPlaying() {
		return this.dataTracker.get(PLAYING);
	}
	
	public void setIncited(boolean incited) {
		this.dataTracker.set(INCITED, incited);
	}
	
	public boolean isIncited() {
		return dataTracker.get(INCITED);
	}
	
	@Override
	public boolean isAngry() {
		return super.isAngry() || this.getAngerTime() > 0;
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
	
	public void takeRevenge(UUID target) {
		setAngryAt(target);
		setIncited(false);
		setPlaying(false);
		
		chooseRandomAngerTime();
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
			super(kindling, KindlingEntity.class);
		}
		
		@Override
		public boolean shouldContinue() {
			return KindlingEntity.this.hasAngerTime() && super.shouldContinue();
		}
		
		@Override
		public void start() {
			super.start();
			var attacker = getAttacker();
			if (attacker != null) {
				takeRevenge(getAttacker().getUuid());
			}
		}
		
		@Override
		protected void setMobEntityTarget(MobEntity mob, LivingEntity target) {
			if (mob instanceof BeeEntity && this.mob.canSee(target)) {
				mob.setTarget(target);
			}
		}
		
	}
	
	protected class MeleeChaseGoal extends MeleeAttackGoal {
		
		public MeleeChaseGoal(KindlingEntity kindling) {
			super(kindling, 0.5F, true);
		}
		
		@Override
		public boolean canStart() {
			var kindling = KindlingEntity.this;
			var angryAt = kindling.getAngryAt();
			if (angryAt == null)
				return false;
			return super.canStart() && kindling.hasAngerTime() && !isPlaying() && KindlingEntity.this.distanceTo(this.mob.getTarget()) < 5F;
		}
		
		@Override
		public boolean shouldContinue() {
			return super.shouldContinue() && KindlingEntity.this.distanceTo(this.mob.getTarget()) < 9F;
		}
	}
	
	protected class PlayRoughGoal extends MeleeAttackGoal {
		
		public PlayRoughGoal(PathAwareEntity mob) {
			super(mob, 0.4F, true);
		}
		
		@Override
		public boolean canStart() {
			return super.canStart() && !hasAngerTime() && !hasPassengers() && isPlaying();
		}
		
		@Override
		public boolean shouldContinue() {
			if (!super.shouldContinue())
				return false;
			
			if ((getTarget() instanceof KindlingEntity playMate && playMate.hasAngerTime()) || hasPassengers()) {
				setTarget(null);
				setIncited(false);
				return false;
			}
			
			return !hasAngerTime();
		}
		
		@Override
		protected void attack(LivingEntity target, double squaredDistance) {
			double d = this.getSquaredMaxAttackDistance(target);
			if (squaredDistance <= d && this.getCooldown() <= 0) {
				this.resetCooldown();
				this.mob.swingHand(Hand.MAIN_HAND);
				this.mob.tryAttack(target);
				if (target instanceof KindlingEntity playMate && !playMate.hasAngerTime() && random.nextBoolean()) {
					playMate.setIncited(true);
				}
				
				if (!(target instanceof Monster)) {
					target.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200));
				}
				if (random.nextBoolean()) {
					stop();
					setIncited(false);
					this.mob.setTarget(null);
					KindlingEntity.this.setChillTime(2400 * (target instanceof PlayerEntity ? 2 : 1));
				}
			}
		}
	}
	
	protected class FindPlayMateGoal<T extends LivingEntity> extends ActiveTargetGoal<T> {
		
		private final float waitModifier;
		
		public FindPlayMateGoal(MobEntity mob, int reciprocalChance, float waitModifier, Class<T> targetClass) {
			super(mob, targetClass, reciprocalChance, true, true, null);
			this.waitModifier = waitModifier;
		}
		
		@Override
		public boolean canStart() {
			if (hasAngerTime() || hasPassengers())
				return false;
			
			if (!isIncited()) {
				var chill = getChillTime();
				
				if (chill > 0)
					return false;
			}
			
			if (isIncited() || (this.reciprocalChance > 0 && this.mob.getRandom().nextInt(this.reciprocalChance) != 0)) {
				this.findClosestTarget();
				
				if (this.targetEntity != null) {
					setChillTime((int) (1200 * waitModifier));
					return true;
				}
			}
			return false;
		}
		
		@Override
		public void start() {
			super.start();
			setPlaying(true);
		}
	}
	
	protected class CancellableProjectileAttackGoal extends ProjectileAttackGoal {
		
		public CancellableProjectileAttackGoal(RangedAttackMob mob, double mobSpeed, int intervalTicks, float maxShootRange) {
			super(mob, mobSpeed, intervalTicks, maxShootRange);
		}
		
		@Override
		public boolean shouldContinue() {
			return KindlingEntity.this.hasAngerTime() && super.shouldContinue() && distanceTo(getProjectileTarget()) > 5F;
		}
		
		@Override
		public boolean canStart() {
			return super.canStart() && !isPlaying() && distanceTo(getProjectileTarget()) > 6F;
		}
		
		protected LivingEntity getProjectileTarget() {
			return ((ProjectileAttackGoalAccessor) this).getProjectileAttackTarget();
		}
		
	}
}
