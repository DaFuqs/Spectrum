package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.recipe.anvil_crushing.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.nbt.*;
import net.minecraft.network.protocol.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import org.joml.*;
import org.jspecify.annotations.*;

import java.lang.Math;
import java.util.function.*;

/**
 * A FallingBlock that is able to move in all directions
 */
public class FloatBlockEntity extends FallingBlockEntity {
	
	private static final int MAX_DAMAGE = 16;
	private static final float DAMAGE_PER_FALLEN_BLOCK = 1.0F;
	
	private static final Predicate<Entity> DAMAGE_SELECTOR = EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(entity -> entity.isAlive() && (entity instanceof LivingEntity || entity instanceof ItemEntity));
	
	private static final EntityDataAccessor<Long> LAUNCH_TIME = SynchedEntityData.defineId(FloatBlockEntity.class, EntityDataSerializers.LONG);
	private static final EntityDataAccessor<Float> GRAVITY_MODIFIER = SynchedEntityData.defineId(FloatBlockEntity.class, EntityDataSerializers.FLOAT);
	
	public FloatBlockEntity(Level level, double x, double y, double z, BlockState state) {
		this(SpectrumEntityTypes.FLOAT_BLOCK.get(), level);
		this.blockState = state;
		this.blocksBuilding = true;
		this.setPos(x, y, z);
		this.setDeltaMovement(Vec3.ZERO);
		this.xo = x;
		this.yo = y;
		this.zo = z;
		this.setStartPos(this.blockPosition());
		this.entityData.set(LAUNCH_TIME, level.getGameTime());
		
		if (state.getBlock() instanceof FloatBlock floatBlock) {
			setGravity(floatBlock.getGravityMod());
		}
		setHurtsEntities(DAMAGE_PER_FALLEN_BLOCK, MAX_DAMAGE);
	}
	
	public FloatBlockEntity(EntityType<? extends FloatBlockEntity> entityType, Level level) {
		super(entityType, level);
	}
	
