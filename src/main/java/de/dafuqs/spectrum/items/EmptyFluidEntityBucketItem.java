package de.dafuqs.spectrum.items;

import net.minecraft.advancements.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.stats.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.*;

/**
 * An EntityBucketItem for entities with Fluids.EMPTY.
 */
public class EmptyFluidEntityBucketItem extends MobBucketItem {
	
	public EmptyFluidEntityBucketItem(EntityType<?> type, Fluid fluid, SoundEvent emptyingSound, Properties settings) {
		super(type, fluid, emptyingSound, settings);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack itemStack = user.getItemInHand(hand);
		BlockHitResult blockHitResult = getPlayerPOVHitResult(world, user, ClipContext.Fluid.SOURCE_ONLY);
		if (blockHitResult.getType() == HitResult.Type.MISS) {
			return InteractionResultHolder.pass(itemStack);
		} else if (blockHitResult.getType() != HitResult.Type.BLOCK) {
			return InteractionResultHolder.pass(itemStack);
		} else {
			BlockPos blockPos = blockHitResult.getBlockPos();
			Direction direction = blockHitResult.getDirection();
			BlockPos blockPos2 = blockPos.relative(direction);
			if (world.mayInteract(user, blockPos) && user.mayUseItemAt(blockPos2, direction, itemStack)) {
				this.checkExtraContent(user, world, itemStack, blockPos2);
				if (user instanceof ServerPlayer) {
					CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) user, blockPos2, itemStack);
				}
				
				user.awardStat(Stats.ITEM_USED.get(this));
				return InteractionResultHolder.sidedSuccess(getEmptySuccessItem(itemStack, user), world.isClientSide());
			} else {
				return InteractionResultHolder.fail(itemStack);
			}
		}
	}
	
}
