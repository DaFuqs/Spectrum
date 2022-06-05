package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.interfaces.InventoryInsertionAcceptor;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
	
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
