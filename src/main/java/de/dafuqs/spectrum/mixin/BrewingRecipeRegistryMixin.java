package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.SpectrumPotions;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {
	
	@Inject(method = "hasRecipe(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
	private static void spectrum$disallowPigmentPotionInBrewingStand(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
		Potion potion = PotionUtil.getPotion(input);
		if(potion == SpectrumPotions.PIGMENT_POTION) {
			cir.setReturnValue(false);
		}
	}
	
}
