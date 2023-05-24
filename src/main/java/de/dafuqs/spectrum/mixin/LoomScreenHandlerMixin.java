package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.items.LoomPatternProvider;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LoomScreenHandler.class)
public abstract class LoomScreenHandlerMixin extends ScreenHandler {
	
	@Shadow
	@Final
	private Slot patternSlot;
	
	private LoomScreenHandlerMixin() {
		super(null, 0);
	}
	
	@Inject(method = "getPatternsFor(Lnet/minecraft/item/ItemStack;)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
	private void spectrum$getPatternsFor(ItemStack stack, CallbackInfoReturnable<List<RegistryEntry<BannerPattern>>> cir) {
		if (stack.getItem() instanceof LoomPatternProvider loomPatternProvider) {
			cir.setReturnValue(loomPatternProvider.getPatterns());
		}
	}
	
	@Inject(
			method = "quickMove",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;",
					ordinal = 0,
					shift = At.Shift.BEFORE
			),
			cancellable = true
	)
	private void attemptBppPatternItemTransfer(PlayerEntity player, int slotIdx, CallbackInfoReturnable<ItemStack> info) {
		ItemStack stack = this.slots.get(slotIdx).getStack();
		
		if (stack.getItem() instanceof LoomPatternProvider) {
			if (!this.insertItem(stack, this.patternSlot.id, this.patternSlot.id + 1, false)) {
				info.setReturnValue(ItemStack.EMPTY);
			}
		}
	}
	
}
