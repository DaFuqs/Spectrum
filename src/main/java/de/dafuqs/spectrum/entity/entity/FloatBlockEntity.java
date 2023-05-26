package de.dafuqs.spectrum.entity.entity;

import com.google.common.collect.*;
import de.dafuqs.spectrum.blocks.gravity.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.id.incubus_core.blocklikeentities.api.*;
import net.id.incubus_core.blocklikeentities.util.*;
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

// TODO - Incubus Core requires updating
public class FloatBlockEntity extends BlockLikeEntity implements PostTickEntity {
	
	private static final float MAX_DAMAGE = 5.0F;
	private static final float DAMAGE_PER_FALLEN_BLOCK = 0.5F;
	
	private float gravityModifier = 1.0F;
	
	public FloatBlockEntity(EntityType<? extends FloatBlockEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public FloatBlockEntity(World world, double x, double y, double z, BlockState blockState) {
		this(SpectrumEntityTypes.FLOAT_BLOCK, world);
		this.blockState = blockState;
		this.intersectionChecked = true;
		this.setPosition(x, y, z);
		this.setVelocity(Vec3d.ZERO);
		this.prevX = x;
		this.prevY = y;
		this.prevZ = z;
		this.setOrigin(BlockPos.ofFloored(this.getPos()));
		if (blockState.getBlock() instanceof FloatBlock) {
			this.gravityModifier = ((FloatBlock) blockState.getBlock()).getGravityMod();
		} else {
			this.gravityModifier = 1.0F;
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
	public void postTickMovement() {
		if (!this.hasNoGravity()) {
			if (this.gravityModifier != 0) {
				if (this.moveTime > 100) {
					this.addVelocity(0.0D, (this.gravityModifier / 10), 0.0D);
				} else {
					this.addVelocity(0.0D, Math.min(Math.sin((Math.PI * this.age) / 100D), 1) * (this.gravityModifier / 10), 0.0D);
				}
			}
			this.move(MovementType.SELF, this.getVelocity());
		}
	}

	@Override
	public void tick() {
	
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
	public boolean canHit() {
		return !this.isRemoved();
	}

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		if (player.isSneaking()) {
			if (!this.world.isClient) {
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
		if (this.blockState.getBlock() instanceof FloatBlock) {
			this.gravityModifier = ((FloatBlock) blockState.getBlock()).getGravityMod();
		} else {
			this.gravityModifier = 1.0F;
		}
	}
	
	@Override
	public void postTickEntityCollision(Entity entity) {
		super.postTickEntityCollision(entity);
		if (isPaltaeriaStratineCollision(entity)) {
			world.createExplosion(this, this.getX(), this.getY(), this.getZ(), 1.0F, World.ExplosionSourceType.NONE);
			this.discard();
			entity.discard();
			
			ItemStack collisionStack = SpectrumBlocks.HOVER_BLOCK.asItem().getDefaultStack();
			ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), collisionStack);
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