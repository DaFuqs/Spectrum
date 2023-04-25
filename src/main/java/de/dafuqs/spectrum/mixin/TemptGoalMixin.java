package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.items.magic_items.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(TemptGoal.class)
public class TemptGoalMixin {
	
	@Inject(at = @At("HEAD"), method = "isTemptedBy(Lnet/minecraft/entity/LivingEntity;)Z", cancellable = true)
	private void isTemptedBy(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		if (entity.isUsingItem() && entity.getActiveItem().getItem() instanceof HerdingStaffItem) {
			cir.setReturnValue(true);
		}
	}
	
}
