package de.dafuqs.spectrum.mixin.compat.healthoverlay.present;

import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import terrails.colorfulhearts.heart.*;

@Environment(EnvType.CLIENT)
@Mixin(Heart.class)
public abstract class HeartMixin {

    @ModifyVariable(method = "draw", at = @At("STORE"), ordinal = 1)
    private boolean heartRendererRenderPlayerHeartsGetHealthInjector(boolean hardcore) {
        if (!hardcore && MinecraftClient.getInstance().player.hasStatusEffect(SpectrumStatusEffects.DIVINITY)) {
            return true;
        }
        return hardcore;
    }
    
}
