package de.dafuqs.spectrum.api.item;

import de.dafuqs.spectrum.progression.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.world.*;

public interface GravitableItem {
	
	float getGravityMod();
	
	/**
	 * This one is for LivingEntities, like players
	 * Makes entities lighter / heavier, depending on the gravity effect of the item stack
	 *
	 * @return The additional Y Velocity that was applied
	 */
	default double applyGravity(ItemStack stack, World world, Entity entity) {
		if (world != null && entity != null) {
			// don't affect creative/spectators/... players or immune boss mobs
			if (entity.isPushable() && !entity.hasNoGravity() && !entity.isSpectator()) {
				if (entity instanceof PlayerEntity player && player.isCreative()) {
					return 0;
				}
				
				double additionalYVelocity = getGravityMod() * stack.getCount();
				entity.addVelocity(0, additionalYVelocity, 0);
				
				// if falling very slowly => reset fall distance / damage
				if (additionalYVelocity > 0 && entity.getVelocity().y > -0.4) {
					entity.fallDistance = 0;
				}
				
				if (world.getTime() % 20 == 0 && entity instanceof ServerPlayerEntity serverPlayerEntity) {
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
	default void applyGravity(ItemStack stack, World world, ItemEntity itemEntity) {
		if (itemEntity.hasNoGravity()) {
			return;
		}
		
		if (itemEntity.getPos().getY() > world.getTopY() + 200) {
			itemEntity.discard();
		} else {
			// since an ItemEntity is much lighter than a player, we can x10 the gravity effect
			// this is not affected by item entity stack count to make it more predictable
			itemEntity.addVelocity(0, getGravityMod() * 10, 0);
		}
	}
	
}
