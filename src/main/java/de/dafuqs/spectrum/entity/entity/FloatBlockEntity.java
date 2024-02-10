package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.blocks.gravity.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.recipe.anvil_crushing.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.block.piston.*;
import net.minecraft.entity.*;
import net.minecraft.entity.data.*;
import net.minecraft.entity.player.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.network.listener.*;
import net.minecraft.network.packet.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.crash.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

import java.util.function.*;

/**
 * An entity that resembles a block.
 */
public class FloatBlockEntity extends Entity {
	
	private static final float MAX_DAMAGE = 8.0F;
	private static final float DAMAGE_PER_FALLEN_BLOCK = 0.5F;
	
	private static final TrackedData<BlockPos> ORIGIN = DataTracker.registerData(FloatBlockEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
	private static final TrackedData<Long> LAUNCH_TIME = DataTracker.registerData(FloatBlockEntity.class, TrackedDataHandlerRegistry.LONG);
	private static final TrackedData<Float> GRAVITY_MODIFIER = DataTracker.registerData(FloatBlockEntity.class, TrackedDataHandlerRegistry.FLOAT);
	
	public int moveTime;
	public boolean dropItem = true;
	protected NbtCompound blockEntityData;
	protected BlockState blockState = Blocks.STONE.getDefaultState();
	protected boolean canSetBlock = true;
	protected boolean collides;
	
	public FloatBlockEntity(EntityType<? extends FloatBlockEntity> entityType, World world) {
		super(entityType, world);
		this.moveTime = 0;
	}
	
	public FloatBlockEntity(EntityType<? extends FloatBlockEntity> entityType, World world, double x, double y, double z, BlockState blockState) {
		this(entityType, world);
		this.blockState = blockState;
		this.intersectionChecked = true;
		this.setPosition(x, y, z);
		this.setVelocity(Vec3d.ZERO);
		this.prevX = x;
		this.prevY = y;
		this.prevZ = z;
		this.setOrigin(BlockPos.ofFloored(this.getPos()));
		this.setLaunchTime(getWorld().getTime());
		
		if (blockState.getBlock() instanceof FloatBlock floatBlock) {
			setGravity(floatBlock.getGravityMod());
		}
	}
	
	public FloatBlockEntity(World world, BlockPos pos, BlockState blockState) {
		this(SpectrumEntityTypes.FLOAT_BLOCK, world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, blockState);
	}
	
	/**
	 * Calculates the bounding box based on the blockstate's collision shape.
	 * If the blockstate doesn't have collision, this method turns collision
	 * off for this entity and sets the bounding box to the outline shape instead.
	 * Note: Complex bounding boxes are not supported. These are all rectangular prisms.
	 *
	 * @return The bounding box of this entity
	 */
	@Override
	protected Box calculateBoundingBox() {
		if (this.dataTracker == null || this.blockState == null) {
			return super.calculateBoundingBox();
		}
		BlockPos origin = this.dataTracker.get(ORIGIN);
		VoxelShape shape = this.blockState.getCollisionShape(getWorld(), origin);
		if (shape.isEmpty()) {
			this.collides = false;
			shape = this.blockState.getOutlineShape(getWorld(), origin);
			if (shape.isEmpty()) {
				return super.calculateBoundingBox();
			}
		} else {
			this.collides = true;
		}
		Box box = shape.getBoundingBox();
		return box.offset(getPos().subtract(new Vec3d(0.5, 0, 0.5)));
	}
	
	
	@Override
	public void tick() {
		if (this.getBlockState().isAir()) {
			this.discard();
			return;
		}
		
		// Destroy the block in the world that this is spawned from
		// If no block exists, remove this entity
		if (this.moveTime++ == 0) {
			BlockPos blockPos = this.getBlockPos();
			Block block = this.blockState.getBlock();
			if (this.getWorld().getBlockState(blockPos).isOf(block)) {
				this.getWorld().removeBlock(blockPos, false);
			}
		}
		
		if (!this.hasNoGravity()) {
			this.distanceTraveled = (float) this.getPos().getY() - this.getOrigin().getY();
			long launchTime = getWorld().getTime() - getLaunchTime();
			double additionalYVelocity = launchTime > 100 ? this.getGravity() / 10 : Math.min(Math.sin((Math.PI * launchTime) / 100D), 1) * (this.getGravity() / 10);
			this.addVelocity(0.0D, additionalYVelocity, 0.0D);
			this.setVelocity(this.getVelocity().multiply(0.98D));
			
			// recalculate fall damage
			if (!getWorld().isClient) {
				this.dealDamage();
			}
		}
		
		this.move(MovementType.SELF, this.getVelocity());
		this.moveEntities();
		
		if (!this.getWorld().isClient) {
			if (!this.verticalCollision || this.isOnGround()) {
				if (this.age > 100 && this.getWorld().isOutOfHeightLimit(this.getBlockPos())) {
					this.breakApart();
					this.discard();
				}
			} else {
				trySetBlock();
			}
		}
	}
	
	@Override
	public void move(MovementType movementType, Vec3d movement) {
		super.move(movementType, movement);
		
		if (movementType != MovementType.SELF) {
			this.setVelocity(movement);
		}
	}
	
	@Override
	public boolean canHit() {
		return !this.isRemoved();
	}
	
	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		if (player.isSneaking()) {
			if (this.getWorld().isClient()) {
				return ActionResult.SUCCESS;
			} else {
				Item item = this.blockState.getBlock().asItem();
				if (item != null) {
					player.getInventory().offerOrDrop(item.getDefaultStack());
				}
				this.discard();
				return ActionResult.CONSUME;
			}
		}
		return ActionResult.PASS;
	}
	
