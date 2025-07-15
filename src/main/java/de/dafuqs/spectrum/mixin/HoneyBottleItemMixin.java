package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(HoneyBottleItem.class)
public class HoneyBottleItemMixin {
	
	@Inject(method = "finishUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;removeEffect(Lnet/minecraft/core/Holder;)Z"))
	private void spectrum$cureDeadlyPoison(ItemStack stack, Level world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
		user.removeEffect(SpectrumStatusEffects.DEADLY_POISON);
	}
}
