package de.dafuqs.spectrum.api.interaction;

import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.thrown.*;
import net.minecraft.item.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.predicate.item.*;
import net.minecraft.registry.tag.*;
import net.minecraft.server.network.*;
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

	static @Nullable ItemProjectileBehavior get(ItemStack stack) {
		for(Pair<ItemPredicate, ItemProjectileBehavior> entry : BEHAVIORS) {
			if(entry.getLeft().test(stack)) {
				return entry.getRight();
			}
		}
		return DEFAULT;
	}

	boolean onEntityHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, EntityHitResult hitResult);
	boolean onBlockHit(ItemProjectileEntity projectile, ItemStack stack, @Nullable Entity owner, BlockHitResult hitResult);


	static ItemProjectileBehavior damaging(float damage, boolean destroyItemOnHit) {
		return new Damaging() {
			@Override
			public boolean shouldDestroyItemOnHit() {
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
		public boolean onEntityHit(ItemProjectileEntity projectile, ItemStack stack, Entity owner, EntityHitResult hitResult) {
			Entity target = hitResult.getEntity();
			// Lots of fun(tm) is to be had
			if(target instanceof LivingEntity livingTarget) {
				// attaching name tags, saddle horses, memorize entities...
				if(owner instanceof PlayerEntity playerOwner && stack.useOnEntity(playerOwner, livingTarget, Hand.MAIN_HAND).isAccepted()) {
					return false;
				}

				// Force-feeds food, applies potions, ...
				stack.getItem().finishUsing(stack, livingTarget.getWorld(), livingTarget);
				return false;
			}

			return false;
		}

		@Override
		public boolean onBlockHit(ItemProjectileEntity projectile, ItemStack stack, Entity owner, BlockHitResult hitResult) {
			if(owner instanceof PlayerEntity playerOwner) {
				stack.useOnBlock(new ItemPlacementContext(new ItemUsageContext(playerOwner, Hand.MAIN_HAND, hitResult)));
			} else {
				World world = projectile.getWorld();
				BlockPos hitPos = hitResult.getBlockPos();

				hitResult.withSide(hitResult.getSide());
				Direction facing = hitResult.getSide().getOpposite();
				BlockPos placementPos = hitPos.offset(facing);
				Direction placementDirection = world.isAir(placementPos.down()) ? facing : Direction.UP;
				stack.useOnBlock(new AutomaticItemPlacementContext(world, placementPos, facing, stack, placementDirection));
			}

			return false;
		}
	}

	abstract class Damaging implements ItemProjectileBehavior {
		@Override
		public boolean onEntityHit(ItemProjectileEntity projectile, ItemStack stack, Entity owner, EntityHitResult hitResult) {
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

			return shouldDestroyItemOnHit();
		}

		public abstract boolean shouldDestroyItemOnHit();
		public abstract boolean dealDamage(ThrownItemEntity projectile, Entity owner, Entity target);

		@Override
		public boolean onBlockHit(ItemProjectileEntity projectile, ItemStack stack, Entity owner, BlockHitResult hitResult) {
			return false;
		}
	}

}
