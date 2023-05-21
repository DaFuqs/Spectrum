package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.progression.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.world.*;

public interface GravitableItem {
	
	float getGravityModInInventory();
	
	double getGravityModForItemEntity();
	
	/**
	 * Makes entities lighter / heavier, depending on the gravity effect of the item stack
	 *
	 * @param stack
	 * @param world
	 * @param entity
	 * @return The additional Y Velocity that was applied
	 */
	default double applyGravityEffect(ItemStack stack, World world, Entity entity) {
		if (world != null && entity != null) {
			// don't affect creative/spectators/... players or immune boss mobs
			if (entity.isPushable() && !(entity).isSpectator()) {
				if (entity instanceof PlayerEntity && ((PlayerEntity) entity).isCreative()) {
					return 0;
				} else {
					double additionalYVelocity = Math.log(stack.getCount()) * getGravityModInInventory();
					entity.addVelocity(0, additionalYVelocity, 0);
					
					// if falling very slowly => no fall damage
					if (additionalYVelocity > 0 && entity.getVelocity().y > -0.4) {
						entity.fallDistance = 0;
					}
					
					if (world.getTime() % 20 == 0 && entity instanceof ServerPlayerEntity serverPlayerEntity) {
						GravityAdvancementsManager.processAppliedGravityForAdvancements(serverPlayerEntity, additionalYVelocity);
					}
					
					return additionalYVelocity;
				}
			}
		}
		return 0;
	}
	
}
