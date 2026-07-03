package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.entity.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;

public class ItemProjectileEntity extends ThrowableItemProjectile {
	
	public ItemProjectileEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
		super(entityType, world);
	}
	
	public ItemProjectileEntity(Level world, LivingEntity owner) {
		super(SpectrumEntityTypes.ITEM_PROJECTILE.get(), owner, world);
	}
	
	@Override
	protected void onHit(HitResult hitResult) {
		ItemStack stack = getItem();
		
		if (!this.level().isClientSide()) {
			ItemProjectileBehavior behavior = ItemProjectileBehavior.get(stack);
			HitResult.Type type = hitResult.getType();
			if (type == HitResult.Type.ENTITY) {
				this.level().gameEvent(GameEvent.PROJECTILE_LAND, hitResult.getLocation(), GameEvent.Context.of(this, null));
				stack = behavior.onEntityHit(this, stack, getOwner(), (EntityHitResult) hitResult);
			} else if (type == HitResult.Type.BLOCK) {
				BlockHitResult blockHitResult = (BlockHitResult) hitResult;
				BlockPos blockPos = blockHitResult.getBlockPos();
				this.level().gameEvent(GameEvent.PROJECTILE_LAND, blockPos, GameEvent.Context.of(this, this.level().getBlockState(blockPos)));
				stack = behavior.onBlockHit(this, stack, getOwner(), (BlockHitResult) hitResult);
			}
			
			this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
			
			if (!stack.isEmpty()) {
				Entity owner = this.getOwner();
				if (!(owner instanceof Player player) || !player.isCreative()) {
					Containers.dropItemStack(level(), this.getX(), this.getY(), this.getZ(), stack);
				}
			}
			
			this.discard();
		}
	}
	
	@Override
	public void handleEntityEvent(byte status) {
		if (status == EntityEvent.DEATH) {
			ItemStack itemStack = this.getItem();
			ParticleOptions particleEffect = (itemStack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleOption(ParticleTypes.ITEM, itemStack));
			
			for (int i = 0; i < 8; ++i) {
				this.level().addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
			}
		}
	}
	
	@Override
	protected Item getDefaultItem() {
		return Items.AIR;
	}
	
}
