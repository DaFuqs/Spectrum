package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

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