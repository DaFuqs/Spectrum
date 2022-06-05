package de.dafuqs.spectrum.mixin;

import net.minecraft.block.entity.MobSpawnerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobSpawnerBlockEntity.class)
public abstract class MobSpawnerBlockEntityMixin {
	
	@Inject(method = "copyItemDataRequiresOperator", at = @At("HEAD"), cancellable = true)
	public void allowPlacingSpawnerWithBlockData(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		callbackInfoReturnable.setReturnValue(false);
	}
	
}