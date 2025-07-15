package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.component.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(PotionBrewing.class)
public abstract class BrewingRecipeRegistryMixin {
	
	@Inject(method = "isIngredient", at = @At("HEAD"), cancellable = true)
	private void spectrum$disallowPigmentPotionBrewing(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		PotionContents potionContentsComponent = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
		if (potionContentsComponent.potion().isPresent() && potionContentsComponent.potion().get().equals(SpectrumPotions.PIGMENT_POTION)) {
			cir.setReturnValue(false);
		}
	}
	
}
