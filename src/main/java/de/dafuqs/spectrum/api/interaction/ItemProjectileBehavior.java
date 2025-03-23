package de.dafuqs.spectrum.api.interaction;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.thrown.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.predicate.item.*;
import net.minecraft.registry.tag.*;
import net.minecraft.server.network.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface ItemProjectileBehavior {
	
	List<Pair<ItemPredicate, ItemProjectileBehavior>> BEHAVIORS = new ArrayList<>();
	
	ItemProjectileBehavior DEFAULT = new Default();
	
	static void register(ItemProjectileBehavior behavior, ItemPredicate predicate) {
		BEHAVIORS.add(new Pair<>(predicate, behavior));
	}
	
	static void register(ItemProjectileBehavior behavior, Item... items) {
		BEHAVIORS.add(new Pair<>(ItemPredicate.Builder.create().items(items).build(), behavior));
	}
	
	static void register(ItemProjectileBehavior behavior, TagKey<Item> tag) {
		BEHAVIORS.add(new Pair<>(ItemPredicate.Builder.create().tag(tag).build(), behavior));
	}
	
	static ItemProjectileBehavior get(ItemStack stack) {
		for (Pair<ItemPredicate, ItemProjectileBehavior> entry : BEHAVIORS) {
			if (entry.getLeft().test(stack)) {
				return entry.getRight();
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
			public boolean dealDamage(ThrownItemEntity projectile, Entity owner, Entity target) {
				return target.damage(target.getDamageSources().thrown(projectile, owner), damage);
			}
		};
	}
	
	class Default implements ItemProjectileBehavior {
		
		@Override
		public ItemStack onEntityHit(ItemProjectileEntity projectile, ItemStack stack, Entity owner, EntityHitResult hitResult) {
			Entity target = hitResult.getEntity();
			
			if (target instanceof PlayerEntity player && (player.isCreative() || player.isSpectator())) {
				return stack;
			}
			
			// Lots of fun(tm) is to be had
			if (stack.isIn(ItemTags.CREEPER_IGNITERS) && target instanceof CreeperEntity creeperEntity) {
				World world = projectile.getWorld();
				SoundEvent soundEvent = stack.isOf(Items.FIRE_CHARGE) ? SoundEvents.ITEM_FIRECHARGE_USE : SoundEvents.ITEM_FLINTANDSTEEL_USE;
				world.playSound(null, projectile.getX(), projectile.getY(), projectile.getZ(), soundEvent, projectile.getSoundCategory(), 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
				creeperEntity.ignite();
				
				if (stack.isDamageable()) {
					stack.damage(1, world.getRandom(), null);
				} else {
					NbtCompound nbtCompound = stack.getNbt();
					boolean isUnbreakable = nbtCompound != null && nbtCompound.getBoolean("Unbreakable");
					if (!isUnbreakable) {
						stack.decrement(1); // In Vanilla unbreakable Flint & Steel is not handled correctly and therefore consumed. This here probably still does not handle every modded item perfectly
					}
				}
			}
			
			if (target instanceof LivingEntity livingTarget) {
				if (target instanceof PlayerEntity && !SpectrumCommon.CONFIG.OmniAcceleratorPvP) {
					if (stack.isIn(SpectrumItemTags.REQUIRES_OMNI_ACCELERATOR_PVP_ENABLED)) {
						return stack;
					}
				}
				
				// attaching name tags, saddle horses, memorize entities...
				if (owner instanceof PlayerEntity playerOwner && stack.useOnEntity(playerOwner, livingTarget, Hand.MAIN_HAND).isAccepted()) {
					return stack;
				}
				
				// Force-feeds food, applies potions, ...
				stack.getItem().finishUsing(stack, livingTarget.getWorld(), livingTarget);
			}
			return stack;
		}
		
		@Override
		public ItemStack onBlockHit(ItemProjectileEntity projectile, ItemStack stack, Entity owner, BlockHitResult hitResult) {
			World world = projectile.getWorld();
			BlockPos hitPos = hitResult.getBlockPos();
			
			hitResult.withSide(hitResult.getSide());
			Direction facing = hitResult.getSide().getOpposite();
			BlockPos placementPos = hitPos.offset(facing.getOpposite());
			Direction placementDirection = world.isAir(placementPos.down()) ? facing : Direction.UP;
			
			if (!GenericClaimModsCompat.canPlaceBlock(world, placementPos, owner)) {
				return stack;
			}
			
			try {
				if (stack.getItem() instanceof BlockItem blockItem) {
					blockItem.place(new ItemProjectileBehavior.ItemProjectilePlacementContext(world, projectile, hitResult));
				} else {
					stack.useOnBlock(new AutomaticItemPlacementContext(world, placementPos, facing, stack, placementDirection));
				}
			} catch (Exception e) {
				SpectrumCommon.logError("Item Projectile failed to use item " + stack.getItem().getName());
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
				livingOwner.onAttacking(target);
			}
			
			if (dealDamage(projectile, owner, target)) {
				int targetFireTicks = target.getFireTicks();
				if (projectile.isOnFire()) {
					target.setFireTicks(targetFireTicks);
				}
				
				if (target instanceof LivingEntity livingTarget) {
					if (!target.getWorld().isClient() && owner instanceof LivingEntity livingOwner) {
						EnchantmentHelper.onUserDamaged(livingTarget, livingOwner);
						EnchantmentHelper.onTargetDamaged(livingOwner, target);
					}
					if (target != owner && target instanceof PlayerEntity && owner instanceof ServerPlayerEntity serverPlayerOwner && !projectile.isSilent()) {
						serverPlayerOwner.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, 0.0F));
					}
					projectile.playSound(SpectrumSoundEvents.BLOCK_CITRINE_CLUSTER_HIT, 1.0F, 1.2F / (projectile.getWorld().getRandom().nextFloat() * 0.2F + 0.9F));
				}
			}
			
			if (destroyItemOnHit()) {
				stack.decrement(1);
			}
			return stack;
		}
		
		public abstract boolean destroyItemOnHit();
		
		public abstract boolean dealDamage(ThrownItemEntity projectile, Entity owner, Entity target);
		
		@Override
		public ItemStack onBlockHit(ItemProjectileEntity projectile, ItemStack stack, Entity owner, BlockHitResult hitResult) {
			if (destroyItemOnHit()) {
				stack.decrement(1);
			}
			return stack;
		}
	}
	
	class ItemProjectilePlacementContext extends ItemPlacementContext {
		
		ItemProjectileEntity itemProjectileEntity;
		
		public ItemProjectilePlacementContext(World world, ItemProjectileEntity itemProjectileEntity, BlockHitResult blockHitResult) {
			super(world, null, Hand.MAIN_HAND, itemProjectileEntity.getStack(), blockHitResult);
			this.itemProjectileEntity = itemProjectileEntity;
		}
		
		@Override
		public Direction getPlayerLookDirection() {
			return Direction.getLookDirectionForAxis(itemProjectileEntity, Direction.Axis.Y);
		}
		
		@Override
		public Direction getVerticalPlayerLookDirection() {
			return itemProjectileEntity.getPitch(1.0F) < 0.0F ? Direction.UP : Direction.DOWN;
		}
		
		@Override
		public Direction[] getPlacementDirections() {
			Direction[] directions = Direction.getEntityFacingOrder(itemProjectileEntity);
			if (this.canReplaceExisting) {
				return directions;
			} else {
				Direction direction = this.getSide();
				
				int i;
				for (i = 0; i < directions.length && directions[i] != direction.getOpposite(); ++i) {
				}
				
				if (i > 0) {
					System.arraycopy(directions, 0, directions, 1, i);
					directions[0] = direction.getOpposite();
				}
				
				return directions;
			}
		}
		
	}
	
}
