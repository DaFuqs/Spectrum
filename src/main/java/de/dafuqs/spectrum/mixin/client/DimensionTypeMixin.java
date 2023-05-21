package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.world.dimension.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Environment(EnvType.CLIENT)
@Mixin(DimensionType.class)
public abstract class DimensionTypeMixin {
	
	@ModifyArg(method = "getSkyAngle", at = @At(value = "INVOKE", target = "Ljava/util/OptionalLong;orElse(J)J"))
	private long spectrum$getLerpedSkyAngle(long time) {
		if (!MinecraftClient.getInstance().isPaused() && SpectrumClient.skyLerper.isActive((DimensionType) (Object) this)) {
			return SpectrumClient.skyLerper.tickLerp(time, MinecraftClient.getInstance().getTickDelta());
		} else {
			return time;
		}
	}
	
}
