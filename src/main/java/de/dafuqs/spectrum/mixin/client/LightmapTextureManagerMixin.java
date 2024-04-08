package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(LightmapTextureManager.class)

public class LightmapTextureManagerMixin {

	@ModifyReturnValue(method = "getDarkness", at = @At("RETURN"))
	private float spectrum$getDarkness(float original) {
		if (isInDim()) {
			return Math.max(0.12F, original);
		}
		return original;
	}

	@ModifyExpressionValue(method = "update", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 1))
	private float spectrum$decreaseGamma(float original) {
		if (isInDim()) {
			return original - 0.5F;
		}
		return original;
	}

	@Unique
	private static boolean isInDim() {
		MinecraftClient client = MinecraftClient.getInstance();
		return SpectrumDimensions.DIMENSION_KEY.equals(client.player.getWorld().getRegistryKey());
	}

}
