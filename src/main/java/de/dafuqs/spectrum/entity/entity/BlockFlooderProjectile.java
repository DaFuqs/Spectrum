package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.blocks.block_flooder.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.phys.*;

import java.util.*;

public class BlockFlooderProjectile extends ThrowableItemProjectile {
	
	public BlockFlooderProjectile(EntityType<BlockFlooderProjectile> thrownItemEntityEntityType, Level world) {
		super(SpectrumEntityTypes.BLOCK_FLOODER_PROJECTILE.get(), world);
	}
	
	public BlockFlooderProjectile(Level world, LivingEntity owner) {
		super(SpectrumEntityTypes.BLOCK_FLOODER_PROJECTILE.get(), owner, world);
	}
	
	public BlockFlooderProjectile(Level world, double x, double y, double z) {
		super(SpectrumEntityTypes.BLOCK_FLOODER_PROJECTILE.get(), x, y, z, world);
	}
	
	@Override
	protected Item getDefaultItem() {
		return SpectrumItems.BLOCK_FLOODER.get();
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
			
			if (hitResult instanceof BlockHitResult blockHitResult) {
				BlockPos landingPos = blockHitResult.getBlockPos().relative(blockHitResult.getDirection());
				if (BlockFlooderBlock.isReplaceableBlock(world, landingPos) && this.getOwner() instanceof Player owner) {
					world.setBlockAndUpdate(landingPos, SpectrumBlocks.BLOCK_FLOODER.get().defaultBlockState());
					BlockEntity blockEntity = world.getBlockEntity(landingPos);
					if (blockEntity instanceof BlockFlooderBlockEntity blockFlooderBlockEntity) {
						blockFlooderBlockEntity.setOwnerUUID(owner.getUUID());
						blockFlooderBlockEntity.setSourcePos(landingPos);
						
						Optional<Block> targetBlock = ExchangeStaffItem.getStoredBlock(getItem());
						if(targetBlock.isPresent()) {
							Block b = BlockFlooderBlock.removeFromPlayerInventory(owner, targetBlock.get());
							if(b != null) {
								blockFlooderBlockEntity.setTargetBlockState(b.defaultBlockState());
								blockFlooderBlockEntity.setTargetStateForced(true);
							}
						}
					}
					
					this.discard();
				}
			}
		}
	}
	
}
