package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(PassiveEntity.class)
public abstract class PassiveEntityMixin {
	
	@Shadow
	public abstract boolean isBaby();
	
	@Inject(at= @At("HEAD"), method= "setBreedingAge", cancellable = true)
	private void spectrum$preventGrowUp(int age, CallbackInfo ci)
	{
		var entity = (LivingEntity) (Object) this;
		if(this.isBaby() && entity.hasStatusEffect(SpectrumStatusEffects.SCARRED))
		{
			ci.cancel();
		}
	}
	
}
