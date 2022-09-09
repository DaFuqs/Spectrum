package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.entity.entity.SpectrumFishingBobberEntity;
import net.minecraft.entity.Entity;
import net.minecraft.predicate.entity.FishingHookPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingHookPredicate.class)
public class FishingHookPredicateMixin {
	
	@Inject(method = "test(Lnet/minecraft/entity/Entity;)Z", at = @At(value = "HEAD"), cancellable = true)
	public void spectrum$test(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		if ((Object) this != FishingHookPredicate.ANY && entity instanceof SpectrumFishingBobberEntity spectrumFishingBobberEntity && spectrumFishingBobberEntity.isInTheOpen()) {
			cir.setReturnValue(true);
		}
	}
	
}
