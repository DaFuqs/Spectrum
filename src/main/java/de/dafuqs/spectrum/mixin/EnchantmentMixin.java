package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.enchanter.*;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
	
	@Inject(method = "isAcceptableItem(Lnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
	public void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.getItem() instanceof ExtendedEnchantable extendedEnchantable) {
			cir.setReturnValue(extendedEnchantable.getAcceptedEnchantments().contains((Enchantment) (Object) this));
		}
	}
	
}
