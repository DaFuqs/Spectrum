package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.*;
import net.minecraft.client.*;
import net.minecraft.world.level.dimension.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;


@Mixin(DimensionType.class)
public abstract class DimensionTypeMixin {
	
	@ModifyArg(method = "timeOfDay", at = @At(value = "INVOKE", target = "Ljava/util/OptionalLong;orElse(J)J"))
	private long spectrum$getLerpedSkyAngle(long time) {
		if (!Minecraft.getInstance().isPaused() && SpectrumClient.skyLerper.isActive((DimensionType) (Object) this)) {
			return SpectrumClient.skyLerper.tickLerp(time, Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false));
		} else {
			return time;
		}
	}
	
}
