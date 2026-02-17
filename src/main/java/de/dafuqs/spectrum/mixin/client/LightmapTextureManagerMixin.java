package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.deeper_down.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(value = LightTexture.class, priority = 9999)

public class LightmapTextureManagerMixin {
	
	@Shadow
	@Final
	private Minecraft minecraft;
	
	@ModifyReturnValue(method = "calculateDarknessScale", at = @At("RETURN"))
	private float spectrum$getDarkness(float original) {
		if (isInDim()) {
			var lightMod = SpectrumCommon.CONFIG.DimensionBrightnessMod * 0.25F;
			float darkening = Mth.lerp(DimensionRenderEffects.getDarknessInterpolation(), 0.11F - lightMod, 0.2125F - lightMod);
			return Math.max(darkening, original);
		}
		
		return original;
	}
	
	@ModifyExpressionValue(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 1))
	private float spectrum$decreaseGamma(float gamma) {
		if (isInDim()) {
			if (minecraft.getCameraEntity() instanceof LivingEntity living) {
				gamma -= living.hasEffect(MobEffects.NIGHT_VISION) ? 0.275F : 0F;
			}
		}
		
		return gamma;
	}
	
	@Unique
	private static boolean isInDim() {
		Minecraft client = Minecraft.getInstance();
		return SpectrumDimensions.DIMENSION_KEY.equals(client.player.level().dimension());
	}
}
