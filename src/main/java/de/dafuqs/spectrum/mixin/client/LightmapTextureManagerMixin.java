package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.render.biome_rendering.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(value = LightTexture.class, priority = 9999)
public class LightmapTextureManagerMixin {
	
	@Shadow @Final private Minecraft minecraft;
	
	@ModifyReturnValue(method = "calculateDarknessScale", at = @At("RETURN"))
	private float spectrum$calculateDarknessScale(float original) {
		if (EnvironmentalRendering.getRenderState().active()) {
			EnvironmentalData data = EnvironmentalRendering.getCurrentEnvironmentalData();
			return original * data.environmentalLightingMultiplier();
		}
		return original;
	}
	
	@ModifyExpressionValue(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 1))
	private float spectrum$updateLightTexture(float gamma) {
		if (EnvironmentalRendering.getRenderState().ultradark()
				&& minecraft.getCameraEntity() instanceof LivingEntity living
				&& living.hasEffect(MobEffects.NIGHT_VISION)) {
			
			gamma -= 0.275F;
		}
		return gamma;
	}
	
}
