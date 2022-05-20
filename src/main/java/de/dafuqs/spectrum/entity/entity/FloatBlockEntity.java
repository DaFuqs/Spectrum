package de.dafuqs.spectrum.entity.entity;

import com.google.common.collect.Lists;
import de.dafuqs.spectrum.blocks.gravity.FloatBlock;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.registries.SpectrumDamageSources;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.incubus_core.blocklikeentities.api.BlockLikeEntity;
import net.id.incubus_core.blocklikeentities.util.PostTickEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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
		if(blockState.getBlock() instanceof FloatBlock) {
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
			if (this.moveTime > 100) {
				this.addVelocity(0.0D, (gravityModifier / 10), 0.0D);
			} else {
				this.addVelocity(0.0D, Math.min(Math.sin((Math.PI * this.age) / 100D), 1) * (gravityModifier / 10), 0.0D);
			}
			this.move(MovementType.SELF, this.getVelocity());
		}
	}
	
	@Override
	public boolean shouldCease() {
		return this.verticalCollision || super.shouldCease();
	}
	
	@Override
	public boolean handleFallDamage(float distance, float damageMultiplier, DamageSource damageSource) {
		if(!world.isClient) {
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
	@Environment(EnvType.CLIENT)
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		if(this.blockState.getBlock() instanceof FloatBlock) {
			this.gravityModifier = ((FloatBlock) blockState.getBlock()).getGravityMod();
		} else {
			this.gravityModifier = 1.0F;
		}
	}

}