	@Override
	public ItemStack getPickBlockStack() {
		return this.blockState.getBlock().asItem().getDefaultStack();
	}
	
	/**
	 * Take actions on entities on "collision".
	 * By default, it replicates the blockstate's behavior on collision.
	 */
	public void onEntityCollision(Entity entity) {
		if (!(entity instanceof FloatBlockEntity)) {
			this.blockState.onEntityCollision(getWorld(), this.getBlockPos(), entity);
		}
	}
	
	public void dealDamage() {
		int traveledDistance = MathHelper.ceil(this.fallDistance - 1.0F);
		if (traveledDistance > 0) {
			int damage = (int) Math.min(MathHelper.floor(traveledDistance * DAMAGE_PER_FALLEN_BLOCK), MAX_DAMAGE);
			if (damage > 0) {
				// since the players position is tracked at its head and item entities are laying directly on the ground we have to use a relatively big bounding box here
				Predicate<Entity> predicate = EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.and(entity -> entity.isAlive() && (entity instanceof LivingEntity || entity instanceof ItemEntity));
				this.getWorld().getOtherEntities(this, this.getBoundingBox(), predicate).forEach((entity) -> {
					if (entity instanceof ItemEntity itemEntity) {
						AnvilCrusher.crush(itemEntity, damage);
					} else {
						entity.damage(SpectrumDamageTypes.floatblock(entity.getWorld()), damage);
					}
				});
			}
		}
	}
	
	@Override
	protected void writeCustomDataToNbt(NbtCompound compound) {
		compound.put("BlockState", NbtHelper.fromBlockState(this.blockState));
		compound.putInt("Time", this.moveTime);
		compound.putBoolean("DropItem", this.dropItem);
		if (this.blockEntityData != null) {
			compound.put("BlockEntityData", this.blockEntityData);
		}
		compound.putFloat("GravityModifier", getGravity());
	}
	
	@Override
	protected void readCustomDataFromNbt(NbtCompound compound) {
		this.blockState = NbtHelper.toBlockState(this.getWorld().createCommandRegistryWrapper(RegistryKeys.BLOCK), compound.getCompound("BlockState"));
		this.moveTime = compound.getInt("Time");
		if (compound.contains("DropItem", 99)) this.dropItem = compound.getBoolean("DropItem");
		if (compound.contains("BlockEntityData", 10)) this.blockEntityData = compound.getCompound("BlockEntityData");
		if (this.blockState.isAir()) this.blockState = Blocks.STONE.getDefaultState();
		if (compound.contains("GravityModifier", NbtElement.FLOAT_TYPE))
			setGravity(compound.getFloat("GravityModifier"));
	}
	
	@Override
	public boolean doesRenderOnFire() {
		return false;
	}
	
	@Override
	public void populateCrashReport(CrashReportSection section) {
		super.populateCrashReport(section);
		section.add("Imitating BlockState", this.blockState.toString());
	}
	
	public BlockState getBlockState() {
		return this.blockState;
	}
	
