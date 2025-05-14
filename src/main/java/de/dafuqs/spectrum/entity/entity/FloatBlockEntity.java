package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.blocks.gravity.*;
import de.dafuqs.spectrum.entity.*;
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

import java.util.function.*;

/**
 * An entity that resembles a block.
 */
public class FloatBlockEntity extends Entity {
	
	private static final float MAX_DAMAGE = 8.0F;
	private static final float DAMAGE_PER_FALLEN_BLOCK = 0.5F;
	
	private static final EntityDataAccessor<BlockPos> ORIGIN = SynchedEntityData.defineId(FloatBlockEntity.class, EntityDataSerializers.BLOCK_POS);
	private static final EntityDataAccessor<Long> LAUNCH_TIME = SynchedEntityData.defineId(FloatBlockEntity.class, EntityDataSerializers.LONG);
	private static final EntityDataAccessor<Float> GRAVITY_MODIFIER = SynchedEntityData.defineId(FloatBlockEntity.class, EntityDataSerializers.FLOAT);
	
	public int moveTime;
	protected CompoundTag blockEntityData;
	protected BlockState blockState = Blocks.STONE.defaultBlockState();
	protected boolean canSetBlock = true;
	protected boolean collides;
	
	public FloatBlockEntity(EntityType<? extends FloatBlockEntity> entityType, Level world) {
		super(entityType, world);
		this.moveTime = 0;
	}
	
	public FloatBlockEntity(EntityType<? extends FloatBlockEntity> entityType, Level world, double x, double y, double z, BlockState blockState) {
		this(entityType, world);
		this.blockState = blockState;
		this.blocksBuilding = true;
		this.setPos(x, y, z);
		this.setDeltaMovement(Vec3.ZERO);
		this.xo = x;
		this.yo = y;
		this.zo = z;
		this.setOrigin(BlockPos.containing(this.position()));
		this.setLaunchTime(level().getGameTime());
		
		if (blockState.getBlock() instanceof FloatBlock floatBlock) {
			setGravity(floatBlock.getGravityMod());
		}
	}
	
	public FloatBlockEntity(Level world, BlockPos pos, BlockState blockState) {
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
	protected AABB makeBoundingBox() {
		if (this.entityData == null || this.blockState == null) {
			return super.makeBoundingBox();
		}
		BlockPos origin = this.entityData.get(ORIGIN);
		VoxelShape shape = this.blockState.getCollisionShape(level(), origin);
		if (shape.isEmpty()) {
			this.collides = false;
			shape = this.blockState.getShape(level(), origin);
			if (shape.isEmpty()) {
				return super.makeBoundingBox();
			}
		} else {
			this.collides = true;
		}
		AABB box = shape.bounds();
		return box.move(position().subtract(new Vec3(0.5, 0, 0.5)));
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
			BlockPos blockPos = this.blockPosition();
			Block block = this.blockState.getBlock();
			if (this.level().getBlockState(blockPos).is(block)) {
				this.level().removeBlock(blockPos, false);
			}
		}
		
		if (!this.isNoGravity()) {
			this.moveDist = (float) this.position().y() - this.getOrigin().getY();
			long launchTime = level().getGameTime() - getLaunchTime();
			double additionalYVelocity = launchTime > 100 ? this.getDefaultGravity() / 10 : Math.min(Math.sin((Math.PI * launchTime) / 100D), 1) * (this.getDefaultGravity() / 10);
			this.push(0.0D, additionalYVelocity, 0.0D);
			this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
			
			// recalculate fall damage
			if (!level().isClientSide) {
				this.dealDamage();
			}
		}
		
		this.moveEntities();
		this.move(MoverType.SELF, this.getDeltaMovement());
		
		if (!this.level().isClientSide) {
			if (this.verticalCollision) {
				trySetBlock();
			} else if (this.tickCount > 100 && this.level().isOutsideBuildHeight(this.blockPosition())) {
				this.dropAsItem();
				this.discard();
			}
		}
	}
	
	@Override
	public void move(MoverType movementType, Vec3 movement) {
		super.move(movementType, movement);
		
		if (movementType != MoverType.SELF) {
			this.setDeltaMovement(movement);
		}
	}
	
