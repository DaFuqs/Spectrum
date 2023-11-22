package de.dafuqs.spectrum.items;

import net.minecraft.advancement.criterion.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.sound.*;
import net.minecraft.stat.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

/**
 * An EntityBucketItem for entities with Fluids.EMPTY.
 */
public class EmptyFluidEntityBucketItem extends EntityBucketItem {
	
	public EmptyFluidEntityBucketItem(EntityType<?> type, Fluid fluid, SoundEvent emptyingSound, Settings settings) {
		super(type, fluid, emptyingSound, settings);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		BlockHitResult blockHitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
		if (blockHitResult.getType() == HitResult.Type.MISS) {
			return TypedActionResult.pass(itemStack);
		} else if (blockHitResult.getType() != HitResult.Type.BLOCK) {
			return TypedActionResult.pass(itemStack);
		} else {
			BlockPos blockPos = blockHitResult.getBlockPos();
			Direction direction = blockHitResult.getSide();
			BlockPos blockPos2 = blockPos.offset(direction);
			if (world.canPlayerModifyAt(user, blockPos) && user.canPlaceOn(blockPos2, direction, itemStack)) {
				this.onEmptied(user, world, itemStack, blockPos2);
				if (user instanceof ServerPlayerEntity) {
					Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) user, blockPos2, itemStack);
				}
				
				user.incrementStat(Stats.USED.getOrCreateStat(this));
				return TypedActionResult.success(getEmptiedStack(itemStack, user), world.isClient());
			} else {
				return TypedActionResult.fail(itemStack);
			}
		}
	}
	
}
