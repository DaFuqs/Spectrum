package de.dafuqs.spectrum.entity.entity;

import com.mojang.logging.*;
import de.dafuqs.spectrum.api.entity.*;
import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.data_loaders.EntityFishingDataLoader.*;
import de.dafuqs.spectrum.helpers.enchantments.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.loot.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.component.type.*;
import net.minecraft.entity.*;
import net.minecraft.entity.data.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.loot.*;
import net.minecraft.loot.context.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.particle.*;
import net.minecraft.registry.tag.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.stat.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import org.slf4j.*;

import java.util.*;

// yeah, this pretty much is a full reimplementation. Sadge
// I wanted to use more of FishingBobberEntity for mod compat,
// but most of FishingRod's methods are either private or are tricky to extend
public abstract class SpectrumFishingBobberEntity extends ProjectileEntity {
	private static final Logger LOGGER = LogUtils.getLogger();
	private final Random velocityRandom = Random.create();
	private boolean caughtFish;
	private int outOfOpenFluidTicks;
	private static final int MAX_OUT_OF_OPEN_WATER_TICKS = 10;
	private static final TrackedData<Integer> HOOK_ENTITY_ID = DataTracker.registerData(SpectrumFishingBobberEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Boolean> CAUGHT_FISH = DataTracker.registerData(SpectrumFishingBobberEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> ABLAZE = DataTracker.registerData(SpectrumFishingBobberEntity.class, TrackedDataHandlerRegistry.BOOLEAN); // needs to be synced to the client, so it can render on fire
	private int removalTimer;
	private int hookCountdown;
	private int waitCountdown;
	private int fishTravelCountdown;
	private float fishAngle;
	private boolean inOpenWater = true;
	@Nullable
	private Entity hookedEntity;
	private SpectrumFishingBobberEntity.State state = SpectrumFishingBobberEntity.State.FLYING;
	protected final int luckBonus;
	protected final int waitTimeReductionTicks;
	protected final int exuberanceLevel;
	protected final int bigCatchLevel;
	protected final int serendipityReelLevel;
	protected final boolean inventoryInsertion;
	
	public SpectrumFishingBobberEntity(
			EntityType<? extends SpectrumFishingBobberEntity> type, World world,
			int luckBonus, int waitTimeReductionTicks, int exuberanceLevel, int bigCatchLevel,
			int serendipityReelLevel, boolean inventoryInsertion, boolean ablaze
	) {
		super(type, world);
		this.ignoreCameraFrustum = true;
		this.luckBonus = Math.max(0, luckBonus);
		this.waitTimeReductionTicks = Math.max(0, waitTimeReductionTicks);
		this.exuberanceLevel = Math.max(0, exuberanceLevel);
		this.bigCatchLevel = Math.max(0, bigCatchLevel);
		this.serendipityReelLevel = Math.max(0, serendipityReelLevel);
		this.inventoryInsertion = inventoryInsertion;
		this.getDataTracker().set(ABLAZE, ablaze);
	}
	
	public SpectrumFishingBobberEntity(EntityType<? extends SpectrumFishingBobberEntity> entityType, World world) {
		this(entityType, world, 0, 0, 0, 0, 0, false, false);
	}
	
	public SpectrumFishingBobberEntity(
			EntityType<? extends SpectrumFishingBobberEntity> entityType, PlayerEntity thrower, World world,
			int luckBonus, int waitTimeReductionTicks, int exuberanceLevel, int bigCatchLevel,
			int serendipityReelLevel, boolean inventoryInsertion, boolean ablaze
	) {
		this(entityType, world, luckBonus, waitTimeReductionTicks, exuberanceLevel, bigCatchLevel, serendipityReelLevel, inventoryInsertion, ablaze);
		this.setOwner(thrower);
		float f = thrower.getPitch();
		float g = thrower.getYaw();
		float h = MathHelper.cos(-g * (float) (Math.PI / 180.0) - (float) Math.PI);
		float i = MathHelper.sin(-g * (float) (Math.PI / 180.0) - (float) Math.PI);
		float j = -MathHelper.cos(-f * (float) (Math.PI / 180.0));
		float k = MathHelper.sin(-f * (float) (Math.PI / 180.0));
		double d = thrower.getX() - (double) i * 0.3;
		double e = thrower.getEyeY();
		double l = thrower.getZ() - (double) h * 0.3;
		this.refreshPositionAndAngles(d, e, l, g, f);
		Vec3d vec3d = new Vec3d(-i, MathHelper.clamp(-(k / j), -5.0F, 5.0F), -h);
		double m = vec3d.length();
		vec3d = vec3d.multiply(
				0.6 / m + this.random.nextTriangular(0.5, 0.0103365),
				0.6 / m + this.random.nextTriangular(0.5, 0.0103365),
				0.6 / m + this.random.nextTriangular(0.5, 0.0103365)
		);
		this.setVelocity(vec3d);
		//noinspection SuspiciousNameCombination
		this.setYaw((float) (MathHelper.atan2(vec3d.x, vec3d.z) * 180.0f / (float) Math.PI));
		this.setPitch((float) (MathHelper.atan2(vec3d.y, vec3d.horizontalLength()) * 180.0f / (float) Math.PI));
		this.prevYaw = this.getYaw();
		this.prevPitch = this.getPitch();
	}
	
	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		builder.add(HOOK_ENTITY_ID, 0);
		builder.add(CAUGHT_FISH, false);
		builder.add(ABLAZE, false);
	}
	
	@Override
	public void onTrackedDataSet(TrackedData<?> data) {
		if (HOOK_ENTITY_ID.equals(data)) {
			int i = this.getDataTracker().get(HOOK_ENTITY_ID);
			this.hookedEntity = i > 0 ? this.getWorld().getEntityById(i - 1) : null;
		}
		
		if (CAUGHT_FISH.equals(data)) {
			this.caughtFish = this.getDataTracker().get(CAUGHT_FISH);
			if (this.caughtFish) {
				this.setVelocity(this.getVelocity().x, -0.4F * MathHelper.nextFloat(this.velocityRandom, 0.6F, 1.0F), this.getVelocity().z);
			}
		}
		
		super.onTrackedDataSet(data);
	}
	
	@Override
	public boolean shouldRender(double distance) {
		double d = 64.0;
		return distance < d * d;
	}
	
	public abstract int getLineColor();
	
	@Override
	public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps) {
	}
	
