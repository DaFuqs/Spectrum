package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import de.dafuqs.spectrum.blocks.present.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin {
	@Shadow
	@Final
	private Player owner;
	
	@WrapOperation(
			method = "quickMoveStack",
			slice = @Slice(
					from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getEquipmentSlotForItem(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/EquipmentSlot;"),
					to = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;onQuickCraft(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V")
			),
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/InventoryMenu;moveItemStackTo(Lnet/minecraft/world/item/ItemStack;IIZ)Z")
	)
	private boolean spectrum$triggerOnCraftedBeforeQuickMove(InventoryMenu instance, ItemStack itemStack, int startIndex, int endIndex, boolean reverseDirection, Operation<Boolean> original) {
		if (itemStack.getItem() instanceof PresentBlockItem) {
			itemStack.getItem().onCraftedBy(itemStack, this.owner.level(), this.owner);
		}
		return original.call(instance, itemStack, startIndex, endIndex, reverseDirection);
	}
}
