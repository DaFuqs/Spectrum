package de.dafuqs.spectrum.mixin.compat.colorful_hearts.present;

import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import terrails.colorfulhearts.api.heart.drawing.*;

@Environment(EnvType.CLIENT)
@Mixin(Heart.class)
public abstract class HeartMixin {

    @ModifyVariable(method = "draw", at = @At("STORE"), ordinal = 0, argsOnly = true)
    private boolean heartRendererRenderPlayerHeartsGetHealthInjector(boolean hardcore) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!hardcore && client.player != null && client.player.hasStatusEffect(SpectrumStatusEffects.DIVINITY)) {
            return true;
        }
        return hardcore;
    }
    
}
