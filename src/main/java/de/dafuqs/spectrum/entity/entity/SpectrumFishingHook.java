package de.dafuqs.spectrum.entity.entity;

import com.mojang.logging.*;
import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.data_loaders.EntityFishingDataLoader.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.helpers.enchantments.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.loot.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
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
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;
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
public abstract class SpectrumFishingHook extends FishingHook {
	
	private static final EntityDataAccessor<Boolean> DATA_ABLAZE = SynchedEntityData.defineId(SpectrumFishingHook.class, EntityDataSerializers.BOOLEAN); // needs to be synced to the client, so it can render on fire

	protected final int luck;
	protected final int lureSpeed;
	protected final int exuberanceLevel;
	protected final int bigCatchLevel;
	protected final int serendipityReelLevel;
	protected final boolean inventoryInsertion;
	
	public SpectrumFishingHook(EntityType<? extends SpectrumFishingHook> type, Level world, int luck, int lureSpeed, int exuberanceLevel, int bigCatchLevel, int serendipityReelLevel, boolean inventoryInsertion, boolean ablaze) {
		super(type, world);
		
		this.noCulling = true;
		this.luck = Math.max(0, luck);
		this.lureSpeed = Math.max(0, lureSpeed);
		this.exuberanceLevel = Math.max(0, exuberanceLevel);
		this.bigCatchLevel = Math.max(0, bigCatchLevel);
		this.serendipityReelLevel = Math.max(0, serendipityReelLevel);
		this.inventoryInsertion = inventoryInsertion;
		this.getEntityData().set(DATA_ABLAZE, ablaze);
	}
	
	public SpectrumFishingHook(EntityType<? extends SpectrumFishingHook> entityType, Level world) {
		this(entityType, world, 0, 0, 0, 0, 0, false, false);
	}
	
