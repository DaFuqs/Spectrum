package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.SpectrumItemTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {
	
	@Inject(at = @At("HEAD"), method = "wearsGoldArmor", cancellable = true)
	private static void spectrum$piglinSafeEquipment(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		for (ItemStack itemStack : entity.getArmorItems()) {
			if (itemStack.isIn(SpectrumItemTags.PIGLIN_SAFE_EQUIPMENT)) {
				cir.setReturnValue(true);
				break;
			}
		}
	}
	
}