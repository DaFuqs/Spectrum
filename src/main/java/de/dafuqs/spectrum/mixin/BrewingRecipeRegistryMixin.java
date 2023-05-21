package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.recipe.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(BrewingRecipeRegistry.class)
public abstract class BrewingRecipeRegistryMixin {
	
	@Inject(method = "hasRecipe(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
	private static void spectrum$disallowPigmentPotionInBrewingStand(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
		Potion potion = PotionUtil.getPotion(input);
		if (potion == SpectrumPotions.PIGMENT_POTION) {
			cir.setReturnValue(false);
		}
	}
	
}
