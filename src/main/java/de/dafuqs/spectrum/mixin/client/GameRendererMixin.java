package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.deeper_down.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.client.*;
import net.minecraft.client.render.*;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(value = GameRenderer.class, priority = 9999)
public abstract class GameRendererMixin {

    @ModifyReturnValue(method = "getNightVisionStrength(Lnet/minecraft/entity/LivingEntity;F)F", at = @At("RETURN"))
    private static float spectrum$nerfNightVisionInDimension(float original, LivingEntity entity, float tickDelta) {
		if (SpectrumDimensions.DIMENSION_KEY == entity.getWorld().getRegistryKey()) {
			original /= 6F;
		}

        if (DimensionRenderEffects.darkenTicks > 0) {
            original *= 1F - DimensionRenderEffects.getDarknessInterpolation();
        }

        return original;
    }
	
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getFramebuffer()Lnet/minecraft/client/gl/Framebuffer;", shift = At.Shift.BEFORE))
	private void applyPostProcessShaders(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
		RenderSystem.disableBlend();
		RenderSystem.disableDepthTest();
		RenderSystem.resetTextureMatrix();
		SpectrumShaders.colorGradingPostProcess.ifPresent(pps -> pps.render(tickCounter.getLastFrameDuration()));
	}
	
	@Inject(method = "close", at = @At("TAIL"))
	private void closeShaders(CallbackInfo ci) {
		SpectrumShaders.clearDimensionShaders();
	}
	
	@Inject(method = "onResized", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;onResized(II)V"))
	private void resizeShaders(int width, int height, CallbackInfo ci) {
		SpectrumShaders.resizeShaders(width, height);
	}
}
