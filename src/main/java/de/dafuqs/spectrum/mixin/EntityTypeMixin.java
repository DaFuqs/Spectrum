package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.entity.*;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin {
	
	@Inject(at = @At("RETURN"), method = "alwaysUpdateVelocity()Z", cancellable = true)
	public void alwaysUpdateVelocity(CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue() && ((Object) this == SpectrumEntityTypes.PHANTOM_FRAME || (Object) this == SpectrumEntityTypes.GLOW_PHANTOM_FRAME)) {
			cir.setReturnValue(false);
		}
	}
	
}
