package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.variants.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
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
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.stream.*;

public class LizardEntity extends TameableEntity implements PackEntity<LizardEntity> {
	
	private static final TrackedData<LizardScaleVariant> SCALE_VARIANT = DataTracker.registerData(LizardEntity.class, SpectrumTrackedDataHandlerRegistry.LIZARD_SCALE_VARIANT);
	private static final TrackedData<LizardFrillVariant> FRILL_VARIANT = DataTracker.registerData(LizardEntity.class, SpectrumTrackedDataHandlerRegistry.LIZARD_FRILL_VARIANT);
	private static final TrackedData<LizardHornVariant> HORN_VARIANT = DataTracker.registerData(LizardEntity.class, SpectrumTrackedDataHandlerRegistry.LIZARD_HORN_VARIANT);
	
	private @Nullable LizardEntity leader;
	private int groupSize = 1;
	
	public LizardEntity(EntityType<? extends LizardEntity> entityType, World world) {
		super(entityType, world);
		this.experiencePoints = 4;
	}
	
	public static DefaultAttributeContainer.Builder createLizardAttributes() {
		return MobEntity.createMobAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 60.0D)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 16.0D)
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 2.0D)
				.add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 2.0D)
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
		this.goalSelector.add(5, new ClanLeaderWanderAroundGoal(this, 0.8));
		this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(8, new LookAroundGoal(this));
		
		this.targetSelector.add(1, new RevengeGoal(this).setGroupRevenge());
		this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true, target -> !LizardEntity.this.isOwner(target)));
		this.targetSelector.add(3, new ActiveTargetGoal<>(this, LivingEntity.class, true, // different clans attacking each other
				target -> {
					if (target instanceof LizardEntity other) {
						return LizardEntity.this.hasLeader() && other.hasLeader() && LizardEntity.this.leader != other.leader;
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
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(SCALE_VARIANT, LizardScaleVariant.CYAN);
		this.dataTracker.startTracking(FRILL_VARIANT, LizardFrillVariant.SIMPLE);
		this.dataTracker.startTracking(HORN_VARIANT, LizardHornVariant.HORNY);
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putString("Scales", SpectrumRegistries.LIZARD_SCALE_VARIANT.getId(this.getScales()).toString());
		nbt.putString("Frills", SpectrumRegistries.LIZARD_FRILL_VARIANT.getId(this.getFrills()).toString());
		nbt.putString("Horns", SpectrumRegistries.LIZARD_HORN_VARIANT.getId(this.getHorns()).toString());
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		LizardScaleVariant scales = SpectrumRegistries.LIZARD_SCALE_VARIANT.get(Identifier.tryParse(nbt.getString("Scales")));
		if (scales != null) {
			this.setScales(scales);
		}
		LizardFrillVariant frills = SpectrumRegistries.LIZARD_FRILL_VARIANT.get(Identifier.tryParse(nbt.getString("Frills")));
		if (frills != null) {
			this.setFrills(frills);
		}
		LizardHornVariant horns = SpectrumRegistries.LIZARD_HORN_VARIANT.get(Identifier.tryParse(nbt.getString("Horns")));
		if (horns != null) {
			this.setHorns(horns);
		}
	}
	
	@Override
	protected void eat(PlayerEntity player, Hand hand, ItemStack stack) {
		super.eat(player, hand, stack);
		// yes, this also overrides the existing owner
		// there is no god besides the new god
		setOwner(player);
	}
	
	public LizardScaleVariant getScales() {
		return this.dataTracker.get(SCALE_VARIANT);
	}
	
	public void setScales(LizardScaleVariant variant) {
		this.dataTracker.set(SCALE_VARIANT, variant);
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
		return SoundEvents.ENTITY_PIG_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_PIG_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_PIG_DEATH;
	}
	
	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(SoundEvents.ENTITY_PIG_STEP, 0.15F, 1.0F);
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
			child.setScales(getChildScales(this, other));
			child.setFrills(getChildFrills(this, other));
			child.setHorns(getChildHorns(this, other));
		}
		return child;
	}
	
	private LizardFrillVariant getChildFrills(LizardEntity firstParent, LizardEntity secondParent) {
		return this.world.random.nextBoolean() ? firstParent.getFrills() : secondParent.getFrills();
	}
	
	private LizardScaleVariant getChildScales(LizardEntity firstParent, LizardEntity secondParent) {
		return this.world.random.nextBoolean() ? firstParent.getScales() : secondParent.getScales();
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
	public boolean hasLeader() {
		return this.leader != null && this.leader.isAlive();
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
	public boolean canHaveMoreInGroup() {
		return this.hasOthersInGroup() && this.groupSize < this.getMaxGroupSize();
	}
	
	@Override
	public void pullInOthers(Stream<? extends PackEntity> stream) {
		stream.limit((this.getMaxGroupSize() - this.groupSize)).filter((e) -> e != this).forEach((e) -> e.joinGroupOf(this));
	}
	
	protected void increaseGroupSize() {
		++this.groupSize;
	}
	
	protected void decreaseGroupSize() {
		--this.groupSize;
	}
	
	protected class ClanLeaderWanderAroundGoal extends WanderAroundGoal {
		
		public ClanLeaderWanderAroundGoal(PathAwareEntity mob, double speed) {
			super(mob, speed);
		}
		
		@Override
		public boolean canStart() {
			return !LizardEntity.this.hasLeader() && super.canStart();
		}
		
	}
	
	
}
