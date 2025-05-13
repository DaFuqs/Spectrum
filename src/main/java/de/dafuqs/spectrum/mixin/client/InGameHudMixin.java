package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.render.*;
import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.hud.*;
import net.minecraft.client.render.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

	@Shadow @Final private MinecraftClient client;
	
    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow public abstract void render(DrawContext context, RenderTickCounter tickerCounter);

    @Inject(method = "renderStatusBars(Lnet/minecraft/client/gui/DrawContext;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHealthBar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/entity/player/PlayerEntity;IIIIFIIIZ)V", shift = At.Shift.AFTER))
    private void spectrum$renderAzureDikeBar(DrawContext context, CallbackInfo ci, @Local PlayerEntity cameraPlayer, @Local(ordinal = 2) int x, @Local(ordinal = 4) int y, @Local(ordinal = 6) int heartRows, @Local(ordinal = 7) int rowHeight) {
		client.getProfiler().swap("spectrum:azure");
        HudRenderers.renderAzureDike(context, cameraPlayer, x, y - (heartRows - 1) * rowHeight - 10);
    }

    @ModifyExpressionValue(method = "renderMiscOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;isFancyGraphicsOrBetter()Z"))
    private boolean spectrum$disableVignetteInDimension(boolean original) {
		var player = MinecraftClient.getInstance().player;
		var isInDim = player != null && SpectrumDimensions.DIMENSION_KEY.equals(player.getWorld().getRegistryKey());
        return !isInDim && original;
    }

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void spectrum$disableCrosshairSomnolence(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		var potency = SleepStatusEffect.getSleepScaling(getCameraPlayer());
        if (potency > 0.25F)
			ci.cancel();
    }

    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void spectrum$disableHotbarSomnolence(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		var potency = SleepStatusEffect.getSleepScaling(getCameraPlayer());
        if (potency > 0.4F)
			ci.cancel();
    }

    @Inject(method = "renderStatusBars", at = @At("HEAD"), cancellable = true)
    private void spectrum$disableStatusSomnolence(DrawContext context, CallbackInfo ci) {
		var potency = SleepStatusEffect.getSleepScaling(getCameraPlayer());
		if (potency > 0.4F)
			ci.cancel();
    }

    @ModifyArg(method = "renderStatusEffectOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 0))
    private Identifier modifyAmbientEffectBackgrounds(Identifier texture, @Local StatusEffectInstance effect) {
		return StatusEffectHelper.getTexture(texture, effect, StatusEffectHelper.RenderType.HUD_AMBIENT);
    }
    
    @ModifyArg(method = "renderStatusEffectOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 1))
    private Identifier modifyEffectBackgrounds(Identifier texture, @Local StatusEffectInstance effect) {
		return StatusEffectHelper.getTexture(texture, effect, StatusEffectHelper.RenderType.HUD_DEFAULT);
    }
}