	@Override
	public void tick() {
		this.velocityRandom.setSeed(this.getUuid().getLeastSignificantBits() ^ this.getWorld().getTime());
		super.tick();
		PlayerEntity playerEntity = this.getPlayerOwner();
		if (playerEntity == null) {
			this.discard();
		} else if (this.getWorld().isClient || !this.removeIfInvalid(playerEntity)) {
			if (this.isOnGround()) {
				this.removalTimer++;
				if (this.removalTimer >= 1200) {
					this.discard();
					return;
				}
			} else {
				this.removalTimer = 0;
			}
			
			float f = 0.0F;
			BlockPos blockPos = this.getBlockPos();
			FluidState fluidState = this.getWorld().getFluidState(blockPos);
			boolean canFishInFluid = getFishingRod(playerEntity).getItem() instanceof SpectrumFishingRodItem spectrumFishingRodItem && spectrumFishingRodItem.canFishIn(fluidState);
			if (canFishInFluid) {
				f = fluidState.getHeight(this.getWorld(), blockPos);
			}
			
			boolean bl = f > 0.0F;
			if (this.state == SpectrumFishingBobberEntity.State.FLYING) {
				if (this.hookedEntity != null) {
					this.setVelocity(Vec3d.ZERO);
					this.state = SpectrumFishingBobberEntity.State.HOOKED_IN_ENTITY;
					return;
				}
				
				if (bl) {
					this.setVelocity(this.getVelocity().multiply(0.3, 0.2, 0.3));
					this.state = SpectrumFishingBobberEntity.State.BOBBING;
					return;
				}
				
				this.checkForCollision();
			} else {
				if (this.state == SpectrumFishingBobberEntity.State.HOOKED_IN_ENTITY) {
					if (this.hookedEntity != null) {
						if (!this.hookedEntity.isRemoved() && this.hookedEntity.getWorld().getRegistryKey() == this.getWorld().getRegistryKey()) {
							this.setPosition(this.hookedEntity.getX(), this.hookedEntity.getBodyY(0.8), this.hookedEntity.getZ());
							hookedEntityTick(this.hookedEntity);
						} else {
							this.updateHookedEntityId(null);
							this.state = SpectrumFishingBobberEntity.State.FLYING;
						}
					}
					
					return;
				}
				
				if (this.state == SpectrumFishingBobberEntity.State.BOBBING) {
					Vec3d vec3d = this.getVelocity();
					double d = this.getY() + vec3d.y - (double) blockPos.getY() - (double) f;
					if (Math.abs(d) < 0.01) {
						d += Math.signum(d) * 0.1;
					}
					
					this.setVelocity(vec3d.x * 0.9, vec3d.y - d * (double) this.random.nextFloat() * 0.2, vec3d.z * 0.9);
					if (this.hookCountdown <= 0 && this.fishTravelCountdown <= 0) {
						this.inOpenWater = true;
					} else {
						this.inOpenWater = this.inOpenWater && this.outOfOpenFluidTicks < MAX_OUT_OF_OPEN_WATER_TICKS && this.isOpenOrWaterAround(blockPos);
					}
					
					if (bl) {
						this.outOfOpenFluidTicks = Math.max(0, this.outOfOpenFluidTicks - 1);
						if (this.caughtFish) {
							this.setVelocity(this.getVelocity().add(0.0, -0.1 * (double) this.velocityRandom.nextFloat() * (double) this.velocityRandom.nextFloat(), 0.0));
						}
						
						if (!this.getWorld().isClient) {
							this.tickFishingLogic(blockPos);
						}
					} else {
						this.outOfOpenFluidTicks = Math.min(MAX_OUT_OF_OPEN_WATER_TICKS, this.outOfOpenFluidTicks + 1);
					}
				}
			}
			
			if (!canFishInFluid) {
				this.setVelocity(this.getVelocity().add(0.0, -0.03, 0.0));
			}
			
			this.move(MovementType.SELF, this.getVelocity());
			this.updateRotation();
			if (this.state == SpectrumFishingBobberEntity.State.FLYING && (this.isOnGround() || this.horizontalCollision)) {
				this.setVelocity(Vec3d.ZERO);
			}
			
			double e = 0.92;
			this.setVelocity(this.getVelocity().multiply(e));
			this.refreshPosition();
		}
	}
	
