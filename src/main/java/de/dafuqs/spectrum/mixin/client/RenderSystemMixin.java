package de.dafuqs.spectrum.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.deeper_down.DarknessEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RenderSystem.class, priority = 1001)
public class RenderSystemMixin {

    @Shadow @Final private static float[] shaderFogColor;

    @Inject(method = "getShaderFogColor", at = @At("RETURN"), remap = false, cancellable = true)
    private static void alterFogColor(CallbackInfoReturnable<float[]> cir) {
        var darkening = DarknessEffects.fogDarkness;
        var r = shaderFogColor[0] * darkening;
        var g = shaderFogColor[1] * darkening;
        var b = shaderFogColor[2] * darkening;
        cir.setReturnValue(new float[]{r, g, b, shaderFogColor[3]});
        cir.cancel();
    }
}
