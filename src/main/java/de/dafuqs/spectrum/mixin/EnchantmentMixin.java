package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.enchanter.*;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.HashSet;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
	
	@Inject(method = "isAcceptableItem(Lnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
	public void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.getItem() instanceof ExtendedEnchantable extendedEnchantable) {

			var set = new HashSet<Enchantment>();
			extendedEnchantable.appendAcceptedEnchants(set);
			var val = set.contains((Enchantment) (Object) this);

			if (extendedEnchantable.soft() && !val) {
				val = ((Enchantment) (Object) this).type.isAcceptableItem(stack.getItem());
			}

			cir.setReturnValue(val);
		}
	}
	
}
