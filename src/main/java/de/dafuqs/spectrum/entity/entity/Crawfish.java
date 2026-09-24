package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.syncher.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.*;

import java.util.*;
import java.util.function.*;

public class Crawfish extends TamableAnimal implements NeutralMob {
	
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
	private @Nullable UUID persistentAngerTarget;
	private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(Crawfish.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> DATA_XP = SynchedEntityData.defineId(Crawfish.class, EntityDataSerializers.INT);
	
    public Crawfish(EntityType<? extends Crawfish> entityType, Level level) {
        super(entityType, level);
		this.setPathfindingMalus(PathType.WATER_BORDER, 0.0F);
		this.setPathfindingMalus(PathType.WATER, 0.0F);
    }
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.1F));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0, false));
		this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F));
		this.goalSelector.addGoal(6, new BreedGoal(this, 1.0));
		this.goalSelector.addGoal(7, new CollectXPGoal<>(this, 1.0));
		this.goalSelector.addGoal(8, new RandomStrollGoal(this, 1.0));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
		this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, otherEntity -> {
			if(otherEntity.getType() == EntityType.RABBIT) {
				return true;
			}
			if(otherEntity.getType() == SpectrumEntityTypes.CRAWFISH.get()) {
				return true;
			}
			return hatesThisEntityInParticular(otherEntity);
		}));
	}
	
	@Override
	public boolean isPushedByFluid() {
		return true;
	}
	
	protected boolean hatesThisEntityInParticular(LivingEntity other) {
		long a = other.getUUID().getLeastSignificantBits() & 0x10;
		long b = this.getUUID().getLeastSignificantBits() & 0x10;
		return a == b;
	}
	
	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		if (DATA_XP.equals(key)) {
			this.updateXPDependentValues();
		}
		super.onSyncedDataUpdated(key);
	}
	
	public static AttributeSupplier.Builder createCrawfishAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 20.0)
				.add(Attributes.MOVEMENT_SPEED, 0.3)
				.add(Attributes.ATTACK_DAMAGE, 6.0)
				.add(Attributes.ARMOR, 3)
				.add(Attributes.ARMOR_TOUGHNESS, 1);
	}
	
	private void updateXPDependentValues() {
		this.refreshDimensions();
		
		double xp = Math.min(this.getCollectedXP(), 1000);
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20 + (xp / 20.0));
		this.getAttribute(Attributes.SCALE).setBaseValue(1.0 + (xp / 20.0));
		this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(6 + (xp / 20));
		this.getAttribute(Attributes.ARMOR).setBaseValue(3 + (xp / 50));
		this.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(1 + (xp / 200));
	}
	
	// Living XP bottles
	@Override
	protected int getBaseExperienceReward() {
		return 1 + getCollectedXP();
	}
	
	@Override
	protected int decreaseAirSupply(int currentAir) {
		return currentAir;
	}
	
	public void setCollectedXP(int xp) {
		this.entityData.set(DATA_XP, xp);
	}
	
	public int getCollectedXP() {
		return this.entityData.get(DATA_XP);
	}
	
	@Override
	public boolean canBeLeashed() {
		return !this.isAngry();
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_REMAINING_ANGER_TIME, 0);
		builder.define(DATA_XP, 0);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		this.addPersistentAngerSaveData(compound);
		this.setCollectedXP(compound.getInt("collected_xp"));
	}
	
	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.readPersistentAngerSaveData(this.level(), compound);
		compound.putInt("collected_xp", this.getCollectedXP());
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.SILVERFISH_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return SoundEvents.SILVERFISH_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.SILVERFISH_DEATH;
	}
	
	@Override
	protected void playStepSound(BlockPos pos, BlockState block) {
		this.playSound(SoundEvents.SILVERFISH_STEP, 0.15F, 1.0F);
	}
	
	@Override
	public boolean isFood(ItemStack stack) {
		return stack.is(SpectrumItemTags.CRAWFISH_FOOD);
	}
	
	@Override
	public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
		return SpectrumEntityTypes.CRAWFISH.get().create(level);
	}
	
	public boolean isAngryAtAllPlayers(Level level) {
		return this.isAngry();
	}
	
	@Override
	public int getRemainingPersistentAngerTime() {
		return this.entityData.get(DATA_REMAINING_ANGER_TIME);
	}
	
	@Override
	public void setRemainingPersistentAngerTime(int time) {
		this.entityData.set(DATA_REMAINING_ANGER_TIME, time);
	}
	
	@Override
	public void startPersistentAngerTimer() {
		this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
	}
	
	@Override
	public @Nullable UUID getPersistentAngerTarget() {
		return this.persistentAngerTarget;
	}
	
	@Override
	public void setPersistentAngerTarget(@javax.annotation.Nullable UUID target) {
		this.persistentAngerTarget = target;
	}
	
	public static class CollectXPGoal<T extends Crawfish> extends Goal {
		
		private final T mob;
		private final double speedModifier;
		private @Nullable ExperienceOrb experienceOrb;
		private int ticksUntilNextPathRecalculation;
		
		public CollectXPGoal(T mob, double speedModifier) {
			this.mob = mob;
			this.speedModifier = speedModifier;
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET));
		}
		
		@Override
		public boolean canUse() {
			if(!this.mob.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
				return false;
			}
			
			List<ExperienceOrb> orbs = this.mob.level().getEntitiesOfClass(ExperienceOrb.class, mob.getBoundingBox().expandTowards(10, 4, 10));
			if(orbs.isEmpty()) {
				this.experienceOrb = null;
				return false;
			}
 			this.experienceOrb = orbs.getFirst();
			return true;
		}
		
		@Override
		public void start() {
			this.mob.getNavigation().moveTo(experienceOrb, this.speedModifier);
		}
		
		@Override
		public void stop() {
			super.stop();
		}
		
		@Override
		public void tick() {
			if(experienceOrb == null || experienceOrb.isRemoved()) {
				this.experienceOrb = null;
				stop();
				return;
			}
			
			if (--this.ticksUntilNextPathRecalculation <= 0) {
				// in case the orb moved
				this.mob.getNavigation().moveTo(experienceOrb, this.speedModifier);
				this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
			}
			
			if(this.mob.getAttackBoundingBox().intersects(experienceOrb.getBoundingBox())) {
				mob.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.75F, 1.0F);
				mob.setCollectedXP(mob.getCollectedXP() + experienceOrb.getValue());
				experienceOrb.discard();
				stop();
			}
		}
	}
	
}
