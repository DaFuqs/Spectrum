package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(PotionItem.class)
public abstract class PotionItemMixin {
	
	@ModifyReturnValue(method = "getUseDuration", at = @At("RETURN"))
	private int spectrum$modifyDrinkTime(int drinkTime, ItemStack stack) {
		CustomPotionDataComponent component = stack.get(SpectrumDataComponentTypes.CUSTOM_POTION_DATA);
		if (component != null) {
			int additionalDrinkDuration = component.additionalDrinkDuration();
			drinkTime += Math.max(4, drinkTime + additionalDrinkDuration);
		}
		return drinkTime;
	}

}