	public boolean removeIfInvalid(PlayerEntity player) {
		ItemStack itemStack = player.getMainHandStack();
		ItemStack itemStack2 = player.getOffHandStack();
		boolean bl = itemStack.getItem() instanceof SpectrumFishingRodItem;
		boolean bl2 = itemStack2.getItem() instanceof SpectrumFishingRodItem;
		if (!player.isRemoved() && player.isAlive() && (bl || bl2) && !(this.squaredDistanceTo(player) > 1024.0)) {
			return false;
		} else {
			this.discard();
			return true;
		}
	}
	
	private void checkForCollision() {
		HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
		this.hitOrDeflect(hitResult);
	}
	
	@Override
	public boolean canHit(Entity entity) {
		return super.canHit(entity) || entity.isAlive() && entity instanceof ItemEntity;
	}
	
	@Override
	public void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);
		if (!this.getWorld().isClient) {
			this.updateHookedEntityId(entityHitResult.getEntity());
		}
	}
	
	@Override
	public void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		this.setVelocity(this.getVelocity().normalize().multiply(blockHitResult.squaredDistanceTo(this)));
	}
	
	public void updateHookedEntityId(@Nullable Entity entity) {
		this.hookedEntity = entity;
		this.getDataTracker().set(HOOK_ENTITY_ID, entity == null ? 0 : entity.getId() + 1);
	}
	
	public void tickFishingLogic(BlockPos pos) {
		ServerWorld serverWorld = (ServerWorld) this.getWorld();
		int i = 1;
		BlockPos blockPos = pos.up();
		if (this.random.nextFloat() < 0.25F && this.getWorld().hasRain(blockPos)) {
			i++;
		}
		
		if (this.random.nextFloat() < 0.5F && !this.getWorld().isSkyVisible(blockPos)) {
			i--;
		}
		
		if (this.hookCountdown > 0) {
			this.hookCountdown--;
			if (this.hookCountdown <= 0) {
				this.waitCountdown = 0;
				this.fishTravelCountdown = 0;
				this.getDataTracker().set(CAUGHT_FISH, false);
			}
		} else if (this.fishTravelCountdown > 0) {
			this.fishTravelCountdown -= i;
			if (this.fishTravelCountdown > 0)
				this.fishAngle = this.fishAngle + (float) this.random.nextTriangular(0.0, 9.188);
			float f = this.fishAngle * (float) (Math.PI / 180.0);
			float g = MathHelper.sin(f);
			float h = MathHelper.cos(f);
			double d = this.getX() + (double) (g * (float) this.fishTravelCountdown * 0.1F);
			double e = ((float) MathHelper.floor(this.getY()) + 1.0F);
			double j = this.getZ() + (double) (h * (float) this.fishTravelCountdown * 0.1F);
			BlockState blockState = serverWorld.getBlockState(BlockPos.ofFloored(d, e - 1.0, j));
			Pair<SimpleParticleType, SimpleParticleType> particles = getFluidParticles(blockState);
			if (this.fishTravelCountdown > 0) {
				if (particles != null) {
					if (this.random.nextFloat() < 0.15F) {
						serverWorld.spawnParticles(particles.getLeft(), d, e - 0.1F, j, 1, g, 0.1, h, 0.0);
					}
					float k = g * 0.04F;
					float l = h * 0.04F;
					serverWorld.spawnParticles(particles.getRight(), d, e, j, 0, l, 0.01, (-k), 1.0);
					serverWorld.spawnParticles(particles.getRight(), d, e, j, 0, (-l), 0.01, k, 1.0);
				}
			} else if (particles != null) {
				this.playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
				double m = this.getY() + 0.5;
				serverWorld.spawnParticles(
						particles.getLeft(), this.getX(), m, this.getZ(), (int) (1.0F + this.getWidth() * 20.0F), this.getWidth(), 0.0, this.getWidth(), 0.2F
				);
				serverWorld.spawnParticles(
						particles.getRight(), this.getX(), m, this.getZ(), (int) (1.0F + this.getWidth() * 20.0F), this.getWidth(), 0.0, this.getWidth(), 0.2F
				);
				this.hookCountdown = MathHelper.nextInt(this.random, 20, 40);
				this.getDataTracker().set(CAUGHT_FISH, true);
			}
		} else if (this.waitCountdown > 0) {
			this.waitCountdown -= i;
			float f = 0.15F;
			if (this.waitCountdown < 20) {
				f += (float) (20 - this.waitCountdown) * 0.05F;
			} else if (this.waitCountdown < 40) {
				f += (float) (40 - this.waitCountdown) * 0.02F;
			} else if (this.waitCountdown < 60) {
				f += (float) (60 - this.waitCountdown) * 0.01F;
			}
			
			if (this.random.nextFloat() < f) {
				float g = MathHelper.nextFloat(this.random, 0.0F, 360.0F) * (float) (Math.PI / 180.0);
				float h = MathHelper.nextFloat(this.random, 25.0F, 60.0F);
				double d = this.getX() + (double) (MathHelper.sin(g) * h) * 0.1;
				double e = ((float) MathHelper.floor(this.getY()) + 1.0);
				double j = this.getZ() + (double) (MathHelper.cos(g) * h) * 0.1;
				BlockState blockState = serverWorld.getBlockState(BlockPos.ofFloored(d, e - 1.0, j));
				Pair<SimpleParticleType, SimpleParticleType> particles = getFluidParticles(blockState);
				if (particles != null) {
					serverWorld.spawnParticles(particles.getLeft(), d, e, j, 2 + this.random.nextInt(2), 0.1F, 0.0, 0.1F, 0.0);
				}
			}
			
			if (this.waitCountdown <= 0) {
				this.fishAngle = MathHelper.nextFloat(this.random, 0.0F, 360.0F);
				this.fishTravelCountdown = MathHelper.nextInt(this.random, 20, 80);
			}
		} else {
			this.waitCountdown = MathHelper.nextInt(this.random, 100, 600);
			this.waitCountdown = this.waitCountdown - this.waitTimeReductionTicks;
			this.waitCountdown = Math.max(1, this.waitCountdown);
		}
	}
	
	@Nullable
	private Pair<SimpleParticleType, SimpleParticleType> getFluidParticles(BlockState blockState) {
		Pair<SimpleParticleType, SimpleParticleType> particles = null;
		if (this.getWorld().getBlockState(getBlockPos()).getBlock() instanceof SpectrumFluidBlock spectrumFluidBlock) {
			particles = spectrumFluidBlock.getFishingParticles();
		} else if (blockState.isOf(Blocks.LAVA)) {
			particles = new Pair<>(ParticleTypes.FLAME, SpectrumParticleTypes.LAVA_FISHING);
		} else if (blockState.isOf(Blocks.WATER)) {
			particles = new Pair<>(ParticleTypes.BUBBLE, ParticleTypes.FISHING);
		}
		return particles;
	}
	
	public boolean isOpenOrWaterAround(BlockPos pos) {
		SpectrumFishingBobberEntity.PositionType positionType = SpectrumFishingBobberEntity.PositionType.INVALID;
		
		for (int i = -1; i <= 2; i++) {
			SpectrumFishingBobberEntity.PositionType positionType2 = this.getPositionType(pos.add(-2, i, -2), pos.add(2, i, 2));
			switch (positionType2) {
				case ABOVE_FLUID:
					if (positionType == SpectrumFishingBobberEntity.PositionType.INVALID) {
						return false;
					}
					break;
				case INSIDE_FLUID:
					if (positionType == SpectrumFishingBobberEntity.PositionType.ABOVE_FLUID) {
						return false;
					}
					break;
				case INVALID:
					return false;
			}
			
			positionType = positionType2;
		}
		
		return true;
	}
	
	public SpectrumFishingBobberEntity.PositionType getPositionType(BlockPos start, BlockPos end) {
		return BlockPos.stream(start, end)
				.map(this::getPositionType)
				.reduce((positionType, positionType2) -> positionType == positionType2 ? positionType : SpectrumFishingBobberEntity.PositionType.INVALID)
				.orElse(SpectrumFishingBobberEntity.PositionType.INVALID);
	}
	
	public SpectrumFishingBobberEntity.PositionType getPositionType(BlockPos pos) {
		BlockState blockState = this.getWorld().getBlockState(pos);
		if (!blockState.isAir() && !blockState.isOf(Blocks.LILY_PAD)) {
			FluidState fluidState = blockState.getFluidState();
			return !fluidState.isEmpty() && fluidState.isStill() && blockState.getCollisionShape(this.getWorld(), pos).isEmpty()
					? SpectrumFishingBobberEntity.PositionType.INSIDE_FLUID
					: SpectrumFishingBobberEntity.PositionType.INVALID;
		} else {
			return SpectrumFishingBobberEntity.PositionType.ABOVE_FLUID;
		}
	}
	
	public boolean isInOpenWater() {
		return this.inOpenWater;
	}
	
