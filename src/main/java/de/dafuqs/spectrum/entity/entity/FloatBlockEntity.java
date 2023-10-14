package de.dafuqs.spectrum.entity.entity;

import com.google.common.collect.*;
import de.dafuqs.spectrum.blocks.gravity.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.id.incubus_core.blocklikeentities.api.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.data.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.explosion.*;

import java.util.*;

public class FloatBlockEntity extends BlockLikeEntity {
	
	private static final float MAX_DAMAGE = 8.0F;
	private static final float DAMAGE_PER_FALLEN_BLOCK = 0.5F;
	private static final TrackedData<Float> GRAVITY_MODIFIER = DataTracker.registerData(FloatBlockEntity.class, TrackedDataHandlerRegistry.FLOAT);
	
	public FloatBlockEntity(EntityType<? extends FloatBlockEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public FloatBlockEntity(World world, double x, double y, double z, BlockState blockState) {
		super(SpectrumEntityTypes.FLOAT_BLOCK, world, x, y, z, blockState);
		
		if (blockState.getBlock() instanceof FloatBlock floatBlock) {
			setGravity(floatBlock.getGravityMod());
		}
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(GRAVITY_MODIFIER, 0.0F);
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
	
	@Override
	public void postTickMoveEntities() {
		if (FallingBlock.canFallThrough(this.blockState)) return;
		
		for (Entity entity : this.world.getOtherEntities(this, getBoundingBox().offset(0, 0.5, 0).union(getBoundingBox().offset(3 * (this.prevX - this.getX()), 3 * (this.prevY - this.getY()), 3 * (this.prevZ - this.getZ()))))) {
			
			if (entity instanceof BlockLikeEntity other && isPaltaeriaStratineCollision(other)) {
				world.createExplosion(this, this.getX(), this.getY(), this.getZ(), 1.0F, Explosion.DestructionType.NONE);
				
				ItemStack collisionStack = SpectrumBlocks.HOVER_BLOCK.asItem().getDefaultStack();
				ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), collisionStack);
				itemEntity.addVelocity(0.1 - world.random.nextFloat() * 0.2, 0.1 - world.random.nextFloat() * 0.2, 0.1 - world.random.nextFloat() * 0.2);
				world.spawnEntity(itemEntity);
				
				this.discard();
				other.discard();
			} else if (entity.isPushable()) {
				entity.move(MovementType.SHULKER_BOX, this.getVelocity());
				entity.setOnGround(true);
				entity.addVelocity(this.getVelocity().x, this.getVelocity().y, this.getVelocity().z);
				entity.fallDistance = 0F;
				
				this.postTickEntityCollision(entity);
			}
			
		}
	}
	
	@Override
	public void postTickMovement() {
		if (!this.hasNoGravity()) {
			this.setVelocity(this.getVelocity().multiply(0.98D));
			double additionalYVelocity = this.age > 100 ? this.getGravity() / 10 : Math.min(Math.sin((Math.PI * this.age) / 100D), 1) * (this.getGravity() / 10);
			this.addVelocity(0.0D, additionalYVelocity, 0.0D);
		}
		
		this.move(MovementType.SELF, this.getVelocity());
	}
	
	@Override
	public void move(MovementType movementType, Vec3d movement) {
		super.move(movementType, movement);
		
		if (movementType != MovementType.SELF) {
			this.setVelocity(movement);
		}
	}
	
	@Override
	public boolean shouldCease() {
		return this.verticalCollision || super.shouldCease();
	}
	
	@Override
	public boolean handleFallDamage(float distance, float damageMultiplier, DamageSource damageSource) {
		if (!world.isClient) {
			int traveledDistance = MathHelper.ceil(distance - 1.0F);
			if (traveledDistance > 0) {
				int damage = (int) Math.min(MathHelper.floor(traveledDistance * DAMAGE_PER_FALLEN_BLOCK), MAX_DAMAGE);
				if (damage > 0) {
					// since the players position is tracked at its head and item entities are laying directly on the ground we have to use a relatively big bounding box here
					List<Entity> list = Lists.newArrayList(this.world.getOtherEntities(this, this.getBoundingBox().expand(0, 3.0 * Math.signum(this.getVelocity().y), 0).expand(0, -0.5 * Math.signum(this.getVelocity().y), 0)));
					for (Entity entity : list) {
						entity.damage(SpectrumDamageSources.FLOATBLOCK, damage);
					}
				}
			}
		}
		return false;
	}
	
	@Override
	protected void writeCustomDataToNbt(NbtCompound compound) {
		super.writeCustomDataToNbt(compound);
		
		compound.putFloat("GravityModifier", getGravity());
	}
	
	@Override
	protected void readCustomDataFromNbt(NbtCompound compound) {
		super.readCustomDataFromNbt(compound);
		
		if (compound.contains("GravityModifier", NbtElement.FLOAT_TYPE)) {
			setGravity(compound.getFloat("GravityModifier"));
		}
	}
	
	@Override
	public boolean canHit() {
		return !this.isRemoved();
	}
	
	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		if (player.isSneaking()) {
			if (this.world.isClient) {
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
	
	public boolean isPaltaeriaStratineCollision(BlockLikeEntity other) {
		Block thisBlock = this.blockState.getBlock();
		Block otherBlock = other.getBlockState().getBlock();
		return thisBlock == SpectrumBlocks.PALTAERIA_FRAGMENT_BLOCK && otherBlock == SpectrumBlocks.STRATINE_FRAGMENT_BLOCK
				|| thisBlock == SpectrumBlocks.STRATINE_FRAGMENT_BLOCK && otherBlock == SpectrumBlocks.PALTAERIA_FRAGMENT_BLOCK;
	}
	
	@Override
	public boolean collidesWith(Entity other) {
		return other.isCollidable() && !this.isConnectedThroughVehicle(other);
	}
	
}