package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.api.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.data.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.network.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class EraserEntity extends SpiderEntity implements PackEntity<EraserEntity>, Bucketable {
	
	private static final TrackedData<Boolean> FROM_BUCKET = DataTracker.registerData(EraserEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	private @Nullable StatusEffectInstance effectOnHit;
	
	private @Nullable EraserEntity leader;
	private int groupSize = 1;
	
	public EraserEntity(EntityType<? extends EraserEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public static DefaultAttributeContainer.Builder createEraserAttributes() {
		return HostileEntity.createHostileAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 28.0);
	}
	
	@Override
	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(1, new SwimGoal(this));
		this.goalSelector.add(2, new FleeEntityGoal<>(this, LizardEntity.class, 6.0F, 1.0, 1.2));
		this.goalSelector.add(2, new PounceAtTargetGoal(this, 0.4F));
		this.goalSelector.add(3, new AttackGoal(this));
		this.goalSelector.add(4, new FollowClanLeaderGoal<>(this));
		this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8));
		this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.targetSelector.add(1, new RevengeGoal(this));
		this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
	}
	
	@Override
	public boolean canHaveStatusEffect(StatusEffectInstance effect) {
		return super.canHaveStatusEffect(effect) && effect.getEffectType() != SpectrumStatusEffects.DEADLY_POISON;
	}
	
	@Override
	public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
		Random random = world.getRandom();
		
		this.effectOnHit = getRandomOnHitEffect();
		
		if (entityData == null) {
			entityData = new SwarmingSpiderData();
			((SwarmingSpiderData) entityData).setEffect(random);
		}
		
		if (entityData instanceof SwarmingSpiderData swarmingSpiderData) {
			StatusEffect statusEffect = swarmingSpiderData.effect;
			if (statusEffect != null) {
				this.addStatusEffect(swarmingSpiderData.getEffectInstance());
			}
		}
		
		return entityData;
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(FROM_BUCKET, false);
	}
	
	@Override
	public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
		return false;
	}
	
	@Override
	public boolean tryAttack(Entity target) {
		if (super.tryAttack(target)) {
			if (this.effectOnHit != null && target instanceof LivingEntity livingTarget) {
				livingTarget.addStatusEffect(this.effectOnHit, this);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean hasOthersInGroup() {
		return this.groupSize > 1;
	}
	
	@Override
	public @Nullable EraserEntity getLeader() {
		return this.leader;
	}
	
	@Override
	public boolean isCloseEnoughToLeader() {
		return this.squaredDistanceTo(this.leader) <= 121.0;
	}
	
	@Override
	public void leaveGroup() {
		if (this.leader != null) {
			this.leader.decreaseGroupSize();
			this.leader = null;
		}
	}
	
	@Override
	public void moveTowardLeader() {
		if (this.hasLeader()) {
			this.getNavigation().startMovingTo(this.leader, 1.0);
		}
	}
	
	@Override
	public int getMaxGroupSize() {
		return super.getLimitPerChunk();
	}
	
	@Override
	public void joinGroupOf(EraserEntity groupLeader) {
		this.leader = groupLeader;
		groupLeader.increaseGroupSize();
	}
	
	@Override
	public int getGroupSize() {
		return this.groupSize;
	}
	
	protected void increaseGroupSize() {
		++this.groupSize;
	}
	
	protected void decreaseGroupSize() {
		--this.groupSize;
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		
		nbt.putBoolean("FromBucket", this.isFromBucket());
		putEffectOnHit(nbt);
	}
	
	private void putEffectOnHit(NbtCompound nbt) {
		if (this.effectOnHit != null) {
			NbtCompound effectNbt = new NbtCompound();
			this.effectOnHit.writeNbt(effectNbt);
			nbt.put("EffectOnHit", effectNbt);
		}
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		
		this.setFromBucket(nbt.getBoolean("FromBucket"));
		readEffectOnHit(nbt);
	}
	
	private void readEffectOnHit(NbtCompound nbt) {
		if (nbt.contains("EffectOnHit", NbtElement.COMPOUND_TYPE)) {
			this.effectOnHit = StatusEffectInstance.fromNbt(nbt.getCompound("EffectOnHit"));
		}
	}
	
	public StatusEffectInstance getRandomOnHitEffect() {
		World world = this.getWorld();
		Difficulty difficulty = this.getWorld().getDifficulty();
		
		StatusEffect statusEffect;
		int amplifier = 0;
		switch (world.random.nextInt(8)) {
			case 1 -> {
				statusEffect = SpectrumStatusEffects.STIFFNESS;
				amplifier = random.nextInt(2);
			}
			case 2 -> {
				statusEffect = SpectrumStatusEffects.FRENZY;
				amplifier = random.nextInt(2);
			}
			case 3 -> statusEffect = SpectrumStatusEffects.SCARRED;
			case 4 -> {
				statusEffect = SpectrumStatusEffects.VULNERABILITY;
				amplifier = random.nextInt(2);
			}
			default -> {
				statusEffect = SpectrumStatusEffects.DEADLY_POISON;
				amplifier = random.nextInt(2);
			}
		}
		
		int duration = 120 * difficulty.getId();
		return new StatusEffectInstance(statusEffect, duration, amplifier);
	}
	
	@Override
	protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
		return 0.2F;
	}
	
	// Bucketable
	@Override
	public ActionResult interactMob(PlayerEntity player, Hand hand) {
		return tryBucket(player, hand, this).orElse(super.interactMob(player, hand));
	}
	
	// Bucketable.tryBucket() has a dedicated check for WATER_BUCKET in there
	// since we are bucketing with Fluids.EMPTY we have to use a custom implementation
	static <T extends LivingEntity & Bucketable> Optional<ActionResult> tryBucket(PlayerEntity player, Hand hand, T entity) {
		ItemStack handStack = player.getStackInHand(hand);
		if (handStack.getItem() == Items.BUCKET && entity.isAlive()) {
			entity.playSound(entity.getBucketFillSound(), 1.0F, 1.0F);
			ItemStack bucketedStack = entity.getBucketItem();
			entity.copyDataToStack(bucketedStack);
			ItemStack exchangedStack = ItemUsage.exchangeStack(handStack, player, bucketedStack, false);
			player.setStackInHand(hand, exchangedStack);
			World world = entity.getWorld();
			if (!world.isClient) {
				Criteria.FILLED_BUCKET.trigger((ServerPlayerEntity) player, bucketedStack);
			}
			
			entity.discard();
			return Optional.of(ActionResult.success(world.isClient));
		} else {
			return Optional.empty();
		}
	}
	
	@Override
	public boolean cannotDespawn() {
		return super.cannotDespawn() || this.isFromBucket();
	}
	
	@Override
	public boolean canImmediatelyDespawn(double distanceSquared) {
		return !this.isFromBucket() && !this.hasCustomName();
	}
	
	@Override
	public boolean isFromBucket() {
		return this.dataTracker.get(FROM_BUCKET);
	}
	
	@Override
	public void setFromBucket(boolean fromBucket) {
		this.dataTracker.set(FROM_BUCKET, fromBucket);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void copyDataToStack(ItemStack stack) {
		Bucketable.copyDataToStack(this, stack);
		NbtCompound nbtCompound = stack.getOrCreateNbt();
		putEffectOnHit(nbtCompound);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void copyDataFromNbt(NbtCompound nbt) {
		Bucketable.copyDataFromNbt(this, nbt);
		readEffectOnHit(nbt);
	}
	
	@Override
	public ItemStack getBucketItem() {
		return new ItemStack(SpectrumItems.BUCKET_OF_ERASER);
	}
	
	@Override
	public SoundEvent getBucketFillSound() {
		return SoundEvents.ITEM_BUCKET_FILL;
	}
	
	public static class SwarmingSpiderData extends SpiderData {
		public StatusEffect effect;
		public int amplifier = 0;
		
		public SwarmingSpiderData() {
		}
		
		@Override
		public void setEffect(Random random) {
			switch (random.nextInt(5)) {
				case 0 -> {
					this.effect = StatusEffects.SPEED;
					this.amplifier = random.nextInt(2);
				}
				case 1 -> {
					this.effect = StatusEffects.STRENGTH;
					this.amplifier = random.nextInt(2);
				}
				case 2 -> {
					this.effect = StatusEffects.REGENERATION;
					this.amplifier = random.nextInt(2);
				}
				case 3 -> this.effect = StatusEffects.INVISIBILITY;
				default -> {
					this.effect = SpectrumStatusEffects.MAGIC_ANNULATION;
					this.amplifier = 5;
				}
			}
		}
		
		public StatusEffectInstance getEffectInstance() {
			return new StatusEffectInstance(this.effect, Integer.MAX_VALUE, this.amplifier);
		}
	}
	
}
