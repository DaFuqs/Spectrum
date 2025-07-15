package de.dafuqs.spectrum.mixin.compat.connector.present;

import com.llamalad7.mixinextras.sugar.Local;
import de.dafuqs.spectrum.render.HudRenderers;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.world.entity.player.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class InGameHudMixin {
	
	@Shadow
	@Final
	private Minecraft minecraft;
	
	@Inject(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V"))
	private void spectrum$renderAzureDikeBar(GuiGraphics context, CallbackInfo ci, @Local Player cameraPlayer, @Local(ordinal = 3) int x, @Local(ordinal = 5) int y, @Local(ordinal = 7) int heartRows, @Local(ordinal = 8) int rowHeight) {
		minecraft.getProfiler().popPush("spectrum:azure");
		HudRenderers.renderAzureDike(context, cameraPlayer, x, y - (heartRows - 1) * rowHeight - 10);
	}
}
