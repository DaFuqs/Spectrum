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

	@Inject(method = "wearsGoldArmor", at = @At("RETURN"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	private static void wearsGoldArmor(LivingEntity entity, @NotNull CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue()) {
			for (ItemStack itemStack : entity.getArmorItems()) {
				if (itemStack.isIn(SpectrumItemTags.PIGLIN_SAFE_EQUIPMENT)) {
					cir.setReturnValue(true);
				}
			}
		}
	}
	
}