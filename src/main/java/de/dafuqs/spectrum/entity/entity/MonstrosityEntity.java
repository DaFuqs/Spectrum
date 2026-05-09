package de.dafuqs.spectrum.entity.entity;

import com.google.common.collect.*;
import de.dafuqs.additionalentityattributes.*;
import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.ai.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.control.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.ai.navigation.*;
import net.minecraft.world.entity.ai.targeting.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import javax.annotation.*;

import java.util.*;
import java.util.function.*;

public class MonstrosityEntity extends SpectrumBossEntity implements RangedAttackMob {
	
	public static List<? extends MonstrosityEntity> getMonstrosities(ServerLevel level) {
		return level.getEntities(SpectrumEntityTypes.MONSTROSITY.get(), LivingEntity::isAlive);
	}
	
	public static List<? extends MonstrosityEntity> getMonstrosities(Level level, Vec3 center, int maxDistance) {
		return level.getEntitiesOfClass(MonstrosityEntity.class, AABB.ofSize(center, maxDistance, maxDistance, maxDistance), LivingEntity::isAlive);
	}
	
	public static final Predicate<LivingEntity> ENTITY_TARGETS = (entity) -> {
		if (entity instanceof Player player) {
			if (player.isSpectator() || player.isCreative()) {
				return false;
			}
			return !AdvancementHelper.hasAdvancement(player, SpectrumAdvancements.KILLED_MONSTROSITY);
		}
		return false;
	};
	private final TargetingConditions TARGET_PREDICATE = TargetingConditions.forCombat().selector(ENTITY_TARGETS);
	
	private static final float MAX_LIFE_LOST_PER_TICK = 20F;
	private static final int GROW_STRONGER_EVERY_X_TICKS = 400;
	
	private MovementType movementType = MovementType.SWOOPING_TO_POSITION;
	
	private float previousHealth;
	private int timesGottenStronger = 0;
	private int ticksWithoutTarget = 0;
	
	public MonstrosityEntity(EntityType<? extends MonstrosityEntity> entityType, Level world) {
		super(entityType, world);
		this.moveControl = new MonstrosityMoveControl(this);
		this.xpReward = 500;
		this.noPhysics = true;
		this.noCulling = true;
		this.previousHealth = getHealth();
	}
	
