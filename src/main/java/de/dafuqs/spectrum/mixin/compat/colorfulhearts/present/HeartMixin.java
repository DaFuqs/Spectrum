package de.dafuqs.spectrum.mixin.compat.colorfulhearts.present;

import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import terrails.colorfulhearts.heart.Heart;

@Environment(EnvType.CLIENT)
@Mixin(Heart.class)
public abstract class HeartMixin {

    @ModifyVariable(method = "draw(Lnet/minecraft/client/util/math/MatrixStack;IIZZLterrails/colorfulhearts/heart/HeartType;)V", at = @At("STORE"), ordinal = 2)
    private boolean heartRendererRenderPlayerHeartsGetHealthInjector(boolean hardcore) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!hardcore && client.player != null && client.player.hasStatusEffect(SpectrumStatusEffects.DIVINITY)) {
            return true;
        }
        return hardcore;
    }
    
}
