package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.piglin.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(PiglinAi.class)
public abstract class PiglinBrainMixin {
	
	@Inject(at = @At("HEAD"), method = "isWearingGold", cancellable = true)
	private static void spectrum$piglinSafeEquipment(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		for (ItemStack itemStack : entity.getArmorSlots()) {
			if (itemStack.is(SpectrumItemTags.PIGLIN_SAFE_EQUIPMENT)) {
				cir.setReturnValue(true);
				break;
			}
		}
	}
	
}