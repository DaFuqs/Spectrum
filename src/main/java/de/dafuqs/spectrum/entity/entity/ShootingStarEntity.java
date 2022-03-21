package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.shooting_star.ShootingStarBlock;
import de.dafuqs.spectrum.blocks.shooting_star.ShootingStarItem;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.effect.ParticleSpawnerParticleEffectAlwaysShow;
import de.dafuqs.spectrum.registries.SpectrumDamageSources;
import de.dafuqs.spectrum.registries.SpectrumItems;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockLocating;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ShootingStarEntity extends Entity {
	
	private static final Identifier PARTICLE_SPRITE_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "particle/shooting_star");
	
	private static final TrackedData<Integer> SHOOTING_STAR_TYPE = DataTracker.registerData(ShootingStarEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Boolean> PLAYER_PLACED = DataTracker.registerData(ShootingStarEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	protected final float hoverHeight;
	protected int age;
	protected int availableHits;
	protected int lastCollisionCount;

	public ShootingStarEntity(EntityType<? extends ShootingStarEntity> entityType, World world) {
		super(entityType, world);
		this.hoverHeight = (float)(Math.random() * Math.PI * 2.0D);
		this.availableHits = 5 + world.random.nextInt(3);
		this.lastCollisionCount = 0;
	}

	public ShootingStarEntity(World world, double x, double y, double z) {
		this(SpectrumEntityTypes.SHOOTING_STAR, world);
		this.setPosition(x, y, z);
		this.setYaw(this.random.nextFloat() * 360.0F);
		this.setShootingStarType(ShootingStarBlock.Type.COLORFUL, false);
		this.lastCollisionCount = 0;
	}

	public ShootingStarEntity(World world) {
		this(world, 0, 0, 0);
	}

	@Environment(EnvType.CLIENT)
	private ShootingStarEntity(@NotNull ShootingStarEntity entity) {
		super(entity.getType(), entity.world);
		this.setShootingStarType(entity.getShootingStarType(), false);
		this.copyPositionAndRotation(entity);
		this.age = entity.age;
		this.hoverHeight = entity.hoverHeight;
		this.lastCollisionCount = entity.lastCollisionCount;
	}

	public static void doShootingStarSpawns(@NotNull ServerWorld serverWorld) {
		if (SpectrumCommon.CONFIG.ShootingStarWorlds.contains(serverWorld.getRegistryKey().getValue().toString())) {
			if (serverWorld.getTime() % 100 == 0) {
				long timeOfDay = serverWorld.getTimeOfDay() % 24000;
				if (timeOfDay > 13000 && timeOfDay < 22000) { // 90 chances in a night
					for (PlayerEntity playerEntity : serverWorld.getEntitiesByType(EntityType.PLAYER, Entity::isAlive)) {
						if (!playerEntity.isSpectator() && Support.hasAdvancement(playerEntity, SpectrumItems.SHOOTING_STAR.getCloakAdvancementIdentifier()) && serverWorld.getRandom().nextFloat() < getShootingStarChanceWithMultiplier(playerEntity)) {
							// 1 % chance for each cycle to spawn a lot of shooting stars for the player
							// making it an amazing display
							if (serverWorld.getRandom().nextFloat() < 0.01) {
								for (int i = 0; i < 5; i++) {
									spawnShootingStar(serverWorld, playerEntity);
								}
							} else {
								spawnShootingStar(serverWorld, playerEntity);
							}
						}
					}
				}
			}
		}
	}

	public static void spawnShootingStar(ServerWorld serverWorld, @NotNull PlayerEntity playerEntity) {
		ShootingStarEntity shootingStarEntity = new ShootingStarEntity(serverWorld, playerEntity.getPos().getX(), playerEntity.getPos().getY() + 200, playerEntity.getPos().getZ());
		shootingStarEntity.setVelocity(serverWorld.random.nextDouble() * 0.2D - 0.1D, 0.0D, serverWorld.random.nextDouble() * 0.2D - 0.1D);
		shootingStarEntity.setShootingStarType(ShootingStarBlock.Type.getWeightedRandomType(serverWorld.getRandom()), false);
		shootingStarEntity.addVelocity(5 - shootingStarEntity.random.nextFloat() * 10, 0, 5 - shootingStarEntity.random.nextFloat() * 10);
		serverWorld.spawnEntity(shootingStarEntity);
	}
	
	// If the player explicitly searches for shooting stars give them a small boost :)
	// That these things increase the visibility of shooting stars is explicitly stated
	// in the manual, just not that these actually give a boost, too
	public static float getShootingStarChanceWithMultiplier(@NotNull PlayerEntity playerEntity) {
		int multiplier = 1;
		ItemStack handStack = playerEntity.getMainHandStack();
		if(handStack != null && handStack.isOf(Items.SPYGLASS)) {
			multiplier++;
		}
		StatusEffectInstance statusEffectInstance = playerEntity.getStatusEffect(StatusEffects.NIGHT_VISION);
		if(statusEffectInstance != null && statusEffectInstance.getDuration() > 0) {
			multiplier++;
		}
		return SpectrumCommon.CONFIG.ShootingStarChance * multiplier;
	}

	public boolean collidesWith(Entity other) {
		return canCollide(this, other);
	}
	
	public static boolean canCollide(Entity entity, @NotNull Entity other) {
		return (other.isCollidable() || other.isPushable()) && !entity.isConnectedThroughVehicle(other);
	}
	
	public boolean isCollidable() {
		return true;
	}
	
	public boolean isPushable() {
		return true;
	}
	
	protected Vec3d positionInPortal(Direction.Axis portalAxis, BlockLocating.Rectangle portalRect) {
		return LivingEntity.positionInPortal(super.positionInPortal(portalAxis, portalRect));
	}
	
	protected void initDataTracker() {
		this.getDataTracker().startTracking(SHOOTING_STAR_TYPE, ShootingStarBlock.Type.COLORFUL.ordinal());
		this.getDataTracker().startTracking(PLAYER_PLACED, false);
	}
	
	public void tick() {
		super.tick();
		this.tickNetherPortal();
		
		boolean wasOnGround = this.onGround;
		double previousXVelocity = this.getVelocity().getX();
		double previousYVelocity = this.getVelocity().getY();
		double previousZVelocity = this.getVelocity().getZ();
		
		if (world.isClient) {
			this.noClip = false;
		} else {
			this.noClip = !this.world.isSpaceEmpty(this, this.getBoundingBox().contract(1.0E-7D));
			if (this.noClip) {
				this.pushOutOfBlocks(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getZ());
			}
		}
		
		if (!this.hasNoGravity()) {
			double d = this.isTouchingWater() ? -0.005D : -0.04D;
			this.setVelocity(this.getVelocity().add(0.0D, d, 0.0D));
			if (!this.onGround) {
				this.setVelocity(this.getVelocity().multiply(0.95D));
			}
		}
		
		this.move(MovementType.SELF, this.getVelocity());

		var collidingEntities = this.world.getOtherEntities(this, getBoundingBox().expand(0.25, 0.334, 0.25));
		collidingEntities = collidingEntities.stream().filter(entity -> !(entity instanceof ShootingStarEntity)).collect(Collectors.toList());
		
		// make it bounce back
		boolean spawnLoot = false;
		if(this.onGround && !wasOnGround) {
			this.addVelocity(0, -previousYVelocity * 0.9, 0);
			collidingEntities.forEach(entity -> entity.move(MovementType.SHULKER_BOX, this.getVelocity().multiply(0, 1, 0)));
		}
		if(Math.signum(this.getVelocity().x) != Math.signum(previousXVelocity)) {
			this.addVelocity(-previousXVelocity * 0.6, 0, 0);
			if(Math.abs(previousXVelocity) > 0.5) {
				spawnLoot = true;
			}
		}
		if(Math.signum(this.getVelocity().z) != Math.signum(previousZVelocity)) {
			this.addVelocity(0, 0, -previousZVelocity * 0.6);
			if(!spawnLoot && Math.abs(previousZVelocity) > 0.5) {
				spawnLoot = true;
			}
		}

		collidingEntities.forEach(entity -> {
			if (entity.getY() >= this.getBoundingBox().maxY) {
				entity.fallDistance = 0F;
				if(this.collides()) {
					entity.setPosition(entity.getPos().x, this.getBoundingBox().maxY, entity.getPos().z);
				}
				entity.move(MovementType.SHULKER_BOX, this.getVelocity());
				entity.setOnGround(true);
			}
		});

		if(world.isClient) {
			if(!this.dataTracker.get(PLAYER_PLACED)) {
				if (this.onGround) {
					if (world.random.nextInt(10) == 0) {
						playGroundParticles();
					}
				} else {
					playFallingParticles();
				}
			}
		} else {
			// despawning
			if (this.age < 6000) {
				if(!this.dataTracker.get(PLAYER_PLACED)) {
					this.age++;
				}
			} else {
				this.discard();
			}
			
			this.checkBlockCollision();
			
			
			if(spawnLoot) {
				this.lastCollisionCount++;
				if(this.lastCollisionCount > 8) {
					// if the block did collide a lot (maybe bugged or jammed?): break it and drop as item
					this.availableHits--;
					if(this.availableHits > 0) {
						ItemStack shootingStarStack = ShootingStarItem.getWithRemainingHits((ShootingStarItem) this.asItem(), this.availableHits);
						ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), shootingStarStack);
						this.world.spawnEntity(itemEntity);
					} else {
						ItemStack shootingStarStack = SpectrumItems.SHOOTING_STAR.getDefaultStack();
						ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), shootingStarStack);
						this.world.spawnEntity(itemEntity);
					}
					this.discard();
				} else {
					// spawn loot
					List<ItemStack> loot = getLoot((ServerWorld) world, ShootingStarBlock.Type.BOUNCE_LOOT_TABLE);
					for (ItemStack itemStack : loot) {
						ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), itemStack);
						this.world.spawnEntity(itemEntity);
					}
					
					// do effects
					SpectrumS2CPacketSender.sendPlayShootingStarParticles(this);
					world.playSound(null, this.getBlockPos(), SpectrumSoundEvents.SHOOTING_STAR_CRACKER, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
			}
			
			if (!wasOnGround && onGround && previousYVelocity < -0.5) { // hitting the ground after a long fall
				SpectrumS2CPacketSender.playParticleWithExactOffsetAndVelocity((ServerWorld) world, getPos(), ParticleTypes.EXPLOSION, 1, new Vec3d(0, 0, 0), new Vec3d(0, 0, 0));
				if(!spawnLoot) {
					SpectrumS2CPacketSender.sendPlayShootingStarParticles(this);
					world.playSound(null, this.getBlockPos(), SpectrumSoundEvents.SHOOTING_STAR_CRACKER, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
			}
			
			// push other entities away
			this.checkBlockCollision();
			List<Entity> otherEntities = this.world.getOtherEntities(this, this.getBoundingBox().expand(0.20000000298023224D, -0.009999999776482582D, 0.20000000298023224D), EntityPredicates.canBePushedBy(this));
			if (!otherEntities.isEmpty()) {
				for (Entity d : otherEntities) {
					this.pushAwayFrom(d);
				}
			}
			
			this.updateWaterState();
		}
	}

	@Override
	public void onPlayerCollision(PlayerEntity player) {
		// if the shooting star is still falling from the sky, and it hits a player:
		// give the player the star, some damage and grant an advancement
		if(!this.world.isClient && !this.onGround && this.getVelocity().getY() < -0.5) {
			world.playSound(null, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), SpectrumSoundEvents.SHOOTING_STAR_CRACKER, SoundCategory.PLAYERS, 1.5F + random.nextFloat() * 0.4F, 0.8F + random.nextFloat() * 0.4F);
			SpectrumS2CPacketSender.sendPlayShootingStarParticles(this);
			player.damage(SpectrumDamageSources.SHOOTING_STAR, 18);

			ItemStack itemStack = this.getShootingStarType().getBlock().asItem().getDefaultStack();
			int i = itemStack.getCount();
			Support.givePlayer(player, itemStack);
			
			Support.grantAdvancementCriterion((ServerPlayerEntity) player, "catch_shooting_star", "catch");
			player.increaseStat(Stats.PICKED_UP.getOrCreateStat(itemStack.getItem()), i);

			this.discard();
		}
	}
	
	public void pushAwayFrom(Entity entity) {
		if (entity.getBoundingBox().minY <= this.getBoundingBox().minY) {
			super.pushAwayFrom(entity);
		}
	}
	
	public Item asItem() {
		return this.getShootingStarType().getBlock().asItem();
	}
	
	public boolean collides() {
		return !this.isRemoved();
	}
	
	public void playGroundParticles() {
		float randomScale = 0.5F + random.nextFloat();
		int randomLifetime = 30 + random.nextInt(20);
		
		ParticleEffect particleEffect = new ParticleSpawnerParticleEffectAlwaysShow(PARTICLE_SPRITE_IDENTIFIER, 0.05F, getShootingStarType().getRandomParticleColor(random), randomScale, randomLifetime, false, true);
		world.addParticle(particleEffect, this.getX(), this.getY() + 0.05F, this.getZ(), 0.1 - random.nextFloat() * 0.2, 0.4 + random.nextFloat() * 0.2, 0.1 - random.nextFloat() * 0.2);
	}
	
	public void playFallingParticles() {
		float randomScale = this.random.nextFloat() * 0.4F + 0.7F;
		ParticleEffect particleEffect = new ParticleSpawnerParticleEffectAlwaysShow(PARTICLE_SPRITE_IDENTIFIER, (float) ((random.nextDouble() - 0.5F) * 0.05F - 0.125F), getShootingStarType().getRandomParticleColor(random), randomScale, 120, false, true);
		world.addParticle(particleEffect, this.getX(), this.getY() + 0.05F, this.getZ(), 0.2 - random.nextFloat() * 0.4, 0.1, 0.2 - random.nextFloat() * 0.4);
	}
	
	public  void playHitParticles() {
		playHitParticles(this.world, this.getX(), this.getY(), this.getZ(), this.getShootingStarType(), 25);
	}
	
	public static void playHitParticles(World world, double x, double y, double z, ShootingStarBlock.Type type, int amount) {
		Random random = world.random;
		// Everything in this lambda is running on the render thread
		
		for(int i = 0; i < amount; i++) {
			float randomScale = 0.5F + random.nextFloat();
			int randomLifetime = 10 + random.nextInt(20);
			
			ParticleEffect particleEffect = new ParticleSpawnerParticleEffectAlwaysShow(PARTICLE_SPRITE_IDENTIFIER, 0.98F, type.getRandomParticleColor(random), randomScale, randomLifetime, false, true);
			world.addParticle(particleEffect, x, y, z, 0.35 - random.nextFloat() * 0.7, random.nextFloat() * 0.7, 0.35 - random.nextFloat() * 0.7);
		}
	}
	
	public void doPlayerHitEffectsAndLoot(ServerWorld serverWorld, ServerPlayerEntity serverPlayerEntity) {
		// Spawn loot
		Identifier lootTableId = ShootingStarBlock.Type.getLootTableIdentifier(dataTracker.get(SHOOTING_STAR_TYPE));
		List<ItemStack> loot = getLoot(serverWorld, serverPlayerEntity, lootTableId);

		for(ItemStack itemStack : loot) {
			ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), itemStack);
			this.world.spawnEntity(itemEntity);
		}
		
		// spawn particles
		SpectrumS2CPacketSender.sendPlayShootingStarParticles(this);
		world.playSound(null, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), SpectrumSoundEvents.SHOOTING_STAR_CRACKER, SoundCategory.PLAYERS, 1.5F + random.nextFloat() * 0.4F, 0.8F + random.nextFloat() * 0.4F);
	}
	
	public List<ItemStack> getLoot(ServerWorld serverWorld, ServerPlayerEntity serverPlayerEntity, Identifier lootTableId) {
		LootTable lootTable = serverWorld.getServer().getLootManager().getTable(lootTableId);
		return lootTable.generateLoot(new LootContext.Builder(serverWorld)
				.random(world.random)
				.parameter(LootContextParameters.THIS_ENTITY, this)
				.parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(this.getBlockPos()))
				.parameter(LootContextParameters.DAMAGE_SOURCE, DamageSource.player(serverPlayerEntity))
				.optionalParameter(LootContextParameters.LAST_DAMAGE_PLAYER, serverPlayerEntity)
				.build(LootContextTypes.ENTITY));
	}
	
	public List<ItemStack> getLoot(ServerWorld serverWorld, Identifier lootTableId) {
		LootTable lootTable = serverWorld.getServer().getLootManager().getTable(lootTableId);
		return lootTable.generateLoot(new LootContext.Builder(serverWorld)
				.random(world.random)
				.parameter(LootContextParameters.THIS_ENTITY, this)
				.parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(this.getBlockPos()))
				.parameter(LootContextParameters.DAMAGE_SOURCE, DamageSource.GENERIC)
				.build(LootContextTypes.ENTITY));
	}
	
	public Text getName() {
		Text text = this.getCustomName();
		return (text != null ? text : asItem().getName());
	}
	
	@Override
	public boolean handleAttack(Entity attacker) {
		if (!this.isRemoved()) {
			if (!this.world.isClient) {
				this.age = 1; // prevent it from despawning, once interacted
				
				this.availableHits--;
				if (this.world instanceof ServerWorld serverWorld && attacker instanceof ServerPlayerEntity serverPlayerEntity) {
					doPlayerHitEffectsAndLoot(serverWorld, serverPlayerEntity);
					this.lastCollisionCount = 0;
				}
				
				if (this.availableHits <= 0) {
					SpectrumS2CPacketSender.playParticleWithExactOffsetAndVelocity((ServerWorld) world, this.getPos(), ParticleTypes.EXPLOSION, 1, new Vec3d(0, 0, 0), new Vec3d(0, 0, 0));
					
					ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), SpectrumItems.SHOOTING_STAR.getDefaultStack());
					itemEntity.addVelocity(0, 0.15, 0);
					this.world.spawnEntity(itemEntity);
					this.discard();
					
					return true;
				} else {
					this.emitGameEvent(GameEvent.ENTITY_DAMAGED, attacker);
				}
			}
		
			double attackerOffsetX = this.getX() - attacker.getX();
			double attackerOffsetZ = this.getZ() - attacker.getZ();
			double mod = Math.max(attackerOffsetX, attackerOffsetZ);
			this.addVelocity((attackerOffsetX / mod) * 0.75, 0.25, (attackerOffsetZ / mod) * 0.75);

			var colidingEntities = this.world.getOtherEntities(this, getBoundingBox().expand(0.25, 0.334, 0.25));
			colidingEntities = colidingEntities.stream().filter(entity -> !(entity instanceof ShootingStarEntity)).collect(Collectors.toList());
			colidingEntities.forEach(entity -> {
				if (entity.getY() >= this.getBoundingBox().maxY) {
					entity.fallDistance = 0F;
					if(this.collides()) {
						entity.setPosition(entity.getPos().x, this.getBoundingBox().maxY, entity.getPos().z);
					}
					entity.move(MovementType.SHULKER_BOX, this.getVelocity());
					entity.setOnGround(true);
				}
			});

			this.scheduleVelocityUpdate();
		}
		
		return false;
	}
	
	public void setAvailableHits(int availableHits) {
		this.availableHits = availableHits;
	}
	
	@Override
	public boolean isInvulnerableTo(@NotNull DamageSource damageSource) {
		if(damageSource == DamageSource.ANVIL || damageSource == SpectrumDamageSources.FLOATBLOCK) {
			return false;
		} else {
			return damageSource.isFire() || super.isInvulnerableTo(damageSource);
		}
	}
	
	@Override
	public boolean damage(DamageSource damageSource, float amount) {
		if (amount > 5 && (damageSource == DamageSource.ANVIL || damageSource == SpectrumDamageSources.FLOATBLOCK)) {
			this.playHitParticles();
			
			ItemStack shootingStarStack = SpectrumItems.SHOOTING_STAR.getDefaultStack();
			shootingStarStack.setCount(2);
			ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), shootingStarStack);
			this.world.spawnEntity(itemEntity);
			this.discard();
			return true;
		}
		this.scheduleVelocityUpdate();
		return false;
	}
	
	public ShootingStarBlock.Type getShootingStarType() {
		return ShootingStarBlock.Type.getType(this.getDataTracker().get(SHOOTING_STAR_TYPE));
	}

	public void setShootingStarType(ShootingStarBlock.@NotNull Type type, boolean playerPlaced) {
		this.getDataTracker().set(SHOOTING_STAR_TYPE, type.ordinal());
		this.getDataTracker().set(PLAYER_PLACED, playerPlaced);
	}
	
	public void writeCustomDataToNbt(@NotNull NbtCompound tag) {
		tag.putShort("Age", (short)this.age);
		tag.putString("Type", this.getShootingStarType().getName());
		tag.putInt("LastCollisionCount", this.lastCollisionCount);
		tag.putBoolean("PlayerPlaced", this.dataTracker.get(PLAYER_PLACED));
	}
	
	public void readCustomDataFromNbt(@NotNull NbtCompound tag) {
		this.age = tag.getShort("Age");
		if(tag.contains("LastCollisionCount", NbtElement.INT_TYPE)) {
			this.lastCollisionCount = tag.getInt("LastCollisionCount");
		}
		
		boolean playerPlaced = false;
		
		if (tag.contains("PlayerPlaced")) {
			playerPlaced = tag.getBoolean("PlayerPlaced");
		}
		if (tag.contains("Type", 8)) {
			this.setShootingStarType(ShootingStarBlock.Type.getType(tag.getString("Type")), playerPlaced);
		} else {
			this.discard();
		}
	}
	
	public ActionResult interact(PlayerEntity player, Hand hand) {
		if(!this.world.isClient && player.isSneaking()) {
			Support.givePlayer(player, ShootingStarItem.getWithRemainingHits((ShootingStarItem) this.asItem(), this.availableHits));
			this.discard();
			return ActionResult.CONSUME;
		} else {
			return ActionResult.PASS;
		}
	}
	
	public ItemStack getPickBlockStack() {
		return ShootingStarItem.getWithRemainingHits((ShootingStarItem) this.asItem(), this.availableHits);
	}
	
	@Environment(EnvType.CLIENT)
	public int getAge() {
		return this.age;
	}

	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this);
	}

	public SoundCategory getSoundCategory() {
		return SoundCategory.AMBIENT;
	}
	
}
