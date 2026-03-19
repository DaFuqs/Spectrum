package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.capabilities.*;
import net.neoforged.neoforge.items.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Inventory.class)
public abstract class PlayerInventoryMixin {
	
	@Inject(at = @At("HEAD"), method = "add(Lnet/minecraft/world/item/ItemStack;)Z", cancellable = true)
	private void addStack(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (!stack.isEmpty()) {
			Inventory playerInventory = (Inventory) (Object) this;
			spectrum$tryAddToItemStorages(stack, playerInventory);
			if(stack.isEmpty()) {
				cir.setReturnValue(true);
			}
		}
	}
	
	@Inject(at = @At("HEAD"), method = "placeItemBackInInventory(Lnet/minecraft/world/item/ItemStack;Z)V", cancellable = true)
	private void offer(ItemStack stack, boolean notifiesClient, CallbackInfo ci) {
		if(!stack.isEmpty()) {
			Inventory playerInventory = (Inventory) (Object) this;
			spectrum$tryAddToItemStorages(stack, playerInventory);
			if(stack.isEmpty()) {
				ci.cancel();
			}
		}
	}
	
	@Unique
	private static void spectrum$tryAddToItemStorages(ItemStack stackToAdd, Inventory playerInventory) {
		for (int i = 0; i < playerInventory.getContainerSize(); i++) {
			ItemStack inventoryStack = playerInventory.getItem(i);
			if(!inventoryStack.is(SpectrumItemTags.STORES_ITEMS_ADDED_TO_INVENTORY)) {
				continue;
			}
			
			@Nullable IItemHandler itemHandler = inventoryStack.getCapability(Capabilities.ItemHandler.ITEM);
			if(itemHandler != null) {
				if(!ItemStack.isSameItemSameComponents(stackToAdd, itemHandler.getStackInSlot(0))) {
					continue;
				}
				if(!itemHandler.isItemValid(0, stackToAdd)) {
					continue;
				}
				ItemStack remainder = itemHandler.insertItem(i, stackToAdd, false);
				stackToAdd.setCount(remainder.getCount());
				if (remainder.isEmpty()) {
					return;
				}
			}
		}
	}
	
}
