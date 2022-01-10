package de.dafuqs.spectrum.entity.entity;

import com.google.common.collect.Lists;
import de.dafuqs.spectrum.blocks.gravity.GravitableBlock;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumDamageSources;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.List;

public class GravityBlockEntity extends Entity {

	public int floatTime;
	private BlockState blockState;
	private float gravityModifier;
	private int maxDamage;
	private float damagePerFallenBlock;
	protected static final TrackedData<BlockPos> ORIGIN = DataTracker.registerData(GravityBlockEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);

	public GravityBlockEntity(EntityType<? extends GravityBlockEntity> entityTypeIn, World worldIn) {
		super(entityTypeIn, worldIn);
		this.blockState = Blocks.SAND.getDefaultState();
		this.gravityModifier = 1.0F;
		this.maxDamage = 5;
		this.damagePerFallenBlock = 0.5F;
	}

	public GravityBlockEntity(World world, double x, double y, double z, BlockState gravityBlockState) {
		this(SpectrumEntityTypes.GRAVITY_BLOCK, world);
		this.blockState = gravityBlockState;
		if(gravityBlockState.getBlock() instanceof GravitableBlock) {
			this.gravityModifier = ((GravitableBlock) gravityBlockState.getBlock()).getGravityMod();
		} else {
			this.gravityModifier = 1.0F;
		}
		this.inanimate = true;
		this.updatePosition(x, y, z);
		this.setVelocity(Vec3d.ZERO);
		this.prevX = x;
		this.prevY = y;
		this.prevZ = z;
		this.setOrigin(new BlockPos(this.getPos()));
	}

	@Override
	public Box calculateBoundingBox() {
		if (dataTracker == null) {
			return super.calculateBoundingBox();
		}
		
		VoxelShape collisionShape = VoxelShapes.fullCube();
		return collisionShape.getBoundingBox().offset(getPos().subtract(new Vec3d(0.5, 0, 0.5)));
	}

	@Override
	public boolean isAttackable() {
		return false;
	}

	@Environment(EnvType.CLIENT)
	public BlockPos getOrigin() {
		return this.dataTracker.get(ORIGIN);
	}

	public void setOrigin(BlockPos origin) {
		this.dataTracker.set(ORIGIN, origin);
		this.updatePosition(getX(), getY(), getZ());
	}

	@Override
	public boolean collides() {
		return !this.isRemoved();
	}

	@Override
	public boolean isCollidable() {
		return this.collides();
	}

	@Override
	public boolean collidesWith(Entity other) {
		return !(other instanceof GravityBlockEntity) && super.collidesWith(other);
	}

	@Override
	public void tick() {

	}

