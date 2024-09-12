package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(HoneyBottleItem.class)
public class HoneyBottleItemMixin {

	@Inject(method = "finishUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;removeStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z"))
	private void spectrum$cureDeadlyPoison(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
		user.removeStatusEffect(SpectrumStatusEffects.DEADLY_POISON);
	}
}
