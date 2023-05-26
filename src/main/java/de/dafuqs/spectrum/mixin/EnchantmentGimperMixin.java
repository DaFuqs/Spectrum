package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(EnchantmentHelper.class)
public class EnchantmentGimperMixin {

    @Inject(at = @At("RETURN"), method = "getLevel", cancellable = true)
    private static void nerfEfficiencyAndCrumbling(Enchantment enchantment, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        var effLevel = cir.getReturnValue();

        if (enchantment == Enchantments.EFFICIENCY && effLevel > 0) {
            var razingLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.RAZING, stack);
    
            if (razingLevel > 0) {
                cir.setReturnValue(Math.max(0, effLevel - 2));
            }
        }
    }
}
