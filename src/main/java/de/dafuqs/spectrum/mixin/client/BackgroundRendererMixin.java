package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.*;
import com.llamalad7.mixinextras.sugar.ref.*;
import com.mojang.blaze3d.shaders.*;
import de.dafuqs.spectrum.deeper_down.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(FogRenderer.class)
public class BackgroundRendererMixin {
	
	@Inject(method = "setupFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V", remap = false, shift = At.Shift.BEFORE))
	private static void spectrum$modifyFog(Camera camera, FogRenderer.FogMode fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci, @Local FogRenderer.FogData fogData) {
		var world = Minecraft.getInstance().level;
		
		if (world == null)
			return;
		
		var dim = world.dimension();
		var inDim = dim == SpectrumDimensions.DIMENSION_KEY;
		
		if (inDim || DimensionRenderEffects.forceFogEffects) {
			fogData.shape = FogShape.SPHERE;
			fogData.end = Math.min(Math.min(viewDistance, 192F), DimensionRenderEffects.getFar(fogData.end));
			fogData.start = DimensionRenderEffects.getNear(fogData.start);
		}
	}
	
	@Inject(method = "setupFog", at = @At(value = "HEAD"))
	private static void spectrum$makeFogThick(Camera camera, FogRenderer.FogMode fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci, @Local(argsOnly = true, ordinal = 0) LocalBooleanRef tfog) {
		if (!thickFog && Minecraft.getInstance().cameraEntity instanceof LivingEntity livingEntity && SleepStatusEffect.getStrongestSleepEffect(livingEntity) != null)
			tfog.set(true);
	}
	
	@WrapOperation(method = "setupColor", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clearColor(FFFF)V", remap = false, ordinal = 1))
	private static void spectrum$darkenBackground(float red, float green, float blue, float alpha, Operation<Void> original) {
		var darkening = DimensionRenderEffects.fogDarkness;
		var blend = DimensionRenderEffects.blend;
		red = Mth.lerp(blend, red, DimensionRenderEffects.red);
		green = Mth.lerp(blend, green, DimensionRenderEffects.green);
		blue = Mth.lerp(blend, blue, DimensionRenderEffects.blue);
		if (darkening > 0) {
			red *= darkening;
			green *= darkening;
			blue *= darkening;
		}
		original.call(red, green, blue, alpha);
	}
}
