package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.additionalentityattributes.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.variants.*;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
import net.minecraft.block.*;
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
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.loot.*;
import net.minecraft.loot.context.*;
import net.minecraft.nbt.*;
import net.minecraft.particle.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.tag.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import net.minecraft.world.explosion.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class KindlingEntity extends AbstractHorseEntity implements RangedAttackMob, Angerable, Shearable {
	
	private static final UUID HORSE_ARMOR_BONUS_ID = UUID.fromString("f55b70e7-db42-4384-8843-6e9c843336af");
	
	protected static final TrackedData<KindlingVariant> VARIANT = DataTracker.registerData(KindlingEntity.class, SpectrumTrackedDataHandlerRegistry.KINDLING_VARIANT);
	protected static final Ingredient FOOD = Ingredient.fromTag(SpectrumItemTags.KINDLING_FOOD);
	
	private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(30, 59);
	private static final TrackedData<Integer> ANGER = DataTracker.registerData(KindlingEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> CLIPPED = DataTracker.registerData(KindlingEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> CHILL = DataTracker.registerData(KindlingEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> EEPY_SNEEZE = DataTracker.registerData(KindlingEntity.class, TrackedDataHandlerRegistry.INTEGER);
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
		this.goalSelector.add(2, new PounceAtTargetGoal(this, 0.5F));
		this.goalSelector.add(3, new CancellableProjectileAttackGoal(this, 1.25, 30, 20.0F));
		this.goalSelector.add(3, new MeleeChaseGoal(this));
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
		this.dataTracker.startTracking(VARIANT, KindlingVariant.DEFAULT);
		this.dataTracker.startTracking(ANGER, 0);
		this.dataTracker.startTracking(CHILL, 40);
		this.dataTracker.startTracking(EEPY_SNEEZE, 0);
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
				case SNIFFING -> this.walkingAnimationState.start(this.age);
				case ROARING -> this.standingAngryAnimationState.start(this.age);
				case EMERGING -> this.walkingAngryAnimationState.start(this.age);
				case FALL_FLYING -> this.glidingAnimationState.start(this.age);
				default -> {
				}
			}
		}
		super.onTrackedDataSet(data);
	}
	
	public KindlingVariant getKindlingVariant() {
		return this.dataTracker.get(VARIANT);
	}
	
	public void setKindlingVariant(KindlingVariant variant) {
		this.dataTracker.set(VARIANT, variant);
	}
	
	@Override
	public double getMountedHeightOffset() {
		return super.getMountedHeightOffset() - 0.25;
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		this.writeAngerToNbt(nbt);
		nbt.putString("variant", SpectrumRegistries.KINDLING_VARIANT.getId(this.getKindlingVariant()).toString());
		nbt.putInt("chillTime", getChillTime());
		nbt.putInt("eepyTime", getEepyTime());
		nbt.putBoolean("playing", isPlaying());
		
		if (!this.items.getStack(1).isEmpty()) {
			nbt.put("ArmorItem", this.items.getStack(1).writeNbt(new NbtCompound()));
		}
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.readAngerFromNbt(this.getWorld(), nbt);
		
		KindlingVariant variant = SpectrumRegistries.KINDLING_VARIANT.get(Identifier.tryParse(nbt.getString("variant")));
		this.setKindlingVariant(variant == null ? KindlingVariant.DEFAULT : variant);

		setChillTime(nbt.getInt("chillTime"));
		setEepyTime(nbt.getInt("eepyTime"));
		setPlaying(nbt.getBoolean("playing"));

		if (nbt.contains("ArmorItem", 10)) {
			ItemStack itemStack = ItemStack.fromNbt(nbt.getCompound("ArmorItem"));
			if (!itemStack.isEmpty() && this.isHorseArmor(itemStack)) {
				this.items.setStack(1, itemStack);
			}
		}
		
		this.updateSaddle();
	}
	
	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return FOOD.test(stack);
	}
	
	@Nullable
	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
		KindlingEntity baby = SpectrumEntityTypes.KINDLING.create(world);
		if (baby != null) {
			baby.setKindlingVariant(this.random.nextBoolean() ? this.getKindlingVariant() : ((KindlingEntity) entity).getKindlingVariant());
		}
		return baby;
	}
	
	public ItemStack getArmorType() {
		return this.getEquippedStack(EquipmentSlot.CHEST);
	}
	
	private void equipArmor(ItemStack stack) {
		this.equipStack(EquipmentSlot.CHEST, stack);
		this.setEquipmentDropChance(EquipmentSlot.CHEST, 0.0F);
	}
	
	protected void updateSaddle() {
		if (!this.getWorld().isClient) {
			super.updateSaddle();
			this.setArmorTypeFromStack(this.items.getStack(1));
			this.setEquipmentDropChance(EquipmentSlot.CHEST, 0.0F);
		}
	}
	
	private void setArmorTypeFromStack(ItemStack stack) {
		this.equipArmor(stack);
		if (!this.getWorld().isClient) {
			this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).removeModifier(HORSE_ARMOR_BONUS_ID);
			if (this.isHorseArmor(stack)) {
				int armorBonus = ((HorseArmorItem) stack.getItem()).getBonus();
				if (armorBonus != 0) {
					this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).addTemporaryModifier(new EntityAttributeModifier(HORSE_ARMOR_BONUS_ID, "Horse armor bonus", armorBonus, EntityAttributeModifier.Operation.ADDITION));
				}
			}
		}
	}
	
	public void onInventoryChanged(Inventory sender) {
		ItemStack itemStack = this.getArmorType();
		super.onInventoryChanged(sender);
		ItemStack itemStack2 = this.getArmorType();
		if (this.age > 20 && this.isHorseArmor(itemStack2) && itemStack != itemStack2) {
			this.playSound(SoundEvents.ENTITY_HORSE_ARMOR, 0.5F, 1.0F);
		}
	}
	
	public boolean hasArmorSlot() {
		return true;
	}
	
	public boolean isHorseArmor(ItemStack item) {
		return item.getItem() instanceof HorseArmorItem;
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

			if (hasStatusEffect(SpectrumStatusEffects.ETERNAL_SLUMBER)) {
				ascend(2);
			}

			if (hasStatusEffect(SpectrumStatusEffects.FATAL_SLUMBER)) {
				ascend(3);
			}

			if (hasStatusEffect(SpectrumStatusEffects.SOMNOLENCE) && getEepyTime() == 0) {
				setEepyTime(100);
			}

			var eepy = getEepyTime();

			if (eepy > 1) {
				setEepyTime(eepy - 1);
			}
			else if (eepy == 1){
				setEepyTime(0);
				ascend(1);
			}
		}
		if (this.age % 600 == 0) {
			this.heal(1.0F);
		}
	}

	@Override
	public boolean canExplosionDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float explosionPower) {
		return super.canExplosionDestroyBlock(explosion, world, pos, state, explosionPower);
	}

	private void ascend(int blastMod) {
		var world = getWorld();

		world.addParticle(ParticleTypes.EXPLOSION_EMITTER, getX(), getY(), getZ(), 1.0, 0.0, 0.0);
		world.createExplosion(this, SpectrumDamageTypes.incandescence(world), null, getX(), getY(), getZ(), 10F * blastMod, true, World.ExplosionSourceType.MOB);
		playSound(SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, 2F, 0.5F);
		playSound(SpectrumSoundEvents.DEEP_CRYSTAL_RING, 2F, 0.334F);
		playSound(SoundEvents.ENTITY_ENDER_DRAGON_AMBIENT, 1F, 2F);

		((ServerWorld) world).getPlayers(p -> p.distanceTo(this) < 64).forEach( p -> {
			Support.grantAdvancementCriterion(p, "ascend_kindling", "he_explarded");
		});

		for (int i = 0; i < 5; i++) {
			((ServerWorld) world).spawnParticles(ParticleTypes.DRAGON_BREATH, getParticleX(1.5), getY() + random.nextDouble(), getParticleZ(1.5), random.nextInt(6) + 1, 0, random.nextFloat() / 3, 0, 0);
			((ServerWorld) world).spawnParticles(ParticleTypes.END_ROD, getParticleX(1.5), getY() + random.nextDouble(), getParticleZ(1.5), random.nextInt(6) + 1, 0, random.nextFloat() / 3, 0, 0);
		}

		for (BlockPos transmutePos : BlockPos.iterateOutwards(getBlockPos(), 12 * blastMod, 6 * blastMod, 12 * blastMod)) {
			var distance = Math.sqrt(transmutePos.getSquaredDistance(getBlockPos()));
			if (distance <= 6 * blastMod || random.nextFloat() < 1 / ((distance - 6) / 3)) {
				var candidate = world.getBlockState(transmutePos);
				
				// Do not the bedrock nor the claims
				if (candidate.getHardness(world, transmutePos) < 0 || !GenericClaimModsCompat.canBreak(world, transmutePos, this))
					continue;

				if (candidate.isAir()) {
					if (random.nextFloat() < 0.125F) {
						((ServerWorld) world).spawnParticles(ParticleTypes.DRAGON_BREATH, transmutePos.getX() + random.nextDouble(), transmutePos.getY() + random.nextDouble(), transmutePos.getZ() + random.nextDouble(), random.nextInt(3) + 1, random.nextFloat() / 5 - 0.1, random.nextFloat() / 5 - 0.1, random.nextFloat() / 5 - 0.1, 0);
					}
					continue;
				}

				if (!GenericClaimModsCompat.canModify(world, transmutePos, this)) {
					continue;
				}

				if (candidate.getFluidState().isIn(FluidTags.WATER)) {
					continue;
				}

				if (random.nextFloat() < 0.025F) {
					world.setBlockState(transmutePos, Blocks.MAGMA_BLOCK.getDefaultState());
					continue;
				}

				if (candidate.isIn(BlockTags.BASE_STONE_OVERWORLD) || candidate.isIn(BlockTags.BASE_STONE_NETHER)) {

					if (random.nextFloat() < 0.05F) {
						world.setBlockState(transmutePos, Blocks.CRYING_OBSIDIAN.getDefaultState());
					}
					else {
						world.setBlockState(transmutePos, Blocks.END_STONE.getDefaultState());
					}

					continue;
				}

				if (candidate.isIn(SpectrumBlockTags.BASE_STONE_DEEPER_DOWN)) {
					world.setBlockState(transmutePos, SpectrumBlocks.BLACK_MATERIA.getDefaultState());
					continue;
				}

				if (candidate.isIn(BlockTags.LOGS)) {
					world.setBlockState(transmutePos, Blocks.COAL_BLOCK.getDefaultState());
					continue;
				}

				if (candidate.isIn(BlockTags.DIRT)) {
					world.setBlockState(transmutePos, Blocks.BROWN_STAINED_GLASS.getDefaultState());
					continue;
				}

				if (candidate.isOf(Blocks.CLAY)) {
					world.setBlockState(transmutePos, Blocks.TERRACOTTA.getDefaultState());
					continue;
				}

				if (candidate.isIn(BlockTags.SAND)) {
					world.setBlockState(transmutePos, Blocks.WHITE_STAINED_GLASS.getDefaultState());
					continue;
				}

				if (candidate.isOf(Blocks.OBSIDIAN)) {
					world.setBlockState(transmutePos, Blocks.CRYING_OBSIDIAN.getDefaultState());
				}
			}
		}
		remove(RemovalReason.DISCARDED);
		var lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
		lightning.setPos(getX(), getY(), getZ());
		world.spawnEntity(lightning);
	}

	@Override
	public void tickMovement() {
		super.tickMovement();
		
		Vec3d velocity = this.getVelocity();
		boolean onGround = this.isOnGround();
		if (!onGround && velocity.y < 0.0) {
			this.setVelocity(velocity.multiply(1.0, 0.6, 1.0));
		}
		if (onGround || this.fallDistance < 0.2) {
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
		if (this.getAngerTime() > 0) {
			return ActionResult.FAIL;
		}
		
		// clipping using shears
		ItemStack handStack = player.getMainHandStack();
		if (this.isShearable() && handStack.isIn(ConventionalItemTags.SHEARS)) {
			
			if (!this.getWorld().isClient()) {
				setTarget(player);
				takeRevenge(player.getUuid());
				this.playAngrySound();
				
				this.sheared(SoundCategory.PLAYERS);
				this.emitGameEvent(GameEvent.SHEAR, player);
				if (!this.getWorld().isClient) {
					handStack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));
				}
			}
			
			return ActionResult.success(this.getWorld().isClient());
		}
		
		boolean bl = !this.isBaby() && this.isTame() && player.shouldCancelInteraction();
		if (!this.hasPassengers() && !bl) {
			if (!handStack.isEmpty()) {
				if (this.isBreedingItem(handStack)) {
					return this.interactHorse(player, handStack);
				}
				
				if (!this.isTame()) {
					this.playAngrySound();
					return ActionResult.success(this.getWorld().isClient);
				}
			}
			
			if (player.shouldCancelInteraction()) {
				return super.interactMob(player, hand);
			}
		}
		
		return super.interactMob(player, hand);
	}
	
	@Override
	public void sheared(SoundCategory shearedSoundCategory) {
		this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_SHEEP_SHEAR, shearedSoundCategory, 1.0f, 1.0f);
		
		setClipped(4800); // 4 minutes
		for (ItemStack clippedStack : getClippedStacks((ServerWorld) this.getWorld())) {
			dropStack(clippedStack, 0.3F);
		}
	}
	
	@Override
	public boolean isShearable() {
		return this.isAlive() && !this.isBaby() && !this.isClipped();
	}
	
	@Override
	protected boolean receiveFood(PlayerEntity player, ItemStack item) {
		boolean canEat = false;
		
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
		} else if (!this.getWorld().isClient && !this.isInLove()) {
			this.lovePlayer(player);
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
	
	public List<ItemStack> getClippedStacks(ServerWorld world) {
		LootTable lootTable = world.getServer().getLootManager().getLootTable(this.getKindlingVariant().clippingLootTable());
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

	public void setEepyTime(int eepySneeze) {
		this.dataTracker.set(EEPY_SNEEZE, eepySneeze);
	}

	public int getEepyTime() {
		return this.dataTracker.get(EEPY_SNEEZE);
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
			super(kindling, 0.6F, true);
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
			if (hasAngerTime() || hasPassengers() || isInLove())
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
			return KindlingEntity.this.hasAngerTime() && super.shouldContinue() && distanceTo(getProjectileTarget()) > 3F;
		}
		
		@Override
		public boolean canStart() {
			return super.canStart() && !isPlaying() && distanceTo(getProjectileTarget()) > 4F;
		}
		
		protected LivingEntity getProjectileTarget() {
			return ((ProjectileAttackGoalAccessor) this).getProjectileAttackTarget();
		}
		
	}
}