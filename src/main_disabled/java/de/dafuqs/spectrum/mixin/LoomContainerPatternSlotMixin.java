package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.api.item.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(targets = "net.minecraft.world.inventory.LoomMenu$5")
public abstract class LoomContainerPatternSlotMixin extends Slot {
	private LoomContainerPatternSlotMixin() {
		super(null, 0, 0, 0);
	}
	
	@Inject(
			at = {@At("HEAD")},
			method = {"mayPlace"},
			cancellable = true
	)
	private void checkBppLoomPatternItem(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
		if (stack.getItem() instanceof LoomPatternProvider) {
			info.setReturnValue(true);
		}
	}
}
