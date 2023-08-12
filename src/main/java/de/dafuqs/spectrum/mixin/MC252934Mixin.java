package de.dafuqs.spectrum.mixin;

import net.minecraft.entity.decoration.*;
import net.minecraft.nbt.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(AbstractDecorationEntity.class)
public abstract class MC252934Mixin {
	
	@Inject(method = "readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;)V"), cancellable = true)
	private void spectrum$fixMC252934(NbtCompound nbt, CallbackInfo ci) {
		ci.cancel();
	}
	
}
