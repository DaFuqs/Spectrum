package de.dafuqs.spectrum.entity.entity;

import com.mojang.logging.*;
import de.dafuqs.spectrum.api.entity.*;
import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.data_loaders.EntityFishingDataLoader.*;
import de.dafuqs.spectrum.enchantments.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.data.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.loot.*;
import net.minecraft.loot.context.*;
import net.minecraft.nbt.*;
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
	private static final TrackedData<Integer> HOOK_ENTITY_ID = DataTracker.registerData(SpectrumFishingBobberEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Boolean> CAUGHT_FISH = DataTracker.registerData(SpectrumFishingBobberEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> ABLAZE = DataTracker.registerData(SpectrumFishingBobberEntity.class, TrackedDataHandlerRegistry.BOOLEAN); // needs to be synced to the client, so it can render on fire
	
	private final Random velocityRandom;
	private boolean caughtFish;
	private int outOfOpenFluidTicks;
	private int removalTimer;
	private int hookCountdown;
	private int waitCountdown;
	private int fishTravelCountdown;
	private float fishAngle;
	private boolean inTheOpen;
	private @Nullable Entity hookedEntity;
	private SpectrumFishingBobberEntity.State state;

	protected final int luckOfTheSeaLevel;
	protected final int lureLevel;
	protected final int exuberanceLevel;
	protected final int bigCatchLevel;
	protected final int serendipityReelLevel;
	protected final boolean inventoryInsertion;

	public SpectrumFishingBobberEntity(EntityType<? extends SpectrumFishingBobberEntity> type, World world,
									   int luckOfTheSeaLevel, int lureLevel, int exuberanceLevel, int bigCatchLevel,
									   int serendipityReelLevel, boolean inventoryInsertion, boolean ablaze) {
		super(type, world);
		this.velocityRandom = Random.create();
		this.inTheOpen = true;
		this.state = SpectrumFishingBobberEntity.State.FLYING;
		this.ignoreCameraFrustum = true;
		this.luckOfTheSeaLevel = Math.max(0, luckOfTheSeaLevel);
		this.lureLevel = Math.max(0, lureLevel);
		this.exuberanceLevel = Math.max(0, exuberanceLevel);
		this.bigCatchLevel = Math.max(0, bigCatchLevel);
		this.serendipityReelLevel = Math.max(0, serendipityReelLevel);
		this.inventoryInsertion = inventoryInsertion;
		this.getDataTracker().set(ABLAZE, ablaze);
	}
	
	public SpectrumFishingBobberEntity(EntityType<? extends SpectrumFishingBobberEntity> entityType, World world) {
		this(entityType, world, 0, 0, 0, 0, 0, false, false);
	}

	public SpectrumFishingBobberEntity(EntityType<? extends SpectrumFishingBobberEntity> entityType, PlayerEntity thrower, World world,
									   int luckOfTheSeaLevel, int lureLevel, int exuberanceLevel, int bigCatchLevel,
									   int serendipityReelLevel, boolean inventoryInsertion, boolean ablaze) {
		this(entityType, world, luckOfTheSeaLevel, lureLevel, exuberanceLevel, bigCatchLevel, serendipityReelLevel, inventoryInsertion, ablaze);
		this.setOwner(thrower);
		float f = thrower.getPitch();
		float g = thrower.getYaw();
		float h = MathHelper.cos(-g * 0.017453292F - 3.1415927F);
		float i = MathHelper.sin(-g * 0.017453292F - 3.1415927F);
		float j = -MathHelper.cos(-f * 0.017453292F);
		float k = MathHelper.sin(-f * 0.017453292F);
		double d = thrower.getX() - (double) i * 0.3D;
		double e = thrower.getEyeY();
		double l = thrower.getZ() - (double) h * 0.3D;
		this.refreshPositionAndAngles(d, e, l, g, f);
		Vec3d vec3d = new Vec3d((-i), MathHelper.clamp(-(k / j), -5.0F, 5.0F), (-h));
		double m = vec3d.length();
		vec3d = vec3d.multiply(0.6D / m + 0.5D + this.random.nextGaussian() * 0.0045D, 0.6D / m + 0.5D + this.random.nextGaussian() * 0.0045D, 0.6D / m + 0.5D + this.random.nextGaussian() * 0.0045D);
		this.setVelocity(vec3d);
		this.setYaw((float) (MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875D));
		this.setPitch((float) (MathHelper.atan2(vec3d.y, vec3d.horizontalLength()) * 57.2957763671875D));
		this.prevYaw = this.getYaw();
		this.prevPitch = this.getPitch();
	}
	
	@Override
	public boolean shouldRender(double distance) {
		return distance < 4096.0;
	}
	
	@Override
	public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
	}
	
	public boolean isAblaze() {
		return this.getDataTracker().get(ABLAZE);
	}
	
	@Override
	public boolean doesRenderOnFire() {
		return isAblaze();
	}
	
	@Override
	protected void initDataTracker() {
		this.getDataTracker().startTracking(HOOK_ENTITY_ID, 0);
		this.getDataTracker().startTracking(CAUGHT_FISH, false);
		this.getDataTracker().startTracking(ABLAZE, false);
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
				this.setVelocity(this.getVelocity().x, (-0.4F * MathHelper.nextFloat(this.velocityRandom, 0.6F, 1.0F)), this.getVelocity().z);
			}
		}
		
		super.onTrackedDataSet(data);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		this.velocityRandom.setSeed(this.getUuid().getLeastSignificantBits() ^ this.getWorld().getTime());
		PlayerEntity playerEntity = this.getPlayerOwner();
		if (playerEntity == null) {
			this.discard();
		} else if (this.getWorld().isClient() || !this.removeIfInvalid(playerEntity)) {
			if (this.isOnGround()) {
				++this.removalTimer;
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
			ItemStack rodStack = getFishingRod(playerEntity);
			boolean canFishInFluid = false;
			if (rodStack.getItem() instanceof SpectrumFishingRodItem spectrumFishingRodItem && spectrumFishingRodItem.canFishIn(fluidState)) {
				canFishInFluid = true;
				f = fluidState.getHeight(this.getWorld(), blockPos);
			}
			
			boolean bl = f > 0.0F;
			if (this.state == State.FLYING) {
				if (this.hookedEntity != null) {
					this.setVelocity(Vec3d.ZERO);
					this.state = State.HOOKED_IN_ENTITY;
					onHookedEntity(hookedEntity);
					return;
				}
				
				if (bl) {
					this.setVelocity(this.getVelocity().multiply(0.3D, 0.2D, 0.3D));
					this.state = State.BOBBING;
					return;
				}
				
				this.onCollision(ProjectileUtil.getCollision(this, this::canHit));
			} else {
				if (this.state == State.HOOKED_IN_ENTITY) {
					if (this.hookedEntity != null) {
						if (!this.hookedEntity.isRemoved() && this.hookedEntity.getWorld().getRegistryKey() == this.getWorld().getRegistryKey()) {
							this.setPosition(this.hookedEntity.getX(), this.hookedEntity.getBodyY(0.8D), this.hookedEntity.getZ());
							hookedEntityTick(this.hookedEntity);
						} else {
							this.updateHookedEntityId(null);
							this.state = State.FLYING;
						}
					}
					
					return;
				}
				
				if (this.state == State.BOBBING) {
					Vec3d vec3d = this.getVelocity();
					double d = this.getY() + vec3d.y - (double) blockPos.getY() - (double) f;
					if (Math.abs(d) < 0.01D) {
						d += Math.signum(d) * 0.1D;
					}
					
					this.setVelocity(vec3d.x * 0.9D, vec3d.y - d * (double) this.random.nextFloat() * 0.2D, vec3d.z * 0.9D);
					if (this.hookCountdown <= 0 && this.fishTravelCountdown <= 0) {
						this.inTheOpen = true;
					} else {
						this.inTheOpen = this.inTheOpen && this.outOfOpenFluidTicks < 10 && this.isInTheOpen(blockPos);
					}
					
					if (bl) {
						this.outOfOpenFluidTicks = Math.max(0, this.outOfOpenFluidTicks - 1);
						if (this.caughtFish) {
							this.setVelocity(this.getVelocity().add(0.0D, -0.1D * (double) this.velocityRandom.nextFloat() * (double) this.velocityRandom.nextFloat(), 0.0D));
						}
						
						if (!this.getWorld().isClient()) {
							this.tickFishingLogic(blockPos);
						}
					} else {
						this.outOfOpenFluidTicks = Math.min(10, this.outOfOpenFluidTicks + 1);
					}
				}
			}
			
			if (!canFishInFluid) {
				this.setVelocity(this.getVelocity().add(0.0D, -0.03D, 0.0D));
			}
			
			this.move(MovementType.SELF, this.getVelocity());
			this.updateRotation();
			if (this.state == State.FLYING && (this.isOnGround() || this.horizontalCollision)) {
				this.setVelocity(Vec3d.ZERO);
			}
			
			
			this.setVelocity(this.getVelocity().multiply(0.92D));
			this.refreshPosition();
		}
	}
	
	protected void onHookedEntity(Entity hookedEntity) {
	}
	
	protected void hookedEntityTick(Entity hookedEntity) {
	}
	
	@Override
	protected Entity.MoveEffect getMoveEffect() {
		return MoveEffect.NONE;
	}
	
	@Override
	public boolean canUsePortals() {
		return false;
	}
	
	public ItemStack getFishingRod(PlayerEntity player) {
		ItemStack itemStack = player.getMainHandStack();
		if (itemStack.getItem() instanceof SpectrumFishingRodItem) {
			return itemStack;
		}
		
		itemStack = player.getOffHandStack();
		if (itemStack.getItem() instanceof SpectrumFishingRodItem) {
			return itemStack;
		}
		return ItemStack.EMPTY;
	}
	
	public boolean removeIfInvalid(PlayerEntity player) {
		ItemStack rodStack = getFishingRod(player);
		if (!player.isRemoved() && player.isAlive() && !rodStack.isEmpty() && !(this.squaredDistanceTo(player) > 1024.0D)) {
			return false;
		} else {
			this.discard();
			return true;
		}
	}
	
	@Override
	public boolean canHit(Entity entity) {
		return super.canHit(entity) || entity.isAlive() && entity instanceof ItemEntity;
	}
	
	@Override
	public void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);
		if (!this.getWorld().isClient()) {
			Entity hookedEntity = entityHitResult.getEntity();
			this.updateHookedEntityId(hookedEntity);
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
			++i;
		}
		
		if (this.random.nextFloat() < 0.5F && !this.getWorld().isSkyVisible(blockPos)) {
			--i;
		}
		
		if (this.hookCountdown > 0) {
			--this.hookCountdown;
			if (this.hookCountdown <= 0) {
				this.waitCountdown = 0;
				this.fishTravelCountdown = 0;
				this.getDataTracker().set(CAUGHT_FISH, false);
			}
		} else {
			float f;
			float g;
			float h;
			double d;
			double e;
			double j;
			BlockState blockState;
			if (this.fishTravelCountdown > 0) {
				this.fishTravelCountdown -= i;
				
				this.fishAngle += (float) (this.random.nextGaussian() * 4.0D);
				f = this.fishAngle * 0.017453292F;
				g = MathHelper.sin(f);
				h = MathHelper.cos(f);
				d = this.getX() + (double) (g * (float) this.fishTravelCountdown * 0.1F);
				e = ((float) MathHelper.floor(this.getY()) + 1.0F);
				j = this.getZ() + (double) (h * (float) this.fishTravelCountdown * 0.1F);
				blockState = serverWorld.getBlockState(BlockPos.ofFloored(d, e - 1.0D, j));
				
				Pair<DefaultParticleType, DefaultParticleType> particles = getFluidParticles(blockState);
				if (this.fishTravelCountdown > 0) {
					float k = g * 0.04F;
					float l = h * 0.04F;
					if (particles != null) {
						if (this.random.nextFloat() < 0.15F) {
							serverWorld.spawnParticles(particles.getLeft(), d, e - 0.10000000149011612D, j, 1, g, 0.1D, h, 0.0D);
						}
						serverWorld.spawnParticles(particles.getRight(), d, e, j, 0, l, 0.01D, (-k), 1.0D);
						serverWorld.spawnParticles(particles.getRight(), d, e, j, 0, (-l), 0.01D, k, 1.0D);
					}
				} else {
					this.playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
					if (particles != null) {
						double m = this.getY() + 0.5D;
						serverWorld.spawnParticles(particles.getLeft(), this.getX(), m, this.getZ(), (int) (1.0F + this.getWidth() * 20.0F), this.getWidth(), 0.0D, this.getWidth(), 0.20000000298023224D);
						serverWorld.spawnParticles(particles.getRight(), this.getX(), m, this.getZ(), (int) (1.0F + this.getWidth() * 20.0F), this.getWidth(), 0.0D, this.getWidth(), 0.20000000298023224D);
					}
					this.hookCountdown = MathHelper.nextInt(this.random, 20, 40);
					this.getDataTracker().set(CAUGHT_FISH, true);
				}
			} else if (this.waitCountdown > 0) {
				this.waitCountdown -= i;
				f = 0.15F;
				if (this.waitCountdown < 20) {
					f += (float) (20 - this.waitCountdown) * 0.05F;
				} else if (this.waitCountdown < 40) {
					f += (float) (40 - this.waitCountdown) * 0.02F;
				} else if (this.waitCountdown < 60) {
					f += (float) (60 - this.waitCountdown) * 0.01F;
				}
				
				if (this.random.nextFloat() < f) {
					g = MathHelper.nextFloat(this.random, 0.0F, 360.0F) * 0.017453292F;
					h = MathHelper.nextFloat(this.random, 25.0F, 60.0F);
					d = this.getX() + (double) (MathHelper.sin(g) * h) * 0.1D;
					e = ((float) MathHelper.floor(this.getY()) + 1.0F);
					j = this.getZ() + (double) (MathHelper.cos(g) * h) * 0.1D;
					blockState = serverWorld.getBlockState(BlockPos.ofFloored(d, e - 1.0D, j));
					
					Pair<DefaultParticleType, DefaultParticleType> particles = getFluidParticles(blockState);
					if (particles != null) {
						serverWorld.spawnParticles(particles.getLeft(), d, e, j, 2 + this.random.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D);
					}
				}
				
				if (this.waitCountdown <= 0) {
					this.fishAngle = MathHelper.nextFloat(this.random, 0.0F, 360.0F);
					this.fishTravelCountdown = MathHelper.nextInt(this.random, 20, 80);
				}
			} else {
				this.waitCountdown = MathHelper.nextInt(this.random, 100, 600);
				this.waitCountdown -= this.lureLevel * 20 * 5;
				this.waitCountdown = Math.max(1, this.waitCountdown);
			}
		}
	}
	
	@Nullable
	private Pair<DefaultParticleType, DefaultParticleType> getFluidParticles(BlockState blockState) {
		Pair<DefaultParticleType, DefaultParticleType> particles = null;
		if (this.getWorld().getBlockState(getBlockPos()).getBlock() instanceof SpectrumFluidBlock spectrumFluidBlock) {
			particles = spectrumFluidBlock.getFishingParticles();
		} else if (blockState.isOf(Blocks.LAVA)) {
			particles = new Pair<>(ParticleTypes.FLAME, SpectrumParticleTypes.LAVA_FISHING);
		} else if (blockState.isOf(Blocks.WATER)) {
			particles = new Pair<>(ParticleTypes.BUBBLE, ParticleTypes.FISHING);
		}
		return particles;
	}
	
	public boolean isInTheOpen(BlockPos pos) {
		PositionType positionType = PositionType.INVALID;
		
		for (int i = -1; i <= 2; ++i) {
			PositionType positionType2 = this.getPositionType(pos.add(-2, i, -2), pos.add(2, i, 2));
			switch (positionType2) {
				case INVALID -> {
					return false;
				}
				case ABOVE_FLUID -> {
					if (positionType == PositionType.INVALID) {
						return false;
					}
				}
				case INSIDE_FLUID -> {
					if (positionType == PositionType.ABOVE_FLUID) {
						return false;
					}
				}
			}
			
			positionType = positionType2;
		}
		
		return true;
	}
	
	public PositionType getPositionType(BlockPos start, BlockPos end) {
		return BlockPos.stream(start, end).map(this::getPositionType).reduce((positionType, positionType2) -> positionType == positionType2 ? positionType : PositionType.INVALID).orElse(PositionType.INVALID);
	}
	
	public PositionType getPositionType(BlockPos pos) {
		BlockState blockState = this.getWorld().getBlockState(pos);
		if (!blockState.isAir() && !blockState.isOf(Blocks.LILY_PAD)) {
			FluidState fluidState = blockState.getFluidState();
			return !fluidState.isEmpty() && fluidState.isStill() && blockState.getCollisionShape(this.getWorld(), pos).isEmpty() ? PositionType.INSIDE_FLUID : PositionType.INVALID;
		} else {
			return PositionType.ABOVE_FLUID;
		}
	}
	
	public boolean isInTheOpen() {
		return this.inTheOpen;
	}
	
	public int use(ItemStack usedItem) {
		PlayerEntity playerEntity = this.getPlayerOwner();
		if (!this.getWorld().isClient() && playerEntity != null && !this.removeIfInvalid(playerEntity)) {
			int i = 0;
			if (this.hookedEntity != null) {
				this.pullHookedEntity(this.hookedEntity);
				SpectrumAdvancementCriteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity) playerEntity, usedItem, this, null, Collections.emptyList());
				this.getWorld().sendEntityStatus(this, EntityStatuses.PULL_HOOKED_ENTITY);
				i = this.hookedEntity instanceof ItemEntity ? 3 : 5;
			} else if (this.hookCountdown > 0) {
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
	
	private boolean tryCatchEntity(ItemStack usedItem, PlayerEntity playerEntity, ServerWorld world, BlockPos blockPos) {
		Optional<EntityFishingEntity> caughtEntityType = EntityFishingDataLoader.tryCatchEntity(world, blockPos, this.bigCatchLevel);
		if (caughtEntityType.isPresent()) {
			EntityType<?> entityType = caughtEntityType.get().entityType();
			Optional<NbtCompound> nbt = caughtEntityType.get().nbt();
			
			NbtCompound entityNbt = null;
			if (nbt.isPresent()) {
				entityNbt = new NbtCompound();
				entityNbt.put("EntityTag", nbt.get());
			}
			
			Entity entity = entityType.spawn(world, entityNbt, null, blockPos, SpawnReason.TRIGGERED, false, false);
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
				.luck((float) this.luckOfTheSeaLevel + playerEntity.getLuck())
				.build(LootContextTypes.FISHING);
		
		LootTable lootTable = this.getWorld().getServer().getLootManager().getLootTable(SpectrumLootTables.UNIVERSAL_FISHING);
		List<ItemStack> list = lootTable.generateLoot(lootContextParameterSet);
		SpectrumAdvancementCriteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity) playerEntity, usedItem, this, null, list);
		
		for (ItemStack itemStack : list) {
			if (itemStack.isIn(ItemTags.FISHES)) {
				playerEntity.increaseStat(Stats.FISH_CAUGHT, 1);
			}
		}
		
		if (isAblaze()) {
			list = FoundryEnchantment.applyFoundry(this.getWorld(), list);
		}
		
		float exuberanceMod = ExuberanceEnchantment.getExuberanceMod(this.exuberanceLevel);
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
	
	@Override
	public void handleStatus(byte status) {
		if (status == 31 && this.getWorld().isClient() && this.hookedEntity instanceof PlayerEntity player && player.isMainPlayer()) {
			this.pullHookedEntity(player);
		}
		super.handleStatus(status);
	}
	
	public void pullHookedEntity(Entity entity) {
		Entity owner = this.getOwner();
		if (owner != null) {
			Vec3d vec3d = (new Vec3d(owner.getX() - this.getX(), owner.getY() - this.getY(), owner.getZ() - this.getZ())).multiply(0.1D);
			entity.setVelocity(entity.getVelocity().add(vec3d));
		}
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
		return entity instanceof PlayerEntity player ? player : null;
	}
	
	@Nullable
	public Entity getHookedEntity() {
		return this.hookedEntity;
	}
	
	@Override
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		if (this.getPlayerOwner() == null) {
			int entityData = packet.getEntityData();
			LOGGER.error("Failed to recreate fishing hook on client. {} (id: {}) is not a valid owner.", this.getWorld().getEntityById(entityData), entityData);
			this.kill();
		}
	}
	
	protected enum State {
		FLYING,
		HOOKED_IN_ENTITY,
		BOBBING
	}
	
	protected enum PositionType {
		ABOVE_FLUID,
		INSIDE_FLUID,
		INVALID
	}
	
}