	public void trySetBlock() {
		if (!this.dropItem) {
			boolean canBeReplaced = this.blockState.canReplace(new AutomaticItemPlacementContext(this.getWorld(), this.getBlockPos(), Direction.UP, ItemStack.EMPTY, Direction.DOWN));
			
			BlockState upperState = this.getWorld().getBlockState(this.getBlockPos().up());
			boolean isAboveFree = upperState.isAir() || upperState.isIn(BlockTags.FIRE) || upperState.isLiquid() || upperState.isReplaceable();
			
			boolean canBlockSurvive = this.getBlockState().canPlaceAt(this.getWorld(), this.getBlockPos()) && !isAboveFree;
			if (canBeReplaced && canBlockSurvive) {
				if (this.blockState.contains(Properties.WATERLOGGED) && this.getWorld().getFluidState(this.getBlockPos()).getFluid() == Fluids.WATER) {
					this.blockState = this.blockState.with(Properties.WATERLOGGED, true);
				}
				
				if (this.getWorld().setBlockState(this.getBlockPos(), this.blockState, Block.NOTIFY_ALL)) {
					this.discard();
					if (this.blockEntityData != null && this.blockState.hasBlockEntity()) {
						BlockEntity blockEntity = this.getWorld().getBlockEntity(this.getBlockPos());
						if (blockEntity != null) {
							NbtCompound compoundTag = blockEntity.createNbt();
							for (String keyName : this.blockEntityData.getKeys()) {
								NbtElement tag = this.blockEntityData.get(keyName);
								if (tag != null && !"x".equals(keyName) && !"y".equals(keyName) && !"z".equals(keyName)) {
									compoundTag.put(keyName, tag.copy());
								}
							}
							
							blockEntity.readNbt(compoundTag);
							blockEntity.markDirty();
						}
					}
					// Stop entities from clipping through the block when it's set
					this.moveEntities();
					
				} else if (this.blockState.isToolRequired() && this.dropItem && this.getWorld().getGameRules().get(GameRules.DO_ENTITY_DROPS).get()) {
					this.breakApart();
				}
			} else {
				this.breakApart();
			}
			
			this.moveEntities();
		}
		
		BlockPos blockPos = this.getBlockPos();
		BlockState blockState = this.getWorld().getBlockState(blockPos);
		boolean canReplace = blockState.canReplace(new AutomaticItemPlacementContext(this.getWorld(), blockPos, Direction.UP, ItemStack.EMPTY, Direction.DOWN));
		boolean canPlace = this.blockState.canPlaceAt(this.getWorld(), blockPos);
		if (!this.canSetBlock || !canPlace || !canReplace) {
			return;
		}
		
		if (this.blockState.contains(Properties.WATERLOGGED) && this.getWorld().getFluidState(blockPos).getFluid() == Fluids.WATER) {
			this.blockState = this.blockState.with(Properties.WATERLOGGED, true);
		}
		
		if (this.getWorld().setBlockState(blockPos, this.blockState, Block.NOTIFY_ALL)) {
			this.discard();
			if (this.blockEntityData != null && this.blockState.hasBlockEntity()) {
				BlockEntity blockEntity = this.getWorld().getBlockEntity(blockPos);
				if (blockEntity != null) {
					NbtCompound compoundTag = blockEntity.createNbt();
					for (String keyName : this.blockEntityData.getKeys()) {
						NbtElement tag = this.blockEntityData.get(keyName);
						if (tag != null && !"x".equals(keyName) && !"y".equals(keyName) && !"z".equals(keyName)) {
							compoundTag.put(keyName, tag.copy());
						}
					}
					
					blockEntity.readNbt(compoundTag);
					blockEntity.markDirty();
				}
			}
		}
	}
	
