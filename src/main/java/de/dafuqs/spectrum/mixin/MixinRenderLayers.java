package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.*;
import net.minecraft.block.*;
import net.minecraft.client.render.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(RenderLayers.class)
public class MixinRenderLayers {

    @Inject(method = "getBlockLayer", at = @At("HEAD"), cancellable = true)
    private static void translucentHook(final BlockState state, final CallbackInfoReturnable<RenderLayer> cir) {
        if (SpectrumClient.FORCE_TRANSLUCENT) {
            cir.setReturnValue(RenderLayer.getTranslucent());
        }
    }

}