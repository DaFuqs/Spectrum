package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.variants.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.data.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.tag.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.*;
import net.minecraft.world.poi.*;
import org.jetbrains.annotations.*;

// funny little creatures always out for trouble
public class LizardEntity extends TameableEntity implements PackEntity<LizardEntity>, POIMemorized {
	
	protected static final TrackedData<LizardFrillVariant> FRILL_VARIANT = DataTracker.registerData(LizardEntity.class, SpectrumTrackedDataHandlerRegistry.LIZARD_FRILL_VARIANT);
	protected static final TrackedData<LizardHornVariant> HORN_VARIANT = DataTracker.registerData(LizardEntity.class, SpectrumTrackedDataHandlerRegistry.LIZARD_HORN_VARIANT);
	protected static final TrackedData<InkColor> COLOR = DataTracker.registerData(LizardEntity.class, SpectrumTrackedDataHandlerRegistry.INK_COLOR);
	
	protected @Nullable LizardEntity leader;
	protected int groupSize = 1;
	
	protected int ticksLeftToFindPOI;
	protected @Nullable BlockPos poiPos;
	
	public LizardEntity(EntityType<? extends LizardEntity> entityType, World world) {
		super(entityType, world);
		this.experiencePoints = 4;
	}
	
	public static DefaultAttributeContainer.Builder createLizardAttributes() {
		return MobEntity.createMobAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 60.0D)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 16.0D)
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D)
				.add(EntityAttributes.GENERIC_ARMOR, 6.0D)
				.add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 1.0D)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2D)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 12.0D);
	}
	
	@Override
	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(1, new SwimGoal(this));
		this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));
		this.goalSelector.add(3, new AttackGoal(this));
		this.goalSelector.add(4, new FollowParentGoal(this, 1.2D));
		this.goalSelector.add(4, new FollowClanLeaderGoal<>(this));
		this.goalSelector.add(5, new FindPOIGoal(PointOfInterestTypes.LODESTONE, 32));
		this.goalSelector.add(6, new ClanLeaderWanderAroundGoal(this, 0.8, 20, 8, 4));
		this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(8, new LookAroundGoal(this));
		
		this.targetSelector.add(1, new RevengeGoal(this).setGroupRevenge());
		this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true, target -> !LizardEntity.this.isOwner(target)));
		this.targetSelector.add(3, new ActiveTargetGoal<>(this, LivingEntity.class, true, // different clans attacking each other
				target -> {
					if (target instanceof LizardEntity other) {
						return isDifferentPack(other);
					}
					return !target.isBaby();
				}));
	}
	
	@Override
	public float getBrightnessAtEyes() {
		return 1.0F;
	}
	
	@Override
	public boolean isOwner(LivingEntity entity) {
		return entity == this.getOwner() || this.leader != null && entity == this.leader.getOwner();
	}
	
	@Override
	protected void mobTick() {
		super.mobTick();
		if (this.age % 1200 == 0) {
			this.heal(1.0F);
		}
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(COLOR, InkColors.MAGENTA);
		this.dataTracker.startTracking(FRILL_VARIANT, LizardFrillVariant.SIMPLE);
		this.dataTracker.startTracking(HORN_VARIANT, LizardHornVariant.HORNY);
	}
	
	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
		this.setFrills(SpectrumRegistries.getRandomTagEntry(SpectrumRegistries.LIZARD_FRILL_VARIANT, LizardFrillVariant.NATURAL_VARIANT, world.getRandom(), LizardFrillVariant.SIMPLE));
		this.setHorns(SpectrumRegistries.getRandomTagEntry(SpectrumRegistries.LIZARD_HORN_VARIANT, LizardHornVariant.NATURAL_VARIANT, world.getRandom(), LizardHornVariant.HORNY));
		this.setColor(SpectrumRegistries.getRandomTagEntry(SpectrumRegistries.INK_COLORS, InkColorTags.ELEMENTAL_COLORS, world.getRandom(), InkColors.MAGENTA));
		
		return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putString("color", SpectrumRegistries.INK_COLORS.getId(this.getColor()).toString());
		nbt.putString("frills", SpectrumRegistries.LIZARD_FRILL_VARIANT.getId(this.getFrills()).toString());
		nbt.putString("horns", SpectrumRegistries.LIZARD_HORN_VARIANT.getId(this.getHorns()).toString());
		writePOIPosToNbt(nbt);
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		
		InkColor color = SpectrumRegistries.INK_COLORS.get(Identifier.tryParse(nbt.getString("color")));
		this.setColor(color == null ? SpectrumRegistries.getRandomTagEntry(SpectrumRegistries.INK_COLORS, InkColorTags.ELEMENTAL_COLORS, world.random, InkColors.CYAN) : color);
		
		LizardFrillVariant frills = SpectrumRegistries.LIZARD_FRILL_VARIANT.get(Identifier.tryParse(nbt.getString("frills")));
		this.setFrills(frills == null ? SpectrumRegistries.getRandomTagEntry(SpectrumRegistries.LIZARD_FRILL_VARIANT, LizardFrillVariant.NATURAL_VARIANT, world.random, LizardFrillVariant.SIMPLE) : frills);
		
		LizardHornVariant horns = SpectrumRegistries.LIZARD_HORN_VARIANT.get(Identifier.tryParse(nbt.getString("horns")));
		this.setHorns(horns == null ? SpectrumRegistries.getRandomTagEntry(SpectrumRegistries.LIZARD_HORN_VARIANT, LizardHornVariant.NATURAL_VARIANT, world.random, LizardHornVariant.HORNY) : horns);
		
		readPOIPosFromNbt(nbt);
	}
	
	@Override
	public void tickMovement() {
		super.tickMovement();
		if (!this.world.isClient && this.ticksLeftToFindPOI > 0) {
			--this.ticksLeftToFindPOI;
		}
	}
	
	@Override
	public ActionResult interactMob(PlayerEntity player, Hand hand) {
		ItemStack itemStack = player.getStackInHand(hand);
		if (this.isBreedingItem(itemStack)) {
			int i = this.getBreedingAge();
			if (!this.world.isClient && i == 0 && this.canEat() && this.random.nextInt(5) == 0) {
				// yes, this also overrides the existing owner
				// there is no god besides the new god
				this.eat(player, hand, itemStack);
				this.setOwner(player);
				this.lovePlayer(player);
				return ActionResult.SUCCESS;
			}
			
			if (this.isBaby()) {
				this.eat(player, hand, itemStack);
				this.growUp(toGrowUpAge(-i), true);
				return ActionResult.success(this.world.isClient);
			}
			
			if (this.world.isClient) {
				return ActionResult.CONSUME;
			}
		}
		
		return ActionResult.PASS;
	}
	
	@Override
	public boolean canEat() {
		return super.canEat() || getOwner() != null;
	}
	
	public InkColor getColor() {
		return this.dataTracker.get(COLOR);
	}
	
	public void setColor(InkColor color) {
		this.dataTracker.set(COLOR, color);
	}
	
	public LizardFrillVariant getFrills() {
		return this.dataTracker.get(FRILL_VARIANT);
	}
	
	public void setFrills(LizardFrillVariant variant) {
		this.dataTracker.set(FRILL_VARIANT, variant);
	}
	
	public LizardHornVariant getHorns() {
		return this.dataTracker.get(HORN_VARIANT);
	}
	
	public void setHorns(LizardHornVariant variant) {
		this.dataTracker.set(HORN_VARIANT, variant);
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return SpectrumSoundEvents.ENTITY_LIZARD_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SpectrumSoundEvents.ENTITY_LIZARD_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return SpectrumSoundEvents.ENTITY_LIZARD_DEATH;
	}
	
	@Override
	protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
		return 0.5F * dimensions.height;
	}
	
	// Breeding
	
	@Override
	public boolean isBreedingItem(ItemStack stack) {
		if (stack.isOf(SpectrumItems.LIZARD_MEAT)) {
			return false;
		}
		FoodComponent food = stack.getItem().getFoodComponent();
		return food != null && food.isMeat();
	}
	
	@Override
	public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
		LizardEntity other = (LizardEntity) entity;
		LizardEntity child = SpectrumEntityTypes.LIZARD.create(world);
		if (child != null) {
			child.setColor(getChildColor(this, other));
			child.setFrills(getChildFrills(this, other));
			child.setHorns(getChildHorns(this, other));
		}
		return child;
	}
	
	private InkColor getChildColor(LizardEntity firstParent, LizardEntity secondParent) {
		InkColor color1 = firstParent.getColor();
		InkColor color2 = secondParent.getColor();
		
		return InkColor.getRandomMixedColor(color1, color2, firstParent.world.random);
	}
	
	private LizardFrillVariant getChildFrills(LizardEntity firstParent, LizardEntity secondParent) {
		return this.world.random.nextBoolean() ? firstParent.getFrills() : secondParent.getFrills();
	}
	
	private LizardHornVariant getChildHorns(LizardEntity firstParent, LizardEntity secondParent) {
		return this.world.random.nextBoolean() ? firstParent.getHorns() : secondParent.getHorns();
	}
	
	// PackEntity
	
	@Override
	public boolean hasOthersInGroup() {
		return this.groupSize > 1;
	}
	
	@Override
	public @Nullable LizardEntity getLeader() {
		return this.leader;
	}
	
	@Override
	public boolean isCloseEnoughToLeader() {
		return this.squaredDistanceTo(this.leader) <= 121.0;
	}
	
	@Override
	public void leaveGroup() {
		this.leader.decreaseGroupSize();
		this.leader = null;
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
	public void joinGroupOf(LizardEntity groupLeader) {
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
	
	// POIMemorized
	@Override
	public TagKey<PointOfInterestType> getPOITag() {
		return SpectrumPointOfInterestTypeTags.LIZARD_DENS;
	}
	
	@Override
	public @Nullable BlockPos getPOIPos() {
		return this.poiPos;
	}
	
	@Override
	public void setPOIPos(@Nullable BlockPos blockPos) {
		this.poiPos = blockPos;
	}
	
	// Goals
	protected class ClanLeaderWanderAroundGoal extends WanderAroundGoal {
		
		int chanceToNavigateToPOI;
		int maxDistanceFromPOI;
		
		public ClanLeaderWanderAroundGoal(PathAwareEntity mob, double speed, int chance, int chanceToNavigateToPOI, int maxDistanceFromPOI) {
			super(mob, speed, chance);
			this.chanceToNavigateToPOI = chanceToNavigateToPOI;
			this.maxDistanceFromPOI = maxDistanceFromPOI;
		}
		
		@Override
		public boolean canStart() {
			return !LizardEntity.this.hasLeader() && super.canStart();
		}
		
		@Override
		protected @Nullable Vec3d getWanderTarget() {
			// when we are away from our poi (their den) there is a chance they navigate back to it, so they always stay near
			if (random.nextFloat() < this.chanceToNavigateToPOI
					&& LizardEntity.this.isPOIValid((ServerWorld) LizardEntity.this.world)
					&& !LizardEntity.this.getBlockPos().isWithinDistance(LizardEntity.this.poiPos, this.maxDistanceFromPOI)) {
				
				return Vec3d.ofCenter(LizardEntity.this.poiPos);
			}
			
			return NoPenaltyTargeting.find(LizardEntity.this, 8, 7);
		}
		
	}
	
	private class FindPOIGoal extends Goal {
		
		RegistryKey<PointOfInterestType> poiType;
		int maxDistance;
		
		FindPOIGoal(RegistryKey<PointOfInterestType> poiType, int maxDistance) {
			super();
			this.poiType = poiType;
			this.maxDistance = maxDistance;
		}
		
		@Override
		public boolean canStart() {
			return LizardEntity.this.hasOthersInGroup()
					&& LizardEntity.this.ticksLeftToFindPOI == 0
					&& !LizardEntity.this.isPOIValid((ServerWorld) LizardEntity.this.world);
		}
		
		@Override
		public void start() {
			LizardEntity.this.ticksLeftToFindPOI = 200;
			LizardEntity.this.poiPos = LizardEntity.this.findNearestPOI((ServerWorld) LizardEntity.this.world, LizardEntity.this.getBlockPos(), 40);
		}
		
	}
	
	
}
