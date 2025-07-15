package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.items.magic_items.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(TemptGoal.class)
public class TemptGoalMixin {
	
	@Inject(at = @At("HEAD"), method = "shouldFollow", cancellable = true)
	private void isTemptedBy(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		if (entity.isUsingItem() && entity.getUseItem().getItem() instanceof NaturesStaffItem) {
			cir.setReturnValue(true);
		}
	}
	
}
