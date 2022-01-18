package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.blocks.decoration.ShootingStarBlock;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItems;
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
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockLocating;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ShootingStarEntity extends Entity {
	
	private static final TrackedData<Integer> SHOOTING_STAR_TYPE = DataTracker.registerData(ShootingStarEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private int age;
	public final float hoverHeight;
	public int availableHits;

	public ShootingStarEntity(EntityType<? extends ShootingStarEntity> entityType, World world) {
		super(entityType, world);
		this.hoverHeight = (float)(Math.random() * 3.141592653589793D * 2.0D);
		this.availableHits = 3 + world.random.nextInt(3);
	}

	public ShootingStarEntity(World world, double x, double y, double z) {
		this(SpectrumEntityTypes.SHOOTING_STAR, world);
		this.setPosition(x, y, z);
		this.setYaw(this.random.nextFloat() * 360.0F);
		this.setVelocity(this.random.nextDouble() * 0.2D - 0.1D, 0.2D, this.random.nextDouble() * 0.2D - 0.1D);
		this.setShootingStarType(ShootingStarBlock.Type.COLORFUL);
	}

	public ShootingStarEntity(World world) {
		this(world, 0, 0, 0);
	}

	@Environment(EnvType.CLIENT)
	private ShootingStarEntity(@NotNull ShootingStarEntity entity) {
		super(entity.getType(), entity.world);
		this.setShootingStarType(entity.getShootingStarType());
		this.copyPositionAndRotation(entity);
		this.age = entity.age;
		this.hoverHeight = entity.hoverHeight;
	}

	public static void doShootingStarSpawns(@NotNull ServerWorld serverWorld) {
		if(SpectrumCommon.CONFIG.ShootingStarWorlds.contains(serverWorld.getRegistryKey().getValue().toString())) {
			if(serverWorld.getTime() % 100 == 0) {
				long timeOfDay = serverWorld.getTimeOfDay() % 24000;
				if(timeOfDay > 13000 && timeOfDay < 22000){
					for (PlayerEntity playerEntity : serverWorld.getEntitiesByType(EntityType.PLAYER, Entity::isAlive)) {
						if (!playerEntity.isSpectator() && Support.hasAdvancement(playerEntity, SpectrumItems.SHOOTING_STAR.getCloakAdvancementIdentifier()) && serverWorld.getRandom().nextFloat() < getShootingStarChanceWithMultiplier(playerEntity)) {
							// 1 % chance for each cycle to spawn a lot of shooting stars for the player
							// making it an amazing display
							if (serverWorld.getRandom().nextFloat() < 0.01) {
								for (int i = 0; i < 10; i++) {
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
		shootingStarEntity.setShootingStarType(ShootingStarBlock.Type.values()[serverWorld.getRandom().nextInt(ShootingStarBlock.Type.values().length)]);
		shootingStarEntity.addVelocity(3 - shootingStarEntity.random.nextFloat() * 6, 0, 3 - shootingStarEntity.random.nextFloat() * 6);
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

	protected MoveEffect getMoveEffect() {
		return MoveEffect.NONE;
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
	}

	public void tick() {
		super.tick();

		this.prevX = this.getX();
		this.prevY = this.getY();
		this.prevZ = this.getZ();
		Vec3d vec3d = this.getVelocity();

		if (!this.hasNoGravity()) {
			this.setVelocity(this.getVelocity().add(0.0D, -0.008D, 0.0D));
		}

		if (this.world.isClient) {
			this.noClip = false;
		} else {
			this.noClip = !this.world.isSpaceEmpty(this, this.getBoundingBox().contract(1.0E-7D));
			if (this.noClip) {
				this.pushOutOfBlocks(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getZ());
			}
		}

		if (!this.onGround || (this.age + this.getId()) % 4 == 0) {
			Vec3d velocity = this.getVelocity();
			world.addParticle(SpectrumParticleTypes.SHOOTING_STAR, this.getX(), this.getY(), this.getZ(), -velocity.x, -velocity.y, -velocity.z);

			this.move(MovementType.SELF, this.getVelocity());
			float g = 0.98F;
			if (this.onGround) {
				g = this.world.getBlockState(new BlockPos(this.getX(), this.getY() - 1.0D, this.getZ())).getBlock().getSlipperiness() * 0.98F;
			}

			this.setVelocity(this.getVelocity().multiply(g, 0.98D, g));
		}

		this.velocityDirty |= this.updateWaterState();
		if (!this.world.isClient) {
			double d = this.getVelocity().subtract(vec3d).lengthSquared();
			if (d > 0.01D) {
				this.velocityDirty = true;
			}
		}

		if (!this.world.isClient && this.age >= 6000) {
			this.discard();
		}
	}

	public void onPlayerCollision(PlayerEntity player) {
		if (!this.world.isClient) {
			player.damage(DamageSource.FALLING_BLOCK, 5);

			ItemStack itemStack = this.getShootingStarType().getBlock().asItem().getDefaultStack();
			int i = itemStack.getCount();
			if (player.getInventory().insertStack(itemStack)) {
				if(!player.isDead()) {
					player.sendPickup(this, i);
					if (itemStack.isEmpty()) {
						this.discard();
						itemStack.setCount(i);
					}

					Support.grantAdvancementCriterion((ServerPlayerEntity) player, "catch_shooting_star", "catch");
					player.increaseStat(Stats.PICKED_UP.getOrCreateStat(itemStack.getItem()), i);

					this.discard();
				}
			}
		}
	}
	
	public void pushAwayFrom(Entity entity) {
		if (entity instanceof BoatEntity) {
			if (entity.getBoundingBox().minY < this.getBoundingBox().maxY) {
				super.pushAwayFrom(entity);
			}
		} else if (entity.getBoundingBox().minY <= this.getBoundingBox().minY) {
			super.pushAwayFrom(entity);
		}
	}
	
	public Item asItem() {
		return switch (this.getShootingStarType()) {
			case COLORFUL -> SpectrumBlocks.COLORFUL_SHOOTING_STAR.asItem();
			case FIERY -> SpectrumBlocks.FIERY_SHOOTING_STAR.asItem();
			case GEMSTONE -> SpectrumBlocks.GEMSTONE_SHOOTING_STAR.asItem();
			case PRISTINE -> SpectrumBlocks.PRISTINE_SHOOTING_STAR.asItem();
			case GLISTERING -> SpectrumBlocks.GLISTERING_SHOOTING_STAR.asItem();
		};
	}
	
	public boolean collides() {
		return !this.isRemoved();
	}
	
	/*private void updateVelocity() {
		double d = -0.03999999910593033D;
		double e = this.hasNoGravity() ? 0.0D : -0.03999999910593033D;
		double f = 0.0D;
		this.velocityDecay = 0.05F;
		if (this.lastLocation == BoatEntity.Location.IN_AIR && this.location != BoatEntity.Location.IN_AIR && this.location != BoatEntity.Location.ON_LAND) {
			this.waterLevel = this.getBodyY(1.0D);
			this.setPosition(this.getX(), (double)(this.method_7544() - this.getHeight()) + 0.101D, this.getZ());
			this.setVelocity(this.getVelocity().multiply(1.0D, 0.0D, 1.0D));
			this.fallVelocity = 0.0D;
			this.location = BoatEntity.Location.IN_WATER;
		} else {
			if (this.location == BoatEntity.Location.IN_WATER) {
				f = (this.waterLevel - this.getY()) / (double)this.getHeight();
				this.velocityDecay = 0.9F;
			} else if (this.location == BoatEntity.Location.UNDER_FLOWING_WATER) {
				e = -7.0E-4D;
				this.velocityDecay = 0.9F;
			} else if (this.location == BoatEntity.Location.UNDER_WATER) {
				f = 0.009999999776482582D;
				this.velocityDecay = 0.45F;
			} else if (this.location == BoatEntity.Location.IN_AIR) {
				this.velocityDecay = 0.9F;
			} else if (this.location == BoatEntity.Location.ON_LAND) {
				this.velocityDecay = this.field_7714;
				if (this.getPrimaryPassenger() instanceof PlayerEntity) {
					this.field_7714 /= 2.0F;
				}
			}
			
			Vec3d vec3d = this.getVelocity();
			this.setVelocity(vec3d.x * (double)this.velocityDecay, vec3d.y + e, vec3d.z * (double)this.velocityDecay);
			this.yawVelocity *= this.velocityDecay;
			if (f > 0.0D) {
				Vec3d vec3d2 = this.getVelocity();
				this.setVelocity(vec3d2.x, (vec3d2.y + f * 0.06153846016296973D) * 0.75D, vec3d2.z);
			}
		}
		
	}*/
	
	public void spawnLootAndParticles(ServerWorld serverWorld, ServerPlayerEntity serverPlayerEntity) {
		Identifier lootTableId = ShootingStarBlock.Type.getLootTableIdentifier(dataTracker.get(SHOOTING_STAR_TYPE));
		LootTable lootTable = serverWorld.getServer().getLootManager().getTable(lootTableId);
		List<ItemStack> loot = lootTable.generateLoot(new LootContext.Builder(serverWorld)
				.random(world.random)
				.parameter(LootContextParameters.THIS_ENTITY, this)
				.parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(this.getBlockPos()))
				.parameter(LootContextParameters.DAMAGE_SOURCE, DamageSource.player(serverPlayerEntity))
				.optionalParameter(LootContextParameters.LAST_DAMAGE_PLAYER, serverPlayerEntity)
				.build(LootContextTypes.ENTITY));
		
		for(ItemStack itemStack : loot) {
			ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), itemStack);
			this.world.spawnEntity(itemEntity);
		}
		
		// TODO: Particles
	}
	
	public Text getName() {
		Text text = this.getCustomName();
		return (text != null ? text : asItem().getName());
	}
	
	@Override
	public boolean handleAttack(Entity attacker) {
		if (!this.world.isClient && !this.isRemoved()) {
			this.availableHits--;
			if(this.world instanceof ServerWorld serverWorld && attacker instanceof ServerPlayerEntity serverPlayerEntity) {
				spawnLootAndParticles(serverWorld, serverPlayerEntity);
			}
			
			if(this.availableHits <= 0) {
				ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), SpectrumItems.SHOOTING_STAR.getDefaultStack());
				this.world.spawnEntity(itemEntity);
				this.discard();
				
				return true;
			} else {
				this.scheduleVelocityUpdate();
				this.emitGameEvent(GameEvent.ENTITY_DAMAGED, attacker);
			}
		}
		
		return false;
	}
	
	@Override
	public boolean isInvulnerableTo(@NotNull DamageSource damageSource) {
		return damageSource.isFire() || super.isInvulnerableTo(damageSource);
	}
	
	public ShootingStarBlock.Type getShootingStarType() {
		return ShootingStarBlock.Type.getType(this.getDataTracker().get(SHOOTING_STAR_TYPE));
	}

	public void setShootingStarType(ShootingStarBlock.@NotNull Type type) {
		this.getDataTracker().set(SHOOTING_STAR_TYPE, type.ordinal());
	}
	
	public void writeCustomDataToNbt(@NotNull NbtCompound tag) {
		tag.putShort("Age", (short)this.age);
		tag.putString("Type", this.getShootingStarType().getName());
	}
	
	public void readCustomDataFromNbt(@NotNull NbtCompound tag) {
		this.age = tag.getShort("Age");
		if (tag.contains("Type", 8)) {
			this.setShootingStarType(ShootingStarBlock.Type.getType(tag.getString("Type")));
		} else {
			this.discard();
		}
	}
	
	public ActionResult interact(PlayerEntity player, Hand hand) {
		return ActionResult.PASS;
	}
	
	public ItemStack getPickBlockStack() {
		return new ItemStack(this.asItem());
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
