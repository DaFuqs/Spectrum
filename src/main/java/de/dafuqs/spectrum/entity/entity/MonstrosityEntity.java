package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.additionalentityattributes.*;
import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.ai.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.control.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.registry.tag.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public class MonstrosityEntity extends SpectrumBossEntity implements RangedAttackMob {

	private static final Identifier KILLED_MONSTROSITY_ADVANCEMENT_IDENTIFIER = SpectrumCommon.locate("lategame/killed_monstrosity");
	private static final Predicate<LivingEntity> SHOULD_NOT_BE_IN_DD_PLAYER_PREDICATE = (entity) -> {
		if (entity instanceof PlayerEntity player) {
			return !player.isSpectator() && !player.isCreative() && !AdvancementHelper.hasAdvancement(player, KILLED_MONSTROSITY_ADVANCEMENT_IDENTIFIER);
		}
		return false;
	};
	private final TargetPredicate TARGET_PREDICATE = TargetPredicate.createAttackable().setPredicate(SHOULD_NOT_BE_IN_DD_PLAYER_PREDICATE);
	
	private static final float MAX_LIFE_LOST_PER_TICK = 20F;
	private static final float GET_STRONGER_EVERY_X_TICKS = 400F;
	
	private Vec3d targetPosition = Vec3d.ZERO;
	private MovementType movementType = MovementType.SWOOPING_TO_POSITION;
	
	private float previousHealth;
	private int timesGottenStronger = 0;
	
	private static @Nullable MonstrosityEntity currentMonstrosity = null;
	
	public MonstrosityEntity(EntityType<? extends MonstrosityEntity> entityType, World world) {
		super(entityType, world);
		this.moveControl = new MonstrosityMoveControl(this);
		this.experiencePoints = 500;
		this.noClip = true;
		this.ignoreCameraFrustum = true;
		this.previousHealth = getHealth();
		
		if (world.isClient) {
			MonstrositySoundInstance.startSoundInstance(this);
		}
	}
	
	protected MonstrosityEntity(World world, double x, double y, double z) {
		this(SpectrumEntityTypes.MONSTROSITY, world);
		this.setPosition(x, y, z);
		this.setYaw(this.random.nextFloat() * 360.0F);
		this.setInvincibilityTicks(200);
	}
	
	public static void checkForSpawn(ServerWorld world) {
		/*if (world.getDifficulty() == Difficulty.PEACEFUL) {
			return;
		}*/
		for (PlayerEntity playerEntity : world.getEntitiesByType(EntityType.PLAYER, Entity::isAlive)) {
			// 1 % chance to spawn
			if (world.getRandom().nextFloat() < 0.1 && SHOULD_NOT_BE_IN_DD_PLAYER_PREDICATE.test(playerEntity)) {
				if (currentMonstrosity == null) {
					currentMonstrosity = new MonstrosityEntity(world, playerEntity.getPos().getX(), playerEntity.getPos().getY() - 100, playerEntity.getPos().getZ());
					world.spawnEntity(currentMonstrosity);
				}
				if (currentMonstrosity.getTarget() == null) {
					currentMonstrosity.setTarget(playerEntity);
					currentMonstrosity.setPosition(playerEntity.getPos().getX(), playerEntity.getPos().getY() - 100, playerEntity.getPos().getZ());
				}
				currentMonstrosity.playSound(currentMonstrosity.getAmbientSound(), 4.0F, 1.0F);
			}
			break;
		}
	}
	
	@Override
	public void onRemoved() {
		if (currentMonstrosity == this) {
			currentMonstrosity = null;
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
		this.goalSelector.add(3, new RetreatAndAttackGoal(this, 20));
		this.goalSelector.add(3, new ProjectileAttackGoal(this, 1.0, 40, 28.0F));
		this.goalSelector.add(4, new WanderAroundFarGoal(this, 1.0));
		this.goalSelector.add(5, new FlyGoal(this, 1.0));
		// TODO: spawn mines
		// TODO: dripstone pillar entities from the ground (with short telegraphing)
		
		this.targetSelector.add(1, new ActiveTargetGoal<>(this, LivingEntity.class, 0, false, false, SHOULD_NOT_BE_IN_DD_PLAYER_PREDICATE));
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
		
		if (this.age % GET_STRONGER_EVERY_X_TICKS == 0) {
			this.growStronger(1);
		}
		
		destroyBlocks(this.getBoundingBox());
		
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
			
			this.updateVelocity(this.onGround ? 0.1F * g : 0.02F, movementInput);
			this.move(net.minecraft.entity.MovementType.SELF, this.getVelocity());
			this.setVelocity(this.getVelocity().multiply(f));
		}
		
		this.updateLimbs(false);
	}
	
	@Override
	public boolean isClimbing() {
		return false;
	}
	
	public void tick() {
		super.tick();
		if (this.hasInvincibilityTicks()) {
			for (int j = 0; j < 3; ++j) {
				this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + this.random.nextGaussian(), this.getY() + (double) (this.random.nextFloat() * 3.3F), this.getZ() + this.random.nextGaussian(), 0.7, 0.7, 0.7);
			}
		}
	}
	
	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
		this.targetPosition = getPos();
		return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
	}
	
	@Override
	protected EntityNavigation createNavigation(World world) {
		BirdNavigation birdNavigation = new BirdNavigation(this, world);
		birdNavigation.setCanPathThroughDoors(false);
		birdNavigation.setCanSwim(true);
		birdNavigation.setCanEnterOpenDoors(true);
		return birdNavigation;
	}
	
	public void growStronger(int amount) {
		this.timesGottenStronger += amount;
		this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(6 + timesGottenStronger);
		// TODO: spawn effects
	}
	
	@Override
	public void kill() {
		/*if (this.previousHealth > this.getMaxHealth() / 4) {
			// naha, I do not feel like doing that
			this.setHealth(this.getHealth() + this.getMaxHealth() / 2);
			this.growStronger(8);
			this.playSound(getHurtSound(DamageSource.OUT_OF_WORLD), 2.0F, 1.5F);
			return;
		}*/
		
		this.remove(RemovalReason.KILLED);
		this.emitGameEvent(GameEvent.ENTITY_DIE);
	}
	
	public static DefaultAttributeContainer createMonstrosityAttributes() {
		return HostileEntity.createHostileAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 800.0)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 20.0)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0)
				.add(EntityAttributes.GENERIC_ARMOR, 18.0)
				.add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 4.0)
				.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 2.0)
				.add(AdditionalEntityAttributes.MAGIC_PROTECTION, 2.0)
				.build();
	}
	
	private void destroyBlocks(Box box) {
		int minX = MathHelper.floor(box.minX);
		int minY = MathHelper.floor(box.minY);
		int minZ = MathHelper.floor(box.minZ);
		int maxX = MathHelper.floor(box.maxX);
		int maxY = MathHelper.floor(box.maxY);
		int maxZ = MathHelper.floor(box.maxZ);
		boolean blockDestroyed = false;
		
		for (int x = minX; x <= maxX; ++x) {
			for (int y = minY; y <= maxY; ++y) {
				for (int z = minZ; z <= maxZ; ++z) {
					BlockPos blockPos = new BlockPos(x, y, z);
					BlockState blockState = this.world.getBlockState(blockPos);
					if (!blockState.isAir() && !blockState.isIn(BlockTags.DRAGON_TRANSPARENT)) {
						if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) && !blockState.isIn(BlockTags.DRAGON_IMMUNE)) {
							blockDestroyed = this.world.removeBlock(blockPos, false) || blockDestroyed;
						}
					}
				}
			}
		}
		
		if (blockDestroyed) {
			BlockPos randomPos = new BlockPos(minX + this.random.nextInt(maxX - minX + 1), minY + this.random.nextInt(maxY - minY + 1), minZ + this.random.nextInt(maxZ - minZ + 1));
			this.world.syncWorldEvent(2008, randomPos, 0);
		}
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
		this.shootAt(target, pullProgress, this.random.nextFloat() < 0.001F);
	}
	
	private void shootAt(LivingEntity target, float pullProgress, boolean powerful) {
		ItemStack itemStack = this.getProjectileType(this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW)));
		PersistentProjectileEntity persistentProjectileEntity = ProjectileUtil.createArrowProjectile(this, itemStack, pullProgress);
		double d = target.getX() - this.getX();
		double e = target.getBodyY(0.3) - persistentProjectileEntity.getY();
		double f = target.getZ() - this.getZ();
		double g = Math.sqrt(d * d + f * f);
		persistentProjectileEntity.setVelocity(d, e + g * 0.2, f, 1.6F, (float) (14 - this.world.getDifficulty().getId() * 4));
		this.playSound(SpectrumSoundEvents.ENTITY_MONSTROSITY_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
		this.world.spawnEntity(persistentProjectileEntity);
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		// TODO
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		// TODO
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
						MonstrosityEntity.this.world.syncWorldEvent(WorldEvents.PHANTOM_BITES, MonstrosityEntity.this.getBlockPos(), 0);
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
			PlayerEntity newTarget = MonstrosityEntity.this.world.getClosestPlayer(TARGET_PREDICATE, MonstrosityEntity.this);
			if (newTarget != null && MonstrosityEntity.this.isTarget(newTarget, TARGET_PREDICATE)) {
				MonstrosityEntity.this.setTarget(newTarget);
				return true;
			}
			
			return false;
		}
		
		@Override
		public boolean shouldContinue() {
			LivingEntity target = MonstrosityEntity.this.getTarget();
			return target != null && MonstrosityEntity.this.isTarget(target, TARGET_PREDICATE);
		}
	}
	
	private class RetreatAndAttackGoal extends Goal {
		
		protected MobEntity mob;
		protected float retreatDistance;
		
		RetreatAndAttackGoal(PathAwareEntity mob, float retreatDistance) {
			super();
			this.mob = mob;
			this.retreatDistance = retreatDistance;
		}
		
		@Override
		public boolean canStart() {
			return MonstrosityEntity.this.movementType == MovementType.START_SWOOPING && mob.getTarget() != null && world.random.nextBoolean() && mob.distanceTo(mob.getTarget()) < retreatDistance - 4;
		}
		
		@Override
		public boolean shouldContinue() {
			return mob.getTarget() != null && mob.isTarget(mob.getTarget(), TARGET_PREDICATE) && mob.distanceTo(mob.getTarget()) < retreatDistance;
		}
		
		@Override
		public void start() {
			super.start();
			Vec3d differenceToTarget = mob.getPos().subtract(mob.getTarget().getPos());
			Vec3d multipliedDifference = differenceToTarget.multiply(1, 0, 1).normalize().multiply(retreatDistance);
			MonstrosityEntity.this.targetPosition = mob.getPos().add(multipliedDifference);
			MonstrosityEntity.this.movementType = MovementType.RETREATING;
		}
		
		@Override
		public void stop() {
			LivingEntity target = mob.getTarget();
			if (target != null && mob.isTarget(target, TARGET_PREDICATE)) {
				LightShardEntity.summonBarrage(world, mob, target);
			}
			MonstrosityEntity.this.movementType = MovementType.START_SWOOPING;
			super.stop();
		}
		
	}
	
}
