package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.SpectrumClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DimensionType.class)
public class DimensionTypeMixin {
	
	@ModifyArg(method = "getSkyAngle", at = @At(value = "INVOKE", target = "Ljava/util/OptionalLong;orElse(J)J"))
	private long spectrum$getLerpedSkyAngle(long time) {
		if (!MinecraftClient.getInstance().isPaused() && SpectrumClient.skyLerper.isActive((DimensionType) (Object) this)) {
			return SpectrumClient.skyLerper.tickLerp(time, MinecraftClient.getInstance().getTickDelta());
		} else {
			return time;
		}
	}
	
}
