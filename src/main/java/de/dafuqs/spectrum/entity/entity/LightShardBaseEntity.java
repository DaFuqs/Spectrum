package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.nbt.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public abstract class LightShardBaseEntity extends ProjectileEntity {
	
	public static final int DEFAULT_MAX_AGE = 200, DECELERATION_PHASE_LENGTH = 25;
	public static final float DEFAULT_ACCELERATION = 0.03F;
	protected float scaleOffset, damage, detectionRange;
	protected long maxAge;
	protected Optional<UUID> target = Optional.empty();
	protected Optional<Entity> targetEntity = Optional.empty();
	protected Vec3d initialVelocity = Vec3d.ZERO;
	
	public LightShardBaseEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
		super(entityType, world);
		
		this.maxAge = (int) ((DEFAULT_MAX_AGE + MathHelper.nextGaussian(world.getRandom(), 10, 7)) * 1.0); // TODO: sync from server => client
	}
	
	public LightShardBaseEntity(EntityType<? extends ProjectileEntity> entityType, World world, LivingEntity owner, Optional<Entity> target) {
		super(entityType, world);
		
		target.ifPresent(this::setTarget);
		this.setOwner(owner);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		age++;
		if (world.isClient() && age > DECELERATION_PHASE_LENGTH - 1 && getVelocity().length() > 0.075) {
			if (getVelocity().length() > 0.2 || world.getTime() % 2 == 0)
				world.addParticle(SpectrumParticleTypes.LIGHT_TRAIL, true, prevX, prevY, prevZ, 0, 0, 0);
		}
		
		if (age > maxAge) {
			playSound(SpectrumSoundEvents.BLOCK_MOONSTONE_CLUSTER_BREAK, random.nextFloat() + 0.25F, 1.2F + random.nextFloat());
			this.remove(RemovalReason.DISCARDED);
		}
		
		var velocity = getVelocity();
		updatePosition(getX() + velocity.getX(), getY() + velocity.getY(), getZ() + velocity.getZ());
		
		if (age < DECELERATION_PHASE_LENGTH) {
			var deceleration = Math.max((float) age / DECELERATION_PHASE_LENGTH, 0.5);
			setVelocity(
					MathHelper.lerp(deceleration, initialVelocity.x, 0),
					MathHelper.lerp(deceleration, initialVelocity.y, 0),
					MathHelper.lerp(deceleration, initialVelocity.z, 0)
			);
			velocityDirty = true;
			scheduleVelocityUpdate();
			return;
		}
		
		var hitResult = ProjectileUtil.getCollision(this, this::canHit);
		onCollision(hitResult);
		
		if (detectionRange > 0 && !isValidTarget(targetEntity)) {
			if (world.isClient)
				return;
			
			if (random.nextFloat() > 0.25)
				return;
			
			var serverWorld = (ServerWorld) world;
			
			var entities = serverWorld.getOtherEntities(this, Box.of(getPos(), detectionRange, detectionRange, detectionRange));
			
			Collections.shuffle(entities);
			var potentialTarget = entities
					.stream()
					.filter(entity -> entity instanceof HostileEntity)
					.filter(entity -> ((HostileEntity) entity).canSee(this))
					.findAny();
			
			if (isValidTarget(potentialTarget)) {
				setTarget(potentialTarget.get());
			}
		}
		
		if (isValidTarget(targetEntity)) {
			var entity = targetEntity.get();
			
			var transformVector = entity
					.getPos()
					.add(0, entity.getHeight() / 2, 0)
					.subtract(getPos())
					.normalize();
			
			var accelerationVector = transformVector.multiply(DEFAULT_ACCELERATION);
			addVelocity(accelerationVector.x, accelerationVector.y, accelerationVector.z);
		}
	}
	
	public void setInitialVelocity(Vec3d vector) {
		initialVelocity = vector;
		setVelocity(vector);
	}
	
	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		var attacked = entityHitResult.getEntity();
		
		if (attacked == getOwner())
			return;
		
		if (attacked instanceof Tameable pet && pet.getOwner() == getOwner())
			return;
		
		if (attacked instanceof PlayerEntity player && targetEntity.<Boolean>map(entity -> entity == player).orElse(false)) {
			return;
		}
		
		if (!(attacked instanceof LivingEntity owner))
			return;
		
		onHitEntity(owner, attacked);
		
		this.remove(RemovalReason.DISCARDED);
		super.onEntityHit(entityHitResult);
	}
	
	protected void onHitEntity(LivingEntity owner, Entity attacked) {
		float finalDamage = damage * (random.nextFloat() + 0.5F) * (1 - getVanishingProgress(age));
		attacked.timeUntilRegen = 0;
		attacked.damage(SpectrumDamageSources.irradiance(owner), finalDamage);
		
		attacked.playSound(SpectrumSoundEvents.BLOCK_MOONSTONE_CLUSTER_BREAK, 2, 0.8F + random.nextFloat());
		attacked.playSound(SoundEvents.BLOCK_GLASS_BREAK, random.nextFloat() * 0.5F + 0.2F, 0.8F + random.nextFloat());
	}
	
	@Override
	public void onRemoved() {
		var bound = random.nextInt(11);
		for (int i = 0; i < bound + 5; i++) {
			if (random.nextFloat() < 0.665) {
				world.addImportantParticle(SpectrumParticleTypes.WHITE_SPARKLE_RISING, true, getX(), getY(), getZ(),
						random.nextFloat() * 0.25F - 0.125F,
						random.nextFloat() * 0.25F - 0.125F,
						random.nextFloat() * 0.25F - 0.125F
				);
			} else {
				world.addImportantParticle(SpectrumParticleTypes.SHOOTING_STAR, true, getX(), getY(), getZ(),
						random.nextFloat() * 0.5F - 0.25F,
						random.nextFloat() * 0.5F - 0.25F,
						random.nextFloat() * 0.5F - 0.25F
				);
			}
		}
		super.onRemoved();
	}
	
	protected static void summonBarrageInternal(World world, LivingEntity user, Supplier<LightShardBaseEntity> supplier) {
		var random = user.getRandom();
		var projectiles = MathHelper.nextGaussian(random, 13, 5);
		var pos = user.getEyePos();
		
		user.playSound(SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 1.2F + random.nextFloat() * 0.6F);
		
		for (int i = 0; i < projectiles; i++) {
			LightShardBaseEntity shard = supplier.get();
			
			var velocityY = 0.0;
			if (user.isOnGround()) {
				velocityY = random.nextFloat() * 0.75;
			} else {
				velocityY = random.nextFloat() - 0.5;
			}
			
			var initVel = new Vec3d(
					random.nextFloat() * 2 - 1,
					velocityY,
					random.nextFloat() * 2 - 1
			);
			
			shard.setPosition(pos);
			shard.setInitialVelocity(initVel.add(user.getVelocity()));
			world.spawnEntity(shard);
		}
		
		for (int i = 0; i < projectiles * 3; i++) {
			
			var velocityY = 0.0;
			
			if (user.isOnGround()) {
				velocityY = random.nextFloat() * 0.75;
			} else {
				velocityY = random.nextFloat() - 0.5;
			}
			
			world.addParticle(SpectrumParticleTypes.SHOOTING_STAR, pos.x, pos.y, pos.z,
					random.nextFloat() * 0.8F - 0.4F,
					velocityY * 2,
					random.nextFloat() * 0.8F - 0.4F
			);
		}
	}
	
	public float getScaleOffset() {
		return scaleOffset;
	}
	
	public float getVanishingProgress(int age) {
		return 1 - (float) Math.min(maxAge - age, getVanishingLength()) / getVanishingLength();
	}
	
	public int getVanishingLength() {
		return Math.round(maxAge / 4F);
	}
	
	public void setTarget(@NotNull Entity target) {
		this.target = Optional.ofNullable(target.getUuid());
		this.targetEntity = Optional.of(target);
	}
	
	// this should probably be a target predicate
	public boolean isValidTarget(Optional<Entity> entityOptional) {
		if (entityOptional.isEmpty())
			return false;
		
		var entity = entityOptional.get();
		Entity owner = getOwner();
		if (owner != null && entity.isTeammate(owner)) {
			return false;
		}
		
		return !entity.isRemoved() && entity.isAlive() && !entity.isInvisible() && !entity.isInvulnerable();
	}
	
	@Override
	protected void initDataTracker() {
	}
	
	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		target.ifPresent(uuid -> nbt.putUuid("target", uuid));
		nbt.putDouble("initX", initialVelocity.x);
		nbt.putDouble("initY", initialVelocity.y);
		nbt.putDouble("initZ", initialVelocity.z);
		
		nbt.putFloat("damage", damage);
		nbt.putFloat("scale", scaleOffset);
		nbt.putLong("maxAge", maxAge);
	}
	
	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (nbt.contains("target")) {
			target = Optional.ofNullable(nbt.getUuid("target"));
		}
		
		initialVelocity = new Vec3d(
				nbt.getDouble("initX"),
				nbt.getDouble("initY"),
				nbt.getDouble("initZ")
		);
		
		damage = nbt.getFloat("damage");
		scaleOffset = nbt.getFloat("scale");
		maxAge = nbt.getLong("maxAge");
	}
	
	public abstract Identifier getTexture();
	
}
