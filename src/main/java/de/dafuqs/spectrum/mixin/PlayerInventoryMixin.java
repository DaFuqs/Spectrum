package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.api.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Inventory.class)
public abstract class PlayerInventoryMixin {
	
	@Inject(at = @At("HEAD"), method = "add(Lnet/minecraft/world/item/ItemStack;)Z", cancellable = true)
	private void addStack(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		Inventory playerInventory = (Inventory) (Object) this;
		
		for (int i = 0; i < playerInventory.getContainerSize(); i++) {
			ItemStack inventoryStack = playerInventory.getItem(i);
			if (inventoryStack.getItem() instanceof InventoryInsertionAcceptor inventoryInsertionAcceptor) {
				if (inventoryInsertionAcceptor.acceptsItemStack(inventoryStack, stack)) {
					int remainingCount = inventoryInsertionAcceptor.acceptItemStack(inventoryStack, stack, playerInventory.player);
					stack.setCount(remainingCount);
					if (remainingCount == 0) {
						cir.cancel();
						break;
					}
				}
			}
		}
	}
	
	@Inject(at = @At("HEAD"), method = "placeItemBackInInventory(Lnet/minecraft/world/item/ItemStack;Z)V", cancellable = true)
	private void offer(ItemStack stack, boolean notifiesClient, CallbackInfo ci) {
		Inventory playerInventory = (Inventory) (Object) this;
		
		for (int i = 0; i < playerInventory.getContainerSize(); i++) {
			ItemStack inventoryStack = playerInventory.getItem(i);
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