	public SpectrumFishingHook(EntityType<? extends SpectrumFishingHook> entityType, Player player, Level world, int luck, int lureSpeed, int exuberanceLevel, int bigCatchLevel, int serendipityReelLevel, boolean inventoryInsertion, boolean ablaze) {
		this(entityType, world, luck, lureSpeed, exuberanceLevel, bigCatchLevel, serendipityReelLevel, inventoryInsertion, ablaze);
		this.setOwner(player);
		float f = player.getXRot();
		float f1 = player.getYRot();
		float f2 = Mth.cos(-f1 * (float) (Math.PI / 180.0) - (float) Math.PI);
		float f3 = Mth.sin(-f1 * (float) (Math.PI / 180.0) - (float) Math.PI);
		float f4 = -Mth.cos(-f * (float) (Math.PI / 180.0));
		float f5 = Mth.sin(-f * (float) (Math.PI / 180.0));
		double d0 = player.getX() - (double)f3 * 0.3;
		double d1 = player.getEyeY();
		double d2 = player.getZ() - (double)f2 * 0.3;
		this.moveTo(d0, d1, d2, f1, f);
		Vec3 vec3 = new Vec3((-f3), Mth.clamp(-(f5 / f4), -5.0F, 5.0F), (-f2));
		double d3 = vec3.length();
		vec3 = vec3.multiply(0.6 / d3 + this.random.triangle(0.5, 0.0103365), 0.6 / d3 + this.random.triangle(0.5, 0.0103365), 0.6 / d3 + this.random.triangle(0.5, 0.0103365));
		this.setDeltaMovement(vec3);
		//noinspection SuspiciousNameCombination
		this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * 180.0F / (float)Math.PI));
		this.setXRot((float)(Mth.atan2(vec3.y, vec3.horizontalDistance()) * 180.0F / (float)Math.PI));
		this.yRotO = this.getYRot();
		this.xRotO = this.getXRot();
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_ABLAZE, false);
	}
	
	public abstract int getLineColor();
	
	@Override
	public void tick() {
		this.syncronizedRandom.setSeed(this.getUUID().getLeastSignificantBits() ^ this.level().getGameTime());
		
		if (!this.hasBeenShot) {
			this.gameEvent(GameEvent.PROJECTILE_SHOOT, this.getOwner());
			this.hasBeenShot = true;
		}
		
		if (!this.leftOwner) {
			this.leftOwner = this.checkLeftOwner();
		}
		this.baseTick();
		
		Player owner = this.getPlayerOwner();
		if (owner == null) {
			this.discard();
		} else if (this.level().isClientSide || !this.shouldStopFishing(owner)) {
			if (this.onGround()) {
				this.life++;
				if (this.life >= 1200) {
					this.discard();
					return;
				}
			} else {
				this.life = 0;
			}
			
			float f = 0.0F;
			BlockPos blockPos = this.blockPosition();
			FluidState fluidState = this.level().getFluidState(blockPos);
			boolean canFishInFluid = getFishingRod(owner).getItem() instanceof SpectrumFishingRodItem spectrumFishingRodItem && spectrumFishingRodItem.canFishIn(fluidState);
			if (canFishInFluid) {
				f = fluidState.getHeight(this.level(), blockPos);
			}
			
			boolean bl = f > 0.0F;
			if (this.currentState == FishingHook.FishHookState.FLYING) {
				if (this.hookedIn != null) {
					this.setDeltaMovement(Vec3.ZERO);
					this.currentState = FishingHook.FishHookState.HOOKED_IN_ENTITY;
					return;
				}
				
				if (bl) {
					this.setDeltaMovement(this.getDeltaMovement().multiply(0.3, 0.2, 0.3));
					this.currentState = FishingHook.FishHookState.BOBBING;
					return;
				}
				
				this.checkCollision();
			} else {
				if (this.currentState == FishingHook.FishHookState.HOOKED_IN_ENTITY) {
					if (this.hookedIn != null) {
						if (!this.hookedIn.isRemoved() && this.hookedIn.level().dimension() == this.level().dimension()) {
							this.setPos(this.hookedIn.getX(), this.hookedIn.getY(0.8), this.hookedIn.getZ());
							hookedEntityTick(this.hookedIn);
						} else {
							this.setHookedEntity(null);
							this.currentState = FishingHook.FishHookState.FLYING;
						}
					}
					
					return;
				}
				
				if (this.currentState == FishingHook.FishHookState.BOBBING) {
					Vec3 vec3d = this.getDeltaMovement();
					double d = this.getY() + vec3d.y - (double) blockPos.getY() - (double) f;
					if (Math.abs(d) < 0.01) {
						d += Math.signum(d) * 0.1;
					}
					
					this.setDeltaMovement(vec3d.x * 0.9, vec3d.y - d * (double) this.random.nextFloat() * 0.2, vec3d.z * 0.9);
					if (this.nibble <= 0 && this.timeUntilHooked <= 0) {
						this.openWater = true;
					} else {
						this.openWater = this.openWater && this.outOfWaterTime < MAX_OUT_OF_WATER_TIME && this.calculateOpenWater(blockPos);
					}
					
					if (bl) {
						this.outOfWaterTime = Math.max(0, this.outOfWaterTime - 1);
						if (this.biting) {
							this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.1 * (double) this.syncronizedRandom.nextFloat() * (double) this.syncronizedRandom.nextFloat(), 0.0));
						}
						
						if (!this.level().isClientSide) {
							this.catchingFish(blockPos);
						}
					} else {
						this.outOfWaterTime = Math.min(MAX_OUT_OF_WATER_TIME, this.outOfWaterTime + 1);
					}
				}
			}
			
			if (!canFishInFluid) {
				this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.03, 0.0));
			}
			
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.updateRotation();
			if (this.currentState == FishingHook.FishHookState.FLYING && (this.onGround() || this.horizontalCollision)) {
				this.setDeltaMovement(Vec3.ZERO);
			}
			
			double e = 0.92;
			this.setDeltaMovement(this.getDeltaMovement().scale(e));
			this.reapplyPosition();
		}
	}
	
	public boolean shouldStopFishing(Player player) {
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
	
	@Override
	public void catchingFish(BlockPos pos) {
		ServerLevel serverWorld = (ServerLevel) this.level();
		int i = 1;
		BlockPos blockPos = pos.above();
		if (this.random.nextFloat() < 0.25F && this.level().isRainingAt(blockPos)) {
			i++;
		}
		
		if (this.random.nextFloat() < 0.5F && !this.level().canSeeSky(blockPos)) {
			i--;
		}
		
		if (this.nibble > 0) {
			this.nibble--;
			if (this.nibble <= 0) {
				this.timeUntilLured = 0;
				this.timeUntilHooked = 0;
				this.getEntityData().set(DATA_BITING, false);
			}
		} else if (this.timeUntilHooked > 0) {
			this.timeUntilHooked -= i;
			if (this.timeUntilHooked > 0)
				this.fishAngle = this.fishAngle + (float) this.random.triangle(0.0, 9.188);
			float f = this.fishAngle * (float) (Math.PI / 180.0);
			float g = Mth.sin(f);
			float h = Mth.cos(f);
			double d = this.getX() + (double) (g * (float) this.timeUntilHooked * 0.1F);
			double e = ((float) Mth.floor(this.getY()) + 1.0F);
			double j = this.getZ() + (double) (h * (float) this.timeUntilHooked * 0.1F);
			BlockState blockState = serverWorld.getBlockState(BlockPos.containing(d, e - 1.0, j));
			Tuple<SimpleParticleType, SimpleParticleType> particles = getFluidParticles(blockState);
			if (this.timeUntilHooked > 0) {
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
				this.nibble = Mth.nextInt(this.random, 20, 40);
				this.getEntityData().set(DATA_BITING, true);
			}
		} else if (this.timeUntilLured > 0) {
			this.timeUntilLured -= i;
			float f = 0.15F;
			if (this.timeUntilLured < 20) {
				f += (float) (20 - this.timeUntilLured) * 0.05F;
			} else if (this.timeUntilLured < 40) {
				f += (float) (40 - this.timeUntilLured) * 0.02F;
			} else if (this.timeUntilLured < 60) {
				f += (float) (60 - this.timeUntilLured) * 0.01F;
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
			
			if (this.timeUntilLured <= 0) {
				this.fishAngle = Mth.nextFloat(this.random, 0.0F, 360.0F);
				this.timeUntilHooked = Mth.nextInt(this.random, 20, 80);
			}
		} else {
			this.timeUntilLured = Mth.nextInt(this.random, 100, 600);
			this.timeUntilLured = this.timeUntilLured - this.lureSpeed;
			this.timeUntilLured = Math.max(1, this.timeUntilLured);
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
	
	@Override
	public FishingHook.OpenWaterType getOpenWaterTypeForBlock(BlockPos pos) {
		BlockState blockState = this.level().getBlockState(pos);
		if (!blockState.isAir() && !blockState.is(Blocks.LILY_PAD)) {
			FluidState fluidState = blockState.getFluidState();
			return !fluidState.isEmpty() && fluidState.isSource() && blockState.getCollisionShape(this.level(), pos).isEmpty()
					? FishingHook.OpenWaterType.INSIDE_WATER
					: FishingHook.OpenWaterType.INVALID;
		} else {
			return FishingHook.OpenWaterType.ABOVE_WATER;
		}
	}
	
	public int retrieve(ItemStack stack) {
		Player playerEntity = this.getPlayerOwner();
		if (!this.level().isClientSide() && playerEntity != null && !this.shouldStopFishing(playerEntity)) {
			int i = 0;
			if (this.hookedIn != null) {
				this.pullEntity(this.hookedIn);
				SpectrumAdvancementCriteria.FISHING_ROD_HOOKED.trigger((ServerPlayer) playerEntity, stack, this, null, Collections.emptyList());
				CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer) playerEntity, stack, this, Collections.emptyList());
				this.level().broadcastEntityEvent(this, EntityEvent.FISHING_ROD_REEL_IN);
				i = this.hookedIn instanceof ItemEntity ? 3 : 5;
			} else if (this.nibble > 0) {
				if (!tryCatchEntity(stack, playerEntity, (ServerLevel) this.level(), this.blockPosition())) {
					int lootAmount = random.nextIntBetweenInclusive(1, (int) Math.pow(2, 1 + serendipityReelLevel) - 1);
					for (int j = 0; j < lootAmount; j++) {
						catchLoot(stack, playerEntity);
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
	public void pullEntity(Entity entity) {
		Entity entity2 = this.getOwner();
		if (entity2 != null) {
			Vec3 vec3d = (new Vec3(entity2.getX() - this.getX(), entity2.getY() - this.getY(), entity2.getZ() - this.getZ())).scale(0.1);
			entity.setDeltaMovement(entity.getDeltaMovement().add(vec3d));
		}
	}
	
	public enum OpenWaterType {
		ABOVE_FLUID,
		INSIDE_FLUID,
		INVALID
	}
	
	public enum FishHookState {
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
				.withLuck((float) this.luck + playerEntity.getLuck())
				.create(SpectrumLootContextParamSets.FISHING);
		
		if (level().getServer() == null) return;
		LootTable lootTable = this.level().getServer().reloadableRegistries().getLootTable(SpectrumLootTableKeys.UNIVERSAL_FISHING);
		List<ItemStack> fishedStacks = lootTable.getRandomItems(lootContextParameterSet);
		SpectrumAdvancementCriteria.FISHING_ROD_HOOKED.trigger((ServerPlayer) playerEntity, usedItem, this, null, fishedStacks);
		
		for (ItemStack itemStack : fishedStacks) {
			if (itemStack.is(ItemTags.FISHES)) {
				playerEntity.awardStat(Stats.FISH_CAUGHT, 1);
			}
		}
		
		if (isAblaze()) {
			fishedStacks = FoundryHelper.applyFoundry(this.level(), fishedStacks); // TODO: use vanilla method
		}
		
		for (ItemStack fishedStack : fishedStacks) {
			int experienceAmount = this.random.nextInt(6) + 1;
			
			ItemStack rod = playerEntity.getMainHandItem().getItem() instanceof SpectrumFishingRodItem ? playerEntity.getMainHandItem() : playerEntity.getOffhandItem();
			experienceAmount = EnchantmentHelper.processBlockExperience((ServerLevel) level(), rod, experienceAmount);
			
			if (this.inventoryInsertion) {
				playerEntity.getInventory().placeItemBackInInventory(fishedStack);
				playerEntity.giveExperiencePoints(experienceAmount);
				
				playerEntity.level().playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
						SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS,
						0.2F, ((playerEntity.getRandom().nextFloat() - playerEntity.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
			} else {
				// fireproof item, so it does not burn when fishing in lava
				ItemEntity itemEntity = new FireproofItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), fishedStack);
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
		return this.getEntityData().get(DATA_ABLAZE);
	}
	
	@Override
	public boolean displayFireAnimation() {
		return isAblaze();
	}
	
	protected void hookedEntityTick(Entity hookedEntity) {
	}
	
}
