package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.blocks.block_flooder.BlockFlooderBlock;
import de.dafuqs.spectrum.blocks.block_flooder.BlockFlooderBlockEntity;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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
	
	protected Item getDefaultItem() {
		return SpectrumItems.BLOCK_FLOODER;
	}
	
	private ParticleEffect getParticleParameters() {
		ItemStack itemStack = this.getItem();
		return (itemStack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));
	}
	
	public void handleStatus(byte status) {
		if (status == 3) {
			ParticleEffect particleEffect = this.getParticleParameters();
			
			for (int i = 0; i < 8; ++i) {
				this.world.addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		}
	}
	
	protected void onCollision(HitResult hitResult) {
		super.onCollision(hitResult);
		if (!this.world.isClient) {
			this.world.sendEntityStatus(this, (byte) 3);
			
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
	 * on hit the blockPos has to be corrected to the closest neighboring Non full block pos
	 *
	 * @return The "actual" hit block pos
	 */
	public BlockPos getCorrectedBlockPos(Vec3d hitPos) {
		BlockPos hitBlockPos = new BlockPos(hitPos);
		if (world.getBlockState(hitBlockPos).isSolidBlock(world, hitBlockPos)) {
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
