package de.dafuqs.spectrum.mixin.compat.healthoverlay.present;

import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import terrails.healthoverlay.heart.Heart;

@Environment(EnvType.CLIENT)
@Mixin(Heart.class)
public abstract class HeartMixin {

    @ModifyVariable(method = "draw(Lnet/minecraft/client/util/math/MatrixStack;IIZLterrails/healthoverlay/heart/HeartType;)V", at = @At("STORE"), ordinal = 1)
    private boolean heartRendererRenderPlayerHeartsGetHealthInjector(boolean hardcore) {
        if (!hardcore && MinecraftClient.getInstance().player.hasStatusEffect(SpectrumStatusEffects.DIVINITY)) {
            return true;
        }
        return hardcore;
    }
    
}