//	@Override
//	public void writeCustomDataToNbt(NbtCompound nbt) {
//	}
//
//	@Override
//	public void readCustomDataFromNbt(NbtCompound nbt) {
//	}
	
	public int use(ItemStack usedItem) {
		PlayerEntity playerEntity = this.getPlayerOwner();
		if (!this.getWorld().isClient() && playerEntity != null && !this.removeIfInvalid(playerEntity)) {
			int i = 0;
			if (this.hookedEntity != null) {
				this.pullHookedEntity(this.hookedEntity);
//				Criteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity) playerEntity, usedItem, null, Collections.emptyList());
				SpectrumAdvancementCriteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity) playerEntity, usedItem, this, null, Collections.emptyList());
				this.getWorld().sendEntityStatus(this, EntityStatuses.PULL_HOOKED_ENTITY);
				i = this.hookedEntity instanceof ItemEntity ? 3 : 5;
			} else if (this.hookCountdown > 0) {
//				LootContextParameterSet lootContextParameterSet = new net.minecraft.loot.context.LootContextParameterSet.Builder((ServerWorld) this.getWorld())
//						.add(LootContextParameters.ORIGIN, this.getPos())
//						.add(LootContextParameters.TOOL, usedItem)
//						.add(LootContextParameters.THIS_ENTITY, this)
//						.luck((float) this.luckBonus + playerEntity.getLuck())
//						.build(LootContextTypes.FISHING);
//				LootTable lootTable = this.getWorld().getServer().getReloadableRegistries().getLootTable(LootTables.FISHING_GAMEPLAY);
//				List<ItemStack> list = lootTable.generateLoot(lootContextParameterSet);
//				Criteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity) playerEntity, usedItem, this, list);
//
//				for (ItemStack itemStack : list) {
//					ItemEntity itemEntity = new ItemEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), itemStack);
//					double d = playerEntity.getX() - this.getX();
//					double e = playerEntity.getY() - this.getY();
//					double f = playerEntity.getZ() - this.getZ();
//					double g = 0.1;
//					itemEntity.setVelocity(d * g, e * g + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08, f * g);
//					this.getWorld().spawnEntity(itemEntity);
//					playerEntity.getWorld()
//							.spawnEntity(
//									new ExperienceOrbEntity(playerEntity.getWorld(), playerEntity.getX(), playerEntity.getY() + 0.5, playerEntity.getZ() + 0.5, this.random.nextInt(6) + 1)
//							);
//					if (itemStack.isIn(ItemTags.FISHES)) {
//						playerEntity.increaseStat(Stats.FISH_CAUGHT, 1);
//					}
//				}
				
				if (!tryCatchEntity(usedItem, playerEntity, (ServerWorld) this.getWorld(), this.getBlockPos())) {
					int lootAmount = random.nextBetween(1, (int) Math.pow(2, 1 + serendipityReelLevel) - 1);
					for (int j = 0; j < lootAmount; j++) {
						catchLoot(usedItem, playerEntity);
					}
				}
				
				i = 1;
			}
			
			if (this.isOnGround()) {
				i = 2;
			}
			
			this.discard();
			return i;
		} else {
			return 0;
		}
	}
	
	@Override
	public void handleStatus(byte status) {
		if (status == EntityStatuses.PULL_HOOKED_ENTITY
				&& this.getWorld().isClient
				&& this.hookedEntity instanceof PlayerEntity
				&& ((PlayerEntity) this.hookedEntity).isMainPlayer()) {
			this.pullHookedEntity(this.hookedEntity);
		}
		
		super.handleStatus(status);
	}
	
	public void pullHookedEntity(Entity entity) {
		Entity entity2 = this.getOwner();
		if (entity2 != null) {
			Vec3d vec3d = (new Vec3d(entity2.getX() - this.getX(), entity2.getY() - this.getY(), entity2.getZ() - this.getZ())).multiply(0.1);
			entity.setVelocity(entity.getVelocity().add(vec3d));
		}
	}
	
	@Override
	protected MoveEffect getMoveEffect() {
		return MoveEffect.NONE;
	}
	
	@Override
	public void remove(RemovalReason reason) {
		this.setPlayerFishHook(null);
		super.remove(reason);
	}
	
	@Override
	public void onRemoved() {
		this.setPlayerFishHook(null);
	}
	
	@Override
	public void setOwner(@Nullable Entity entity) {
		super.setOwner(entity);
		this.setPlayerFishHook(this);
	}
	
	public void setPlayerFishHook(@Nullable SpectrumFishingBobberEntity fishingBobber) {
		PlayerEntity playerEntity = this.getPlayerOwner();
		if (playerEntity != null) {
			((PlayerEntityAccessor) playerEntity).setSpectrumBobber(fishingBobber);
		}
	}
	
	@Nullable
	public PlayerEntity getPlayerOwner() {
		Entity entity = this.getOwner();
		return entity instanceof PlayerEntity ? (PlayerEntity) entity : null;
	}
	
	@Nullable
	public Entity getHookedEntity() {
		return this.hookedEntity;
	}
	
	@Override
	public boolean canUsePortals(boolean allowVehicles) {
		return false;
	}
	
