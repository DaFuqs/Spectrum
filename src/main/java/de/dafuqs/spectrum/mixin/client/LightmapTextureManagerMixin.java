package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.deeper_down.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(value = LightmapTextureManager.class, priority = 9999)

public class LightmapTextureManagerMixin {

	@Shadow @Final private MinecraftClient client;

	@ModifyReturnValue(method = "getDarkness", at = @At("RETURN"))
	private float spectrum$getDarkness(float original) {
		var lightMod = SpectrumCommon.CONFIG.DimensionBrightnessMod * 0.25F;

		if (isInDim()) {
			var darkening = MathHelper.lerp(DimensionRenderEffects.getDarknessInterpolation(), 0.11F - lightMod, 0.2125F - lightMod);
			return Math.max(darkening, original);

		}
		return original;
	}
	
	@ModifyExpressionValue(method = "update", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 1))
	private float spectrum$decreaseGamma(float gamma) {
		if (isInDim()) {
			if (client.getCameraEntity() instanceof LivingEntity living) {
				gamma -= living.hasStatusEffect(StatusEffects.NIGHT_VISION) ? 0.275F : 0F;
			}

			if (DimensionRenderEffects.darkenTicks > 0) {
				gamma = MathHelper.lerp(DimensionRenderEffects.getDarknessInterpolation(), gamma, gamma - 25F + SpectrumCommon.CONFIG.DimensionBrightnessMod);
			}
		}

		return gamma;
	}

	@Unique
	private static boolean isInDim() {
		MinecraftClient client = MinecraftClient.getInstance();
		return SpectrumDimensions.DIMENSION_KEY.equals(client.player.getWorld().getRegistryKey());
	}
}
