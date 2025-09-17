package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.v2.*;
import de.dafuqs.spectrum.attachment_types.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.world.entity.player.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(InBedChatScreen.class)
public class SleepingChatScreenMixin {
	
	@Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Button;builder(Lnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)Lnet/minecraft/client/gui/components/Button$Builder;"), cancellable = true)
	private void spectrum$removeSleepButton(CallbackInfo ci) {
		if (Minecraft.getInstance().cameraEntity instanceof Player player && MiscPlayerDataAttachmentType.get(player).isSleeping())
			ci.cancel();
	}
	
	@WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Button;render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V"))
	private boolean spectrum$stopButtonRendering(Button instance, GuiGraphics drawContext, int mouseX, int mouseY, float v) {
		return !(Minecraft.getInstance().cameraEntity instanceof Player player) || !MiscPlayerDataAttachmentType.get(player).isSleeping();
	}
}
