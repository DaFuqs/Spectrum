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
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class LizardEntity extends TameableEntity {
	
	private static final TrackedData<LizardScaleVariant> SCALE_VARIANT = DataTracker.registerData(LizardEntity.class, SpectrumTrackedDataHandlerRegistry.LIZARD_SCALE_VARIANT);
	private static final TrackedData<LizardFrillVariant> FRILL_VARIANT = DataTracker.registerData(LizardEntity.class, SpectrumTrackedDataHandlerRegistry.LIZARD_FRILL_VARIANT);
	private static final TrackedData<LizardHornVariant> HORN_VARIANT = DataTracker.registerData(LizardEntity.class, SpectrumTrackedDataHandlerRegistry.LIZARD_HORN_VARIANT);
	
	public LizardEntity(EntityType<? extends LizardEntity> entityType, World world) {
		super(entityType, world);
		this.experiencePoints = 4;
	}
	
	public static DefaultAttributeContainer.Builder createLizardAttributes() {
		return MobEntity.createMobAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0D)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.12D);
	}
	
	@Override
	public boolean isBreedingItem(ItemStack stack) {
		FoodComponent food = stack.getItem().getFoodComponent();
		return food != null && food.isMeat();
	}
	
	@Override
	public float getBrightnessAtEyes() {
		return 1.0F;
	}
	
	@Override
	protected void initGoals() {
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));
		this.goalSelector.add(4, new FollowParentGoal(this, 1.1D));
		this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0D));
		this.goalSelector.add(7, new LookAtEntityGoal(this, LivingEntity.class, 8.0F));
		this.goalSelector.add(8, new LookAroundGoal(this));
		
		this.targetSelector.add(1, new RevengeGoal(this).setGroupRevenge());
		this.targetSelector.add(2, new ActiveTargetGoal<>(this, LivingEntity.class, false));
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(SCALE_VARIANT, LizardScaleVariant.CYAN);
		this.dataTracker.startTracking(FRILL_VARIANT, LizardFrillVariant.SIMPLE);
		this.dataTracker.startTracking(HORN_VARIANT, LizardHornVariant.HORNY);
	}
	
	@Nullable
	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
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
		return 0.8F * dimensions.height;
	}
	
}
