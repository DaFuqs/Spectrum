package de.dafuqs.spectrum.api.item;

import de.dafuqs.spectrum.progression.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

public interface GravitableItem {

	float getGravityMod(ItemStack stack);
	
	/**
	 * This one is for LivingEntities, like players
	 * Makes entities lighter / heavier, depending on the gravity effect of the item stack
	 *
	 * @return The additional Y Velocity that was applied
	 */
	default double applyGravity(ItemStack stack, Level world, Entity entity) {
		if (world != null && entity != null) {
			// don't affect creative/spectators/... players or immune boss mobs
			if (entity.isPushable() && !entity.isNoGravity() && !entity.isSpectator()) {
				if (entity instanceof Player player && player.isCreative()) {
					return 0;
				}

				double additionalYVelocity = getGravityMod(stack) * stack.getCount();
				entity.push(0, additionalYVelocity, 0);
				
				// if falling very slowly => reset fall distance / damage
				if (additionalYVelocity > 0 && entity.getDeltaMovement().y > -0.4) {
					entity.fallDistance = 0;
				}
				
				if (world.getGameTime() % 20 == 0 && entity instanceof ServerPlayer serverPlayerEntity) {
					GravityAdvancementsManager.processAppliedGravityForAdvancements(serverPlayerEntity, additionalYVelocity);
				}
				
				return additionalYVelocity;
			}
		}
		return 0;
	}
	
	/**
	 * This one is for ItemEntities
	 * Since an ItemEntity is much lighter than a player, we can x10 the gravity effect
	 */
	default void applyGravity(ItemStack stack, Level world, ItemEntity itemEntity) {
		if (itemEntity.isNoGravity()) {
			return;
		}
		
		if (itemEntity.position().y() > world.getMaxBuildHeight() + 200) {
			itemEntity.discard();
		} else {
			// since an ItemEntity is much lighter than a player, we can x10 the gravity effect
			// this is not affected by item entity stack count to make it more predictable
			itemEntity.push(0, getGravityMod(stack) * 10, 0);
		}
	}
	
}
