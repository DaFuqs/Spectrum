package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.*;
import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.deeper_down.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
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
			fogData.fogShape = FogShape.SPHERE;
			fogData.fogEnd = Math.min(Math.min(viewDistance, 192F), DarknessEffects.getFar(fogData.fogEnd));
			fogData.fogStart = Math.min(fogData.fogEnd * 0.9F, DarknessEffects.getNear(fogData.fogStart));
		}
	}

	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clearColor(FFFF)V", ordinal = 1))
	private static void darkenBackground(float red, float green, float blue, float alpha, Operation<Void> original) {
		var darkening = DarknessEffects.fogDarkness;
		if (darkening > 0) {
			red *= darkening;
			green *= darkening;
			blue *= darkening;
			RenderSystem.clearColor(red, green, blue, alpha);
			return;
		}
		original.call(red, green, blue, alpha);
	}
}
