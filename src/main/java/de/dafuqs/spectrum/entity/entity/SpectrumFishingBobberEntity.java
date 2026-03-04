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
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.stats.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;
import org.slf4j.*;

import java.util.*;

// yeah, this pretty much is a full reimplementation. Sadge
// I wanted to use more of FishingBobberEntity for mod compat,
// but most of FishingRod's methods are either private or are tricky to extend
public abstract class SpectrumFishingBobberEntity extends Projectile {
	private static final Logger LOGGER = LogUtils.getLogger();
	private final RandomSource velocityRandom = RandomSource.create();
	private boolean caughtFish;
	private int outOfOpenFluidTicks;
	private static final int MAX_OUT_OF_OPEN_WATER_TICKS = 10;
	private static final EntityDataAccessor<Integer> HOOK_ENTITY_ID = SynchedEntityData.defineId(SpectrumFishingBobberEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> CAUGHT_FISH = SynchedEntityData.defineId(SpectrumFishingBobberEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> ABLAZE = SynchedEntityData.defineId(SpectrumFishingBobberEntity.class, EntityDataSerializers.BOOLEAN); // needs to be synced to the client, so it can render on fire
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
			EntityType<? extends SpectrumFishingBobberEntity> type, Level world,
			int luckBonus, int waitTimeReductionTicks, int exuberanceLevel, int bigCatchLevel,
			int serendipityReelLevel, boolean inventoryInsertion, boolean ablaze
	) {
		super(type, world);
		this.noCulling = true;
		this.luckBonus = Math.max(0, luckBonus);
		this.waitTimeReductionTicks = Math.max(0, waitTimeReductionTicks);
		this.exuberanceLevel = Math.max(0, exuberanceLevel);
		this.bigCatchLevel = Math.max(0, bigCatchLevel);
		this.serendipityReelLevel = Math.max(0, serendipityReelLevel);
		this.inventoryInsertion = inventoryInsertion;
		this.getEntityData().set(ABLAZE, ablaze);
	}
	
	public SpectrumFishingBobberEntity(EntityType<? extends SpectrumFishingBobberEntity> entityType, Level world) {
		this(entityType, world, 0, 0, 0, 0, 0, false, false);
	}
	
	public SpectrumFishingBobberEntity(
			EntityType<? extends SpectrumFishingBobberEntity> entityType, Player thrower, Level world,
			int luckBonus, int waitTimeReductionTicks, int exuberanceLevel, int bigCatchLevel,
			int serendipityReelLevel, boolean inventoryInsertion, boolean ablaze
	) {
		this(entityType, world, luckBonus, waitTimeReductionTicks, exuberanceLevel, bigCatchLevel, serendipityReelLevel, inventoryInsertion, ablaze);
		this.setOwner(thrower);
		float f = thrower.getXRot();
		float g = thrower.getYRot();
		float h = Mth.cos(-g * (float) (Math.PI / 180.0) - (float) Math.PI);
		float i = Mth.sin(-g * (float) (Math.PI / 180.0) - (float) Math.PI);
		float j = -Mth.cos(-f * (float) (Math.PI / 180.0));
		float k = Mth.sin(-f * (float) (Math.PI / 180.0));
		double d = thrower.getX() - (double) i * 0.3;
		double e = thrower.getEyeY();
		double l = thrower.getZ() - (double) h * 0.3;
		this.moveTo(d, e, l, g, f);
		Vec3 vec3d = new Vec3(-i, Mth.clamp(-(k / j), -5.0F, 5.0F), -h);
		double m = vec3d.length();
		vec3d = vec3d.multiply(
				0.6 / m + this.random.triangle(0.5, 0.0103365),
				0.6 / m + this.random.triangle(0.5, 0.0103365),
				0.6 / m + this.random.triangle(0.5, 0.0103365)
		);
		this.setDeltaMovement(vec3d);
		//noinspection SuspiciousNameCombination
		this.setYRot((float) (Mth.atan2(vec3d.x, vec3d.z) * 180.0f / (float) Math.PI));
		this.setXRot((float) (Mth.atan2(vec3d.y, vec3d.horizontalDistance()) * 180.0f / (float) Math.PI));
		this.yRotO = this.getYRot();
		this.xRotO = this.getXRot();
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(HOOK_ENTITY_ID, 0);
		builder.define(CAUGHT_FISH, false);
		builder.define(ABLAZE, false);
	}
	
	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
		if (HOOK_ENTITY_ID.equals(data)) {
			int i = this.getEntityData().get(HOOK_ENTITY_ID);
			this.hookedEntity = i > 0 ? this.level().getEntity(i - 1) : null;
		}
		
		if (CAUGHT_FISH.equals(data)) {
			this.caughtFish = this.getEntityData().get(CAUGHT_FISH);
			if (this.caughtFish) {
				this.setDeltaMovement(this.getDeltaMovement().x, -0.4F * Mth.nextFloat(this.velocityRandom, 0.6F, 1.0F), this.getDeltaMovement().z);
			}
		}
		
		super.onSyncedDataUpdated(data);
	}
	
