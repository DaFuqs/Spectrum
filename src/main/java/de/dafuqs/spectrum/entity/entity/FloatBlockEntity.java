package de.dafuqs.spectrum.entity.entity;

import com.google.common.collect.Lists;
import de.dafuqs.spectrum.blocks.gravity.FloatBlock;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumDamageSources;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.incubus_core.blocklikeentities.api.BlockLikeEntity;
import net.id.incubus_core.blocklikeentities.util.PostTickEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.List;

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
		this.setOrigin(new BlockPos(this.getPos()));
		if (blockState.getBlock() instanceof FloatBlock) {
			this.gravityModifier = ((FloatBlock) blockState.getBlock()).getGravityMod();
		} else {
			this.gravityModifier = 1.0F;
		}
	}
	
	public FloatBlockEntity(World world, BlockPos pos, BlockState blockState, boolean partOfSet) {
		this(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, blockState);
		this.partOfSet = partOfSet;
	}
	
	@Override
	public void postTickMovement() {
		if (!this.hasNoGravity()) {
			if (this.gravityModifier != 0) {
				if (this.moveTime > 100) {
					this.addVelocity(0.0D, (gravityModifier / 10), 0.0D);
				} else {
					this.addVelocity(0.0D, Math.min(Math.sin((Math.PI * this.age) / 100D), 1) * (gravityModifier / 10), 0.0D);
				}
			}
			this.move(MovementType.SELF, this.getVelocity());
		}
	}
	
	@Override
	public void tick() {
	
	}
	
	@Override
	public void postTickMoveEntities() {
		super.postTickMoveEntities();
		
		List<Entity> otherEntities = this.world.getOtherEntities(this, getBoundingBox().union(getBoundingBox().offset(0, 2 * (this.prevY - this.getY()), 0)));
		for (Entity entity : otherEntities) {
			if (!(entity instanceof BlockLikeEntity) && !entity.noClip && this.collides()) {
				entity.setPosition(entity.getX(), this.getBoundingBox().maxY, entity.getZ());
			}
		}
	}
	
	@Override
	public boolean isCollidable() {
		return true;
	}
	
	@Override
	public boolean isPushable() {
		return true;
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
	public ActionResult interact(PlayerEntity player, Hand hand) {
		if (!this.world.isClient && player.isSneaking()) {
			Item item = this.blockState.getBlock().asItem();
			if (item != null) {
				Support.givePlayer(player, item.getDefaultStack());
				this.discard();
			}
			return ActionResult.CONSUME;
		} else {
			return ActionResult.SUCCESS;
		}
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
		if (isPaltaeriaCrimtaneCollision(entity)) {
			world.createExplosion(this, this.getX(), this.getY(), this.getZ(), 1.0F, Explosion.DestructionType.NONE);
			this.discard();
			entity.discard();
			
			ItemStack collisionStack = SpectrumBlocks.HOVER_BLOCK.asItem().getDefaultStack();
			ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), collisionStack);
			itemEntity.addVelocity(0.1 - world.random.nextFloat() * 0.2, 0.1 - world.random.nextFloat() * 0.2, 0.1 - world.random.nextFloat() * 0.2);
			world.spawnEntity(itemEntity);
		}
	}
	
	public boolean isPaltaeriaCrimtaneCollision(Entity other) {
		if (other instanceof BlockLikeEntity otherBlockLikeEntity) {
			Block thisBlock = this.blockState.getBlock();
			Block otherBlock = otherBlockLikeEntity.getBlockState().getBlock();
			return thisBlock == SpectrumBlocks.PALETUR_FRAGMENT_BLOCK && otherBlock == SpectrumBlocks.SCARLET_FRAGMENT_BLOCK
					|| thisBlock == SpectrumBlocks.SCARLET_FRAGMENT_BLOCK && otherBlock == SpectrumBlocks.PALETUR_FRAGMENT_BLOCK;
		}
		return false;
	}
	
	
}