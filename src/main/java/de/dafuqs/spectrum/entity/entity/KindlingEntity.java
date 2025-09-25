package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.additionalentityattributes.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.variants.*;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.nbt.*;
import net.minecraft.network.syncher.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.common.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class KindlingEntity extends AbstractHorse implements RangedAttackMob, NeutralMob, Shearable {
	
	protected static final EntityDataAccessor<KindlingVariant> VARIANT = SynchedEntityData.defineId(KindlingEntity.class, SpectrumTrackedDataHandlerRegistry.KINDLING_VARIANT);
	protected static final Ingredient FOOD = Ingredient.of(SpectrumItemTags.KINDLING_FOOD);
	
	private static final UniformInt ANGER_TIME_RANGE = TimeUtil.rangeOfSeconds(30, 59);
	private static final EntityDataAccessor<Integer> ANGER = SynchedEntityData.defineId(KindlingEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> CLIPPED = SynchedEntityData.defineId(KindlingEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> CHILL = SynchedEntityData.defineId(KindlingEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> EEPY_SNEEZE = SynchedEntityData.defineId(KindlingEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> PLAYING = SynchedEntityData.defineId(KindlingEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> INCITED = SynchedEntityData.defineId(KindlingEntity.class, EntityDataSerializers.BOOLEAN);
	
	protected @Nullable UUID angryAt;
	
	public AnimationState standingAnimationState = new AnimationState();
	public AnimationState walkingAnimationState = new AnimationState();
	public AnimationState standingAngryAnimationState = new AnimationState();
	public AnimationState walkingAngryAnimationState = new AnimationState();
	public AnimationState glidingAnimationState = new AnimationState();
	
	public KindlingEntity(EntityType<? extends KindlingEntity> entityType, Level world) {
		super(entityType, world);
		
		this.setPathfindingMalus(PathType.WATER, -0.75F);
		
		this.xpReward = 8;
	}
	
	public static AttributeSupplier.Builder createKindlingAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 100.0D)
				.add(Attributes.ARMOR, 25.0D)
				.add(Attributes.ARMOR_TOUGHNESS, 12.0D)
				.add(AdditionalEntityAttributes.MAGIC_PROTECTION, 6.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.6D)
				.add(Attributes.ATTACK_DAMAGE, 25F)
				.add(Attributes.ATTACK_KNOCKBACK, 1.5F)
				.add(Attributes.JUMP_STRENGTH, 2.4D);
	}
	
	@Override
	public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData) {
		this.setPose(Pose.STANDING);
		return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
	}
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new RunAroundLikeCrazyGoal(this, 1.4));
		this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.5F));
		this.goalSelector.addGoal(3, new CancellableProjectileAttackGoal(this, 1.25, 30, 20.0F));
		this.goalSelector.addGoal(3, new MeleeChaseGoal(this));
		this.goalSelector.addGoal(5, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new PlayRoughGoal(this));
		this.goalSelector.addGoal(7, new TemptGoal(this, 1.25, FOOD, false));
		this.goalSelector.addGoal(8, new FollowParentGoal(this, 1.1D));
		this.goalSelector.addGoal(9, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
		
		this.targetSelector.addGoal(1, new CoughRevengeGoal(this));
		this.targetSelector.addGoal(2, new FindPlayMateGoal<>(this, 4, 0.25F, Monster.class));
		this.targetSelector.addGoal(3, new FindPlayMateGoal<>(this, 10, 1F, KindlingEntity.class));
		this.targetSelector.addGoal(4, new FindPlayMateGoal<>(this, 40, 4F, Player.class));
		this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, false));
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(VARIANT, KindlingVariant.DEFAULT);
		builder.define(ANGER, 0);
		builder.define(CHILL, 40);
		builder.define(EEPY_SNEEZE, 0);
		builder.define(CLIPPED, 0);
		builder.define(PLAYING, false);
		builder.define(INCITED, false);
	}
	
	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
		if (DATA_POSE.equals(data)) {
			this.standingAnimationState.stop();
			this.walkingAnimationState.stop();
			this.standingAngryAnimationState.stop();
			this.walkingAngryAnimationState.stop();
			this.glidingAnimationState.stop();
			
			switch (this.getPose()) {
				case STANDING -> this.standingAnimationState.start(this.tickCount);
				case SNIFFING -> this.walkingAnimationState.start(this.tickCount);
				case ROARING -> this.standingAngryAnimationState.start(this.tickCount);
				case EMERGING -> this.walkingAngryAnimationState.start(this.tickCount);
				case FALL_FLYING -> this.glidingAnimationState.start(this.tickCount);
				default -> {
				}
			}
		}
		super.onSyncedDataUpdated(data);
	}
	
	public KindlingVariant getKindlingVariant() {
		return this.entityData.get(VARIANT);
	}
	
	public void setKindlingVariant(KindlingVariant variant) {
		this.entityData.set(VARIANT, variant);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		this.addPersistentAngerSaveData(nbt);
		Optional.ofNullable(SpectrumRegistries.KINDLING_VARIANT.getKey(this.getKindlingVariant())).ifPresent(id -> nbt.putString("variant", id.toString()));
		nbt.putInt("chillTime", getChillTime());
		nbt.putInt("eepyTime", getEepyTime());
		nbt.putBoolean("playing", isPlaying());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		this.readPersistentAngerSaveData(this.level(), nbt);
		
		KindlingVariant variant = SpectrumRegistries.KINDLING_VARIANT.get(ResourceLocation.tryParse(nbt.getString("variant")));
		this.setKindlingVariant(variant == null ? KindlingVariant.DEFAULT : variant);
		
		setChillTime(nbt.getInt("chillTime"));
		setEepyTime(nbt.getInt("eepyTime"));
		setPlaying(nbt.getBoolean("playing"));
		
		this.syncSaddleToClients();
	}
	
	@Override
	public boolean isFood(ItemStack stack) {
		return FOOD.test(stack);
	}
	
	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
		KindlingEntity baby = SpectrumEntityTypes.KINDLING.create(world);
		if (baby != null) {
			baby.setKindlingVariant(this.random.nextBoolean() ? this.getKindlingVariant() : ((KindlingEntity) entity).getKindlingVariant());
		}
		return baby;
	}
	
	@Override
	public void containerChanged(Container sender) {
		ItemStack itemStack = this.getBodyArmorItem();
		super.containerChanged(sender);
		ItemStack itemStack2 = this.getBodyArmorItem();
		if (this.tickCount > 20 && this.isBodyArmorItem(itemStack2) && itemStack != itemStack2) {
			this.playSound(SoundEvents.HORSE_ARMOR, 0.5F, 1.0F);
		}
	}
	
	@Override
	public boolean canUseSlot(EquipmentSlot slot) {
		return true;
	}
	
	public boolean isBodyArmorItem(ItemStack item) {
		return item.getItem() instanceof AnimalArmorItem animalArmorItem && animalArmorItem.getBodyType() == AnimalArmorItem.BodyType.EQUESTRIAN;
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
	public boolean isJumping() {
		return !this.onGround();
	}
	
	@Override
	public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
		return false;
	}
	
	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.getEntity() instanceof KindlingEntity) {
			amount = 1;
			
			if (random.nextBoolean()) {
				setChillTime(0);
			}
		}
		
		if (amount > 1) {
			setPlaying(false);
		}
		
		thornsFlag = source.is(DamageTypes.THORNS);
		
		return super.hurt(source, amount);
	}
	
	// makes it so Kindlings are not angered by thorns damage
	// since they play fight and may damage their owner
	// that would make them aggro otherwise
	boolean thornsFlag = false;
	
	@Override
	public void setLastHurtByMob(@Nullable LivingEntity attacker) {
		if (!thornsFlag) {
			super.setLastHurtByMob(attacker);
		}
	}
	
	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		
		if (!this.level().isClientSide()) {
			this.updatePersistentAnger((ServerLevel) this.level(), false);
			this.setClipped(this.getClipTime() - 1);
			this.setChillTime(this.getChillTime() - 1);
			
			if (hasEffect(SpectrumStatusEffects.ETERNAL_SLUMBER)) {
				ascend(2);
			}
			
			if (hasEffect(SpectrumStatusEffects.FATAL_SLUMBER)) {
				ascend(3);
			}
			
			if (hasEffect(SpectrumStatusEffects.SOMNOLENCE) && getEepyTime() == 0) {
				setEepyTime(100);
			}
			
			var eepy = getEepyTime();
			
			if (eepy > 1) {
				setEepyTime(eepy - 1);
			} else if (eepy == 1) {
				setEepyTime(0);
				ascend(1);
			}
		}
		if (this.tickCount % 600 == 0) {
			this.heal(1.0F);
		}
	}
	
	@Override
	public boolean shouldBlockExplode(Explosion explosion, BlockGetter world, BlockPos pos, BlockState state, float explosionPower) {
		return super.shouldBlockExplode(explosion, world, pos, state, explosionPower);
	}
	
	private void ascend(int blastMod) {
		var world = level();
		
		world.addParticle(ParticleTypes.EXPLOSION_EMITTER, getX(), getY(), getZ(), 1.0, 0.0, 0.0);
		world.explode(this, SpectrumDamageTypes.incandescence(world), null, getX(), getY(), getZ(), 10F * blastMod, true, Level.ExplosionInteraction.MOB);
		playSound(SoundEvents.DRAGON_FIREBALL_EXPLODE, 2F, 0.5F);
		playSound(SpectrumSoundEvents.DEEP_CRYSTAL_RING, 2F, 0.334F);
		playSound(SoundEvents.ENDER_DRAGON_AMBIENT, 1F, 2F);
		
		((ServerLevel) world).getPlayers(p -> p.distanceTo(this) < 64).forEach(p -> {
			Support.grantAdvancementCriterion(p, "ascend_kindling", "he_explarded");
		});
		
		for (int i = 0; i < 5; i++) {
			((ServerLevel) world).sendParticles(ParticleTypes.DRAGON_BREATH, getRandomX(1.5), getY() + random.nextDouble(), getRandomZ(1.5), random.nextInt(6) + 1, 0, random.nextFloat() / 3, 0, 0);
			((ServerLevel) world).sendParticles(ParticleTypes.END_ROD, getRandomX(1.5), getY() + random.nextDouble(), getRandomZ(1.5), random.nextInt(6) + 1, 0, random.nextFloat() / 3, 0, 0);
		}
		
		for (BlockPos transmutePos : BlockPos.withinManhattan(blockPosition(), 12 * blastMod, 6 * blastMod, 12 * blastMod)) {
			var distance = Math.sqrt(transmutePos.distSqr(blockPosition()));
			if (distance <= 6 * blastMod || random.nextFloat() < 1 / ((distance - 6) / 3)) {
				var candidate = world.getBlockState(transmutePos);
				
				// Do not the bedrock nor the claims
				if (candidate.getDestroySpeed(world, transmutePos) < 0 || !GenericClaimModsCompat.canBreak(world, transmutePos, this))
					continue;
				
				if (candidate.isAir()) {
					if (random.nextFloat() < 0.125F) {
						((ServerLevel) world).sendParticles(ParticleTypes.DRAGON_BREATH, transmutePos.getX() + random.nextDouble(), transmutePos.getY() + random.nextDouble(), transmutePos.getZ() + random.nextDouble(), random.nextInt(3) + 1, random.nextFloat() / 5 - 0.1, random.nextFloat() / 5 - 0.1, random.nextFloat() / 5 - 0.1, 0);
					}
					continue;
				}
				
				if (!GenericClaimModsCompat.canModify(world, transmutePos, this)) {
					continue;
				}
				
				if (candidate.getFluidState().is(FluidTags.WATER)) {
					continue;
				}
				
				if (random.nextFloat() < 0.025F) {
					world.setBlockAndUpdate(transmutePos, Blocks.MAGMA_BLOCK.defaultBlockState());
					continue;
				}
				
				if (candidate.is(BlockTags.BASE_STONE_OVERWORLD) || candidate.is(BlockTags.BASE_STONE_NETHER)) {
					
					if (random.nextFloat() < 0.05F) {
						world.setBlockAndUpdate(transmutePos, Blocks.CRYING_OBSIDIAN.defaultBlockState());
					} else {
						world.setBlockAndUpdate(transmutePos, Blocks.END_STONE.defaultBlockState());
					}
					
					continue;
				}
				
				if (candidate.is(SpectrumBlockTags.BASE_STONE_DEEPER_DOWN)) {
					world.setBlockAndUpdate(transmutePos, SpectrumBlocks.BLACK_MATERIA.get().defaultBlockState());
					continue;
				}
				
				if (candidate.is(BlockTags.LOGS)) {
					world.setBlockAndUpdate(transmutePos, Blocks.COAL_BLOCK.defaultBlockState());
					continue;
				}
				
				if (candidate.is(BlockTags.DIRT)) {
					world.setBlockAndUpdate(transmutePos, Blocks.BROWN_STAINED_GLASS.defaultBlockState());
					continue;
				}
				
				if (candidate.is(Blocks.CLAY)) {
					world.setBlockAndUpdate(transmutePos, Blocks.TERRACOTTA.defaultBlockState());
					continue;
				}
				
				if (candidate.is(BlockTags.SAND)) {
					world.setBlockAndUpdate(transmutePos, Blocks.WHITE_STAINED_GLASS.defaultBlockState());
					continue;
				}
				
				if (candidate.is(Blocks.OBSIDIAN)) {
					world.setBlockAndUpdate(transmutePos, Blocks.CRYING_OBSIDIAN.defaultBlockState());
				}
			}
		}
		remove(RemovalReason.DISCARDED);
		var lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, world);
		lightning.setPosRaw(getX(), getY(), getZ());
		world.addFreshEntity(lightning);
	}
	
	@Override
	public void aiStep() {
		super.aiStep();
		
		Vec3 velocity = this.getDeltaMovement();
		boolean onGround = this.onGround();
		if (!onGround && velocity.y < 0.0) {
			this.setDeltaMovement(velocity.multiply(1.0, 0.6, 1.0));
		}
		if (onGround || this.fallDistance < 0.2) {
			boolean isMoving = this.getX() - this.xo != 0 || this.getZ() - this.zo != 0; // pretty ugly, but also triggers when being ridden
			if (getRemainingPersistentAngerTime() > 0) {
				this.setPose(isMoving ? Pose.EMERGING : Pose.ROARING);
			} else {
				this.setPose(isMoving ? Pose.SNIFFING : Pose.STANDING);
			}
		} else {
			this.setPose(Pose.FALL_FLYING);
		}
	}
	
	@Override
	protected boolean isFlapping() {
		return true;
	}
	
	@Override
	protected void onFlap() {
		// TODO - Make the Kindling flap its wings? Maybe while jumping or passively
	}
	
	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (this.getRemainingPersistentAngerTime() > 0) {
			return InteractionResult.FAIL;
		}
		
		// clipping using shears
		ItemStack handStack = player.getMainHandItem();
		if (this.readyForShearing() && handStack.is(Tags.Items.TOOLS_SHEAR)) {
			
			if (!this.level().isClientSide()) {
				setTarget(player);
				takeRevenge(player.getUUID());
				this.makeMad();
				
				this.shear(SoundSource.PLAYERS);
				this.gameEvent(GameEvent.SHEAR, player);
				if (!this.level().isClientSide) {
					handStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
				}
			}
			
			return InteractionResult.sidedSuccess(this.level().isClientSide());
		}
		
		boolean bl = !this.isBaby() && this.isTamed() && player.isSecondaryUseActive();
		if (!this.isVehicle() && !bl) {
			if (!handStack.isEmpty()) {
				if (this.isFood(handStack)) {
					return this.fedFood(player, handStack);
				}
				
				if (!this.isTamed()) {
					this.makeMad();
					return InteractionResult.sidedSuccess(this.level().isClientSide);
				}
			}
			
			if (player.isSecondaryUseActive()) {
				return super.mobInteract(player, hand);
			}
		}
		
		return super.mobInteract(player, hand);
	}
	
	@Override
	public void shear(SoundSource shearedSoundCategory) {
		this.level().playSound(null, this, SoundEvents.SHEEP_SHEAR, shearedSoundCategory, 1.0f, 1.0f);
		
		setClipped(4800); // 4 minutes
		for (ItemStack clippedStack : getClippedStacks((ServerLevel) this.level())) {
			spawnAtLocation(clippedStack, 0.3F);
		}
	}
	
	@Override
	public boolean readyForShearing() {
		return this.isAlive() && !this.isBaby() && !this.isClipped();
	}
	
	@Override
	protected boolean handleEating(Player player, ItemStack item) {
		boolean canEat = false;
		
		if (this.getHealth() < this.getMaxHealth()) {
			this.heal(2.0F);
			canEat = true;
		}
		
		if (this.isBaby()) {
			this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), 0.0, 0.0, 0.0);
			if (!this.level().isClientSide) {
				this.ageUp(20);
			}
			canEat = true;
		} else if (!this.level().isClientSide && !this.isInLove()) {
			this.setInLove(player);
			canEat = true;
		}
		
		if ((canEat || !this.isTamed()) && this.getTemper() < this.getMaxTemper()) {
			canEat = true;
			if (!this.level().isClientSide) {
				this.modifyTemper(3);
			}
		}
		
		if (canEat) {
			//this.playEatingAnimation();
			this.gameEvent(GameEvent.EAT);
		}
		
		return canEat;
	}
	
	@Override
	public void updatePersistentAnger(ServerLevel world, boolean angerPersistent) {
		LivingEntity livingEntity = this.getTarget();
		UUID uUID = this.getPersistentAngerTarget();
		if ((livingEntity == null || livingEntity.isDeadOrDying()) && uUID != null && world.getEntity(uUID) instanceof Mob) {
			this.stopBeingAngry();
		} else {
			if (this.getRemainingPersistentAngerTime() > 0 && (livingEntity == null || livingEntity.getType() != EntityType.PLAYER || !angerPersistent)) {
				this.setRemainingPersistentAngerTime(this.getRemainingPersistentAngerTime() - 1);
				if (this.getRemainingPersistentAngerTime() == 0) {
					this.stopBeingAngry();
				}
			}
			
		}
	}
	
	public List<ItemStack> getClippedStacks(ServerLevel world) {
		LootTable lootTable = world.getServer().reloadableRegistries().getLootTable(this.getKindlingVariant().getClippingLootTable());
		return lootTable.getRandomItems(
				new LootParams.Builder(world)
						.withParameter(LootContextParams.THIS_ENTITY, KindlingEntity.this)
						.create(LootContextParamSets.PIGLIN_BARTER)
		);
	}
	
	protected void coughAt(LivingEntity target) {
		KindlingCoughEntity kindlingCoughEntity = new KindlingCoughEntity(this.level(), this);
		double d = target.getX() - this.getX();
		double e = target.getY(0.33F) - kindlingCoughEntity.getY();
		double f = target.getZ() - this.getZ();
		double g = Math.sqrt(d * d + f * f) * 0.2;
		kindlingCoughEntity.shoot(d, e + g, f, 1.5F, 10.0F);
		
		if (!this.isSilent()) {
			this.playSound(SpectrumSoundEvents.ENTITY_KINDLING_SHOOT, 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
		}
		
		this.level().addFreshEntity(kindlingCoughEntity);
	}
	
	public boolean isClipped() {
		return this.entityData.get(CLIPPED) > 0;
	}
	
	public int getClipTime() {
		return this.entityData.get(CLIPPED);
	}
	
	public void setClipped(int clipTime) {
		this.entityData.set(CLIPPED, clipTime);
	}
	
	public int getChillTime() {
		return this.entityData.get(CHILL);
	}
	
	public void setChillTime(int chillTime) {
		this.entityData.set(CHILL, chillTime);
	}
	
	public void setEepyTime(int eepySneeze) {
		this.entityData.set(EEPY_SNEEZE, eepySneeze);
	}
	
	public int getEepyTime() {
		return this.entityData.get(EEPY_SNEEZE);
	}
	
	public void setPlaying(boolean playing) {
		this.entityData.set(PLAYING, playing);
	}
	
	public boolean isPlaying() {
		return this.entityData.get(PLAYING);
	}
	
	public void setIncited(boolean incited) {
		this.entityData.set(INCITED, incited);
	}
	
	public boolean isIncited() {
		return entityData.get(INCITED);
	}
	
	@Override
	public int getRemainingPersistentAngerTime() {
		return this.entityData.get(ANGER);
	}
	
	@Override
	public void setRemainingPersistentAngerTime(int angerTime) {
		this.entityData.set(ANGER, angerTime);
	}
	
	@Override
	public @Nullable UUID getPersistentAngerTarget() {
		return this.angryAt;
	}
	
	@Override
	public void setPersistentAngerTarget(@Nullable UUID angryAt) {
		this.angryAt = angryAt;
	}
	
	public void takeRevenge(UUID target) {
		setPersistentAngerTarget(target);
		setIncited(false);
		setPlaying(false);
		
		startPersistentAngerTimer();
	}
	
	public
	@Override void startPersistentAngerTimer() {
		this.setRemainingPersistentAngerTime(ANGER_TIME_RANGE.sample(this.random));
	}
	
	@Override
	public void performRangedAttack(LivingEntity target, float pullProgress) {
		this.coughAt(target);
	}
	
	@Override
	public boolean canEatGrass() {
		return false;
	}
	
	@Override
	public boolean canMate(Animal other) {
		return other != this && other instanceof KindlingEntity otherKindling && this.canParent() && otherKindling.canParent();
	}
	
	protected class CoughRevengeGoal extends HurtByTargetGoal {
		
		public CoughRevengeGoal(KindlingEntity kindling) {
			super(kindling, KindlingEntity.class);
		}
		
		@Override
		public boolean canContinueToUse() {
			return KindlingEntity.this.isAngry() && super.canContinueToUse();
		}
		
		@Override
		public void start() {
			super.start();
			var attacker = getLastHurtByMob();
			if (attacker != null) {
				takeRevenge(getLastHurtByMob().getUUID());
			}
		}
		
		@Override
		protected void alertOther(Mob mob, LivingEntity target) {
			if (mob instanceof Bee && this.mob.hasLineOfSight(target)) {
				mob.setTarget(target);
			}
		}
		
	}
	
	protected class MeleeChaseGoal extends MeleeAttackGoal {
		
		public MeleeChaseGoal(KindlingEntity kindling) {
			super(kindling, 0.6F, true);
		}
		
		@Override
		public boolean canUse() {
			var kindling = KindlingEntity.this;
			var angryAt = kindling.getPersistentAngerTarget();
			if (angryAt == null)
				return false;
			return super.canUse() && kindling.isAngry() && !isPlaying() && KindlingEntity.this.distanceTo(this.mob.getTarget()) < 5F;
		}
		
		@Override
		public boolean canContinueToUse() {
			return super.canContinueToUse() && KindlingEntity.this.distanceTo(this.mob.getTarget()) < 9F;
		}
	}
	
	protected class PlayRoughGoal extends MeleeAttackGoal {
		
		public PlayRoughGoal(PathfinderMob mob) {
			super(mob, 0.4F, true);
		}
		
		@Override
		public boolean canUse() {
			return super.canUse() && !isAngry() && !isVehicle() && isPlaying();
		}
		
		@Override
		public boolean canContinueToUse() {
			if (!super.canContinueToUse())
				return false;
			
			if ((getTarget() instanceof KindlingEntity playMate && playMate.isAngry()) || isVehicle()) {
				setTarget(null);
				setIncited(false);
				return false;
			}
			
			return !isAngry();
		}
		
		@Override
		protected void checkAndPerformAttack(LivingEntity target) {
			if (this.canPerformAttack(target)) {
				this.resetAttackCooldown();
				this.mob.swing(InteractionHand.MAIN_HAND);
				this.mob.doHurtTarget(target);
				if (target instanceof KindlingEntity playMate && !playMate.isAngry() && random.nextBoolean()) {
					playMate.setIncited(true);
				}
				
				if (!(target instanceof Enemy)) {
					target.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200));
				}
				if (random.nextBoolean()) {
					stop();
					setIncited(false);
					this.mob.setTarget(null);
					KindlingEntity.this.setChillTime(2400 * (target instanceof Player ? 2 : 1));
				}
			}
		}
	}
	
	protected class FindPlayMateGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
		
		private final float waitModifier;
		
		public FindPlayMateGoal(Mob mob, int reciprocalChance, float waitModifier, Class<T> targetClass) {
			super(mob, targetClass, reciprocalChance, true, true, null);
			this.waitModifier = waitModifier;
		}
		
		@Override
		public boolean canUse() {
			if (isAngry() || isVehicle() || isInLove())
				return false;
			
			if (!isIncited()) {
				var chill = getChillTime();
				
				if (chill > 0)
					return false;
			}
			
			if (isIncited() || (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0)) {
				this.findTarget();
				
				if (this.target != null) {
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
	
	protected class CancellableProjectileAttackGoal extends RangedAttackGoal {
		
		public CancellableProjectileAttackGoal(RangedAttackMob mob, double mobSpeed, int intervalTicks, float maxShootRange) {
			super(mob, mobSpeed, intervalTicks, maxShootRange);
		}
		
		@Override
		public boolean canContinueToUse() {
			return KindlingEntity.this.isAngry() && super.canContinueToUse() && distanceTo(getProjectileTarget()) > 3F;
		}
		
		@Override
		public boolean canUse() {
			return super.canUse() && !isPlaying() && distanceTo(getProjectileTarget()) > 4F;
		}
		
		protected LivingEntity getProjectileTarget() {
			return ((ProjectileAttackGoalAccessor) this).getProjectileAttackTarget();
		}
		
	}
}
