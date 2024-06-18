package de.dafuqs.spectrum.mixin.compat.sodium.present;

import de.dafuqs.spectrum.registries.SpectrumFluids;
import me.jellysquid.mods.sodium.client.model.color.ColorProvider;
import me.jellysquid.mods.sodium.client.model.color.ColorProviderRegistry;
import net.caffeinemc.mods.sodium.api.util.ColorARGB;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
@Mixin(value = ColorProviderRegistry.class, remap = false)
public abstract class ColorProviderRegistryMixin {
    @Shadow protected abstract void registerFluids(ColorProvider<FluidState> resolver, Fluid... fluids);

    @Inject(method = "installOverrides", at = @At("RETURN"))
    private void spectrum$registerFluidColorProviders(CallbackInfo ci) {
        SpectrumFluids.HANDLER_MAP.forEach((handler, fluids) -> registerFluids(createProvider(handler), fluids));
    }

    @Unique
    private ColorProvider<FluidState> createProvider(FluidRenderHandler handler) {
        return (view, pos, state, quad, output) -> Arrays.fill(output, ColorARGB.toABGR(handler.getFluidColor(view, pos, state)));
    }
}