	@Override
	public boolean isPickable() {
		return !this.isRemoved();
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (player.isShiftKeyDown()) {
			if (this.level().isClientSide()) {
				return InteractionResult.SUCCESS;
			} else {
				Item item = this.blockState.getBlock().asItem();
				if (item != null) {
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
		return this.blockState.getBlock().asItem().getDefaultInstance();
	}
	
	/**
	 * Take actions on entities on "collision".
	 * By default, it replicates the blockstate's behavior on collision.
	 */
	public void onEntityCollision(Entity entity) {
		if (!(entity instanceof FloatBlockEntity)) {
			this.blockState.entityInside(level(), this.blockPosition(), entity);
		}
	}
	
	public void dealDamage() {
		int traveledDistance = Mth.ceil(this.fallDistance - 1.0F);
		if (traveledDistance > 0) {
			int damage = (int) Math.min(Mth.floor(traveledDistance * DAMAGE_PER_FALLEN_BLOCK), MAX_DAMAGE);
			if (damage > 0) {
				// since the player position is tracked at its head and item entities are laying directly on the ground, we have to use a relatively big bounding box here
				Predicate<Entity> predicate = EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(entity -> entity.isAlive() && (entity instanceof LivingEntity || entity instanceof ItemEntity));
				this.level().getEntities(this, this.getBoundingBox(), predicate).forEach((entity) -> {
					if (entity instanceof ItemEntity itemEntity) {
						AnvilCrusher.crush(itemEntity, damage);
					} else {
						entity.hurt(SpectrumDamageTypes.floatblock(entity.level()), damage);
					}
				});
			}
		}
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		compound.put("BlockState", NbtUtils.writeBlockState(this.blockState));
		compound.putInt("Time", this.moveTime);
		if (this.blockEntityData != null) {
			compound.put("BlockEntityData", this.blockEntityData);
		}
		compound.putFloat("GravityModifier", (float) getDefaultGravity());
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		this.blockState = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), compound.getCompound("BlockState"));
		this.moveTime = compound.getInt("Time");
		if (compound.contains("BlockEntityData", 10)) this.blockEntityData = compound.getCompound("BlockEntityData");
		if (this.blockState.isAir()) this.blockState = Blocks.STONE.defaultBlockState();
		if (compound.contains("GravityModifier", Tag.TAG_FLOAT))
			setGravity(compound.getFloat("GravityModifier"));
	}
	
	@Override
	public boolean displayFireAnimation() {
		return false;
	}
	
	@Override
	public void fillCrashReportCategory(CrashReportCategory section) {
		super.fillCrashReportCategory(section);
		section.setDetail("Imitating BlockState", this.blockState.toString());
	}
	
	public BlockState getBlockState() {
		return this.blockState;
	}
	
	public void trySetBlock() {
		BlockPos blockPos = this.blockPosition();
		BlockState blockState = this.level().getBlockState(blockPos);
		boolean canReplace = blockState.canBeReplaced(new DirectionalPlaceContext(this.level(), blockPos, Direction.UP, ItemStack.EMPTY, Direction.DOWN));
		boolean canPlace = this.blockState.canSurvive(this.level(), blockPos);
		
		if (!this.canSetBlock || !canPlace || !canReplace) {
			return;
		}
		
		if (this.blockState.hasProperty(BlockStateProperties.WATERLOGGED) && this.level().getFluidState(blockPos).getType() == Fluids.WATER) {
			this.blockState = this.blockState.setValue(BlockStateProperties.WATERLOGGED, true);
		}
		
		if (this.level().setBlock(blockPos, this.blockState, Block.UPDATE_ALL)) {
			this.discard();
			if (this.blockEntityData != null && this.blockState.hasBlockEntity()) {
				BlockEntity blockEntity = this.level().getBlockEntity(blockPos);
				if (blockEntity != null) {
					var registryLookup = level().registryAccess();
					CompoundTag compoundTag = blockEntity.saveWithoutMetadata(registryLookup);
					for (String keyName : this.blockEntityData.getAllKeys()) {
						Tag tag = this.blockEntityData.get(keyName);
						if (tag != null && !"x".equals(keyName) && !"y".equals(keyName) && !"z".equals(keyName)) {
							compoundTag.put(keyName, tag.copy());
						}
					}
					
					blockEntity.loadWithComponents(compoundTag, registryLookup);
					blockEntity.setChanged();
				}
			}
		}
	}
	
	public void moveEntities() {
		if (FallingBlock.isFree(this.blockState)) {
			return;
		}
		
		Level world = this.level();
		AABB collissionBox = getBoundingBox().inflate(0, 2D, 0);
		
		for (Entity entity : world.getEntities(this, collissionBox)) {
			if (entity instanceof FloatBlockEntity other && isPaltaeriaStratineCollision(other)) {
				world.explode(this, this.getX(), this.getY(), this.getZ(), 1.0F, Level.ExplosionInteraction.NONE);
				
				ItemStack collisionStack = SpectrumBlocks.HOVER_BLOCK.asItem().getDefaultInstance();
				ItemEntity itemEntity = new ItemEntity(world, this.getX(), this.getY(), this.getZ(), collisionStack);
				itemEntity.push(0.1 - world.random.nextFloat() * 0.2, 0.1 - world.random.nextFloat() * 0.2, 0.1 - world.random.nextFloat() * 0.2);
				world.addFreshEntity(itemEntity);
				
				this.discard();
				other.discard();
			} else if (entity.isPushable() && entity.getPistonPushReaction() != PushReaction.IGNORE && entity.getBoundingBox().intersects(collissionBox)) {
				entity.move(MoverType.SHULKER_BOX, this.getDeltaMovement());
				entity.setOnGround(true);
				entity.fallDistance = 0F;
				
				this.onEntityCollision(entity);
			}
		}
	}
	
	public boolean isPaltaeriaStratineCollision(FloatBlockEntity other) {
		Block thisBlock = this.blockState.getBlock();
		Block otherBlock = other.getBlockState().getBlock();
		return thisBlock == SpectrumBlocks.PALTAERIA_FLOATBLOCK && otherBlock == SpectrumBlocks.STRATINE_FLOATBLOCK
				|| thisBlock == SpectrumBlocks.STRATINE_FLOATBLOCK && otherBlock == SpectrumBlocks.PALTAERIA_FLOATBLOCK;
	}
	
	/**
	 * Break the block, spawn break particles, and drop stacks if it can.
	 */
	public void dropAsItem() {
		if (this.isRemoved()) return;
		
		this.discard();
		if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
			Block.dropResources(this.blockState, this.level(), this.blockPosition());
		}
		
		// spawn break particles
		level().levelEvent(null, LevelEvent.PARTICLES_DESTROY_BLOCK, this.blockPosition(), Block.getId(blockState));
	}
	
