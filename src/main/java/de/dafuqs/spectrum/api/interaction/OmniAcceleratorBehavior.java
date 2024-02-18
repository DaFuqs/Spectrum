package de.dafuqs.spectrum.api.interaction;

import de.dafuqs.spectrum.entity.entity.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface OmniAcceleratorBehavior {

	Map<Item, OmniAcceleratorBehavior> BEHAVIORS = new Object2ObjectOpenHashMap<>();

	OmniAcceleratorBehavior DEFAULT = new OmniAcceleratorBehavior() {

		@Override
		public boolean onEntityHit(ItemProjectileEntity itemProjectileEntity, ItemStack stack, Entity owner, EntityHitResult hitResult) {
			Entity target = hitResult.getEntity();
			// Force-feeds food, applies potions, ...
			// Lots of fun(tm) is to be had
			if(target instanceof LivingEntity livingEntity) {
				stack.getItem().finishUsing(stack, livingEntity.getWorld(), livingEntity);
				return true;
			}

			return false;
		}

		@Override
		public boolean onBlockHit(ItemProjectileEntity itemProjectileEntity, ItemStack stack, Entity owner, BlockHitResult hitResult) {
			// if the stack is a BlockItem, try to place it
			if(stack.getItem() instanceof BlockItem blockItem) {
				World world = itemProjectileEntity.getWorld();
				BlockPos hitPos = hitResult.getBlockPos();

				hitResult.withSide(hitResult.getSide());
				Direction facing = hitResult.getSide().getOpposite();
				BlockPos placementPos = hitPos.offset(facing);
				Direction placementDirection = world.isAir(placementPos.down()) ? facing : Direction.UP;

				ActionResult result = ActionResult.FAIL;
				try {
					if(owner instanceof PlayerEntity playerOwner) {
						result = blockItem.place(new ItemPlacementContext(new ItemUsageContext(playerOwner, Hand.MAIN_HAND, hitResult)));
					} else {
						result = blockItem.place(new AutomaticItemPlacementContext(world, placementPos, facing, stack, placementDirection));
					}
				} catch (Exception ignored) {
				}
				if(result.isAccepted()) {
					world.emitGameEvent(null, GameEvent.BLOCK_PLACE, placementPos);
					return true;
				}
			}

			return false;
		}
	};

	static void register(Item item, OmniAcceleratorBehavior behavior) {
		BEHAVIORS.put(item, behavior);
	}

	static @Nullable OmniAcceleratorBehavior get(Item item) {
		return BEHAVIORS.getOrDefault(item, DEFAULT);
	}

	boolean onEntityHit(ItemProjectileEntity itemProjectileEntity, ItemStack stack, Entity owner, EntityHitResult hitResult);
	boolean onBlockHit(ItemProjectileEntity itemProjectileEntity, ItemStack stack, Entity owner, BlockHitResult hitResult);
}
