package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {
	
	@Inject(method = "getSpeed(Lnet/minecraft/item/ItemStack;)F", at = @At("RETURN"), cancellable = true)
	private static void getSpeed(ItemStack stack, CallbackInfoReturnable<Float> cir) {
		int sniperLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.SNIPER, stack);
		if (sniperLevel > 0) {
			cir.setReturnValue(cir.getReturnValue() + 1.0F * sniperLevel);
		}
	}
	
}
