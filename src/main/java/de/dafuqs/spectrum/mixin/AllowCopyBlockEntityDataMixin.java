package de.dafuqs.spectrum.mixin;

import net.minecraft.world.level.block.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin({SpawnerBlockEntity.class, SignBlockEntity.class})
public abstract class AllowCopyBlockEntityDataMixin {
	
	@Inject(method = "onlyOpCanSetNbt", at = @At("HEAD"), cancellable = true)
	public void allowPlacingSpawnerWithBlockData(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		callbackInfoReturnable.setReturnValue(false);
	}
	
}