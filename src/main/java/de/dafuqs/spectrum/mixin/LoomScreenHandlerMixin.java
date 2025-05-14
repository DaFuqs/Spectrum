package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.api.item.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(LoomMenu.class)
public abstract class LoomScreenHandlerMixin extends AbstractContainerMenu {
	
	@Shadow
	@Final
	private Slot patternSlot;

	@Shadow
	@Final
	private HolderGetter<BannerPattern> patternGetter;
	
	private LoomScreenHandlerMixin() {
		super(null, 0);
	}
	
	@Inject(method = "getSelectablePatterns(Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
	private void spectrum$getPatternsFor(ItemStack stack, CallbackInfoReturnable<List<Holder<BannerPattern>>> cir) {
		if (stack.getItem() instanceof LoomPatternProvider loomPatternProvider) {
			cir.setReturnValue(LoomPatternProvider.getPatterns(patternGetter, loomPatternProvider));
		}
	}
	
	@Inject(
			method = "quickMoveStack",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;",
					ordinal = 0,
					shift = At.Shift.BEFORE
			),
			cancellable = true
	)
	private void attemptPatternItemTransfer(Player player, int slotIdx, CallbackInfoReturnable<ItemStack> info) {
		ItemStack stack = this.slots.get(slotIdx).getItem();
		
		if (stack.getItem() instanceof LoomPatternProvider) {
			if (!this.moveItemStackTo(stack, this.patternSlot.index, this.patternSlot.index + 1, false)) {
				info.setReturnValue(ItemStack.EMPTY);
			}
		}
	}
	
}
