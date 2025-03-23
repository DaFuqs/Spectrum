package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import de.dafuqs.spectrum.cca.MiscPlayerDataComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SleepingChatScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SleepingChatScreen.class)
public class SleepingChatScreenMixin {

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;"), cancellable = true)
    private void spectrum$removeSleepButton(CallbackInfo ci) {
        if (MinecraftClient.getInstance().cameraEntity instanceof PlayerEntity player && MiscPlayerDataComponent.get(player).isSleeping())
            ci.cancel();;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;render(Lnet/minecraft/client/gui/DrawContext;IIF)V"))
    private boolean spectrum$stopButtonRendering(ButtonWidget instance, DrawContext drawContext, int mouseX, int mouseY, float v) {
        return !(MinecraftClient.getInstance().cameraEntity instanceof PlayerEntity player) || !MiscPlayerDataComponent.get(player).isSleeping();
    }
}
