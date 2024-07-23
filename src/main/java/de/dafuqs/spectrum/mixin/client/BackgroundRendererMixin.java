package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.*;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import de.dafuqs.spectrum.deeper_down.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.status_effects.SleepStatusEffect;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
	
	@Inject(method = "applyFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V"))
	private static void spectrum$modifyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci, @Local BackgroundRenderer.FogData fogData) {
		var world = MinecraftClient.getInstance().world;
		
		if (world == null)
			return;
		
		var dim = world.getRegistryKey();
		var inDim = dim == SpectrumDimensions.DIMENSION_KEY;
		
		if (inDim || DarknessEffects.forceFogEffects) {
			fogData.fogShape = FogShape.SPHERE;
			fogData.fogEnd = Math.min(Math.min(viewDistance, 192F), DarknessEffects.getFar(fogData.fogEnd));
			fogData.fogStart = Math.min(fogData.fogEnd * 0.9F, DarknessEffects.getNear(fogData.fogStart));
		}
	}

	@Inject(method = "applyFog", at = @At(value = "HEAD"))
	private static void spectrum$makeFogThick(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci, @Local(argsOnly = true, ordinal = 0) LocalBooleanRef tfog) {
		if (!thickFog && MinecraftClient.getInstance().cameraEntity instanceof LivingEntity livingEntity && SleepStatusEffect.hasSleepEffect(livingEntity))
			tfog.set(true);
	}

	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clearColor(FFFF)V", ordinal = 1))
	private static void spectrum$darkenBackground(float red, float green, float blue, float alpha, Operation<Void> original) {
		var darkening = DarknessEffects.fogDarkness;
		var blend = DarknessEffects.blend;
		red = MathHelper.lerp(blend, red, DarknessEffects.red);
		green = MathHelper.lerp(blend, green, DarknessEffects.green);
		blue = MathHelper.lerp(blend, blue, DarknessEffects.blue);
		if (darkening > 0) {
			red *= darkening;
			green *= darkening;
			blue *= darkening;
		}
		original.call(red, green, blue, alpha);
	}
}
