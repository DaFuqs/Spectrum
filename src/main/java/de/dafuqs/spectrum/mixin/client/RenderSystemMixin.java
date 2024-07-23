package de.dafuqs.spectrum.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.deeper_down.DarknessEffects;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RenderSystem.class, priority = 1001, remap = false)
public class RenderSystemMixin {

    @Shadow @Final private static float[] shaderFogColor;

    @Inject(method = "getShaderFogColor", at = @At("RETURN"), cancellable = true)
    private static void alterFogColor(CallbackInfoReturnable<float[]> cir) {
        var darkening = DarknessEffects.fogDarkness;
        var blend = DarknessEffects.blend;
        var r = MathHelper.lerp(blend, shaderFogColor[0], DarknessEffects.red);
        var g = MathHelper.lerp(blend, shaderFogColor[1], DarknessEffects.green);
        var b = MathHelper.lerp(blend, shaderFogColor[2], DarknessEffects.blue);
        r = r * darkening;
        g = g * darkening;
        b = b * darkening;
        cir.setReturnValue(new float[]{r, g, b, shaderFogColor[3]});
        cir.cancel();
    }
}
