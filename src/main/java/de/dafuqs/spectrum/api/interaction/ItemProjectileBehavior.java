package de.dafuqs.spectrum.api.interaction;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

public interface ItemProjectileBehavior {
	
	List<Tuple<ItemPredicate, ItemProjectileBehavior>> BEHAVIORS = new ArrayList<>();
	
	ItemProjectileBehavior DEFAULT = new Default();
	
	static void register(ItemProjectileBehavior behavior, ItemPredicate predicate) {
		BEHAVIORS.add(new Tuple<>(predicate, behavior));
	}
	
	static void register(ItemProjectileBehavior behavior, Item... items) {
		BEHAVIORS.add(new Tuple<>(ItemPredicate.Builder.item().of(items).build(), behavior));
	}
	
	static void register(ItemProjectileBehavior behavior, TagKey<Item> tag) {
		BEHAVIORS.add(new Tuple<>(ItemPredicate.Builder.item().of(tag).build(), behavior));
	}
	
	static ItemProjectileBehavior get(ItemStack stack) {
		for (Tuple<ItemPredicate, ItemProjectileBehavior> entry : BEHAVIORS) {
			if (entry.getA().test(stack)) {
				return entry.getB();
			}
		}
		return DEFAULT;
	}
	
	/**
	 * Invoked when the projectile hits an entity.
	 *
	 * @param projectile The ItemProjectile
	 * @param stack      The stack contained in the ItemProjectile. Quick access to projectile.getStack()
	 * @param owner      The owner of the projectile
	 * @param hitResult  The EntityHitResult. Contains the entity hit and position
	 * @return The stack that should be dropped. If the stack has a count > 0, it automatically gets dropped at the position of the impact. If the item should get consumed, decrement the stack from the parameters and return it here
	 */
	ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult);
	
	/**
	 * Invoked when the projectile hits a block
	 *
	 * @param projectile The ItemProjectile
	 * @param stack      The stack contained in the ItemProjectile. Quick access to projectile.getStack()
	 * @param owner      The owner of the projectile
	 * @param hitResult  The EntityHitResult. Contains the entity hit and position
	 * @return The stack that should be dropped. If the stack has a count > 0, it automatically gets dropped at the position of the impact. If the item should get consumed, decrement the stack from the parameters and return it here
	 */
	ItemStack onBlockHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, BlockHitResult hitResult);
	
	
	static ItemProjectileBehavior damaging(float damage, boolean destroyItemOnHit) {
		return new Damaging() {
			@Override
			public boolean destroyItemOnHit() {
				return destroyItemOnHit;
			}
			
			@Override
			public boolean dealDamage(ThrowableItemProjectile projectile, Entity owner, Entity target) {
				return target.hurt(target.damageSources().thrown(projectile, owner), damage);
			}
		};
	}
	
	class Default implements ItemProjectileBehavior {
		
		@Override
		public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, Entity owner, EntityHitResult hitResult) {
			Entity target = hitResult.getEntity();
			
			if (target instanceof Player player && (player.isCreative() || player.isSpectator())) {
				return stack;
			}
			
			// Lots of fun(tm) is to be had
			if (stack.is(ItemTags.CREEPER_IGNITERS) && target instanceof Creeper creeperEntity) {
				Level world = projectile.level();
				SoundEvent soundEvent = stack.is(Items.FIRE_CHARGE) ? SoundEvents.FIRECHARGE_USE : SoundEvents.FLINTANDSTEEL_USE;
				world.playSound(null, projectile.getX(), projectile.getY(), projectile.getZ(), soundEvent, projectile.getSoundSource(), 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
				creeperEntity.ignite();
				
				if (stack.isDamageableItem() && world instanceof ServerLevel serverWorld) {
					stack.hurtAndBreak(1, serverWorld, null, item -> {
					});
				} else if (!stack.has(DataComponents.UNBREAKABLE)) {
					stack.shrink(1); // In Vanilla unbreakable Flint & Steel is not handled correctly and therefore consumed. This here probably still does not handle every modded item perfectly
				}
			}
			
			if (target instanceof LivingEntity livingTarget) {
				if (target instanceof Player && !SpectrumCommon.CONFIG.OmniAcceleratorPvP) {
					if (stack.is(SpectrumItemTags.REQUIRES_OMNI_ACCELERATOR_PVP_ENABLED)) {
						return stack;
					}
				}
				
				// attaching name tags, saddle horses, memorize entities...
				if (owner instanceof Player playerOwner && stack.interactLivingEntity(playerOwner, livingTarget, InteractionHand.MAIN_HAND).consumesAction()) {
					return stack;
				}
				
				// Force-feeds food, applies potions, ...
				return stack.getItem().finishUsingItem(stack, livingTarget.level(), livingTarget);
			}
			return stack;
		}
		
		@Override
		public ItemStack onBlockHit(ItemProjectileEntity projectile, ItemStack stack, Entity owner, BlockHitResult hitResult) {
			Level world = projectile.level();
			BlockPos hitPos = hitResult.getBlockPos();
			
			hitResult.withDirection(hitResult.getDirection());
			Direction facing = hitResult.getDirection().getOpposite();
			BlockPos placementPos = hitPos.relative(facing.getOpposite());
			Direction placementDirection = world.isEmptyBlock(placementPos.below()) ? facing : Direction.UP;
			
			if (!GenericClaimModsCompat.canPlaceBlock(world, placementPos, owner)) {
				return stack;
			}
			
			try {
				if (stack.getItem() instanceof BlockItem blockItem) {
					blockItem.place(new ItemProjectileBehavior.ItemProjectilePlacementContext(world, projectile, hitResult));
				} else {
					stack.useOn(new DirectionalPlaceContext(world, placementPos, facing, stack, placementDirection));
				}
			} catch (Exception e) {
				SpectrumCommon.logError("Item Projectile failed to use item " + stack.getItem().getDescription());
				e.printStackTrace();
			}
			
			return stack;
		}
	}
	
	abstract class Damaging implements ItemProjectileBehavior {
		@Override
		public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, Entity owner, EntityHitResult hitResult) {
			Entity target = hitResult.getEntity();
			
			if (owner instanceof LivingEntity livingOwner) {
				livingOwner.setLastHurtMob(target);
			}
			
			if (dealDamage(projectile, owner, target)) {
				int targetFireTicks = target.getRemainingFireTicks();
				if (projectile.isOnFire()) {
					target.setRemainingFireTicks(targetFireTicks);
				}
				
				if (target instanceof LivingEntity livingTarget) {
					if (owner.level() instanceof ServerLevel serverWorld) {
						EnchantmentHelper.doPostAttackEffectsWithItemSource(serverWorld, target, livingTarget.getLastDamageSource(), stack);
					}
					if (target != owner && target instanceof Player && owner instanceof ServerPlayer serverPlayerOwner && !projectile.isSilent()) {
						serverPlayerOwner.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
					}
					projectile.playSound(SpectrumSoundEvents.BLOCK_CITRINE_CLUSTER_HIT, 1.0F, 1.2F / (projectile.level().getRandom().nextFloat() * 0.2F + 0.9F));
				}
			}
			
			if (destroyItemOnHit()) {
				stack.shrink(1);
			}
			return stack;
		}
		
		public abstract boolean destroyItemOnHit();
		
		public abstract boolean dealDamage(ThrowableItemProjectile projectile, Entity owner, Entity target);
		
		@Override
		public ItemStack onBlockHit(ItemProjectileEntity projectile, ItemStack stack, Entity owner, BlockHitResult hitResult) {
			if (destroyItemOnHit()) {
				stack.shrink(1);
			}
			return stack;
		}
	}
	
	class ItemProjectilePlacementContext extends BlockPlaceContext {
		
		ItemProjectileEntity itemProjectileEntity;
		
		public ItemProjectilePlacementContext(Level world, ItemProjectileEntity itemProjectileEntity, BlockHitResult blockHitResult) {
			super(world, null, InteractionHand.MAIN_HAND, itemProjectileEntity.getItem(), blockHitResult);
			this.itemProjectileEntity = itemProjectileEntity;
		}
		
		@Override
		public Direction getNearestLookingDirection() {
			return Direction.getFacingAxis(itemProjectileEntity, Direction.Axis.Y);
		}
		
		@Override
		public Direction getNearestLookingVerticalDirection() {
			return itemProjectileEntity.getViewXRot(1.0F) < 0.0F ? Direction.UP : Direction.DOWN;
		}
		
		@Override
		public Direction[] getNearestLookingDirections() {
			Direction[] directions = Direction.orderedByNearest(itemProjectileEntity);
			if (!this.replaceClicked) {
				Direction direction = this.getClickedFace();
				
				int i;
				for (i = 0; i < directions.length && directions[i] != direction.getOpposite();)
					i++;
				
				if (i > 0) {
					System.arraycopy(directions, 0, directions, 1, i);
					directions[0] = direction.getOpposite();
				}
			}
			return directions;
		}
		
	}
	
}