//	@Override
//	public Packet<ClientPlayPacketListener> createSpawnPacket(EntityTrackerEntry entityTrackerEntry) {
//		Entity entity = this.getOwner();
//		return new EntitySpawnS2CPacket(this, entityTrackerEntry, entity == null ? this.getId() : entity.getId());
//	}
	
	@Override
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		if (this.getPlayerOwner() == null) {
			int entityData = packet.getEntityData();
			LOGGER.error("Failed to recreate fishing hook on client. {} (id: {}) is not a valid owner.", this.getWorld().getEntityById(entityData), entityData);
			this.kill();
		}
	}
	
	public enum PositionType {
		ABOVE_FLUID,
		INSIDE_FLUID,
		INVALID
	}
	
	public enum State {
		FLYING,
		HOOKED_IN_ENTITY,
		BOBBING
	}
	
	public ItemStack getFishingRod(PlayerEntity player) {
		ItemStack itemStack = player.getMainHandStack();
		if (itemStack.getItem() instanceof SpectrumFishingRodItem)
			return itemStack;
		itemStack = player.getOffHandStack();
		if (itemStack.getItem() instanceof SpectrumFishingRodItem)
			return itemStack;
		return ItemStack.EMPTY;
	}
	
	private boolean tryCatchEntity(ItemStack usedItem, PlayerEntity playerEntity, ServerWorld world, BlockPos blockPos) {
		Optional<EntityFishingEntity> caughtEntityType = EntityFishingDataLoader.tryCatchEntity(world, blockPos, this.bigCatchLevel);
		if (caughtEntityType.isPresent()) {
			var entityType = caughtEntityType.get().entityType();
			var nbt = NbtComponent.of(caughtEntityType.get().nbt());
			
			Entity entity = entityType.value().spawn(world, ent -> EntityType.loadFromEntityNbt(world, playerEntity, ent, nbt), blockPos, SpawnReason.TRIGGERED, false, false);
			if (entity != null) {
				double xDif = playerEntity.getX() - this.getX();
				double yDif = playerEntity.getY() - this.getY();
				double zDif = playerEntity.getZ() - this.getZ();
				double velocityMod = 0.15D;
				entity.addVelocity(xDif * velocityMod, yDif * velocityMod + Math.sqrt(Math.sqrt(xDif * xDif + yDif * yDif + zDif * zDif)) * 0.08D, zDif * velocityMod);
				
				if (isAblaze()) {
					entity.setOnFireFor(4);
				}
				
				if (entity instanceof MobEntity mobEntity) {
					mobEntity.playAmbientSound();
					mobEntity.playSpawnEffects();
				}
				SpectrumAdvancementCriteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity) playerEntity, usedItem, this, entity, List.of());
				
				return true;
			}
		}
		
		return false;
	}
	
	protected void catchLoot(ItemStack usedItem, PlayerEntity playerEntity) {
		LootContextParameterSet lootContextParameterSet = new LootContextParameterSet.Builder((ServerWorld) playerEntity.getWorld())
				.add(LootContextParameters.ORIGIN, this.getPos())
				.add(LootContextParameters.TOOL, usedItem)
				.add(LootContextParameters.THIS_ENTITY, this)
				.add(LootContextParameters.DIRECT_ATTACKING_ENTITY, playerEntity)
				.luck((float) this.luckBonus + playerEntity.getLuck())
				.build(SpectrumLootContextTypes.FISHING);
		
		if (getWorld().getServer() == null) return;
		LootTable lootTable = this.getWorld().getServer().getReloadableRegistries().getLootTable(SpectrumLootTables.UNIVERSAL_FISHING);
		List<ItemStack> list = lootTable.generateLoot(lootContextParameterSet);
		SpectrumAdvancementCriteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity) playerEntity, usedItem, this, null, list);
		
		for (ItemStack itemStack : list) {
			if (itemStack.isIn(ItemTags.FISHES)) {
				playerEntity.increaseStat(Stats.FISH_CAUGHT, 1);
			}
		}
		
		if (isAblaze()) {
			list = FoundryHelper.applyFoundry(this.getWorld(), list);
		}
		
		float exuberanceMod = ExuberanceHelper.getExuberanceMod(this.exuberanceLevel);
		for (ItemStack itemStack : list) {
			int experienceAmount = this.random.nextInt((int) (6 * exuberanceMod) + 1);
			
			if (this.inventoryInsertion) {
				playerEntity.getInventory().offerOrDrop(itemStack);
				playerEntity.addExperience(experienceAmount);
				
				playerEntity.getWorld().playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
						SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS,
						0.2F, ((playerEntity.getRandom().nextFloat() - playerEntity.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
			} else {
				// fireproof item, so it does not burn when fishing in lava
				ItemEntity itemEntity = new FireproofItemEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), itemStack);
				double d = playerEntity.getX() - this.getX();
				double e = playerEntity.getY() - this.getY();
				double f = playerEntity.getZ() - this.getZ();
				double g = 0.1D;
				itemEntity.setVelocity(d * g, e * g + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08D, f * g);
				this.getWorld().spawnEntity(itemEntity);
				
				// experience
				if (experienceAmount > 0) {
					playerEntity.getWorld().spawnEntity(new ExperienceOrbEntity(playerEntity.getWorld(), playerEntity.getX(), playerEntity.getY() + 0.5D, playerEntity.getZ() + 0.5D, experienceAmount));
				}
			}
		}
	}
	
	public boolean isAblaze() {
		return this.getDataTracker().get(ABLAZE);
	}
	
	@Override
	public boolean doesRenderOnFire() {
		return isAblaze();
	}
	
	protected void hookedEntityTick(Entity hookedEntity) {
	}
	
}
