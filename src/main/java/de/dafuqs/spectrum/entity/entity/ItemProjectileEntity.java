package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.thrown.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;

public class ItemProjectileEntity extends ThrownItemEntity {

	public ItemProjectileEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
		super(entityType, world);
	}

	public ItemProjectileEntity(World world, LivingEntity owner) {
		super(SpectrumEntityTypes.ITEM_PROJECTILE, owner, world);
	}

	@Override
	protected void onCollision(HitResult hitResult) {
		ItemStack stack = getStack();

		if (!this.getWorld().isClient) {
			ItemProjectileBehavior behavior = ItemProjectileBehavior.get(stack);
			if (behavior != null) {
				HitResult.Type type = hitResult.getType();
				if (type == HitResult.Type.ENTITY) {
					this.getWorld().emitGameEvent(GameEvent.PROJECTILE_LAND, hitResult.getPos(), GameEvent.Emitter.of(this, null));
					behavior.onEntityHit(this, stack, getOwner(), (EntityHitResult) hitResult);
				} else if (type == HitResult.Type.BLOCK) {
					BlockHitResult blockHitResult = (BlockHitResult) hitResult;
					BlockPos blockPos = blockHitResult.getBlockPos();
					this.getWorld().emitGameEvent(GameEvent.PROJECTILE_LAND, blockPos, GameEvent.Emitter.of(this, this.getWorld().getBlockState(blockPos)));
					behavior.onBlockHit(this, stack, getOwner(), (BlockHitResult) hitResult);
				}
			}
			
			this.getWorld().sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
			
			if (!stack.isEmpty()) {
				Entity owner = this.getOwner();
				if (!(owner instanceof PlayerEntity player) || !player.isCreative()) {
					ItemScatterer.spawn(getWorld(), this.getX(), this.getY(), this.getZ(), stack);
				}
			}
			
			this.discard();
		}
	}

	@Override
	public void handleStatus(byte status) {
		if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
			ItemStack itemStack = this.getItem();
			ParticleEffect particleEffect = (itemStack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));

			for(int i = 0; i < 8; ++i) {
				this.getWorld().addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
			}
		}
	}

	@Override
	protected Item getDefaultItem() {
		return Items.AIR;
	}

}
