package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.interfaces.GravitableItem;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseBaseEntity.class)
public class HorseBaseEntityMixin {
	
	@Shadow
	protected SimpleInventory items;
	
	@Inject(at = @At("HEAD"), method = "tick()V")
	public void tick(CallbackInfo callbackInfo) {
		if ((Object) this instanceof AbstractDonkeyEntity && ((AbstractDonkeyEntity) (Object) this).world instanceof ServerWorld) {
			AbstractDonkeyEntity thisEntity = (AbstractDonkeyEntity) (Object) this;
			
			if (thisEntity.hasChest()) {
				SimpleInventory var1 = this.items;
				
				double addedGravity = 0;
				for (int i = 0; i < var1.size(); i++) {
					ItemStack itemStack = var1.getStack(i);
					if (!itemStack.isEmpty() && (itemStack.getItem() instanceof GravitableItem)) {
						addedGravity += ((GravitableItem) itemStack.getItem()).applyGravityEffect(itemStack, thisEntity.getEntityWorld(), thisEntity);
					}
				}
				
				// about 3.1 stacks of paletur fragments will send an animal flying
				// => trigger a hidden advancement
				if (addedGravity > 0.081 && thisEntity.world.getTime() % 20 == 0) {
					PlayerEntity ownerPlayerEntity = PlayerOwned.getPlayerEntityIfOnline(thisEntity.world, thisEntity.getOwnerUuid());
					if (ownerPlayerEntity != null) {
						Support.grantAdvancementCriterion((ServerPlayerEntity) ownerPlayerEntity, "lategame/put_too_many_low_gravity_blocks_into_animal", "gravity");
					}
					
					// take damage when at height heights
					// otherwise the animal would just be floating forever
					if (thisEntity.getPos().y > thisEntity.getEntityWorld().getHeight() + 1000) {
						thisEntity.damage(DamageSource.OUT_OF_WORLD, 10);
					}
				}
			}
		}
	}
	
	
}
