package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
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
import net.minecraft.nbt.*;
import net.minecraft.recipe.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class KindlingEntity extends AbstractHorseEntity implements RangedAttackMob, Angerable /*,Flutterer*/ {
	
	protected static final Ingredient FOOD = Ingredient.fromTag(SpectrumItemTags.KINDLING_FOOD);
	protected static final Ingredient FOOD_IF_YOU_WANT_A_BAD_TIME = Ingredient.ofItems(SpectrumItems.BLOODBOIL_SYRUP.asItem());
	
	private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(30, 59);
	private static final TrackedData<Integer> ANGER = DataTracker.registerData(KindlingEntity.class, TrackedDataHandlerRegistry.INTEGER);
	
	private @Nullable UUID angryAt;
	protected boolean coughed;
	
	public KindlingEntity(EntityType<? extends KindlingEntity> entityType, World world) {
		super(entityType, world);
		this.setPathfindingPenalty(PathNodeType.WATER, -0.2F);
		this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
		this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 0.0F);
		this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 0.0F);
		this.experiencePoints = 8;
	}
	
	public static DefaultAttributeContainer.Builder createKindlingAttributes() {
		return MobEntity.createMobAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0D)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D);
	}
	
	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return FOOD.test(stack);
	}
	
	@Override
	protected void initGoals() {
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new HorseBondWithPlayerGoal(this, 1.4));
		this.goalSelector.add(2, new ProjectileAttackGoal(this, 1.25, 40, 20.0F));
		this.goalSelector.add(3, new AnimalMateGoal(this, 1.0D));
		this.goalSelector.add(4, new TemptGoal(this, 1.25, Ingredient.ofItems(Items.HAY_BLOCK), false));
		this.goalSelector.add(5, new FollowParentGoal(this, 1.1D));
		this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0D));
		this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.add(8, new LookAroundGoal(this));
		
		this.targetSelector.add(1, new CoughRevengeGoal(this));
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(ANGER, 0);
	}
	
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		this.writeAngerToNbt(nbt);
	}
	
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.readAngerFromNbt(this.world, nbt);
	}
	
	@Nullable
	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
		return SpectrumEntityTypes.KINDLING.create(world);
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_BLAZE_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_BLAZE_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_BLAZE_DEATH;
	}
	
	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(SoundEvents.ENTITY_PIG_STEP, 0.15F, 1.0F);
	}
	
	@Override
	public boolean hasWings() {
		return this.isInAir() && this.age % 2 == 0;
	}
	
	@Override
	public boolean isInAir() {
		return !this.onGround;
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
	public ActionResult interactMob(PlayerEntity player, Hand hand) {
		if (this.isAngry()) {
			return super.interactMob(player, hand);
		}
		if (!this.world.isClient) {
			player.getInventory().offerOrDrop(SpectrumItems.EFFULGENT_FEATHER.getDefaultStack());
			return ActionResult.CONSUME;
		} else {
			return ActionResult.PASS;
		}
	}
	
	private void coughAt(LivingEntity target) {
		KindlingCoughEntity kindlingCoughEntity = new KindlingCoughEntity(this.world, this);
		double d = target.getX() - this.getX();
		double e = target.getBodyY(0.33F) - kindlingCoughEntity.getY();
		double f = target.getZ() - this.getZ();
		double g = Math.sqrt(d * d + f * f) * 0.2;
		kindlingCoughEntity.setVelocity(d, e + g, f, 1.5F, 10.0F);
		if (!this.isSilent()) {
			this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ITEM_FIRECHARGE_USE, this.getSoundCategory(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
		}
		
		this.world.spawnEntity(kindlingCoughEntity);
		this.coughed = true;
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
	
	class CoughRevengeGoal extends RevengeGoal {
		
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
