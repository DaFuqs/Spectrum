package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @Inject(at = @At("HEAD"), method= "getSpeed(Lnet/minecraft/item/ItemStack;)F", cancellable = true)
    private static void getSpeed(ItemStack stack, CallbackInfoReturnable<Float> cir) {
        int sniperLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.SNIPER, stack);
        if(sniperLevel > 0) {
            cir.setReturnValue(cir.getReturnValue() + 1.0F * sniperLevel);
        }
    }

}