	@Override
	public boolean onlyOpCanSetNbt() {
		return true;
	}
	
	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entityTrackerEntry) {
		return new ClientboundAddEntityPacket(this, entityTrackerEntry, Block.getId(this.getBlockState()));
	}
	
	@Override
	public void recreateFromPacket(ClientboundAddEntityPacket packet) {
		super.recreateFromPacket(packet);
		this.blockState = Block.stateById(packet.getData());
		this.blocksBuilding = true;
		double d = packet.getX();
		double e = packet.getY();
		double f = packet.getZ();
		this.setPos(d, e + (double) ((1.0F - this.getBbHeight()) / 2.0F), f);
		this.setOrigin(this.blockPosition());
	}
	
	@Override
	public boolean isAttackable() {
		return false;
	}
	
	public BlockPos getOrigin() {
		return this.entityData.get(ORIGIN);
	}
	
	public void setOrigin(BlockPos origin) {
		this.entityData.set(ORIGIN, origin);
		this.setPos(getX(), getY(), getZ());
	}
	
	public Long getLaunchTime() {
		return this.entityData.get(LAUNCH_TIME);
	}
	
	public void setLaunchTime(long spawnTime) {
		this.entityData.set(LAUNCH_TIME, spawnTime);
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(ORIGIN, BlockPos.ZERO);
		builder.define(GRAVITY_MODIFIER, 0.0F);
		builder.define(LAUNCH_TIME, 0L);
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return collides;
	}
	
	@Override
	public double getDefaultGravity() {
		return this.entityData.get(GRAVITY_MODIFIER);
	}
	
	protected void setGravity(float modifier) {
		this.entityData.set(GRAVITY_MODIFIER, modifier);
	}
	
	@Override
	public boolean isNoGravity() {
		return this.getDefaultGravity() == 0.0 || super.isNoGravity();
	}
	
}
