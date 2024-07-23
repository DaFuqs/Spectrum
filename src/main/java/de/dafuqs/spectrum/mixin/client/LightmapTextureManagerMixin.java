package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.deeper_down.DarknessEffects;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(value = LightmapTextureManager.class, priority = 9999)

public class LightmapTextureManagerMixin {

	@Shadow @Final private MinecraftClient client;

	@ModifyReturnValue(method = "getDarkness", at = @At("RETURN"))
	private float spectrum$getDarkness(float original) {
		if (isInDim()) {
			var darkening = MathHelper.lerp(DarknessEffects.getDarknessInterpolation(), 0.12F, 0.225F);

			if (SpectrumCommon.CONFIG.ExtraDarkDimension) {
				darkening *= 2;
			}

			return Math.max(darkening, original);

		}
		return original;
	}
	
	@ModifyExpressionValue(method = "update", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 1))
	private float spectrum$decreaseGamma(float gamma) {
		if (isInDim()) {
			if (SpectrumCommon.CONFIG.ExtraDarkDimension) {
				gamma = -1.5F;
			}
			else if (client.getCameraEntity() instanceof LivingEntity living) {
				gamma -= living.hasStatusEffect(StatusEffects.NIGHT_VISION) ? 0.5F : 0F;
			}
		}

		if (DarknessEffects.darkenTicks > 0) {
			gamma = MathHelper.lerp(DarknessEffects.getDarknessInterpolation(), gamma, gamma - 3F);
		}

		return gamma;
	}

	@Unique
	private static boolean isInDim() {
		MinecraftClient client = MinecraftClient.getInstance();
		return SpectrumDimensions.DIMENSION_KEY.equals(client.player.getWorld().getRegistryKey());
	}
}
