package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.additionalentityattributes.*;
import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.sound.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.control.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.*;
import net.minecraft.particle.*;
import net.minecraft.tag.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;

import java.util.*;
import java.util.function.*;

public class MonstrosityEntity extends SpectrumBossEntity implements RangedAttackMob {
	
	private static final Identifier ENTERED_DD_ADVANCEMENT_IDENTIFIER = SpectrumCommon.locate("lategame/spectrum_lategame");
	private static final Predicate<LivingEntity> SHOULD_NOT_BE_IN_DD_PLAYER_PREDICATE = (entity) -> {
		if (entity instanceof PlayerEntity player) {
			return !AdvancementHelper.hasAdvancement(player, ENTERED_DD_ADVANCEMENT_IDENTIFIER);
		}
		return false;
	};
	
	private static final float MAX_LIFE_LOST_PER_TICK = 20;
	private static final float GET_STRONGER_EVERY_X_TICKS = 400;
	private float previousHealth;
	
	public MonstrosityEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
		this.moveControl = new FlightMoveControl(this, 4, false);
		this.experiencePoints = 500;
		this.noClip = true;
		this.ignoreCameraFrustum = true;
		this.previousHealth = getHealth();
		
		if (world.isClient()) {
			MonstrositySoundInstance.startSoundInstance(this);
		}
	}
	
	@Override
	protected void initGoals() {
		this.goalSelector.add(2, new ProjectileAttackGoal(this, 1.0, 40, 20.0F));
		this.goalSelector.add(5, new FlyGoal(this, 1.0));
		this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(7, new LookAroundGoal(this));
		
		this.targetSelector.add(1, new ActiveTargetGoal<>(this, LivingEntity.class, 0, false, false, SHOULD_NOT_BE_IN_DD_PLAYER_PREDICATE));
		this.targetSelector.add(2, new RevengeGoal(this));
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
	public void tickMovement() {
		Vec3d vec3d = this.getVelocity().multiply(1.0, 0.6, 1.0);
		if (!this.world.isClient && this.getTarget() != null) {
			LivingEntity entity = this.getTarget();
			if (entity != null) {
				double d = vec3d.y;
				if (this.getY() < entity.getY() || this.getY() < entity.getY() + 5.0) {
					d = Math.max(0.0, d);
					d += 0.3 - d * 0.6;
				}
				
				vec3d = new Vec3d(vec3d.x, d, vec3d.z);
				Vec3d vec3d2 = new Vec3d(entity.getX() - this.getX(), 0.0, entity.getZ() - this.getZ());
				if (vec3d2.horizontalLengthSquared() > 9.0) {
					Vec3d vec3d3 = vec3d2.normalize();
					vec3d = vec3d.add(vec3d3.x * 0.3 - vec3d.x * 0.6, 0.0, vec3d3.z * 0.3 - vec3d.z * 0.6);
				}
			}
		}
		
		this.setVelocity(vec3d);
		if (vec3d.horizontalLengthSquared() > 0.05) {
			this.setYaw((float) MathHelper.atan2(vec3d.z, vec3d.x) * 57.295776F - 90.0F);
		}
		
		super.tickMovement();
		
		if (this.hasInvincibilityTicks()) {
			for (int j = 0; j < 3; ++j) {
				this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + this.random.nextGaussian(), this.getY() + (double) (this.random.nextFloat() * 3.3F), this.getZ() + this.random.nextGaussian(), 0.7, 0.7, 0.7);
			}
		}
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
		//this.getAttributes().addTemporaryModifiers(itemStack2.getAttributeModifiers(equipmentSlot));
	}
	
	@Override
	public void kill() {
		if (this.previousHealth > this.getMaxHealth() / 4) {
			// naha, I do not feel like doing that
			this.setHealth(this.getHealth() + this.getMaxHealth() / 2);
			this.growStronger(8);
			this.playSound(getHurtSound(DamageSource.OUT_OF_WORLD), 2.0F, 1.5F);
		} else {
			this.remove(RemovalReason.KILLED);
			this.emitGameEvent(GameEvent.ENTITY_DIE);
		}
	}
	
	public static DefaultAttributeContainer createMonstrosityAttributes() {
		return HostileEntity.createHostileAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 800.0)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12.0)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6)
				.add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0)
				.add(EntityAttributes.GENERIC_ARMOR, 12.0)
				.add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 4.0)
				.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 2.0)
				.add(AdditionalEntityAttributes.MAGIC_PROTECTION, 2.0)
				.build();
	}
	
	private void damageLivingEntities(List<Entity> entities) {
		for (Entity entity : entities) {
			if (entity instanceof LivingEntity) {
				entity.damage(DamageSource.mob(this), 10.0F);
				this.applyDamageEffects(this, entity);
			}
		}
	}
	
	private boolean destroyBlocks(Box box) {
		int i = MathHelper.floor(box.minX);
		int j = MathHelper.floor(box.minY);
		int k = MathHelper.floor(box.minZ);
		int l = MathHelper.floor(box.maxX);
		int m = MathHelper.floor(box.maxY);
		int n = MathHelper.floor(box.maxZ);
		boolean bl = false;
		boolean bl2 = false;
		
		for (int o = i; o <= l; ++o) {
			for (int p = j; p <= m; ++p) {
				for (int q = k; q <= n; ++q) {
					BlockPos blockPos = new BlockPos(o, p, q);
					BlockState blockState = this.world.getBlockState(blockPos);
					if (!blockState.isAir() && !blockState.isIn(BlockTags.DRAGON_TRANSPARENT)) {
						if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) && !blockState.isIn(BlockTags.DRAGON_IMMUNE)) {
							bl2 = this.world.removeBlock(blockPos, false) || bl2;
						} else {
							bl = true;
						}
					}
				}
			}
		}
		
		if (bl2) {
			BlockPos blockPos2 = new BlockPos(i + this.random.nextInt(l - i + 1), j + this.random.nextInt(m - j + 1), k + this.random.nextInt(n - k + 1));
			this.world.syncWorldEvent(2008, blockPos2, 0);
		}
		
		return bl;
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
	
	}
	
}