	@Override
	public void tick() {
		if (this.getBlockState().isAir()) {
			this.discard();
			return;
		}
		
		++this.time;
		
		Vec3 deltaMovement = this.getDeltaMovement();
		this.applyGravity();
		this.handlePortal();
		this.move(MoverType.SELF, this.getDeltaMovement());
		
		Block block = this.blockState.getBlock();
		
		Level level = this.level();
		if (!level.isClientSide && this.isAlive() && !this.isNoGravity()) {
			
			BlockPos blockpos = this.blockPosition();
			boolean isConcretePowder = this.blockState.getBlock() instanceof ConcretePowderBlock;
			boolean hydrateConcretePowder = isConcretePowder && this.blockState.canBeHydrated(level, blockpos, level.getFluidState(blockpos), blockpos);
			double d0 = deltaMovement.lengthSqr();
			if (isConcretePowder && d0 > (double)1.0F) {
				BlockHitResult blockhitresult = level.clip(new ClipContext(new Vec3(this.xo, this.yo, this.zo), this.position(), net.minecraft.world.level.ClipContext.Block.COLLIDER, ClipContext.Fluid.SOURCE_ONLY, this));
				if (blockhitresult.getType() != HitResult.Type.MISS && this.blockState.canBeHydrated(level, blockpos, level.getFluidState(blockhitresult.getBlockPos()), blockhitresult.getBlockPos())) {
					blockpos = blockhitresult.getBlockPos();
					hydrateConcretePowder = true;
				}
			}
			
			if (!this.verticalCollision && !hydrateConcretePowder) {
				if (this.time > 100 && (blockpos.getY() <= level.getMinBuildHeight() || blockpos.getY() > level.getMaxBuildHeight()) || this.time > 600) {
					if (this.dropItem && level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
						this.spawnAtLocation(block);
					}
					
					this.discard();
				}
			} else {
				BlockState blockstate = level.getBlockState(blockpos);
				// Since `Direction.fromDelta` only takes ints, we have to pull off some tomfoolery
				Direction movementDirection = Direction.fromDelta((int) (deltaMovement.x() * 100), (int) (deltaMovement.y() * 100), (int) (deltaMovement.z() * 100));
				movementDirection = movementDirection == null ? Direction.DOWN : movementDirection;
				boolean canBePlacedAtPos = blockstate.canBeReplaced(new DirectionalPlaceContext(level, blockpos, movementDirection.getOpposite(), ItemStack.EMPTY, movementDirection));
				boolean canPlaceAtDirection = FallingBlock.isFree(level.getBlockState(blockpos.relative(movementDirection))) && (!isConcretePowder || !hydrateConcretePowder);
				boolean canSurviveAtPos = this.blockState.canSurvive(level, blockpos) && !canPlaceAtDirection;
				if (canBePlacedAtPos && canSurviveAtPos) {
					// place as block
					if (this.blockState.hasProperty(BlockStateProperties.WATERLOGGED) && level.getFluidState(blockpos).getType() == Fluids.WATER) {
						this.blockState = this.blockState.setValue(BlockStateProperties.WATERLOGGED, true);
					}
					
					if (!level.setBlockAndUpdate(blockpos, this.blockState)) {
						if (this.dropItem && level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
							this.discard();
							this.callOnBrokenAfterFall(block, blockpos);
							this.spawnAtLocation(block);
						}
					} else {
						((ServerLevel) level).getChunkSource().chunkMap.broadcast(this, new ClientboundBlockUpdatePacket(blockpos, level.getBlockState(blockpos)));
						this.discard();
						if (block instanceof Fallable fallable) {
							fallable.onLand(level, blockpos, this.blockState, blockstate, this);
						}
						
						if (this.blockData != null && this.blockState.hasBlockEntity()) {
							BlockEntity blockentity = level.getBlockEntity(blockpos);
							if (blockentity != null) {
								CompoundTag compoundtag = blockentity.saveWithoutMetadata(level.registryAccess());
								
								for(String s : this.blockData.getAllKeys()) {
									compoundtag.put(s, this.blockData.get(s).copy());
								}
								
								blockentity.loadWithComponents(compoundtag, level.registryAccess());
								blockentity.setChanged();
							}
						}
					}
				} else {
					// drop as item
					this.discard();
					if (this.dropItem && level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
						this.callOnBrokenAfterFall(block, blockpos);
						this.spawnAtLocation(block);
					}
				}
			}
		}
		
		if (!this.isNoGravity()) {
			this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
		}
		
		// if we are standing still, anchor to center of block
		if(this.getDeltaMovement().lengthSqr() == 0.0) {
			BlockPos blockPos = this.blockPosition();
			this.setPos(blockPos.getX() + 0.5F, blockPos.getY(), blockPos.getZ() + 0.5F);
		}
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (player.isShiftKeyDown()) {
			if (this.level().isClientSide()) {
				return InteractionResult.SUCCESS;
			} else {
				Item item = this.getBlockState().getBlock().asItem();
				if (item != Items.AIR) {
					player.getInventory().placeItemBackInInventory(item.getDefaultInstance());
				}
				this.discard();
				return InteractionResult.CONSUME;
			}
		}
		return InteractionResult.PASS;
	}
	
	@Override
	public ItemStack getPickResult() {
		return this.getBlockState().getBlock().asItem().getDefaultInstance();
	}
	
	/**
	 * Take actions on entities on "collision".
	 * By default, it replicates the blockstate's behavior on collision.
	 */
	public void onEntityCollision(Entity entity) {
		if (!(entity instanceof FloatBlockEntity)) {
			this.getBlockState().entityInside(level(), this.blockPosition(), entity);
		}
	}
	
	@Override
	public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
		int traveledDistance = Math.abs(Mth.ceil(this.moveDist - 1.0F));
		int damage = Math.min(Mth.floor(traveledDistance * DAMAGE_PER_FALLEN_BLOCK), MAX_DAMAGE);
		if (damage <= 0) {
			return false;
		}
		Block block = this.blockState.getBlock();
		DamageSource damageSource;
		if (block instanceof Fallable fallable) {
			damageSource = fallable.getFallDamageSource(this);
		} else {
			damageSource = this.damageSources().fallingBlock(this);
		}
		
