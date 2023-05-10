package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentGimperMixin {

    @Inject(at = @At("RETURN"), method = "getLevel", cancellable = true)
    private static void nerfEfficiencyAndCrumbling(Enchantment enchantment, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        var effLevel = cir.getReturnValue();

        if (enchantment == Enchantments.EFFICIENCY && effLevel > 0) {
            var crumblingLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.CRUMBLING, stack);

            if (crumblingLevel > 0) {
                cir.setReturnValue(Math.max(0, effLevel - 2));
            }
        }
    }
}