	@Override
	protected BodyRotationControl createBodyControl() {
		return new EmptyBodyControl(this);
	}
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new StartSwoopAttackGoal());
		this.goalSelector.addGoal(2, new SwoopMovementGoal());
		this.goalSelector.addGoal(3, new RetreatAndAttackGoal(40));
		this.goalSelector.addGoal(3, new RangedAttackGoal(this, 1.0, 40, 28.0F));
		
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, false, false, ENTITY_TARGETS));
		this.targetSelector.addGoal(2, new FindTargetGoal());
	}
	
	@Override
	protected void customServerAiStep() {
		float currentHealth = this.getHealth();
		if (currentHealth < this.previousHealth - MAX_LIFE_LOST_PER_TICK) {
			this.setHealth(this.previousHealth - MAX_LIFE_LOST_PER_TICK);
		}
		this.previousHealth = currentHealth;
		this.tickInvincibility();
		
		if (!this.level().isClientSide() && this.tickCount % GROW_STRONGER_EVERY_X_TICKS == 0) {
			this.growStronger(1);
		}
		
		destroyBlocks(this.getBoundingBox());
		
		super.customServerAiStep();
		
		if (this.tickCount % 10 == 0) {
			this.heal(1.0F);
		}
	}
	
	private boolean destroyBlocks(AABB area) {
		int i = Mth.floor(area.minX);
		int j = Mth.floor(area.minY);
		int k = Mth.floor(area.minZ);
		int l = Mth.floor(area.maxX);
		int m = Mth.floor(area.maxY);
		int n = Mth.floor(area.maxZ);
		boolean bl = false;
		boolean bl2 = false;
		
		for (int o = i; o <= l; ++o) {
			for (int p = j; p <= m; ++p) {
				for (int q = k; q <= n; ++q) {
					BlockPos blockPos = new BlockPos(o, p, q);
					BlockState blockState = this.level().getBlockState(blockPos);
					if (!blockState.isAir() && !blockState.is(BlockTags.DRAGON_TRANSPARENT)) {
						if (this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && !blockState.is(BlockTags.DRAGON_IMMUNE)) {
							bl2 = this.level().removeBlock(blockPos, false) || bl2;
						} else {
							bl = true;
						}
					}
				}
			}
		}
		
		if (bl2) {
			BlockPos blockPos2 = new BlockPos(i + this.random.nextInt(l - i + 1), j + this.random.nextInt(m - j + 1), k + this.random.nextInt(n - k + 1));
			this.level().levelEvent(2008, blockPos2, 0);
		}
		
		return bl;
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (this.level().isClientSide()) {
			if (this.tickCount == 0) {
				MonstrositySoundInstance.startSoundInstance(this);
			}
		} else {
			checkDespawn();
		}
		
		if (this.hasInvincibilityTicks()) {
			for (int j = 0; j < 3; ++j) {
				this.level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.7f, 0.7f, 0.7f), this.getX() + this.random.nextGaussian(), this.getY() + (double) (this.random.nextFloat() * 3.3F), this.getZ() + this.random.nextGaussian(), 0.0, 0.0, 0.0);
			}
		}
	}
	
	@Override
	public void checkDespawn() {
		super.checkDespawn();
		
		if (hasValidTarget()) {
			ticksWithoutTarget = 0;
		} else {
			this.ticksWithoutTarget++;
			if (ticksWithoutTarget > 600) {
				this.playAmbientSound();
				this.discard();
			}
		}
	}
	
	public boolean hasValidTarget() {
		LivingEntity target = getTarget();
		return target != null && canAttack(target, TARGET_PREDICATE);
	}
	
	@Override
	protected PathNavigation createNavigation(Level world) {
		FlyingPathNavigation birdNavigation = new FlyingPathNavigation(this, world);
		birdNavigation.setCanOpenDoors(true);
		birdNavigation.setCanFloat(true);
		birdNavigation.setCanPassDoors(true);
		return birdNavigation;
	}
	
	private static final ResourceLocation STONKS_BONUS_ID = SpectrumCommon.locate("monstrosity_stonks");
	
	public void growStronger(int amount) {
		this.timesGottenStronger += amount;
		
		Multimap<Holder<Attribute>, AttributeModifier> map = Multimaps.newMultimap(Maps.newLinkedHashMap(), ArrayList::new);
		map.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(STONKS_BONUS_ID, 1.0 + timesGottenStronger * 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		this.getAttributes().addTransientAttributeModifiers(map);
		
		playSound(SpectrumSoundEvents.ENTITY_MONSTROSITY_GROWL, 1.0F, 1.0F);
		for (float i = 0; i <= 1.0; i += 0.2F) {
			PlayParticleWithPatternAndVelocityPayload.playParticleWithPatternAndVelocity(null, (ServerLevel) this.level(), new Vec3(getX(), getY(i), getZ()), ColoredSparkleRisingParticleEffect.WHITE, VectorPattern.SIXTEEN, 0.05F);
		}
	}
	
	public static AttributeSupplier.Builder createMonstrosityAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 600.0)
				.add(Attributes.ATTACK_DAMAGE, 24.0)
				.add(Attributes.FOLLOW_RANGE, 48.0)
				.add(Attributes.ARMOR, 18.0)
				.add(Attributes.ARMOR_TOUGHNESS, 4.0)
				.add(Attributes.ATTACK_KNOCKBACK, 2.0)
				.add(AdditionalEntityAttributes.MAGIC_PROTECTION, 4.0);
	}
	
	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (!this.level().isClientSide() && isNonVanillaKillCommandDamage(source, amount)) {
			// na, we do not feel like dying rn, we ballin
			this.setHealth(this.getHealth() + this.getMaxHealth() / 2);
			this.growStronger(8);
			this.playSound(getHurtSound(source), 2.0F, 1.5F);
			return false;
		}
		return super.hurt(source, amount);
	}
	
	@Override
	public boolean hasLineOfSight(Entity entity) {
		if (entity.level() != this.level()) {
			return false;
		}
		return entity.position().distanceTo(this.position()) < 128;
	}
	
	@Override
	protected Component getTypeName() {
		return Component.literal("§kLivingNightmare");
	}
	
	@Override
	public void performRangedAttack(LivingEntity target, float pullProgress) {
		var world = target.level();
		if (world.random.nextBoolean()) {
			LightShardBaseEntity.summonBarrageInternal(world, this, () -> new LightSpearEntity(world, this, 6.0F, 800), target, ENTITY_TARGETS, this.getEyePosition(), UniformInt.of(5, 7));
		} else {
			LightShardBaseEntity.summonBarrageInternal(world, this, () -> {
				LightMineEntity entity = new LightMineEntity(world, MonstrosityEntity.this, 4, 8.0F, 800);
				entity.setEffects(List.of(getRandomMineStatusEffect(random)));
				return entity;
			}, target, ENTITY_TARGETS, this.getEyePosition(), UniformInt.of(7, 11));
		}
		
		this.playSound(SpectrumSoundEvents.ENTITY_MONSTROSITY_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
	}
	
	protected MobEffectInstance getRandomMineStatusEffect(net.minecraft.util.RandomSource random) {
		int i = random.nextInt();
		switch (i) {
			case 0 -> {
				return new MobEffectInstance(SpectrumMobEffects.SCARRED, 200, 0);
			}
			case 1 -> {
				return new MobEffectInstance(SpectrumMobEffects.STIFFNESS, 200, 1);
			}
			case 2 -> {
				return new MobEffectInstance(SpectrumMobEffects.DENSITY, 200, 2);
			}
			case 3 -> {
				return new MobEffectInstance(SpectrumMobEffects.VULNERABILITY, 200, 1);
			}
			default -> {
				return new MobEffectInstance(SpectrumMobEffects.LIFE_DRAIN, 200, 0);
			}
		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		
		nbt.putFloat("previous_health", this.previousHealth);
		nbt.putInt("times_gotten_stronger", this.timesGottenStronger);
		nbt.putInt("ticks_without_target", this.ticksWithoutTarget);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		
		if (nbt.contains("previous_health", Tag.TAG_FLOAT)) {
			this.previousHealth = nbt.getFloat("previous_health");
		}
		if (nbt.contains("times_gotten_stronger", Tag.TAG_ANY_NUMERIC)) {
			this.timesGottenStronger = nbt.getInt("times_gotten_stronger");
		}
		if (nbt.contains("ticks_without_target", Tag.TAG_ANY_NUMERIC)) {
			this.ticksWithoutTarget = nbt.getInt("ticks_without_target");
		}
	}
	
	private enum MovementType {
		SWOOPING_TO_POSITION, // position based movement
		START_SWOOPING, // swoop to player and try hitting them
		RETREATING // pissing off far, far away
	}
	
	private class MonstrosityMoveControl extends MoveControl {
		
		private float speed = 0.1F;
		
		public MonstrosityMoveControl(Mob owner) {
			super(owner);
		}
		
		@Override
		public void tick() {
			if (MonstrosityEntity.this.horizontalCollision) {
				MonstrosityEntity.this.setYRot(MonstrosityEntity.this.getYRot() + 180.0F);
				this.speed = 0.1F;
			}
			
			double d = MonstrosityEntity.this.getMoveControl().getWantedX() - MonstrosityEntity.this.getX();
			double e = MonstrosityEntity.this.getMoveControl().getWantedY() - MonstrosityEntity.this.getY();
			double f = MonstrosityEntity.this.getMoveControl().getWantedZ() - MonstrosityEntity.this.getZ();
			double g = Math.sqrt(d * d + f * f);
			if (Math.abs(g) > (double) 1.0E-5F) {
				double h = (double) 1.0F - Math.abs(e * (double) 0.7F) / g;
				d *= h;
				f *= h;
				g = Math.sqrt(d * d + f * f);
				double i = Math.sqrt(d * d + f * f + e * e);
				float j = MonstrosityEntity.this.getYRot();
				float k = (float) Mth.atan2(f, d);
				float l = Mth.wrapDegrees(MonstrosityEntity.this.getYRot() + 90.0F);
				float m = Mth.wrapDegrees(k * (180F / (float) Math.PI));
				MonstrosityEntity.this.setYRot(Mth.approachDegrees(l, m, 4.0F) - 90.0F);
				MonstrosityEntity.this.yBodyRot = MonstrosityEntity.this.getYRot();
				if (Mth.degreesDifferenceAbs(j, MonstrosityEntity.this.getYRot()) < 3.0F) {
					this.speed = Mth.approach(this.speed, 1.8F, 0.005F * (1.8F / this.speed));
				} else {
					this.speed = Mth.approach(this.speed, 0.2F, 0.025F);
				}
				
				float n = (float) (-(Mth.atan2(-e, g) * (double) (180F / (float) Math.PI)));
				MonstrosityEntity.this.setXRot(n);
				float o = MonstrosityEntity.this.getYRot() + 90.0F;
				double p = (double) (this.speed * Mth.cos(o * ((float) Math.PI / 180F))) * Math.abs(d / i);
				double q = (double) (this.speed * Mth.sin(o * ((float) Math.PI / 180F))) * Math.abs(f / i);
				double r = (double) (this.speed * Mth.sin(n * ((float) Math.PI / 180F))) * Math.abs(e / i);
				Vec3 vec3 = MonstrosityEntity.this.getDeltaMovement();
				MonstrosityEntity.this.setDeltaMovement(vec3.add((new Vec3(p, r, q)).subtract(vec3).scale(0.2)));
			}
		}
	}
	
	private class StartSwoopAttackGoal extends Goal {
		private int cooldown;
		
		@Override
		public boolean canUse() {
			LivingEntity target = MonstrosityEntity.this.getTarget();
			return target != null && MonstrosityEntity.this.canAttack(target, TARGET_PREDICATE);
		}
		
		@Override
		public void start() {
			this.cooldown = this.adjustedTickDelay(10);
			MonstrosityEntity.this.movementType = MovementType.SWOOPING_TO_POSITION;
			this.aimAtTarget();
		}
		
		@Override
		public void tick() {
			if (MonstrosityEntity.this.movementType == MovementType.SWOOPING_TO_POSITION) {
				--this.cooldown;
				if (this.cooldown <= 0) {
					MonstrosityEntity.this.movementType = MovementType.START_SWOOPING;
					this.aimAtTarget();
					this.cooldown = this.adjustedTickDelay((8 + MonstrosityEntity.this.random.nextInt(4)) * 20);
					MonstrosityEntity.this.playSound(SpectrumSoundEvents.ENTITY_MONSTROSITY_SWOOP, 10.0F, 0.95F + MonstrosityEntity.this.random.nextFloat() * 0.1F);
				}
			}
		}
		
		private void aimAtTarget() {
			Vec3 goalPos = MonstrosityEntity.this.getTarget().position();
			MonstrosityEntity.this.moveControl.setWantedPosition(goalPos.x, goalPos.y, goalPos.z, 1.25F);
		}
	}
	
	private class SwoopMovementGoal extends Goal {
		
		SwoopMovementGoal() {
			super();
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}
		
		@Override
		public boolean canUse() {
			return MonstrosityEntity.this.getTarget() != null && MonstrosityEntity.this.movementType == MovementType.START_SWOOPING;
		}
		
		@Override
		public boolean canContinueToUse() {
			LivingEntity livingEntity = MonstrosityEntity.this.getTarget();
			if (livingEntity == null) {
				return false;
			} else if (!livingEntity.isAlive()) {
				return false;
			} else {
				if (livingEntity instanceof Player playerEntity) {
					if (livingEntity.isSpectator() || playerEntity.isCreative()) {
						return false;
					}
				}
				return this.canUse();
			}
		}
		
		@Override
		public void stop() {
			MonstrosityEntity.this.movementType = MovementType.SWOOPING_TO_POSITION;
		}
		
		@Override
		public void tick() {
			LivingEntity livingEntity = MonstrosityEntity.this.getTarget();
			if (livingEntity != null) {
				MonstrosityEntity.this.moveControl.setWantedPosition(livingEntity.getX(), livingEntity.getY(0.5), livingEntity.getZ(), 1.0F);
				if (MonstrosityEntity.this.getBoundingBox().inflate(0.2).intersects(livingEntity.getBoundingBox())) {
					// the monstrosity hit the entity
					MonstrosityEntity.this.doHurtTarget(livingEntity);
					MonstrosityEntity.this.movementType = MovementType.SWOOPING_TO_POSITION;
					if (!MonstrosityEntity.this.isSilent()) {
						MonstrosityEntity.this.level().levelEvent(LevelEvent.SOUND_PHANTOM_BITE, MonstrosityEntity.this.blockPosition(), 0);
					}
				} else if (MonstrosityEntity.this.horizontalCollision || MonstrosityEntity.this.hurtTime > 0) {
					// the player hit monstrosity
					MonstrosityEntity.this.movementType = MovementType.SWOOPING_TO_POSITION;
				}
			}
		}
	}
	
	private class FindTargetGoal extends Goal {
		
		private int delay = reducedTickDelay(20);
		
		FindTargetGoal() {
		}
		
		@Override
		public boolean canUse() {
			if (this.delay > 0) {
				--this.delay;
				return false;
			}
			
			this.delay = reducedTickDelay(60);
			Player newTarget = MonstrosityEntity.this.level().getNearestPlayer(TARGET_PREDICATE, MonstrosityEntity.this);
			if (newTarget == null) {
				return false;
			}
			
			MonstrosityEntity.this.setTarget(newTarget);
			return true;
		}
		
		@Override
		public boolean canContinueToUse() {
			LivingEntity target = MonstrosityEntity.this.getTarget();
			return target != null && MonstrosityEntity.this.canAttack(target, TARGET_PREDICATE);
		}
	}
	
	private class RetreatAndAttackGoal extends Goal {
		
		protected final float retreatDistance;
		
		RetreatAndAttackGoal(float retreatDistance) {
			super();
			this.retreatDistance = retreatDistance;
		}
		
		@Override
		public boolean canUse() {
			return MonstrosityEntity.this.movementType == MovementType.START_SWOOPING
					&& MonstrosityEntity.this.getTarget() != null
					&& MonstrosityEntity.this.level().random.nextBoolean() && MonstrosityEntity.this.distanceTo(MonstrosityEntity.this.getTarget()) < retreatDistance - 4;
		}
		
		@Override
		public boolean canContinueToUse() {
			return MonstrosityEntity.this.getTarget() != null
					&& MonstrosityEntity.this.canAttack(MonstrosityEntity.this.getTarget(), TARGET_PREDICATE)
					&& MonstrosityEntity.this.distanceTo(MonstrosityEntity.this.getTarget()) < retreatDistance;
		}
		
		@Override
		public void start() {
			super.start();
			Vec3 differenceToTarget = MonstrosityEntity.this.position().subtract(MonstrosityEntity.this.getTarget().position());
			Vec3 multipliedDifference = differenceToTarget.multiply(1, 0, 1).normalize().scale(retreatDistance);
			Vec3 wantedPos = MonstrosityEntity.this.position().add(multipliedDifference);
			MonstrosityEntity.this.moveControl.setWantedPosition(wantedPos.x, wantedPos.y, wantedPos.z, 1.0F);
			MonstrosityEntity.this.movementType = MovementType.RETREATING;
		}
		
		@Override
		public void stop() {
			LivingEntity target = MonstrosityEntity.this.getTarget();
			if (target != null && MonstrosityEntity.this.canAttack(target, TARGET_PREDICATE)) {
				LightShardEntity.summonBarrage(MonstrosityEntity.this.level(), MonstrosityEntity.this, target, ENTITY_TARGETS, getEyePosition(), LightShardBaseEntity.DEFAULT_COUNT_PROVIDER);
			}
			MonstrosityEntity.this.movementType = MovementType.START_SWOOPING;
			super.stop();
		}
		
	}
	
}
