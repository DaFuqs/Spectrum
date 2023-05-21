package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.items.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
	
	@Inject(at = @At("HEAD"), method = "addStack(Lnet/minecraft/item/ItemStack;)I", cancellable = true)
	private void addStack(ItemStack stack, CallbackInfoReturnable<Integer> callbackInfoReturnable) {
		PlayerInventory playerInventory = (PlayerInventory) (Object) this;
		
		for (int i = 0; i < playerInventory.size(); i++) {
			ItemStack inventoryStack = playerInventory.getStack(i);
			if (inventoryStack.getItem() instanceof InventoryInsertionAcceptor) {
				if (((InventoryInsertionAcceptor) inventoryStack.getItem()).acceptsItemStack(inventoryStack, stack)) {
					int remainingCount = ((InventoryInsertionAcceptor) inventoryStack.getItem()).acceptItemStack(inventoryStack, stack, playerInventory.player);
					stack.setCount(remainingCount);
					if (remainingCount == 0) {
						callbackInfoReturnable.cancel();
						break;
					}
				}
			}
		}
	}
	
}