	/**
	 * Because this entity moves other entities, including players, this entity has
	 * to tick after the all other entities have ticked to prevent them phasing though this
	 */
	public void onPostTick() {
		if (this.blockState.isAir()) {
			this.discard();
		} else {
			Block block = this.blockState.getBlock();
			BlockPos blockPos;
			if (this.floatTime++ == 0) {
				blockPos = this.getBlockPos();
				if (this.world.getBlockState(blockPos).isOf(block)) {
					this.world.removeBlock(blockPos, false);
				} else if (!this.world.isClient) {
					this.discard();
					return;
				}
			}

			if (!this.hasNoGravity()) {
				if (this.floatTime > 100) {
					this.setVelocity(this.getVelocity().add(0.0D, (gravityModifier / 10), 0.0D));
				} else {
					this.setVelocity(this.getVelocity().add(0.0D, Math.min(Math.sin((Math.PI * this.age) / 100D), 1) * (gravityModifier / 10), 0.0D));
				}
			}

			Box oldBox = getBoundingBox();
			this.move(MovementType.SELF, this.getVelocity());

			Box newBox = getBoundingBox();
			List<Entity> otherEntities = this.world.getOtherEntities(this, oldBox.union(newBox));
			for (Entity entity : otherEntities) {
				if (!(entity instanceof GravityBlockEntity) && !entity.noClip && this.collides()) {
					// since this is only ticked 20 times per second it feels very bumpy, when standing on top of it
					entity.fallDistance = 0F;
					entity.setPosition(entity.getPos().x, getBoundingBox().maxY, entity.getPos().z);
					entity.setOnGround(true);
				}
			}

			if (!this.world.isClient) {
				blockPos = this.getBlockPos();

				if (!this.verticalCollision) {
					if (!this.world.isClient) {
						if (this.floatTime > 100 && blockPos.getY() < 1) {
							if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
								this.dropItem(block);
							}
							this.discard();
						} else if (floatTime > 500) {
							 this.discard();
						}
					}
				} else {
					BlockState blockState = this.world.getBlockState(blockPos);
					this.setVelocity(this.getVelocity().multiply(0.7, 0.5, 0.7));
					this.discard();
					boolean canReplace = blockState.canReplace(new AutomaticItemPlacementContext(this.world, blockPos, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
					if (canReplace) {
						canReplace = blockState.canReplace(new AutomaticItemPlacementContext(this.world, blockPos, Direction.UP, ItemStack.EMPTY, Direction.DOWN));
					}
					boolean canPlace = this.blockState.canPlaceAt(this.world, blockPos);

					if (canReplace && canPlace) {
						if (this.world.setBlockState(blockPos, this.blockState, 3)) {
							// replace the previously existing, replaceable block
						} else if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
							this.dropItem(block);
						}
					} else if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
						this.dropItem(block);
					}
				}
			}

			// assures terminal velocity
			this.setVelocity(this.getVelocity().multiply(0.99D));
		}
	}

	@Override
	public boolean handleFallDamage(float distance, float damageMultiplier, DamageSource damageSource) {
		int traveledDistance = MathHelper.ceil(distance - 1.0F);
		if (traveledDistance > 0) {
			int damage = Math.min(MathHelper.floor(traveledDistance * this.damagePerFallenBlock), this.maxDamage);
			if(damage > 0) {
				// since the players position is tracked at its head and item entities are laying directly on the ground we have to use a relatively big bounding box here
				List<Entity> list = Lists.newArrayList(this.world.getOtherEntities(this, this.getBoundingBox().expand(0, 3.0 * Math.signum(this.getVelocity().y), 0).expand(0, -0.5 * Math.signum(this.getVelocity().y), 0)));
				for (Entity entity : list) {
					entity.damage(SpectrumDamageSources.FLOATBLOCK, damage);
				}
			}
		}
		return false;
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound compound) {
		compound.put("BlockState", NbtHelper.fromBlockState(this.blockState));
		compound.putInt("Time", this.floatTime);
		compound.putFloat("FallHurtAmount", this.damagePerFallenBlock);
		compound.putInt("FallHurtMax", this.maxDamage);
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound compound) {
		this.blockState = NbtHelper.toBlockState(compound.getCompound("BlockState"));
		this.floatTime = compound.getInt("Time");
		if (compound.contains("HurtEntities", 99)) {
			this.damagePerFallenBlock = compound.getFloat("FallHurtAmount");
			this.maxDamage = compound.getInt("FallHurtMax");
		}

		if (this.blockState.isAir()) this.blockState = SpectrumBlocks.PALETUR_FRAGMENT_BLOCK.getDefaultState();
	}

	@Environment(EnvType.CLIENT)
	public World getWorldClient() {
		return this.world;
	}

	@Override
	public boolean doesRenderOnFire() {
		return false;
	}

	public BlockState getBlockState() {
		return this.blockState;
	}

	@Override
	public boolean entityDataRequiresOperator() {
		return true;
	}

	@Override
	protected void initDataTracker() {
		this.dataTracker.startTracking(ORIGIN, BlockPos.ORIGIN);
	}

	public interface PostTicker {
		void postTick();
	}

	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this, Block.getRawIdFromState(this.getBlockState()));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		this.blockState = Block.getStateFromRawId(packet.getEntityData());
		if(blockState.getBlock() instanceof GravitableBlock) {
			this.gravityModifier = ((GravitableBlock) blockState.getBlock()).getGravityMod();
		} else {
			this.gravityModifier = 1.0F;
		}
		this.inanimate = true;
		double d = packet.getX();
		double e = packet.getY();
		double f = packet.getZ();
		this.setPosition(d, e + (double)((1.0F - this.getHeight()) / 2.0F), f);
		this.setOrigin(this.getBlockPos());
	}

}