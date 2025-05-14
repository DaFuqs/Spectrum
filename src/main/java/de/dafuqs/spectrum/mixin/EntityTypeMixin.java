package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.entity.*;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin {
	
	@Inject(at = @At("TAIL"), method = "trackDeltas", cancellable = true)
	public void alwaysUpdateVelocity(CallbackInfoReturnable<Boolean> cir) {
		Object thisObject = this;
		if (thisObject == SpectrumEntityTypes.PHANTOM_FRAME || thisObject == SpectrumEntityTypes.GLOW_PHANTOM_FRAME || thisObject == SpectrumEntityTypes.KINDLING_COUGH) {
			cir.setReturnValue(false);
		}
	}
	
}
