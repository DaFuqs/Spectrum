package de.dafuqs.spectrum.mixin;

import net.minecraft.block.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(MobSpawnerBlockEntity.class)
public abstract class MobSpawnerBlockEntityMixin {
	
	@Inject(method = "copyItemDataRequiresOperator", at = @At("HEAD"), cancellable = true)
	public void allowPlacingSpawnerWithBlockData(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		callbackInfoReturnable.setReturnValue(false);
	}
	
}