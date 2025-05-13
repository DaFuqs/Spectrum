package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.*;
import com.llamalad7.mixinextras.sugar.ref.*;
import de.dafuqs.spectrum.deeper_down.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
	
	@Inject(method = "applyFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V", shift = At.Shift.BEFORE))
	private static void spectrum$modifyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci, @Local BackgroundRenderer.FogData fogData) {
		var world = MinecraftClient.getInstance().world;
		
		if (world == null)
			return;
		
		var dim = world.getRegistryKey();
		var inDim = dim == SpectrumDimensions.DIMENSION_KEY;
		
		if (inDim || DimensionRenderEffects.forceFogEffects) {
			fogData.fogShape = FogShape.SPHERE;
			fogData.fogEnd = Math.min(Math.min(viewDistance, 192F), DimensionRenderEffects.getFar(fogData.fogEnd));
			fogData.fogStart = DimensionRenderEffects.getNear(fogData.fogStart);
		}
	}

	@Inject(method = "applyFog", at = @At(value = "HEAD"))
	private static void spectrum$makeFogThick(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci, @Local(argsOnly = true, ordinal = 0) LocalBooleanRef tfog) {
		if (!thickFog && MinecraftClient.getInstance().cameraEntity instanceof LivingEntity livingEntity && SleepStatusEffect.getStrongestSleepEffect(livingEntity) != null)
			tfog.set(true);
	}

	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clearColor(FFFF)V", ordinal = 1))
	private static void spectrum$darkenBackground(float red, float green, float blue, float alpha, Operation<Void> original) {
		var darkening = DimensionRenderEffects.fogDarkness;
		var blend = DimensionRenderEffects.blend;
		red = MathHelper.lerp(blend, red, DimensionRenderEffects.red);
		green = MathHelper.lerp(blend, green, DimensionRenderEffects.green);
		blue = MathHelper.lerp(blend, blue, DimensionRenderEffects.blue);
		if (darkening > 0) {
			red *= darkening;
			green *= darkening;
			blue *= darkening;
		}
		original.call(red, green, blue, alpha);
	}
}
