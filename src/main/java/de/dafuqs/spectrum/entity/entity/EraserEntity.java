package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.stream.*;

public class EraserEntity extends SpiderEntity implements PackEntity<EraserEntity> {
	
	private @Nullable StatusEffectInstance effectOnHit;
	
	private @Nullable EraserEntity leader;
	private int groupSize = 1;
	
	public EraserEntity(EntityType<? extends EraserEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public static DefaultAttributeContainer.Builder createEraserAttributes() {
		return HostileEntity.createHostileAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 28.0)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0);
	}
	
	@Override
	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(1, new SwimGoal(this));
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
	public void joinGroupOf(EraserEntity groupLeader) {
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
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		
		if (this.effectOnHit != null) {
			NbtCompound effectNbt = new NbtCompound();
			this.effectOnHit.writeNbt(effectNbt);
			nbt.put("EffectOnHit", effectNbt);
		}
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		
		if (nbt.contains("EffectOnHit", NbtElement.COMPOUND_TYPE)) {
			this.effectOnHit = StatusEffectInstance.fromNbt(nbt.getCompound("EffectOnHit"));
		}
	}
	
	public StatusEffectInstance getRandomOnHitEffect() {
		Difficulty difficulty = this.world.getDifficulty();
		
		StatusEffect statusEffect;
		int amplifier = 0;
		switch (world.random.nextInt(8)) {
			case 1 -> {
				statusEffect = SpectrumStatusEffects.STIFFNESS;
				amplifier = 2;
			}
			case 2 -> {
				statusEffect = SpectrumStatusEffects.FRENZY;
				amplifier = 2;
			}
			case 3 -> {
				statusEffect = SpectrumStatusEffects.SCARRED;
			}
			default -> {
				statusEffect = SpectrumStatusEffects.DEADLY_POISON;
				amplifier = 2;
			}
		}
		
		int duration = difficulty == Difficulty.HARD ? 60 : difficulty == Difficulty.NORMAL ? 40 : 20;
		return new StatusEffectInstance(statusEffect, duration * 20, amplifier);
	}
	
	@Override
	protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
		return 0.2F;
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
					this.amplifier = 1 + random.nextInt(2);
				}
				case 1 -> {
					this.effect = StatusEffects.STRENGTH;
					this.amplifier = 1 + random.nextInt(2);
				}
				case 2 -> {
					this.effect = StatusEffects.REGENERATION;
					this.amplifier = 1 + random.nextInt(2);
				}
				case 3 -> {
					this.effect = StatusEffects.INVISIBILITY;
				}
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
