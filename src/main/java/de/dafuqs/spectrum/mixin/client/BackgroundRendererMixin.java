package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.client.*;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
	
	@Inject(method = "applyFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V"))
	private static void modifyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci, @Local BackgroundRenderer.FogData fogData) {
		var world = MinecraftClient.getInstance().world;
		
		if (world == null)
			return;
		
		var dim = world.getRegistryKey();
		
		if (dim == SpectrumDimensions.DIMENSION_KEY) {
			var fogPoint = fogData.fogStart;
			fogData.fogShape = FogShape.SPHERE;
			float adjustedTicks = SpectrumClientEventListeners.spireTicks;
			
			if (SpectrumClientEventListeners.spireTicks > SpectrumClientEventListeners.lastSpireTicks) {
				adjustedTicks = SpectrumClientEventListeners.spireTicks + tickDelta;
			} else if (SpectrumClientEventListeners.spireTicks < SpectrumClientEventListeners.lastSpireTicks) {
				adjustedTicks = SpectrumClientEventListeners.spireTicks - tickDelta;
			}
			
			fogPoint = MathHelper.clampedLerp(fogPoint, -fogData.fogEnd, adjustedTicks / 60F);
			fogData.fogStart = fogPoint;
		}
	}
}
