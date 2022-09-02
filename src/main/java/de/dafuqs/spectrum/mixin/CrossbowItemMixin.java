package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.items.tools.SpectrumCrossbowItem;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
	
	@Inject(method = "getSpeed(Lnet/minecraft/item/ItemStack;)F", at = @At("RETURN"), cancellable = true)
	private static void getSpeed(ItemStack stack, CallbackInfoReturnable<Float> cir) {
		int sniperLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.SNIPER, stack);
		float speedMod = 1.0F;
		if(stack.getItem() instanceof SpectrumCrossbowItem spectrumCrossbowItem) {
			speedMod = spectrumCrossbowItem.getProjectileVelocityModifier();
		}
		
		if (sniperLevel > 0 && speedMod != 1.0) {
			cir.setReturnValue((float) Math.ceil(speedMod * (cir.getReturnValue() + 1.0F * sniperLevel)));
		}
	}
	
	@Inject(method = "getPullTime(Lnet/minecraft/item/ItemStack;)I", at = @At("RETURN"), cancellable = true)
	private static void getPullTime(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (stack.getItem() instanceof SpectrumCrossbowItem spectrumCrossbowItem) {
			cir.setReturnValue((int) Math.ceil(cir.getReturnValueI() * spectrumCrossbowItem.getPullTimeModifier()));
		}
	}
	
}
