package de.dafuqs.spectrum.mixin;

import net.minecraft.world.entity.decoration.*;
import org.slf4j.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(BlockAttachedEntity.class)
public abstract class MC252934Mixin {
	
	@Redirect(method = "readAdditionalSaveData", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;)V", remap = false))
	private void spectrum$fixMC252934(Logger thisLogger, String format, Object arg) {
	}
	
}