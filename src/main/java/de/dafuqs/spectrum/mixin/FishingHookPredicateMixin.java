package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.entity.entity.SpectrumFishingBobberEntity;
import net.minecraft.entity.Entity;
import net.minecraft.predicate.entity.FishingHookPredicate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingHookPredicate.class)
public abstract class FishingHookPredicateMixin {
	
	@Inject(method = "test(Lnet/minecraft/entity/Entity;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;)Z", at = @At(value = "HEAD"), cancellable = true)
	public void spectrum$test(Entity entity, ServerWorld world, Vec3d pos, CallbackInfoReturnable<Boolean> cir) {
		if (this != FishingHookPredicate.ANY && entity instanceof SpectrumFishingBobberEntity spectrumFishingBobberEntity && spectrumFishingBobberEntity.isInTheOpen()) {
			cir.setReturnValue(true);
		}
	}
	
}
