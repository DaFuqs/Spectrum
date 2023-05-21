package de.dafuqs.spectrum.mixin.compat.healthoverlay.present;

import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import terrails.healthoverlay.heart.*;

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
