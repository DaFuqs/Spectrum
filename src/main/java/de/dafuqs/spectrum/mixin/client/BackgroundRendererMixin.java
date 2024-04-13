package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import de.dafuqs.spectrum.SpectrumClient;
import de.dafuqs.spectrum.registries.SpectrumBiomes;
import de.dafuqs.spectrum.registries.SpectrumDimensions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.FogShape;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
            float adjustedTicks = SpectrumClient.spireTicks;

            if (SpectrumClient.spireTicks > SpectrumClient.lastSpireTicks) {
                adjustedTicks = SpectrumClient.spireTicks + tickDelta;
            }
            else if (SpectrumClient.spireTicks < SpectrumClient.lastSpireTicks) {
                adjustedTicks = SpectrumClient.spireTicks - tickDelta;
            }

            fogPoint = MathHelper.clampedLerp(fogPoint, -fogData.fogEnd, adjustedTicks / 60F);
            fogData.fogStart = fogPoint;
        }
    }
}
