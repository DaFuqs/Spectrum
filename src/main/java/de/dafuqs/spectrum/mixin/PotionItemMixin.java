package de.dafuqs.spectrum.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionItem.class)
public class PotionItemMixin {
	
	@Inject(method = "getMaxUseTime(Lnet/minecraft/item/ItemStack;)I", at = @At("RETURN"), cancellable = true)
	private void spectrum$applyFastDrink(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (cir.getReturnValue() > 4) {
			NbtCompound compound = stack.getNbt();
			if (compound != null && compound.contains("SpectrumFastDrinkable")) {
				cir.setReturnValue(4);
			}
		}
	}
	
}
