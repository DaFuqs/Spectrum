package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.api.item.*;
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
			if (inventoryStack.getItem() instanceof InventoryInsertionAcceptor inventoryInsertionAcceptor) {
				if (inventoryInsertionAcceptor.acceptsItemStack(inventoryStack, stack)) {
					int remainingCount = inventoryInsertionAcceptor.acceptItemStack(inventoryStack, stack, playerInventory.player);
					stack.setCount(remainingCount);
					if (remainingCount == 0) {
						callbackInfoReturnable.cancel();
						break;
					}
				}
			}
		}
	}
	
	@Inject(at = @At("HEAD"), method = "offer(Lnet/minecraft/item/ItemStack;Z)V", cancellable = true)
	private void offer(ItemStack stack, boolean notifiesClient, CallbackInfo ci) {
		PlayerInventory playerInventory = (PlayerInventory) (Object) this;
		
		for (int i = 0; i < playerInventory.size(); i++) {
			ItemStack inventoryStack = playerInventory.getStack(i);
			if (inventoryStack.getItem() instanceof InventoryInsertionAcceptor inventoryInsertionAcceptor) {
				if (inventoryInsertionAcceptor.acceptsItemStack(inventoryStack, stack)) {
					int remainingCount = inventoryInsertionAcceptor.acceptItemStack(inventoryStack, stack, playerInventory.player);
					stack.setCount(remainingCount);
					if (remainingCount == 0) {
						ci.cancel();
						break;
					}
				}
			}
		}
	}
	
}