	@Override
	public boolean shouldRenderAtSqrDistance(double distance) {
		double d = 64.0;
		return distance < d * d;
	}
	
	public abstract int getLineColor();
	
	@Override
	public void lerpTo(double x, double y, double z, float yaw, float pitch, int interpolationSteps) {
	}
	
	@Override
	public void tick() {
		this.velocityRandom.setSeed(this.getUUID().getLeastSignificantBits() ^ this.level().getGameTime());
		super.tick();
		Player playerEntity = this.getPlayerOwner();
		if (playerEntity == null) {
			this.discard();
		} else if (this.level().isClientSide || !this.removeIfInvalid(playerEntity)) {
			if (this.onGround()) {
				this.removalTimer++;
				if (this.removalTimer >= 1200) {
					this.discard();
					return;
				}
			} else {
				this.removalTimer = 0;
			}
			
			float f = 0.0F;
			BlockPos blockPos = this.blockPosition();
			FluidState fluidState = this.level().getFluidState(blockPos);
			boolean canFishInFluid = getFishingRod(playerEntity).getItem() instanceof SpectrumFishingRodItem spectrumFishingRodItem && spectrumFishingRodItem.canFishIn(fluidState);
			if (canFishInFluid) {
				f = fluidState.getHeight(this.level(), blockPos);
			}
			
			boolean bl = f > 0.0F;
			if (this.state == SpectrumFishingBobberEntity.State.FLYING) {
				if (this.hookedEntity != null) {
					this.setDeltaMovement(Vec3.ZERO);
					this.state = SpectrumFishingBobberEntity.State.HOOKED_IN_ENTITY;
					return;
				}
				
				if (bl) {
					this.setDeltaMovement(this.getDeltaMovement().multiply(0.3, 0.2, 0.3));
					this.state = SpectrumFishingBobberEntity.State.BOBBING;
					return;
				}
				
				this.checkForCollision();
			} else {
				if (this.state == SpectrumFishingBobberEntity.State.HOOKED_IN_ENTITY) {
					if (this.hookedEntity != null) {
						if (!this.hookedEntity.isRemoved() && this.hookedEntity.level().dimension() == this.level().dimension()) {
							this.setPos(this.hookedEntity.getX(), this.hookedEntity.getY(0.8), this.hookedEntity.getZ());
							hookedEntityTick(this.hookedEntity);
						} else {
							this.updateHookedEntityId(null);
							this.state = SpectrumFishingBobberEntity.State.FLYING;
						}
					}
					
					return;
				}
				
				if (this.state == SpectrumFishingBobberEntity.State.BOBBING) {
					Vec3 vec3d = this.getDeltaMovement();
					double d = this.getY() + vec3d.y - (double) blockPos.getY() - (double) f;
					if (Math.abs(d) < 0.01) {
						d += Math.signum(d) * 0.1;
					}
					
					this.setDeltaMovement(vec3d.x * 0.9, vec3d.y - d * (double) this.random.nextFloat() * 0.2, vec3d.z * 0.9);
					if (this.hookCountdown <= 0 && this.fishTravelCountdown <= 0) {
						this.inOpenWater = true;
					} else {
						this.inOpenWater = this.inOpenWater && this.outOfOpenFluidTicks < MAX_OUT_OF_OPEN_WATER_TICKS && this.isOpenOrWaterAround(blockPos);
					}
					
					if (bl) {
						this.outOfOpenFluidTicks = Math.max(0, this.outOfOpenFluidTicks - 1);
						if (this.caughtFish) {
							this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.1 * (double) this.velocityRandom.nextFloat() * (double) this.velocityRandom.nextFloat(), 0.0));
						}
						
						if (!this.level().isClientSide) {
							this.tickFishingLogic(blockPos);
						}
					} else {
						this.outOfOpenFluidTicks = Math.min(MAX_OUT_OF_OPEN_WATER_TICKS, this.outOfOpenFluidTicks + 1);
					}
				}
			}
			
