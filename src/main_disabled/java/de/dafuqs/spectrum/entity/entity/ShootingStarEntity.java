package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.blocks.shooting_star.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.particles.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.syncher.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.stats.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ShootingStarEntity extends Entity {
	
	private static final EntityDataAccessor<Integer> SHOOTING_STAR_TYPE = SynchedEntityData.defineId(ShootingStarEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> PLAYER_PLACED = SynchedEntityData.defineId(ShootingStarEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> HARDENED = SynchedEntityData.defineId(ShootingStarEntity.class, EntityDataSerializers.BOOLEAN);
	
	protected final float hoverHeight;
	protected long age;
	protected int availableHits;
	protected int lastCollisionCount;
	
	public ShootingStarEntity(EntityType<? extends ShootingStarEntity> entityType, Level world) {
		super(entityType, world);
		this.hoverHeight = (float) (Math.random() * Math.PI * 2.0D);
		this.availableHits = 5 + world.random.nextInt(3);
		this.lastCollisionCount = 0;
	}
	
	public ShootingStarEntity(Level world, double x, double y, double z, ShootingStar.Variant type, boolean playerPlaced, int availableHits, boolean hardened) {
		this(SpectrumEntityTypes.SHOOTING_STAR, world);
		this.setPos(x, y, z);
		this.setYRot(this.random.nextFloat() * 360.0F);
		this.setShootingStarType(type, playerPlaced, availableHits, hardened);
		this.lastCollisionCount = 0;
	}
	
	public static boolean canCollide(Entity entity, @NotNull Entity other) {
		return (other.canBeCollidedWith() || other.isPushable()) && !entity.isPassengerOfSameVehicle(other);
	}
	
	public static void playHitParticles(Level world, double x, double y, double z, ShootingStar.Variant type, int amount) {
		RandomSource random = world.random;
		// Everything in this lambda is running on the render thread
		
		for (int i = 0; i < amount; i++) {
			float randomScale = 0.5F + random.nextFloat();
			int randomLifetime = 10 + random.nextInt(20);
			
			ParticleOptions particleEffect = new DynamicParticleEffect(0.98F, type.getRandomParticleColor(random), randomScale, randomLifetime, false, true, true);
			world.addParticle(particleEffect, x, y, z, 0.35 - random.nextFloat() * 0.7, random.nextFloat() * 0.7, 0.35 - random.nextFloat() * 0.7);
		}
	}
	
	@Override
	public boolean canCollideWith(Entity other) {
		return canCollide(this, other);
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return true;
	}
	
	@Override
	public boolean isPickable() {
		return !this.isRemoved();
	}
	
	@Override
	public boolean isPushable() {
		return true;
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(SHOOTING_STAR_TYPE, ShootingStar.Variant.COLORFUL.ordinal());
		builder.define(PLAYER_PLACED, false);
		builder.define(HARDENED, false);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		boolean wasOnGround = this.onGround();
		double previousXVelocity = this.getDeltaMovement().x();
		double previousYVelocity = this.getDeltaMovement().y();
		double previousZVelocity = this.getDeltaMovement().z();
		
		Level world = this.level();
		if (world.isClientSide) {
			this.noPhysics = false;
		} else {
			this.noPhysics = !this.level().noCollision(this, this.getBoundingBox().deflate(1.0E-7D));
			if (this.noPhysics) {
				this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getZ());
			}
		}
		
		if (!this.isNoGravity()) {
			double d = this.isInWater() ? -0.005D : -0.04D;
			this.setDeltaMovement(this.getDeltaMovement().add(0.0D, d, 0.0D));
			if (!this.onGround()) {
				this.setDeltaMovement(this.getDeltaMovement().scale(0.95D));
			}
		}
		
		this.move(MoverType.SELF, this.getDeltaMovement());
		
		var collidingEntities = this.level().getEntities(this, getBoundingBox().inflate(0.25, 0.334, 0.25));
		collidingEntities = collidingEntities.stream().filter(entity -> !(entity instanceof ShootingStarEntity) && (entity.isPushable())).toList();
		
		// make it bounce back
		boolean spawnLoot = false;
		boolean playerPlaced = this.entityData.get(PLAYER_PLACED);
		boolean hardened = this.entityData.get(HARDENED);
		if (this.onGround() && !wasOnGround) {
			this.push(0, -previousYVelocity * 0.9, 0);
			collidingEntities.forEach(entity -> entity.move(MoverType.SHULKER_BOX, this.getDeltaMovement().multiply(0, 1, 0)));
		}
		if (Math.signum(this.getDeltaMovement().x) != Math.signum(previousXVelocity)) {
			this.push(-previousXVelocity * 0.6, 0, 0);
			if (!hardened && Math.abs(previousXVelocity) > 0.5) {
				spawnLoot = true;
			}
		}
		if (Math.signum(this.getDeltaMovement().z) != Math.signum(previousZVelocity)) {
			this.push(0, 0, -previousZVelocity * 0.6);
			if (!hardened && !spawnLoot && Math.abs(previousZVelocity) > 0.5) {
				spawnLoot = true;
			}
		}
		
		collidingEntities.forEach(entity -> {
			if (entity.getY() >= this.getBoundingBox().maxY) {
				entity.fallDistance = 0F;
				if (this.isPickable()) {
					entity.setPos(entity.position().x, this.getBoundingBox().maxY, entity.position().z);
				}
				entity.move(MoverType.SHULKER_BOX, this.getDeltaMovement());
				entity.setOnGround(true);
			}
		});
		
		if (world.isClientSide) {
			if (!playerPlaced && !hardened) {
				if (this.onGround()) {
					if (world.random.nextInt(10) == 0) {
						playGroundParticles();
					}
				} else {
					if (world.random.nextBoolean()) {
						playFallingParticles();
					}
				}
			}
		} else {
			// despawning
			this.age++;
			if (this.age > 6000 && !playerPlaced && !hardened) {
				this.discard();
				return;
			}
			
			this.checkInsideBlocks();
			
			if (spawnLoot && this.age > 1) {
				this.lastCollisionCount++;
				if (this.lastCollisionCount > 8) {
					// if the block did collide a lot (maybe bugged or jammed?): break it and drop as item
					this.availableHits--;
					if (this.availableHits > 0) {
						ItemStack shootingStarStack = ShootingStarItem.getWithRemainingHits((ShootingStarItem) this.asItem(), this.availableHits, this.entityData.get(HARDENED));
						ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), shootingStarStack);
						this.level().addFreshEntity(itemEntity);
					} else {
						ItemStack starFragmentStack = SpectrumItems.STAR_FRAGMENT.getDefaultInstance();
						ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), starFragmentStack);
						this.level().addFreshEntity(itemEntity);
					}
					this.discard();
				} else {
					// spawn loot
					List<ItemStack> loot = getLoot((ServerLevel) this.level(), SpectrumLootTables.SHOOTING_STAR_BOUNCE);
					for (ItemStack itemStack : loot) {
						ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), itemStack);
						this.level().addFreshEntity(itemEntity);
					}
					
					// do effects
					PlayShootingStarParticlesPayload.sendPlayShootingStarParticles(this);
					this.level().playSound(null, this.blockPosition(), SpectrumSoundEvents.SHOOTING_STAR_CRACKER, SoundSource.BLOCKS, 1.0F, 1.0F);
				}
			}
			
			if (!hardened && !wasOnGround && this.onGround() && previousYVelocity < -0.5) { // hitting the ground after a long fall
				PlayParticleWithExactVelocityPayload.playParticleWithExactVelocity((ServerLevel) this.level(), position(), ParticleTypes.EXPLOSION, 1, Vec3.ZERO);
				if (!spawnLoot) {
					PlayShootingStarParticlesPayload.sendPlayShootingStarParticles(this);
					this.level().playSound(null, this.blockPosition(), SpectrumSoundEvents.SHOOTING_STAR_CRACKER, SoundSource.BLOCKS, 1.0F, 1.0F);
				}
			}
			
			// push other entities away
			List<Entity> otherEntities = this.level().getEntities(this, this.getBoundingBox().inflate(0.2D, -0.01D, 0.2D), EntitySelector.pushableBy(this));
			if (!otherEntities.isEmpty()) {
				for (Entity d : otherEntities) {
					this.push(d);
				}
			}
			
			this.updateInWaterStateAndDoFluidPushing();
		}
	}
	
	@Override
	public void playerTouch(Player player) {
		// if the shooting star is still falling from the sky, and it hits a player:
		// give the player the star, some damage and grant an advancement
		if (!this.level().isClientSide() && !this.entityData.get(HARDENED) && !this.onGround() && this.getDeltaMovement().y() < -0.5) {
			this.level().playSound(null, this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ(), SpectrumSoundEvents.SHOOTING_STAR_CRACKER, SoundSource.PLAYERS, 1.5F + random.nextFloat() * 0.4F, 0.8F + random.nextFloat() * 0.4F);
			PlayShootingStarParticlesPayload.sendPlayShootingStarParticles(this);
			player.hurt(SpectrumDamageTypes.shootingStar(this.level()), 18);
			
			ItemStack itemStack = this.getShootingStarType().getBlock().asItem().getDefaultInstance();
			int i = itemStack.getCount();
			player.getInventory().placeItemBackInInventory(itemStack);
			
			Support.grantAdvancementCriterion((ServerPlayer) player, "catch_shooting_star", "catch");
			player.awardStat(Stats.ITEM_PICKED_UP.get(itemStack.getItem()), i);
			
			this.discard();
		}
	}
	
	@Override
	public void push(Entity entity) {
		if (entity.getBoundingBox().minY <= this.getBoundingBox().minY) {
			super.push(entity);
		}
	}
	
	public Item asItem() {
		return this.getShootingStarType().getBlock().asItem();
	}
	
	public void playGroundParticles() {
		float randomScale = 0.5F + random.nextFloat();
		int randomLifetime = 30 + random.nextInt(20);
		
		ParticleOptions particleEffect = new DynamicParticleEffect(0.05F, getShootingStarType().getRandomParticleColor(random), randomScale, randomLifetime, false, true, true);
		this.level().addParticle(particleEffect, this.getX(), this.getEyeY(), this.getZ(), 0.1 - random.nextFloat() * 0.2, 0.4 + random.nextFloat() * 0.2, 0.1 - random.nextFloat() * 0.2);
	}
	
	public void playFallingParticles() {
		float randomScale = this.random.nextFloat() * 0.4F + 0.7F;
		ParticleOptions particleEffect = new DynamicParticleEffect((float) ((random.nextDouble() - 0.5F) * 0.05F - 0.125F), getShootingStarType().getRandomParticleColor(random), randomScale, 120, false, true, true);
		this.level().addParticle(particleEffect, this.getX(), this.getEyeY(), this.getZ(), 0.2 - random.nextFloat() * 0.4, 0.1, 0.2 - random.nextFloat() * 0.4);
	}
	
	public void playHitParticles() {
		playHitParticles(this.level(), this.getX(), this.getEyeY(), this.getZ(), this.getShootingStarType(), 25);
	}
	
	public void doPlayerHitEffectsAndLoot(ServerLevel serverWorld, ServerPlayer serverPlayerEntity) {
		// Spawn loot
		@NotNull ResourceKey<LootTable> lootTableKey = ShootingStar.Variant.getLootTable(entityData.get(SHOOTING_STAR_TYPE));
		List<ItemStack> loot = getLoot(serverWorld, serverPlayerEntity, lootTableKey);
		
		for (ItemStack itemStack : loot) {
			ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), itemStack);
			this.level().addFreshEntity(itemEntity);
		}
		
		// spawn particles
		PlayShootingStarParticlesPayload.sendPlayShootingStarParticles(this);
		this.level().playSound(null, this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ(), SpectrumSoundEvents.SHOOTING_STAR_CRACKER, SoundSource.PLAYERS, 1.5F + random.nextFloat() * 0.4F, 0.8F + random.nextFloat() * 0.4F);
	}
	
	public List<ItemStack> getLoot(ServerLevel serverWorld, ServerPlayer serverPlayerEntity, ResourceKey<LootTable> lootTableKey) {
		LootTable lootTable = serverWorld.getServer().reloadableRegistries().getLootTable(lootTableKey);
		return lootTable.getRandomItems(new LootParams.Builder(serverWorld)
				.withParameter(LootContextParams.THIS_ENTITY, this)
				.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.blockPosition()))
				.withParameter(LootContextParams.DAMAGE_SOURCE, serverPlayerEntity.level().damageSources().playerAttack(serverPlayerEntity))
				.withOptionalParameter(LootContextParams.LAST_DAMAGE_PLAYER, serverPlayerEntity)
				.create(LootContextParamSets.ENTITY));
	}
	
	public List<ItemStack> getLoot(ServerLevel serverWorld, ResourceKey<LootTable> lootTableKey) {
		LootTable lootTable = serverWorld.getServer().reloadableRegistries().getLootTable(lootTableKey);
		return lootTable.getRandomItems(new LootParams.Builder(serverWorld)
				.withParameter(LootContextParams.THIS_ENTITY, this)
				.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.blockPosition()))
				.withParameter(LootContextParams.DAMAGE_SOURCE, serverWorld.damageSources().generic())
				.create(LootContextParamSets.ENTITY));
	}
	
	@Override
	public Component getName() {
		Component text = this.getCustomName();
		return (text != null ? text : asItem().getDescription());
	}
	
	@Override
	public boolean skipAttackInteraction(Entity attacker) {
		if (!this.isRemoved()) {
			if (!this.level().isClientSide()) {
				if (!this.entityData.get(HARDENED)) {
					this.age = 1; // prevent it from despawning, once interacted
					
					this.availableHits--;
					if (this.level() instanceof ServerLevel serverWorld && attacker instanceof ServerPlayer serverPlayerEntity) {
						doPlayerHitEffectsAndLoot(serverWorld, serverPlayerEntity);
						this.lastCollisionCount = 0;
					}
					
					if (this.availableHits <= 0) {
						PlayParticleWithExactVelocityPayload.playParticleWithExactVelocity((ServerLevel) this.level(), this.position(), ParticleTypes.EXPLOSION, 1, Vec3.ZERO);
						
						ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), SpectrumItems.STAR_FRAGMENT.getDefaultInstance());
						itemEntity.push(0, 0.15, 0);
						this.level().addFreshEntity(itemEntity);
						this.discard();
						
						return true;
					}
				}
				this.gameEvent(GameEvent.ENTITY_DAMAGE, attacker);
			}
			
			double attackerOffsetX = this.getX() - attacker.getX();
			double attackerOffsetZ = this.getZ() - attacker.getZ();
			double mod = Math.max(attackerOffsetX, attackerOffsetZ);
			this.push((attackerOffsetX / mod) * 0.75, 0.25, (attackerOffsetZ / mod) * 0.75);
			
			var collidingEntities = this.level().getEntities(this, getBoundingBox().inflate(0.25, 0.334, 0.25));
			collidingEntities = collidingEntities.stream().filter(entity -> !(entity instanceof ShootingStarEntity)).toList();
			collidingEntities.forEach(entity -> {
				if (entity.getY() >= this.getBoundingBox().maxY) {
					entity.fallDistance = 0F;
					if (this.isPickable()) {
						entity.setPos(entity.position().x, this.getBoundingBox().maxY, entity.position().z);
					}
					entity.move(MoverType.SHULKER_BOX, this.getDeltaMovement());
					entity.setOnGround(true);
				}
			});
			
			this.markHurt();
		}
		
		return false;
	}
	
	@Override
	public boolean isInvulnerableTo(@NotNull DamageSource damageSource) {
		if (damageSource.is(DamageTypes.FALLING_ANVIL) || damageSource.is(SpectrumDamageTypes.FLOATBLOCK)) {
			return false;
		} else {
			return damageSource.is(DamageTypeTags.IS_FIRE) || super.isInvulnerableTo(damageSource);
		}
	}
	
	@Override
	public boolean hurt(DamageSource damageSource, float amount) {
		if (amount > 5 && (damageSource.is(DamageTypes.FALLING_ANVIL) || damageSource.is(SpectrumDamageTypes.FLOATBLOCK))) {
			this.playHitParticles();
			
			ItemStack starFragmentStack = SpectrumItems.STAR_FRAGMENT.getDefaultInstance();
			starFragmentStack.setCount(2);
			ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), starFragmentStack);
			this.level().addFreshEntity(itemEntity);
			this.discard();
			return true;
		}
		this.markHurt();
		return false;
	}
	
	public ShootingStar.Variant getShootingStarType() {
		return ShootingStar.Variant.getType(this.getEntityData().get(SHOOTING_STAR_TYPE));
	}
	
	private void setShootingStarType(@NotNull ShootingStar.Variant type, boolean playerPlaced, int availableHits, boolean hardened) {
		this.getEntityData().set(SHOOTING_STAR_TYPE, type.ordinal());
		this.getEntityData().set(PLAYER_PLACED, playerPlaced);
		this.getEntityData().set(HARDENED, hardened);
		this.availableHits = availableHits;
	}
	
	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag tag) {
		tag.putLong("Age", (short) this.age);
		tag.putString("Variant", this.getShootingStarType().getName());
		tag.putInt("LastCollisionCount", this.lastCollisionCount);
		tag.putBoolean("PlayerPlaced", this.entityData.get(PLAYER_PLACED));
		tag.putBoolean("Hardened", this.entityData.get(HARDENED));
		tag.putInt("AvailableHits", this.availableHits);
	}
	
	@Override
	public void readAdditionalSaveData(@NotNull CompoundTag tag) {
		this.age = tag.getLong("Age");
		if (tag.contains("LastCollisionCount", Tag.TAG_ANY_NUMERIC)) {
			this.lastCollisionCount = tag.getInt("LastCollisionCount");
		}
		
		if (tag.contains("PlayerPlaced") && tag.contains("Hardened") && tag.contains("AvailableHits", Tag.TAG_ANY_NUMERIC) && tag.contains("Variant", Tag.TAG_STRING)) {
			boolean playerPlaced = tag.getBoolean("PlayerPlaced");
			int availableHits = tag.getInt("AvailableHits");
			boolean hardened = tag.getBoolean("Hardened");
			this.setShootingStarType(ShootingStar.Variant.getType(tag.getString("Variant")), playerPlaced, availableHits, hardened);
		}
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (!this.level().isClientSide() && player.isShiftKeyDown()) {
			this.level().playSound(null, this.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
			player.getInventory().placeItemBackInInventory(ShootingStarItem.getWithRemainingHits((ShootingStarItem) this.asItem(), this.availableHits, this.entityData.get(HARDENED)));
			this.discard();
			return InteractionResult.CONSUME;
		} else {
			return InteractionResult.PASS;
		}
	}
	
	@Override
	public ItemStack getPickResult() {
		return ShootingStarItem.getWithRemainingHits((ShootingStarItem) this.asItem(), this.availableHits, this.entityData.get(HARDENED));
	}
	
	@Override
	public SoundSource getSoundSource() {
		return SoundSource.AMBIENT;
	}
	
}