		// since the player position is tracked at its head and item entities are laying directly on the ground,
		// we have to use a relatively big bounding box here
		this.level().getEntities(this, this.getBoundingBox().inflate(0.5), DAMAGE_SELECTOR).forEach((entity) -> {
			if (entity instanceof ItemEntity itemEntity) {
				AnvilCrusher.crush(itemEntity, damage * 2);
			} else {
				entity.hurt(damageSource, damage);
			}
		});
		
		return false;
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putFloat("GravityModifier", (float) getDefaultGravity());
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("GravityModifier", Tag.TAG_FLOAT))
			setGravity(compound.getFloat("GravityModifier"));
	}
	// Sexy Piston on Floatblock action
	@Override
	public void move(MoverType movementType, Vec3 movement) {
		this.moveEntities();
		
		super.move(movementType, movement);
		
		if (movementType != MoverType.SELF) {
			this.setDeltaMovement(movement);
		}
	}
	
	public void moveEntities() {
		if (FallingBlock.isFree(this.getBlockState())) {
			return;
		}
		
		Level world = this.level();
		AABB collisionBox = getBoundingBox().inflate(0, 2D, 0);
		
		for (Entity entity : world.getEntities(this, collisionBox)) {
			if (entity instanceof FloatBlockEntity other && isPaltaeriaStratineCollision(other)) {
				world.explode(this, this.getX(), this.getY(), this.getZ(), 1.0F, Level.ExplosionInteraction.NONE);
				
				ItemStack collisionStack = SpectrumBlocks.HOVER_BLOCK.asItem().getDefaultInstance();
				ItemEntity itemEntity = new ItemEntity(world, this.getX(), this.getY(), this.getZ(), collisionStack);
				itemEntity.push(0.1 - world.getRandom().nextFloat() * 0.2, 0.1 - world.getRandom().nextFloat() * 0.2, 0.1 - world.getRandom().nextFloat() * 0.2);
				world.addFreshEntity(itemEntity);
				
				this.discard();
				other.discard();
			} else if (entity.isPushable() && entity.getPistonPushReaction() != PushReaction.IGNORE && entity.getBoundingBox().intersects(collisionBox)) {
				entity.move(MoverType.PISTON, this.getDeltaMovement());
				entity.setOnGround(true);
				entity.fallDistance = 0F;
				
				this.onEntityCollision(entity);
			}
		}
	}
	
	public boolean isPaltaeriaStratineCollision(FloatBlockEntity other) {
		Block thisBlock = this.getBlockState().getBlock();
		Block otherBlock = other.getBlockState().getBlock();
		return thisBlock == SpectrumBlocks.PALTAERIA_FLOATBLOCK.get() && otherBlock == SpectrumBlocks.STRATINE_FLOATBLOCK.get()
				|| thisBlock == SpectrumBlocks.STRATINE_FLOATBLOCK.get() && otherBlock == SpectrumBlocks.PALTAERIA_FLOATBLOCK.get();
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(GRAVITY_MODIFIER, 0.0F);
		builder.define(LAUNCH_TIME, 0L);
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return true;
	}
	
	@Override
	public double getDefaultGravity() {
		return this.entityData.get(GRAVITY_MODIFIER);
	}
	
	protected void setGravity(float modifier) {
		this.entityData.set(GRAVITY_MODIFIER, modifier);
	}
	
	@Override
	protected void applyGravity() {
		double d0 = this.getDefaultGravity();
		this.moveDist = (float) this.position().y() - this.getStartPos().getY();
		long launchTime = level().getGameTime() - this.entityData.get(LAUNCH_TIME);
		double additionalYVelocity = launchTime > 100 ? d0 / 10 : Math.min(Math.sin((Math.PI * launchTime) / 100D), 1) * (d0 / 10);
		
		if (additionalYVelocity != 0.0F) {
			this.setDeltaMovement(this.getDeltaMovement().add(0.0F, additionalYVelocity,0.0F));
		}
	}
	
	@Override
	public boolean isNoGravity() {
		return this.getDefaultGravity() == 0.0 || super.isNoGravity();
	}
	
}
