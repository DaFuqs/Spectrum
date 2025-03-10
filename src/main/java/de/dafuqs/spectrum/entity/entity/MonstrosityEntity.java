package de.dafuqs.spectrum.entity.entity;

import com.google.common.collect.*;
import de.dafuqs.additionalentityattributes.*;
import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.entity.ai.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.control.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public class MonstrosityEntity extends SpectrumBossEntity implements RangedAttackMob {
	
	public static final UUID BONUS_DAMAGE_UUID = UUID.fromString("4425979b-f987-4937-875a-1e26d727c67f");

	public static @Nullable MonstrosityEntity theOneAndOnly = null;
	public static final Predicate<LivingEntity> ENTITY_TARGETS = (entity) -> {
		if (entity instanceof PlayerEntity player) {
			if (player.isSpectator() || player.isCreative()) {
				return false;
			}
			return !AdvancementHelper.hasAdvancement(player, SpectrumAdvancements.KILLED_MONSTROSITY);
		}
		return false;
	};
	private final TargetPredicate TARGET_PREDICATE = TargetPredicate.createAttackable().setPredicate(ENTITY_TARGETS);
	
	private static final float MAX_LIFE_LOST_PER_TICK = 20F;
	private static final int GROW_STRONGER_EVERY_X_TICKS = 400;
	
	private Vec3d targetPosition = Vec3d.ZERO;
	private MovementType movementType = MovementType.SWOOPING_TO_POSITION;
	
	private float previousHealth;
	private int timesGottenStronger = 0;
	private int ticksWithoutTarget = 0;

	public MonstrosityEntity(EntityType<? extends MonstrosityEntity> entityType, World world) {
		super(entityType, world);
		this.moveControl = new MonstrosityMoveControl(this);
		this.experiencePoints = 500;
		this.noClip = true;
		this.ignoreCameraFrustum = true;
		this.previousHealth = getHealth();

		if (!world.isClient && (MonstrosityEntity.theOneAndOnly == null || MonstrosityEntity.theOneAndOnly.isRemoved() || !MonstrosityEntity.theOneAndOnly.isAlive())) {
			MonstrosityEntity.theOneAndOnly = this;
		} else {
			this.remove(RemovalReason.DISCARDED);
		}
	}

	@Override
	public void playSpawnEffects() {
		super.playSpawnEffects();
	}
	
	@Override
	public void onRemoved() {
		if (theOneAndOnly == this) {
			theOneAndOnly = null;
		}
		super.onRemoved();
	}
	
	@Override
	protected BodyControl createBodyControl() {
		return new EmptyBodyControl(this);
	}
	
	@Override
	protected void initGoals() {
		this.goalSelector.add(1, new StartSwoopAttackGoal());
		this.goalSelector.add(2, new SwoopMovementGoal());
		this.goalSelector.add(3, new RetreatAndAttackGoal(40));
		this.goalSelector.add(3, new ProjectileAttackGoal(this, 1.0, 40, 28.0F));
		this.goalSelector.add(4, new WanderAroundFarGoal(this, 1.0));
		this.goalSelector.add(5, new FlyGoal(this, 1.0));
		
		this.targetSelector.add(1, new ActiveTargetGoal<>(this, LivingEntity.class, 0, false, false, ENTITY_TARGETS));
		this.targetSelector.add(2, new FindTargetGoal());
	}
	
	@Override
	protected void mobTick() {
		float currentHealth = this.getHealth();
		if (currentHealth < this.previousHealth - MAX_LIFE_LOST_PER_TICK) {
			this.setHealth(this.previousHealth - MAX_LIFE_LOST_PER_TICK);
		}
		this.previousHealth = currentHealth;
		this.tickInvincibility();
		
		if (!this.getWorld().isClient() && this.age % GROW_STRONGER_EVERY_X_TICKS == 0) {
			this.growStronger(1);
		}
		
		//destroyBlocks(this.getBoundingBox());
		
		super.mobTick();
		
		if (this.age % 10 == 0) {
			this.heal(1.0F);
		}
	}
	
	@Override
	protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
	}
	
	@Override
	public void travel(Vec3d movementInput) {
		if (this.canMoveVoluntarily() || this.isLogicalSideForUpdatingMovement()) {
			float f = 0.91F;
			float g = 0.16277137F / (f * f * f);
			
			this.updateVelocity(this.isOnGround() ? 0.1F * g : 0.02F, movementInput);
			this.move(net.minecraft.entity.MovementType.SELF, this.getVelocity());
			this.setVelocity(this.getVelocity().multiply(f));
		}
		
		this.updateLimbs(false);
	}
	
	@Override
	public boolean isClimbing() {
		return false;
	}
	
	@Override
	public void tick() {
		super.tick();

		if (this.getWorld().isClient()) {
			if (this.age == 0) {
				MonstrositySoundInstance.startSoundInstance(this);
			}
		} else {
			checkDespawn();
		}

		if (this.hasInvincibilityTicks()) {
			for (int j = 0; j < 3; ++j) {
				this.getWorld().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + this.random.nextGaussian(), this.getY() + (double) (this.random.nextFloat() * 3.3F), this.getZ() + this.random.nextGaussian(), 0.7, 0.7, 0.7);
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
		return target != null && isTarget(target, TARGET_PREDICATE);
	}

	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
		if (spawnReason == SpawnReason.NATURAL && theOneAndOnly != null && theOneAndOnly != this) {
			discard();
		}

		this.targetPosition = getPos();
		return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
	}
	
	@Override
	protected EntityNavigation createNavigation(World world) {
		BirdNavigation birdNavigation = new BirdNavigation(this, world);
		birdNavigation.setCanPathThroughDoors(true);
		birdNavigation.setCanSwim(true);
		birdNavigation.setCanEnterOpenDoors(true);
		return birdNavigation;
	}
	
	public void growStronger(int amount) {
		this.timesGottenStronger += amount;

		Multimap<EntityAttribute, EntityAttributeModifier> map = Multimaps.newMultimap(Maps.newLinkedHashMap(), ArrayList::new);
		EntityAttributeModifier jeopardantModifier = new EntityAttributeModifier(BONUS_DAMAGE_UUID, "spectrum:monstrosity_bonus", 1.0 + timesGottenStronger * 0.1, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
		map.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, jeopardantModifier);
		this.getAttributes().addTemporaryModifiers(map);

		playSound(SpectrumSoundEvents.ENTITY_MONSTROSITY_GROWL, 1.0F, 1.0F);
		for (float i = 0; i <= 1.0; i += 0.2) {
			SpectrumS2CPacketSender.playParticleWithPatternAndVelocity(null, (ServerWorld) this.getWorld(), new Vec3d(getX(), getBodyY(i), getZ()), SpectrumParticleTypes.WHITE_SPARKLE_RISING, VectorPattern.SIXTEEN, 0.05F);
		}
	}
	
	public static DefaultAttributeContainer createMonstrosityAttributes() {
		return HostileEntity.createHostileAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 600.0)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 24.0)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0)
				.add(EntityAttributes.GENERIC_ARMOR, 18.0)
				.add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 4.0)
				.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 2.0)
				.add(AdditionalEntityAttributes.MAGIC_PROTECTION, 4.0)
				.build();
	}
	
	@Override
	public boolean damage(DamageSource source, float amount) {
		if (!this.getWorld().isClient() && isNonVanillaKillCommandDamage(source, amount)) {
			// na, we do not feel like dying rn, we ballin
			this.setHealth(this.getHealth() + this.getMaxHealth() / 2);
			this.growStronger(8);
			this.playSound(getHurtSound(source), 2.0F, 1.5F);
			return false;
		}
		return super.damage(source, amount);
	}

	@Override
	public boolean canSee(Entity entity) {
		if (entity.getWorld() != this.getWorld()) {
			return false;
		}
		return entity.getPos().distanceTo(this.getPos()) < 128;
	}

	@Override
	public EntityGroup getGroup() {
		return EntityGroup.UNDEAD;
	}
	
	@Override
	protected Text getDefaultName() {
		return Text.literal("§kLivingNightmare");
	}
	
	@Override
	public void attack(LivingEntity target, float pullProgress) {
		var world = target.getWorld();
		if (world.random.nextBoolean()) {
			LightShardBaseEntity.summonBarrageInternal(world, this, () -> new LightSpearEntity(world, this, 6.0F, 800), target, ENTITY_TARGETS, this.getEyePos(), UniformIntProvider.create(5, 7));
		} else {
			LightShardBaseEntity.summonBarrageInternal(world, this, () -> {
				LightMineEntity entity = new LightMineEntity(world, MonstrosityEntity.this, 4, 8.0F, 800);
				entity.setEffects(List.of(getRandomMineStatusEffect(random)));
				return entity;
			}, target, ENTITY_TARGETS, this.getEyePos(), UniformIntProvider.create(7, 11));
		}

		this.playSound(SpectrumSoundEvents.ENTITY_MONSTROSITY_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
	}

	protected StatusEffectInstance getRandomMineStatusEffect(net.minecraft.util.math.random.Random random) {
		int i = random.nextInt();
		switch (i) {
			case 0 -> {
				return new StatusEffectInstance(SpectrumStatusEffects.SCARRED, 200, 0);
			}
			case 1 -> {
				return new StatusEffectInstance(SpectrumStatusEffects.STIFFNESS, 200, 1);
			}
			case 2 -> {
				return new StatusEffectInstance(SpectrumStatusEffects.DENSITY, 200, 2);
			}
			case 3 -> {
				return new StatusEffectInstance(SpectrumStatusEffects.VULNERABILITY, 200, 1);
			}
			default -> {
				return new StatusEffectInstance(SpectrumStatusEffects.LIFE_DRAIN, 200, 0);
			}
		}
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);

		nbt.putFloat("previous_health", this.previousHealth);
		nbt.putInt("times_gotten_stronger", this.timesGottenStronger);
		nbt.putInt("ticks_without_target", this.ticksWithoutTarget);
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);

		if (nbt.contains("previous_health", NbtElement.FLOAT_TYPE)) {
			this.previousHealth = nbt.getFloat("previous_health");
		}
		if (nbt.contains("times_gotten_stronger", NbtElement.NUMBER_TYPE)) {
			this.timesGottenStronger = nbt.getInt("times_gotten_stronger");
		}
		if (nbt.contains("ticks_without_target", NbtElement.NUMBER_TYPE)) {
			this.ticksWithoutTarget = nbt.getInt("ticks_without_target");
		}
	}
	
	private enum MovementType {
		SWOOPING_TO_POSITION, // position based movement
		START_SWOOPING, // swoop to player and try hitting them
		RETREATING // pissing off far, far away
	}
	
	private class MonstrosityMoveControl extends MoveControl {
		
		private float targetSpeed = 0.1F;
		
		public MonstrosityMoveControl(MobEntity owner) {
			super(owner);
		}
		
		@Override
		public void tick() {
			if (MonstrosityEntity.this.horizontalCollision) {
				MonstrosityEntity.this.setYaw(MonstrosityEntity.this.getYaw() + 180.0F);
				this.targetSpeed = 0.1F;
			}
			
			double d = MonstrosityEntity.this.targetPosition.x - MonstrosityEntity.this.getX();
			double e = MonstrosityEntity.this.targetPosition.y - MonstrosityEntity.this.getY();
			double f = MonstrosityEntity.this.targetPosition.z - MonstrosityEntity.this.getZ();
			double g = Math.sqrt(d * d + f * f);
			if (Math.abs(g) > 10.0E-6) {
				double h = 1.0 - Math.abs(e * 0.7) / g;
				d *= h;
				f *= h;
				g = Math.sqrt(d * d + f * f);
				double i = Math.sqrt(d * d + f * f + e * e);
				float j = MonstrosityEntity.this.getYaw();
				float k = (float) MathHelper.atan2(f, d);
				float l = MathHelper.wrapDegrees(MonstrosityEntity.this.getYaw() + 90.0F);
				float m = MathHelper.wrapDegrees(k * 57.295776F);
				MonstrosityEntity.this.setYaw(MathHelper.stepUnwrappedAngleTowards(l, m, 4.0F) - 90.0F);
				MonstrosityEntity.this.bodyYaw = MonstrosityEntity.this.getYaw();
				if (MathHelper.angleBetween(j, MonstrosityEntity.this.getYaw()) < 3.0F) {
					this.targetSpeed = MathHelper.stepTowards(this.targetSpeed, 1.8F, 0.005F * (1.8F / this.targetSpeed));
				} else {
					this.targetSpeed = MathHelper.stepTowards(this.targetSpeed, 0.2F, 0.025F);
				}
				
				float n = (float) (-(MathHelper.atan2(-e, g) * 57.2957763671875));
				MonstrosityEntity.this.setPitch(n);
				float o = MonstrosityEntity.this.getYaw() + 90.0F;
				double p = (double) (this.targetSpeed * MathHelper.cos(o * 0.017453292F)) * Math.abs(d / i);
				double q = (double) (this.targetSpeed * MathHelper.sin(o * 0.017453292F)) * Math.abs(f / i);
				double r = (double) (this.targetSpeed * MathHelper.sin(n * 0.017453292F)) * Math.abs(e / i);
				Vec3d vec3d = MonstrosityEntity.this.getVelocity();
				MonstrosityEntity.this.setVelocity(vec3d.add((new Vec3d(p, r, q)).subtract(vec3d).multiply(0.2)));
			}
		}
	}
	
	private class StartSwoopAttackGoal extends Goal {
		private int cooldown;
		
		@Override
		public boolean canStart() {
			LivingEntity target = MonstrosityEntity.this.getTarget();
			return target != null && MonstrosityEntity.this.isTarget(target, TARGET_PREDICATE);
		}
		
		@Override
		public void start() {
			this.cooldown = this.getTickCount(10);
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
					this.cooldown = this.getTickCount((8 + MonstrosityEntity.this.random.nextInt(4)) * 20);
					MonstrosityEntity.this.playSound(SpectrumSoundEvents.ENTITY_MONSTROSITY_SWOOP, 10.0F, 0.95F + MonstrosityEntity.this.random.nextFloat() * 0.1F);
				}
			}
		}
		
		private void aimAtTarget() {
			MonstrosityEntity.this.targetPosition = MonstrosityEntity.this.getTarget().getPos();
		}
	}
	
	private class SwoopMovementGoal extends Goal {
		
		SwoopMovementGoal() {
			super();
			this.setControls(EnumSet.of(Goal.Control.MOVE));
		}
		
		@Override
		public boolean canStart() {
			return MonstrosityEntity.this.getTarget() != null && MonstrosityEntity.this.movementType == MovementType.START_SWOOPING;
		}
		
		@Override
		public boolean shouldContinue() {
			LivingEntity livingEntity = MonstrosityEntity.this.getTarget();
			if (livingEntity == null) {
				return false;
			} else if (!livingEntity.isAlive()) {
				return false;
			} else {
				if (livingEntity instanceof PlayerEntity playerEntity) {
					if (livingEntity.isSpectator() || playerEntity.isCreative()) {
						return false;
					}
				}
				return this.canStart();
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
				MonstrosityEntity.this.targetPosition = new Vec3d(livingEntity.getX(), livingEntity.getBodyY(0.5), livingEntity.getZ());
				if (MonstrosityEntity.this.getBoundingBox().expand(0.2).intersects(livingEntity.getBoundingBox())) {
					// the monstrosity hit the entity
					MonstrosityEntity.this.tryAttack(livingEntity);
					MonstrosityEntity.this.movementType = MovementType.SWOOPING_TO_POSITION;
					if (!MonstrosityEntity.this.isSilent()) {
						MonstrosityEntity.this.getWorld().syncWorldEvent(WorldEvents.PHANTOM_BITES, MonstrosityEntity.this.getBlockPos(), 0);
					}
				} else if (MonstrosityEntity.this.horizontalCollision || MonstrosityEntity.this.hurtTime > 0) {
					// the player hit monstrosity
					MonstrosityEntity.this.movementType = MovementType.SWOOPING_TO_POSITION;
				}
			}
		}
	}
	
	private class FindTargetGoal extends Goal {
		
		private int delay = toGoalTicks(20);
		
		FindTargetGoal() {
		}
		
		@Override
		public boolean canStart() {
			if (this.delay > 0) {
				--this.delay;
				return false;
			}
			
			this.delay = toGoalTicks(60);
			PlayerEntity newTarget = MonstrosityEntity.this.getWorld().getClosestPlayer(TARGET_PREDICATE, MonstrosityEntity.this);
			if (newTarget == null) {
				return false;
			}

			MonstrosityEntity.this.setTarget(newTarget);
			return true;
		}
		
		@Override
		public boolean shouldContinue() {
			LivingEntity target = MonstrosityEntity.this.getTarget();
			return target != null && MonstrosityEntity.this.isTarget(target, TARGET_PREDICATE);
		}
	}
	
	private class RetreatAndAttackGoal extends Goal {

		protected final float retreatDistance;
		
		RetreatAndAttackGoal(float retreatDistance) {
			super();
			this.retreatDistance = retreatDistance;
		}
		
		@Override
		public boolean canStart() {
			return MonstrosityEntity.this.movementType == MovementType.START_SWOOPING
					&& MonstrosityEntity.this.getTarget() != null
					&& MonstrosityEntity.this.getWorld().random.nextBoolean() && MonstrosityEntity.this.distanceTo(MonstrosityEntity.this.getTarget()) < retreatDistance - 4;
		}
		
		@Override
		public boolean shouldContinue() {
			return MonstrosityEntity.this.getTarget() != null
					&& MonstrosityEntity.this.isTarget(MonstrosityEntity.this.getTarget(), TARGET_PREDICATE)
					&& MonstrosityEntity.this.distanceTo(MonstrosityEntity.this.getTarget()) < retreatDistance;
		}
		
		@Override
		public void start() {
			super.start();
			Vec3d differenceToTarget = MonstrosityEntity.this.getPos().subtract(MonstrosityEntity.this.getTarget().getPos());
			Vec3d multipliedDifference = differenceToTarget.multiply(1, 0, 1).normalize().multiply(retreatDistance);
			MonstrosityEntity.this.targetPosition = MonstrosityEntity.this.getPos().add(multipliedDifference);
			MonstrosityEntity.this.movementType = MovementType.RETREATING;
		}
		
		@Override
		public void stop() {
			LivingEntity target = MonstrosityEntity.this.getTarget();
			if (target != null && MonstrosityEntity.this.isTarget(target, TARGET_PREDICATE)) {
				LightShardEntity.summonBarrage(MonstrosityEntity.this.getWorld(), MonstrosityEntity.this, target, ENTITY_TARGETS, getEyePos(), LightShardBaseEntity.DEFAULT_COUNT_PROVIDER);
			}
			MonstrosityEntity.this.movementType = MovementType.START_SWOOPING;
			super.stop();
		}
		
	}
	
}
