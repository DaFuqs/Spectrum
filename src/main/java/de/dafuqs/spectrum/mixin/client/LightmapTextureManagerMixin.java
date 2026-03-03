package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.render.biome_rendering.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(value = LightTexture.class, priority = 9999)
public class LightmapTextureManagerMixin {
	
	@Shadow @Final private Minecraft minecraft;
	
	@ModifyReturnValue(method = "calculateDarknessScale", at = @At("RETURN"))
	private float getDarkness(float original) {
		if (EnvironmentalRendering.getRenderState().active()) {
			EnvironmentalData data = EnvironmentalRendering.getCurrentEnvironmentalData();
			float lightMod = SpectrumCommon.CONFIG.DimensionBrightnessMod * 0.25F;
			return Math.max(data.darkening() - lightMod, original);
		}
		return original;
	}
	
	@ModifyExpressionValue(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 1))
	private float decreaseGamma(float gamma) {
		EnvironmentalRendering.RenderState state = EnvironmentalRendering.getRenderState();
		
		if (state.ultradark() && minecraft.getCameraEntity() instanceof LivingEntity living) {
			gamma -= living.hasEffect(MobEffects.NIGHT_VISION) ? 0.275F : 0F;
		}
		
		if (state.active()) {
			float modifier = state.ultradark() ? SpectrumCommon.CONFIG.DimensionBrightnessMod : 0.25F;
			gamma = Mth.lerp(EnvironmentalRendering.getCurrentEnvironmentalData().darkening(), gamma, gamma - 25F + modifier);
		}
		
		return gamma;
	}
	
}
