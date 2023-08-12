package de.dafuqs.spectrum.entity.entity;

import com.google.common.collect.*;
import de.dafuqs.spectrum.blocks.gravity.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.id.incubus_core.blocklikeentities.api.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;

public class FloatBlockEntity extends BlockLikeEntity {
	
	private static final float MAX_DAMAGE = 8.0F;
	private static final float DAMAGE_PER_FALLEN_BLOCK = 0.5F;
	
	private float gravityModifier = 1.0F;
	
	public FloatBlockEntity(EntityType<? extends FloatBlockEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public FloatBlockEntity(World world, double x, double y, double z, BlockState blockState) {
		super(SpectrumEntityTypes.FLOAT_BLOCK, world, x, y, z, blockState);
		if (blockState.getBlock() instanceof FloatBlock floatBlock) {
			this.gravityModifier = floatBlock.getGravityMod();
		}
	}

	@Override
	public void postTickMoveEntities() {

	}

	@Override
	public void postTickMovement() {

	}

	@Override
	public void move(MovementType movementType, Vec3d movement) {
		super.move(movementType, movement);

		if (movementType != MovementType.SELF) {
			this.setVelocity(movement);
		}

		if (movement.length() > 0) {
			moveEntitiesOnTop();
		}
	}

	private void moveEntitiesOnTop() {
		this.getWorld().getOtherEntities(this, getBoundingBox().offset(0, 0.5, 0)
						.union(getBoundingBox().offset(3 * (this.prevX - this.getX()), 3 * (this.prevY - this.getY()), 3 * (this.prevZ - this.getZ()))))
				.stream()
				.filter(entity -> !(entity instanceof BlockLikeEntity) && entity.isPushable())
				.forEach(entity -> {
					entity.fallDistance = 0F;
					if (this.canHit()) {
						entity.setPosition(entity.getPos().x, this.getBoundingBox().maxY, entity.getPos().z);
					}
					entity.move(MovementType.SHULKER_BOX, this.getVelocity());
					entity.setOnGround(true);
				});
	}

	@Override
	public void tick() {
		if (!this.hasNoGravity()) {
			if (this.moveTime > 100) {
				this.addVelocity(0.0D, (this.gravityModifier / 10), 0.0D);
			} else {
				this.addVelocity(0.0D, Math.min(Math.sin((Math.PI * this.age) / 100D), 1) * (this.gravityModifier / 10), 0.0D);
			}
		}

		this.move(MovementType.SELF, this.getVelocity());

		this.checkBlockCollision();
		this.updateWaterState();
	}

	@Override
	public boolean hasNoGravity() {
		return this.gravityModifier != 0.0 && super.hasNoGravity();
	}

	@Override
	public boolean shouldCease() {
		return this.verticalCollision || super.shouldCease();
	}
	
	@Override
	public boolean handleFallDamage(float distance, float damageMultiplier, DamageSource damageSource) {
		World world = this.getWorld();
		if (!world.isClient) {
			int traveledDistance = MathHelper.ceil(distance - 1.0F);
			if (traveledDistance > 0) {
				int damage = (int) Math.min(MathHelper.floor(traveledDistance * DAMAGE_PER_FALLEN_BLOCK), MAX_DAMAGE);
				if (damage > 0) {
					// since the players position is tracked at its head and item entities are laying directly on the ground we have to use a relatively big bounding box here
					List<Entity> list = Lists.newArrayList(world.getOtherEntities(this, this.getBoundingBox().expand(0, 3.0 * Math.signum(this.getVelocity().y), 0).expand(0, -0.5 * Math.signum(this.getVelocity().y), 0)));
					for (Entity entity : list) {
						entity.damage(SpectrumDamageSources.floatblock(entity.getWorld()), damage);
					}
				}
			}
		}
		return false;
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

	@Override
	@Environment(EnvType.CLIENT)
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		if (this.blockState.getBlock() instanceof FloatBlock floatBlock) {
			this.gravityModifier = floatBlock.getGravityMod();
		}
	}
	
	@Override
	public void postTickEntityCollision(Entity entity) {
		super.postTickEntityCollision(entity);
		World world = this.getWorld();

		if (isPaltaeriaStratineCollision(entity)) {
			world.createExplosion(this, this.getX(), this.getY(), this.getZ(), 1.0F, World.ExplosionSourceType.NONE);
			this.discard();
			entity.discard();
			
			ItemStack collisionStack = SpectrumBlocks.HOVER_BLOCK.asItem().getDefaultStack();
			ItemEntity itemEntity = new ItemEntity(world, this.getX(), this.getY(), this.getZ(), collisionStack);
			itemEntity.addVelocity(0.1 - world.random.nextFloat() * 0.2, 0.1 - world.random.nextFloat() * 0.2, 0.1 - world.random.nextFloat() * 0.2);
			world.spawnEntity(itemEntity);
		}
	}

	public boolean isPaltaeriaStratineCollision(Entity other) {
		if (other instanceof BlockLikeEntity otherBlockLikeEntity) {
			Block thisBlock = this.blockState.getBlock();
			Block otherBlock = otherBlockLikeEntity.getBlockState().getBlock();
			return thisBlock == SpectrumBlocks.PALTAERIA_FRAGMENT_BLOCK && otherBlock == SpectrumBlocks.STRATINE_FRAGMENT_BLOCK
					|| thisBlock == SpectrumBlocks.STRATINE_FRAGMENT_BLOCK && otherBlock == SpectrumBlocks.PALTAERIA_FRAGMENT_BLOCK;
		}
		return false;
	}

	@Override
	public boolean collidesWith(Entity other) {
		return other.isCollidable() && !this.isConnectedThroughVehicle(other);
	}

}