			if (!canFishInFluid) {
				this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.03, 0.0));
			}
			
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.updateRotation();
			if (this.state == SpectrumFishingBobberEntity.State.FLYING && (this.onGround() || this.horizontalCollision)) {
				this.setDeltaMovement(Vec3.ZERO);
			}
			
			double e = 0.92;
			this.setDeltaMovement(this.getDeltaMovement().scale(e));
			this.reapplyPosition();
		}
	}
	
	public boolean removeIfInvalid(Player player) {
		ItemStack itemStack = player.getMainHandItem();
		ItemStack itemStack2 = player.getOffhandItem();
		boolean bl = itemStack.getItem() instanceof SpectrumFishingRodItem;
		boolean bl2 = itemStack2.getItem() instanceof SpectrumFishingRodItem;
		if (!player.isRemoved() && player.isAlive() && (bl || bl2) && !(this.distanceToSqr(player) > 1024.0)) {
			return false;
		} else {
			this.discard();
			return true;
		}
	}
	
	private void checkForCollision() {
		HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
		this.hitTargetOrDeflectSelf(hitResult);
	}
	
	@Override
	public boolean canHitEntity(Entity entity) {
		return super.canHitEntity(entity) || entity.isAlive() && entity instanceof ItemEntity;
	}
	
	@Override
	public void onHitEntity(EntityHitResult entityHitResult) {
		super.onHitEntity(entityHitResult);
		if (!this.level().isClientSide) {
			this.updateHookedEntityId(entityHitResult.getEntity());
		}
	}
	
	@Override
	public void onHitBlock(BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);
		this.setDeltaMovement(this.getDeltaMovement().normalize().scale(blockHitResult.distanceTo(this)));
	}
	
	public void updateHookedEntityId(@Nullable Entity entity) {
		this.hookedEntity = entity;
		this.getEntityData().set(HOOK_ENTITY_ID, entity == null ? 0 : entity.getId() + 1);
	}
	
	public void tickFishingLogic(BlockPos pos) {
		ServerLevel serverWorld = (ServerLevel) this.level();
		int i = 1;
		BlockPos blockPos = pos.above();
		if (this.random.nextFloat() < 0.25F && this.level().isRainingAt(blockPos)) {
			i++;
		}
		
		if (this.random.nextFloat() < 0.5F && !this.level().canSeeSky(blockPos)) {
			i--;
		}
		
		if (this.hookCountdown > 0) {
			this.hookCountdown--;
			if (this.hookCountdown <= 0) {
				this.waitCountdown = 0;
				this.fishTravelCountdown = 0;
				this.getEntityData().set(CAUGHT_FISH, false);
			}
		} else if (this.fishTravelCountdown > 0) {
			this.fishTravelCountdown -= i;
			if (this.fishTravelCountdown > 0)
				this.fishAngle = this.fishAngle + (float) this.random.triangle(0.0, 9.188);
			float f = this.fishAngle * (float) (Math.PI / 180.0);
			float g = Mth.sin(f);
			float h = Mth.cos(f);
			double d = this.getX() + (double) (g * (float) this.fishTravelCountdown * 0.1F);
			double e = ((float) Mth.floor(this.getY()) + 1.0F);
			double j = this.getZ() + (double) (h * (float) this.fishTravelCountdown * 0.1F);
			BlockState blockState = serverWorld.getBlockState(BlockPos.containing(d, e - 1.0, j));
			Tuple<SimpleParticleType, SimpleParticleType> particles = getFluidParticles(blockState);
			if (this.fishTravelCountdown > 0) {
				if (particles != null) {
					if (this.random.nextFloat() < 0.15F) {
						serverWorld.sendParticles(particles.getA(), d, e - 0.1F, j, 1, g, 0.1, h, 0.0);
					}
					float k = g * 0.04F;
					float l = h * 0.04F;
					serverWorld.sendParticles(particles.getB(), d, e, j, 0, l, 0.01, (-k), 1.0);
					serverWorld.sendParticles(particles.getB(), d, e, j, 0, (-l), 0.01, k, 1.0);
				}
			} else if (particles != null) {
				this.playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
				double m = this.getY() + 0.5;
				serverWorld.sendParticles(
						particles.getA(), this.getX(), m, this.getZ(), (int) (1.0F + this.getBbWidth() * 20.0F), this.getBbWidth(), 0.0, this.getBbWidth(), 0.2F
				);
				serverWorld.sendParticles(
						particles.getB(), this.getX(), m, this.getZ(), (int) (1.0F + this.getBbWidth() * 20.0F), this.getBbWidth(), 0.0, this.getBbWidth(), 0.2F
				);
				this.hookCountdown = Mth.nextInt(this.random, 20, 40);
				this.getEntityData().set(CAUGHT_FISH, true);
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
				float g = Mth.nextFloat(this.random, 0.0F, 360.0F) * (float) (Math.PI / 180.0);
				float h = Mth.nextFloat(this.random, 25.0F, 60.0F);
				double d = this.getX() + (double) (Mth.sin(g) * h) * 0.1;
				double e = ((float) Mth.floor(this.getY()) + 1.0);
				double j = this.getZ() + (double) (Mth.cos(g) * h) * 0.1;
				BlockState blockState = serverWorld.getBlockState(BlockPos.containing(d, e - 1.0, j));
				Tuple<SimpleParticleType, SimpleParticleType> particles = getFluidParticles(blockState);
				if (particles != null) {
					serverWorld.sendParticles(particles.getA(), d, e, j, 2 + this.random.nextInt(2), 0.1F, 0.0, 0.1F, 0.0);
				}
			}
			
			if (this.waitCountdown <= 0) {
				this.fishAngle = Mth.nextFloat(this.random, 0.0F, 360.0F);
				this.fishTravelCountdown = Mth.nextInt(this.random, 20, 80);
			}
		} else {
			this.waitCountdown = Mth.nextInt(this.random, 100, 600);
			this.waitCountdown = this.waitCountdown - this.waitTimeReductionTicks;
			this.waitCountdown = Math.max(1, this.waitCountdown);
		}
	}
	
	@Nullable
	private Tuple<SimpleParticleType, SimpleParticleType> getFluidParticles(BlockState blockState) {
		Tuple<SimpleParticleType, SimpleParticleType> particles = null;
		if (this.level().getBlockState(blockPosition()).getBlock() instanceof SpectrumFluidBlock spectrumFluidBlock) {
			particles = spectrumFluidBlock.getFishingParticles();
		} else if (blockState.is(Blocks.LAVA)) {
			particles = new Tuple<>(ParticleTypes.FLAME, SpectrumParticleTypes.LAVA_FISHING);
		} else if (blockState.is(Blocks.WATER)) {
			particles = new Tuple<>(ParticleTypes.BUBBLE, ParticleTypes.FISHING);
		}
		return particles;
	}
	
	public boolean isOpenOrWaterAround(BlockPos pos) {
		SpectrumFishingBobberEntity.PositionType positionType = SpectrumFishingBobberEntity.PositionType.INVALID;
		
		for (int i = -1; i <= 2; i++) {
			SpectrumFishingBobberEntity.PositionType positionType2 = this.getPositionType(pos.offset(-2, i, -2), pos.offset(2, i, 2));
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
		return BlockPos.betweenClosedStream(start, end)
				.map(this::getPositionType)
				.reduce((positionType, positionType2) -> positionType == positionType2 ? positionType : SpectrumFishingBobberEntity.PositionType.INVALID)
				.orElse(SpectrumFishingBobberEntity.PositionType.INVALID);
	}
	
	public SpectrumFishingBobberEntity.PositionType getPositionType(BlockPos pos) {
		BlockState blockState = this.level().getBlockState(pos);
		if (!blockState.isAir() && !blockState.is(Blocks.LILY_PAD)) {
			FluidState fluidState = blockState.getFluidState();
			return !fluidState.isEmpty() && fluidState.isSource() && blockState.getCollisionShape(this.level(), pos).isEmpty()
					? SpectrumFishingBobberEntity.PositionType.INSIDE_FLUID
					: SpectrumFishingBobberEntity.PositionType.INVALID;
		} else {
			return SpectrumFishingBobberEntity.PositionType.ABOVE_FLUID;
		}
	}
	
	public boolean isInOpenWater() {
		return this.inOpenWater;
	}
	
	public int use(ItemStack usedItem) {
		Player playerEntity = this.getPlayerOwner();
		if (!this.level().isClientSide() && playerEntity != null && !this.removeIfInvalid(playerEntity)) {
			int i = 0;
			if (this.hookedEntity != null) {
				this.pullHookedEntity(this.hookedEntity);
				SpectrumAdvancementCriteria.FISHING_ROD_HOOKED.trigger((ServerPlayer) playerEntity, usedItem, this, null, Collections.emptyList());
				this.level().broadcastEntityEvent(this, EntityEvent.FISHING_ROD_REEL_IN);
				i = this.hookedEntity instanceof ItemEntity ? 3 : 5;
			} else if (this.hookCountdown > 0) {
				
				if (!tryCatchEntity(usedItem, playerEntity, (ServerLevel) this.level(), this.blockPosition())) {
					int lootAmount = random.nextIntBetweenInclusive(1, (int) Math.pow(2, 1 + serendipityReelLevel) - 1);
					for (int j = 0; j < lootAmount; j++) {
						catchLoot(usedItem, playerEntity);
					}
				}
				
				i = 1;
			}
			
			if (this.onGround()) {
				i = 2;
			}
			
			this.discard();
			return i;
		} else {
			return 0;
		}
	}
	
	@Override
	public void handleEntityEvent(byte status) {
		if (status == EntityEvent.FISHING_ROD_REEL_IN
				&& this.level().isClientSide
				&& this.hookedEntity instanceof Player
				&& ((Player) this.hookedEntity).isLocalPlayer()) {
			this.pullHookedEntity(this.hookedEntity);
		}
		
		super.handleEntityEvent(status);
	}
	
	public void pullHookedEntity(Entity entity) {
		Entity entity2 = this.getOwner();
		if (entity2 != null) {
			Vec3 vec3d = (new Vec3(entity2.getX() - this.getX(), entity2.getY() - this.getY(), entity2.getZ() - this.getZ())).scale(0.1);
			entity.setDeltaMovement(entity.getDeltaMovement().add(vec3d));
		}
	}
	
	@Override
	protected MovementEmission getMovementEmission() {
		return MovementEmission.NONE;
	}
	
	@Override
	public void remove(RemovalReason reason) {
		this.setPlayerFishHook(null);
		super.remove(reason);
	}
	
	@Override
	public void onClientRemoval() {
		this.setPlayerFishHook(null);
	}
	
	@Override
	public void setOwner(@Nullable Entity entity) {
		super.setOwner(entity);
		this.setPlayerFishHook(this);
	}
	
	public void setPlayerFishHook(@Nullable SpectrumFishingBobberEntity fishingBobber) {
		Player playerEntity = this.getPlayerOwner();
		if (playerEntity != null) {
			((PlayerEntityAccessor) playerEntity).spectrum$setSpectrumBobber(fishingBobber);
		}
	}
	
	@Nullable
	public Player getPlayerOwner() {
		Entity entity = this.getOwner();
		return entity instanceof Player ? (Player) entity : null;
	}
	
	@Nullable
	public Entity getHookedEntity() {
		return this.hookedEntity;
	}
	
	@Override
	public boolean canUsePortal(boolean allowVehicles) {
		return false;
	}

//	@Override
//	public Packet<ClientPlayPacketListener> createSpawnPacket(EntityTrackerEntry entityTrackerEntry) {
//		Entity entity = this.getOwner();
//		return new EntitySpawnS2CPacket(this, entityTrackerEntry, entity == null ? this.getId() : entity.getId());
//	}
	
	@Override
	public void recreateFromPacket(ClientboundAddEntityPacket packet) {
		super.recreateFromPacket(packet);
		if (this.getPlayerOwner() == null) {
			int entityData = packet.getData();
			LOGGER.error("Failed to recreate fishing hook on client. {} (id: {}) is not a valid owner.", this.level().getEntity(entityData), entityData);
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
	
	public ItemStack getFishingRod(Player player) {
		ItemStack itemStack = player.getMainHandItem();
		if (itemStack.getItem() instanceof SpectrumFishingRodItem)
			return itemStack;
		itemStack = player.getOffhandItem();
		if (itemStack.getItem() instanceof SpectrumFishingRodItem)
			return itemStack;
		return ItemStack.EMPTY;
	}
	
	private boolean tryCatchEntity(ItemStack usedItem, Player playerEntity, ServerLevel world, BlockPos blockPos) {
		Optional<EntityFishingEntity> caughtEntityType = EntityFishingDataLoader.tryCatchEntity(world, blockPos, this.bigCatchLevel);
		if (caughtEntityType.isPresent()) {
			var entityType = caughtEntityType.get().entityType();
			var nbt = CustomData.of(caughtEntityType.get().nbt());
			
			Entity entity = entityType.value().spawn(world, ent -> EntityType.updateCustomEntityTag(world, playerEntity, ent, nbt), blockPos, MobSpawnType.TRIGGERED, false, false);
			if (entity != null) {
				double xDif = playerEntity.getX() - this.getX();
				double yDif = playerEntity.getY() - this.getY();
				double zDif = playerEntity.getZ() - this.getZ();
				double velocityMod = 0.15D;
				entity.push(xDif * velocityMod, yDif * velocityMod + Math.sqrt(Math.sqrt(xDif * xDif + yDif * yDif + zDif * zDif)) * 0.08D, zDif * velocityMod);
				
				if (isAblaze()) {
					entity.igniteForSeconds(4);
				}
				
				if (entity instanceof Mob mobEntity) {
					mobEntity.playAmbientSound();
					mobEntity.spawnAnim();
				}
				SpectrumAdvancementCriteria.FISHING_ROD_HOOKED.trigger((ServerPlayer) playerEntity, usedItem, this, entity, List.of());
				
				return true;
			}
		}
		
		return false;
	}
	
	protected void catchLoot(ItemStack usedItem, Player playerEntity) {
		LootParams lootContextParameterSet = new LootParams.Builder((ServerLevel) playerEntity.level())
				.withParameter(LootContextParams.ORIGIN, this.position())
				.withParameter(LootContextParams.TOOL, usedItem)
				.withParameter(LootContextParams.THIS_ENTITY, this)
				.withParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, playerEntity)
				.withLuck((float) this.luckBonus + playerEntity.getLuck())
				.create(SpectrumLootContextParamSets.FISHING);
		
		if (level().getServer() == null) return;
		LootTable lootTable = this.level().getServer().reloadableRegistries().getLootTable(SpectrumLootTableKeys.UNIVERSAL_FISHING);
		List<ItemStack> list = lootTable.getRandomItems(lootContextParameterSet);
		SpectrumAdvancementCriteria.FISHING_ROD_HOOKED.trigger((ServerPlayer) playerEntity, usedItem, this, null, list);
		
		for (ItemStack itemStack : list) {
			if (itemStack.is(ItemTags.FISHES)) {
				playerEntity.awardStat(Stats.FISH_CAUGHT, 1);
			}
		}
		
		if (isAblaze()) {
			list = FoundryHelper.applyFoundry(this.level(), list);
		}
		
		float exuberanceMod = ExuberanceHelper.getExuberanceMod(this.exuberanceLevel);
		for (ItemStack itemStack : list) {
			int experienceAmount = this.random.nextInt((int) (6 * exuberanceMod) + 1);
			
			if (this.inventoryInsertion) {
				playerEntity.getInventory().placeItemBackInInventory(itemStack);
				playerEntity.giveExperiencePoints(experienceAmount);
				
				playerEntity.level().playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
						SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS,
						0.2F, ((playerEntity.getRandom().nextFloat() - playerEntity.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
			} else {
				// fireproof item, so it does not burn when fishing in lava
				ItemEntity itemEntity = new FireproofItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), itemStack);
				double d = playerEntity.getX() - this.getX();
				double e = playerEntity.getY() - this.getY();
				double f = playerEntity.getZ() - this.getZ();
				double g = 0.1D;
				itemEntity.setDeltaMovement(d * g, e * g + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08D, f * g);
				this.level().addFreshEntity(itemEntity);
				
				// experience
				if (experienceAmount > 0) {
					playerEntity.level().addFreshEntity(new ExperienceOrb(playerEntity.level(), playerEntity.getX(), playerEntity.getY() + 0.5D, playerEntity.getZ() + 0.5D, experienceAmount));
				}
			}
		}
	}
	
	public boolean isAblaze() {
		return this.getEntityData().get(ABLAZE);
	}
	
	@Override
	public boolean displayFireAnimation() {
		return isAblaze();
	}
	
	protected void hookedEntityTick(Entity hookedEntity) {
	}
	
}