	public void moveEntities() {
		if (FallingBlock.canFallThrough(this.blockState)) {
			return;
		}
		
		World world = this.getWorld();
		for (Entity entity : world.getOtherEntities(this, getBoundingBox().offset(0, 1.0, 0).offset(this.prevX - this.getX(), this.prevY - this.getY(), this.prevZ - this.getZ()).expand(0.1))) {
			if (entity instanceof FloatBlockEntity other && isPaltaeriaStratineCollision(other)) {
				world.createExplosion(this, this.getX(), this.getY(), this.getZ(), 1.0F, World.ExplosionSourceType.NONE);
				
				ItemStack collisionStack = SpectrumBlocks.HOVER_BLOCK.asItem().getDefaultStack();
				ItemEntity itemEntity = new ItemEntity(world, this.getX(), this.getY(), this.getZ(), collisionStack);
				itemEntity.addVelocity(0.1 - world.random.nextFloat() * 0.2, 0.1 - world.random.nextFloat() * 0.2, 0.1 - world.random.nextFloat() * 0.2);
				world.spawnEntity(itemEntity);
				
				this.discard();
				other.discard();
			} else if (entity.isPushable() && entity.getPistonBehavior() != PistonBehavior.IGNORE) {
				if(entity.getPos().getY() < this.getPos().getY() + 1) {
					entity.setPos(entity.getPos().getX(), this.getPos().getY() + 1, entity.getPos().getZ());
				}
				if (entity.getVelocity().lengthSquared() < this.getVelocity().lengthSquared()) {
					entity.setVelocity(this.getVelocity());
				}
				entity.move(MovementType.SHULKER_BOX, this.getVelocity());
				
				entity.setOnGround(true);
				entity.fallDistance = 0F;
				
				this.onEntityCollision(entity);
			}
			
		}
	}
	
	public boolean isPaltaeriaStratineCollision(FloatBlockEntity other) {
		Block thisBlock = this.blockState.getBlock();
		Block otherBlock = other.getBlockState().getBlock();
		return thisBlock == SpectrumBlocks.PALTAERIA_FRAGMENT_BLOCK && otherBlock == SpectrumBlocks.STRATINE_FRAGMENT_BLOCK
				|| thisBlock == SpectrumBlocks.STRATINE_FRAGMENT_BLOCK && otherBlock == SpectrumBlocks.PALTAERIA_FRAGMENT_BLOCK;
	}
	
	/**
	 * Break the block, spawn break particles, and drop stacks if it can.
	 */
	public void breakApart() {
		if (this.isRemoved()) return;
		
		this.discard();
		if (this.dropItem && this.getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
			Block.dropStacks(this.blockState, this.getWorld(), this.getBlockPos());
		}
		// spawn break particles
		getWorld().syncWorldEvent(null, WorldEvents.BLOCK_BROKEN, this.getBlockPos(), Block.getRawIdFromState(blockState));
	}
	
	@Override
	public boolean entityDataRequiresOperator() {
		return true;
	}
	
	@Override
	public Packet<ClientPlayPacketListener> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this, Block.getRawIdFromState(this.getBlockState()));
	}
	
	@Override
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		this.blockState = Block.getStateFromRawId(packet.getEntityData());
		this.intersectionChecked = true;
		double d = packet.getX();
		double e = packet.getY();
		double f = packet.getZ();
		this.setPosition(d, e + (double) ((1.0F - this.getHeight()) / 2.0F), f);
		this.setOrigin(this.getBlockPos());
	}
	
	@Override
	public boolean isAttackable() {
		return false;
	}
	
	public BlockPos getOrigin() {
		return this.dataTracker.get(ORIGIN);
	}
	
	public void setOrigin(BlockPos origin) {
		this.dataTracker.set(ORIGIN, origin);
		this.setPosition(getX(), getY(), getZ());
	}
	
	public Long getLaunchTime() {
		return this.dataTracker.get(LAUNCH_TIME);
	}
	
	public void setLaunchTime(long spawnTime) {
		this.dataTracker.set(LAUNCH_TIME, spawnTime);
	}
	
	@Override
	protected void initDataTracker() {
		this.dataTracker.startTracking(ORIGIN, BlockPos.ORIGIN);
		this.dataTracker.startTracking(GRAVITY_MODIFIER, 0.0F);
		this.dataTracker.startTracking(LAUNCH_TIME, 0L);
	}
	
	@Override
	public boolean isCollidable() {
		return collides;
	}
	
	@Override
	public boolean collidesWith(Entity other) {
		return other.isCollidable() && !this.isConnectedThroughVehicle(other);
	}
	
	public float getGravity() {
		return this.dataTracker.get(GRAVITY_MODIFIER);
	}
	
	protected void setGravity(float modifier) {
		this.dataTracker.set(GRAVITY_MODIFIER, modifier);
	}
	
	@Override
	public boolean hasNoGravity() {
		return this.getGravity() == 0.0 || super.hasNoGravity();
	}
	
}
