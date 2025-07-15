package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.blocks.block_flooder.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.phys.*;

public class BlockFlooderProjectile extends ThrowableItemProjectile {
	
	public BlockFlooderProjectile(EntityType<ThrowableItemProjectile> thrownItemEntityEntityType, Level world) {
		super(SpectrumEntityTypes.BLOCK_FLOODER_PROJECTILE, world);
	}
	
	public BlockFlooderProjectile(Level world, LivingEntity owner) {
		super(SpectrumEntityTypes.BLOCK_FLOODER_PROJECTILE, owner, world);
	}
	
	public BlockFlooderProjectile(Level world, double x, double y, double z) {
		super(SpectrumEntityTypes.BLOCK_FLOODER_PROJECTILE, x, y, z, world);
	}
	
	@Override
	protected Item getDefaultItem() {
		return SpectrumItems.BLOCK_FLOODER;
	}
	
	private ParticleOptions getParticleParameters() {
		ItemStack itemStack = this.getItem();
		return (itemStack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleOption(ParticleTypes.ITEM, itemStack));
	}
	
	@Override
	public void handleEntityEvent(byte status) {
		if (status == 3) {
			ParticleOptions particleEffect = this.getParticleParameters();
			
			for (int i = 0; i < 8; ++i) {
				this.level().addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		}
	}
	
	@Override
	protected void onHit(HitResult hitResult) {
		super.onHit(hitResult);
		Level world = this.level();
		if (!world.isClientSide()) {
			world.broadcastEntityEvent(this, EntityEvent.DEATH);
			
			if (hitResult.getType() == HitResult.Type.BLOCK) {
				BlockPos landingPos = getCorrectedBlockPos(hitResult.getLocation());
				if (BlockFlooderBlock.isReplaceableBlock(world, landingPos) && this.getOwner() instanceof Player playerEntityOwner) {
					world.setBlockAndUpdate(landingPos, SpectrumBlocks.BLOCK_FLOODER.get().defaultBlockState());
					BlockEntity blockEntity = world.getBlockEntity(landingPos);
					if (blockEntity instanceof BlockFlooderBlockEntity blockFlooderBlockEntity) {
						blockFlooderBlockEntity.setOwnerUUID(playerEntityOwner.getUUID());
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
	public BlockPos getCorrectedBlockPos(Vec3 hitPos) {
		BlockPos hitBlockPos = BlockPos.containing(hitPos);
		if (this.level().getBlockState(hitBlockPos).isRedstoneConductor(this.level(), hitBlockPos)) {
			if (hitPos.x() % 1 < 0.05) {
				return hitBlockPos.offset(-1, 0, 0);
			}
			if (hitPos.y() % 1 < 0.05) {
				return hitBlockPos.offset(0, -1, 0);
			}
			if (hitPos.z() % 1 < 0.05) {
				return hitBlockPos.offset(0, 0, -1);
			}
			
			if (hitPos.x() % 1 < 0.95) {
				return hitBlockPos.offset(1, 0, 0);
			}
			if (hitPos.y() % 1 < 0.95) {
				return hitBlockPos.offset(0, 1, 0);
			}
			if (hitPos.z() % 1 < 0.95) {
				return hitBlockPos.offset(0, 0, 1);
			}
		}
		return hitBlockPos;
	}
	
}
