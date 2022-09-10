package de.dafuqs.spectrum.entity.entity;

import com.mojang.logging.LogUtils;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.enchantments.AutoSmeltEnchantment;
import de.dafuqs.spectrum.enchantments.ExuberanceEnchantment;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.interfaces.PlayerEntityAccessor;
import de.dafuqs.spectrum.items.tools.SpectrumFishingRodItem;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.*;

// yeah, this pretty much is a full reimplementation. Sadge
// I wanted to use more of FishingBobberEntity for mod compat,
// but most methods are either private or are tricky to extend
public abstract class SpectrumFishingBobberEntity extends ProjectileEntity {
	
	private static final Logger field_36336 = LogUtils.getLogger();
	private final Random velocityRandom;
	private boolean caughtFish;
	private int outOfOpenFluidTicks;
	private static final TrackedData<Integer> HOOK_ENTITY_ID = DataTracker.registerData(SpectrumFishingBobberEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Boolean> CAUGHT_FISH = DataTracker.registerData(SpectrumFishingBobberEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private int removalTimer;
	private int hookCountdown;
	private int waitCountdown;
	private int fishTravelCountdown;
	private float fishAngle;
	private boolean inTheOpen;
	@Nullable
	private Entity hookedEntity;
	private SpectrumFishingBobberEntity.State state;
	private final int luckOfTheSeaLevel;
	private final int lureLevel;
	
	public static final Identifier LOOT_IDENTIFIER = SpectrumCommon.locate("gameplay/universal_fishing");
	
	// this should probably become a registry at some point
	public static final Map<Block, Pair<DefaultParticleType, DefaultParticleType>> FISHING_PARTICLES = new HashMap<>() {{
		put(Blocks.WATER, new Pair<>(ParticleTypes.BUBBLE, ParticleTypes.FISHING));
		put(Blocks.LAVA, new Pair<>(ParticleTypes.FLAME, SpectrumParticleTypes.LAVA_FISHING));
		put(SpectrumBlocks.MUD, new Pair<>(SpectrumParticleTypes.MUD_POP, SpectrumParticleTypes.MUD_FISHING));
		put(SpectrumBlocks.LIQUID_CRYSTAL, new Pair<>(SpectrumParticleTypes.LIQUID_CRYSTAL_SPARKLE, SpectrumParticleTypes.LIQUID_CRYSTAL_FISHING));
		put(SpectrumBlocks.MIDNIGHT_SOLUTION, new Pair<>(SpectrumParticleTypes.GRAY_SPARKLE_RISING, SpectrumParticleTypes.MIDNIGHT_SOLUTION_FISHING));
	}};
	
	public SpectrumFishingBobberEntity(EntityType type, World world, int luckOfTheSeaLevel, int lureLevel) {
		super(type, world);
		this.velocityRandom = new Random();
		this.inTheOpen = true;
		this.state = SpectrumFishingBobberEntity.State.FLYING;
		this.ignoreCameraFrustum = true;
		this.luckOfTheSeaLevel = Math.max(0, luckOfTheSeaLevel);
		this.lureLevel = Math.max(0, lureLevel);
	}
	
	public SpectrumFishingBobberEntity(EntityType entityType, World world) {
		this(entityType, world, 0, 0);
	}
	
	public SpectrumFishingBobberEntity(PlayerEntity thrower, World world, int luckOfTheSeaLevel, int lureLevel) {
		this(SpectrumEntityTypes.BEDROCK_FISHING_BOBBER, world, luckOfTheSeaLevel, lureLevel);
		this.setOwner(thrower);
		float f = thrower.getPitch();
		float g = thrower.getYaw();
		float h = MathHelper.cos(-g * 0.017453292F - 3.1415927F);
		float i = MathHelper.sin(-g * 0.017453292F - 3.1415927F);
		float j = -MathHelper.cos(-f * 0.017453292F);
		float k = MathHelper.sin(-f * 0.017453292F);
		double d = thrower.getX() - (double)i * 0.3D;
		double e = thrower.getEyeY();
		double l = thrower.getZ() - (double)h * 0.3D;
		this.refreshPositionAndAngles(d, e, l, g, f);
		Vec3d vec3d = new Vec3d((-i), MathHelper.clamp(-(k / j), -5.0F, 5.0F), (double)(-h));
		double m = vec3d.length();
		vec3d = vec3d.multiply(0.6D / m + 0.5D + this.random.nextGaussian() * 0.0045D, 0.6D / m + 0.5D + this.random.nextGaussian() * 0.0045D, 0.6D / m + 0.5D + this.random.nextGaussian() * 0.0045D);
		this.setVelocity(vec3d);
		this.setYaw((float)(MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875D));
		this.setPitch((float)(MathHelper.atan2(vec3d.y, vec3d.horizontalLength()) * 57.2957763671875D));
		this.prevYaw = this.getYaw();
		this.prevPitch = this.getPitch();
	}
	
	public Pair<DefaultParticleType, DefaultParticleType> getParticles(BlockState blockState) {
		return FISHING_PARTICLES.getOrDefault(blockState.getBlock(), null);
	}
	
	@Override
	protected void initDataTracker() {
		this.getDataTracker().startTracking(HOOK_ENTITY_ID, 0);
		this.getDataTracker().startTracking(CAUGHT_FISH, false);
	}
	
	@Override
	public void onTrackedDataSet(TrackedData<?> data) {
		if (HOOK_ENTITY_ID.equals(data)) {
			int i = this.getDataTracker().get(HOOK_ENTITY_ID);
			this.hookedEntity = i > 0 ? this.world.getEntityById(i - 1) : null;
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
		
		this.velocityRandom.setSeed(this.getUuid().getLeastSignificantBits() ^ this.world.getTime());
		PlayerEntity playerEntity = this.getPlayerOwner();
		if (playerEntity == null) {
			this.discard();
		} else if (this.world.isClient || !this.removeIfInvalid(playerEntity)) {
			if (this.onGround) {
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
			FluidState fluidState = this.world.getFluidState(blockPos);
			ItemStack rodStack = getFishingRod(playerEntity);
			boolean canFishInFluid = false;
			if (rodStack.getItem() instanceof SpectrumFishingRodItem spectrumFishingRodItem && spectrumFishingRodItem.canFishIn(fluidState)) {
				canFishInFluid = true;
				f = fluidState.getHeight(this.world, blockPos);
			}
			
			boolean bl = f > 0.0F;
			if (this.state == State.FLYING) {
				if (this.hookedEntity != null) {
					this.setVelocity(Vec3d.ZERO);
					this.state = State.HOOKED_IN_ENTITY;
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
						if (!this.hookedEntity.isRemoved() && this.hookedEntity.world.getRegistryKey() == this.world.getRegistryKey()) {
							this.setPosition(this.hookedEntity.getX(), this.hookedEntity.getBodyY(0.8D), this.hookedEntity.getZ());
						} else {
							this.updateHookedEntityId(null);
							this.state = State.FLYING;
						}
					}
					
					return;
				}
				
				if (this.state == State.BOBBING) {
					Vec3d vec3d = this.getVelocity();
					double d = this.getY() + vec3d.y - (double)blockPos.getY() - (double)f;
					if (Math.abs(d) < 0.01D) {
						d += Math.signum(d) * 0.1D;
					}
					
					this.setVelocity(vec3d.x * 0.9D, vec3d.y - d * (double)this.random.nextFloat() * 0.2D, vec3d.z * 0.9D);
					if (this.hookCountdown <= 0 && this.fishTravelCountdown <= 0) {
						this.inTheOpen = true;
					} else {
						this.inTheOpen = this.inTheOpen && this.outOfOpenFluidTicks < 10 && this.isInTheOpen(blockPos);
					}
					
					if (bl) {
						this.outOfOpenFluidTicks = Math.max(0, this.outOfOpenFluidTicks - 1);
						if (this.caughtFish) {
							this.setVelocity(this.getVelocity().add(0.0D, -0.1D * (double)this.velocityRandom.nextFloat() * (double)this.velocityRandom.nextFloat(), 0.0D));
						}
						
						if (!this.world.isClient) {
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
			if (this.state == State.FLYING && (this.onGround || this.horizontalCollision)) {
				this.setVelocity(Vec3d.ZERO);
			}
			

			this.setVelocity(this.getVelocity().multiply(0.92D));
			this.refreshPosition();
		}
	}
	
	public ItemStack getFishingRod(PlayerEntity player) {
		ItemStack itemStack = player.getMainHandStack();
		if(itemStack.getItem() instanceof SpectrumFishingRodItem) {
			return itemStack;
		}
		
		itemStack = player.getOffHandStack();
		if(itemStack.getItem() instanceof SpectrumFishingRodItem) {
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
		if (!this.world.isClient) {
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
		ServerWorld serverWorld = (ServerWorld)this.world;
		int i = 1;
		BlockPos blockPos = pos.up();
		if (this.random.nextFloat() < 0.25F && this.world.hasRain(blockPos)) {
			++i;
		}
		
		if (this.random.nextFloat() < 0.5F && !this.world.isSkyVisible(blockPos)) {
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
				blockState = serverWorld.getBlockState(new BlockPos(d, e - 1.0D, j));
				Pair<DefaultParticleType, DefaultParticleType> particles = getParticles(blockState);
				
				if (particles != null) {
					if (this.fishTravelCountdown > 0) {
						if (this.random.nextFloat() < 0.15F) {
							serverWorld.spawnParticles(particles.getLeft(), d, e - 0.10000000149011612D, j, 1, g, 0.1D, h, 0.0D);
						}
						float k = g * 0.04F;
						float l = h * 0.04F;
						serverWorld.spawnParticles(ParticleTypes.FISHING, d, e, j, 0, l, 0.01D, (-k), 1.0D);
						serverWorld.spawnParticles(ParticleTypes.FISHING, d, e, j, 0, (-l), 0.01D, k, 1.0D);
					} else {
						this.playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
						double m = this.getY() + 0.5D;
						serverWorld.spawnParticles(particles.getLeft(), this.getX(), m, this.getZ(), (int) (1.0F + this.getWidth() * 20.0F), this.getWidth(), 0.0D, this.getWidth(), 0.20000000298023224D);
						serverWorld.spawnParticles(particles.getRight(), this.getX(), m, this.getZ(), (int) (1.0F + this.getWidth() * 20.0F), this.getWidth(), 0.0D, this.getWidth(), 0.20000000298023224D);
						this.hookCountdown = MathHelper.nextInt(this.random, 20, 40);
						this.getDataTracker().set(CAUGHT_FISH, true);
					}
				}
			} else if (this.waitCountdown > 0) {
				this.waitCountdown -= i;
				f = 0.15F;
				if (this.waitCountdown < 20) {
					f += (float)(20 - this.waitCountdown) * 0.05F;
				} else if (this.waitCountdown < 40) {
					f += (float)(40 - this.waitCountdown) * 0.02F;
				} else if (this.waitCountdown < 60) {
					f += (float)(60 - this.waitCountdown) * 0.01F;
				}
				
				if (this.random.nextFloat() < f) {
					g = MathHelper.nextFloat(this.random, 0.0F, 360.0F) * 0.017453292F;
					h = MathHelper.nextFloat(this.random, 25.0F, 60.0F);
					d = this.getX() + (double)(MathHelper.sin(g) * h) * 0.1D;
					e = ((float)MathHelper.floor(this.getY()) + 1.0F);
					j = this.getZ() + (double)(MathHelper.cos(g) * h) * 0.1D;
					blockState = serverWorld.getBlockState(new BlockPos(d, e - 1.0D, j));
					if (blockState.isOf(Blocks.WATER)) {
						serverWorld.spawnParticles(ParticleTypes.SPLASH, d, e, j, 2 + this.random.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D);
					}
				}
				
				if (this.waitCountdown <= 0) {
					this.fishAngle = MathHelper.nextFloat(this.random, 0.0F, 360.0F);
					this.fishTravelCountdown = MathHelper.nextInt(this.random, 20, 80);
				}
			} else {
				this.waitCountdown = MathHelper.nextInt(this.random, 100, 600);
				this.waitCountdown -= this.lureLevel * 20 * 5;
			}
		}
		
	}
	
	public boolean isInTheOpen(BlockPos pos) {
		PositionType positionType = PositionType.INVALID;
		
		for(int i = -1; i <= 2; ++i) {
			PositionType positionType2 = this.getPositionType(pos.add(-2, i, -2), pos.add(2, i, 2));
			switch(positionType2) {
				case INVALID:
					return false;
				case ABOVE_FLUID:
					if (positionType == PositionType.INVALID) {
						return false;
					}
					break;
				case INSIDE_FLUID:
					if (positionType == PositionType.ABOVE_FLUID) {
						return false;
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
		BlockState blockState = this.world.getBlockState(pos);
		if (!blockState.isAir() && !blockState.isOf(Blocks.LILY_PAD)) {
			FluidState fluidState = blockState.getFluidState();
			return !fluidState.isEmpty() && fluidState.isStill() && blockState.getCollisionShape(this.world, pos).isEmpty() ? PositionType.INSIDE_FLUID : PositionType.INVALID;
		} else {
			return PositionType.ABOVE_FLUID;
		}
	}
	
	public boolean isInTheOpen() {
		return this.inTheOpen;
	}
	
	public int use(ItemStack usedItem) {
		PlayerEntity playerEntity = this.getPlayerOwner();
		if (!this.world.isClient && playerEntity != null && !this.removeIfInvalid(playerEntity)) {
			int i = 0;
			if (this.hookedEntity != null) {
				this.pullHookedEntity(this.hookedEntity);
				SpectrumAdvancementCriteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity)playerEntity, usedItem, this, Collections.emptyList());
				this.world.sendEntityStatus(this, (byte)31);
				i = this.hookedEntity instanceof ItemEntity ? 3 : 5;
			} else if (this.hookCountdown > 0) {
				
				LootContext.Builder builder = (new LootContext.Builder((ServerWorld)this.world)).parameter(LootContextParameters.ORIGIN, this.getPos()).parameter(LootContextParameters.TOOL, usedItem).parameter(LootContextParameters.THIS_ENTITY, this).random(this.random).luck((float)this.luckOfTheSeaLevel + playerEntity.getLuck());
				LootTable lootTable = this.world.getServer().getLootManager().getTable(LOOT_IDENTIFIER);
				List<ItemStack> list = lootTable.generateLoot(builder.build(LootContextTypes.FISHING));
				SpectrumAdvancementCriteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity)playerEntity, usedItem, this, list);
				
				ItemStack fishingRod = getFishingRod(playerEntity);
				boolean autoSmelt = ((SpectrumFishingRodItem) fishingRod.getItem()).shouldAutosmelt(fishingRod);
				float exuberanceMod = ExuberanceEnchantment.getExuberanceMod(playerEntity);
				
				for (ItemStack itemStack : list) {
					if (itemStack.isIn(ItemTags.FISHES)) {
						playerEntity.increaseStat(Stats.FISH_CAUGHT, 1);
					}
				}
				
				if(autoSmelt) {
					list = AutoSmeltEnchantment.applyAutoSmelt(world, list);
				}
				
				for (ItemStack itemStack : list) {
					ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), itemStack);
					double d = playerEntity.getX() - this.getX();
					double e = playerEntity.getY() - this.getY();
					double f = playerEntity.getZ() - this.getZ();
					double g = 0.1D;
					itemEntity.setVelocity(d * g, e * g + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08D, f * g);
					itemEntity.setInvulnerable(true); // so it does not burn when lava fishing
					this.world.spawnEntity(itemEntity);
					int experienceAmount = this.random.nextInt((int) (6 * exuberanceMod) + 1);
					if(experienceAmount > 0) {
						playerEntity.world.spawnEntity(new ExperienceOrbEntity(playerEntity.world, playerEntity.getX(), playerEntity.getY() + 0.5D, playerEntity.getZ() + 0.5D, experienceAmount));
					}
				}
				
				i = 1;
			}
			
			if (this.onGround) {
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
		if (status == 31 && this.world.isClient && this.hookedEntity instanceof PlayerEntity && ((PlayerEntity)this.hookedEntity).isMainPlayer()) {
			this.pullHookedEntity(this.hookedEntity);
		}
		
		super.handleStatus(status);
	}
	
	public void pullHookedEntity(Entity entity) {
		Entity entity2 = this.getOwner();
		if (entity2 != null) {
			Vec3d vec3d = (new Vec3d(entity2.getX() - this.getX(), entity2.getY() - this.getY(), entity2.getZ() - this.getZ())).multiply(0.1D);
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
		return entity instanceof PlayerEntity ? (PlayerEntity)entity : null;
	}
	
	@Nullable
	public Entity getHookedEntity() {
		return this.hookedEntity;
	}
	
	@Override
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		if (this.getPlayerOwner() == null) {
			int i = packet.getEntityData();
			field_36336.error("Failed to recreate fishing hook on client. {} (id: {}) is not a valid owner.", this.world.getEntityById(i), i);
			this.kill();
		}
		
	}
	
	enum State {
		FLYING,
		HOOKED_IN_ENTITY,
		BOBBING
	}
	
	private enum PositionType {
		ABOVE_FLUID,
		INSIDE_FLUID,
		INVALID
	}

}
