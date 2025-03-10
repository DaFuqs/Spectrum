package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.blocks.block_flooder.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.thrown.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class BlockFlooderProjectile extends ThrownItemEntity {
	
	public BlockFlooderProjectile(EntityType<ThrownItemEntity> thrownItemEntityEntityType, World world) {
		super(SpectrumEntityTypes.BLOCK_FLOODER_PROJECTILE, world);
	}

	public BlockFlooderProjectile(World world, LivingEntity owner) {
		super(SpectrumEntityTypes.BLOCK_FLOODER_PROJECTILE, owner, world);
	}

	public BlockFlooderProjectile(World world, double x, double y, double z) {
		super(SpectrumEntityTypes.BLOCK_FLOODER_PROJECTILE, x, y, z, world);
	}

	@Override
	protected Item getDefaultItem() {
		return SpectrumItems.BLOCK_FLOODER;
	}

	private ParticleEffect getParticleParameters() {
		ItemStack itemStack = this.getItem();
		return (itemStack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));
	}

	@Override
	public void handleStatus(byte status) {
		if (status == 3) {
			ParticleEffect particleEffect = this.getParticleParameters();

			for (int i = 0; i < 8; ++i) {
				this.getWorld().addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	protected void onCollision(HitResult hitResult) {
		super.onCollision(hitResult);
		World world = this.getWorld();
		if (!world.isClient()) {
			world.sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);

			if (hitResult.getType() == HitResult.Type.BLOCK) {
				BlockPos landingPos = getCorrectedBlockPos(hitResult.getPos());
				if (BlockFlooderBlock.isReplaceableBlock(world, landingPos) && this.getOwner() instanceof PlayerEntity playerEntityOwner) {
					world.setBlockState(landingPos, SpectrumBlocks.BLOCK_FLOODER.getDefaultState());
					BlockEntity blockEntity = world.getBlockEntity(landingPos);
					if (blockEntity instanceof BlockFlooderBlockEntity blockFlooderBlockEntity) {
						blockFlooderBlockEntity.setOwnerUUID(playerEntityOwner.getUuid());
						blockFlooderBlockEntity.setSourcePos(landingPos);
					}
					
					this.discard();
				}
			}
		}
	}
	
	/**
	 * Since the projectile sometimes reports its position in the full neighboring position
	 * on hit the blockPos has to be corrected to the closest neighboring Non-full block pos
	 *
	 * @return The "actual" hit block pos
	 */
	public BlockPos getCorrectedBlockPos(Vec3d hitPos) {
		BlockPos hitBlockPos = BlockPos.ofFloored(hitPos);
		if (this.getWorld().getBlockState(hitBlockPos).isSolidBlock(this.getWorld(), hitBlockPos)) {
			if (hitPos.getX() % 1 < 0.05) {
				return hitBlockPos.add(-1, 0, 0);
			}
			if (hitPos.getY() % 1 < 0.05) {
				return hitBlockPos.add(0, -1, 0);
			}
			if (hitPos.getZ() % 1 < 0.05) {
				return hitBlockPos.add(0, 0, -1);
			}
			
			if (hitPos.getX() % 1 < 0.95) {
				return hitBlockPos.add(1, 0, 0);
			}
			if (hitPos.getY() % 1 < 0.95) {
				return hitBlockPos.add(0, 1, 0);
			}
			if (hitPos.getZ() % 1 < 0.95) {
				return hitBlockPos.add(0, 0, 1);
			}
		}
		return hitBlockPos;
	}
	
}
