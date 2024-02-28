package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseEntityMixin {
	
	@Shadow
	protected SimpleInventory items;
	
	/**
	 * This pretty much implements inventoryTick() for Donkeys
	 * since inventoryTick() only triggers for player inventories
	 */
	@Inject(at = @At("HEAD"), method = "tick()V")
	public void tick(CallbackInfo callbackInfo) {
		if ((Object) this instanceof AbstractDonkeyEntity thisEntity
				&& !thisEntity.hasNoGravity()
				&& thisEntity.hasChest()
				&& thisEntity.getWorld() instanceof ServerWorld serverWorld) {
			
			double addedGravity = 0;
			for (ItemStack stack : this.items.stacks) {
				if (stack.getItem() instanceof GravitableItem gravitableItem) {
					addedGravity += gravitableItem.applyGravity(stack, serverWorld, thisEntity);
				}
			}
			
			// when the animal is sent flying trigger a hidden advancement
			if (addedGravity > 0.081 && serverWorld.getTime() % 20 == 0) {
				PlayerEntity owner = PlayerOwned.getPlayerEntityIfOnline(thisEntity.getOwnerUuid());
				if (owner != null) {
					Support.grantAdvancementCriterion((ServerPlayerEntity) owner, "lategame/put_too_many_low_gravity_blocks_into_animal", "gravity");
				}
				
				// take damage when at height heights
				// otherwise the animal would just be floating forever
				if (thisEntity.getPos().y > serverWorld.getHeight() + 1000) {
					thisEntity.damage(thisEntity.getDamageSources().outOfWorld(), 10);
				}
			}
		}
	}
	
}
