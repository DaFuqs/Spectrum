package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.render.*;
import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.resources.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.player.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Gui.class)
public abstract class InGameHudMixin {
	
	@Shadow
	protected abstract Player getCameraPlayer();
	
	@Shadow
	public abstract void render(GuiGraphics context, DeltaTracker tickerCounter);
	
	@Inject(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V"))
	private void spectrum$renderAzureDikeBar(GuiGraphics context, CallbackInfo ci, @Local Player cameraPlayer, @Local(ordinal = 2) int x, @Local(ordinal = 4) int y, @Local(ordinal = 6) int heartRows, @Local(ordinal = 7) int rowHeight) {
		Minecraft.getInstance().getProfiler().popPush("spectrum:azure");
		HudRenderers.renderAzureDike(context, cameraPlayer, x, y - (heartRows - 1) * rowHeight - 10);
	}
	
	@ModifyExpressionValue(method = "renderCameraOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;useFancyGraphics()Z"))
	private boolean spectrum$disableVignietteInDimension(boolean original) {
		var player = Minecraft.getInstance().player;
		var isInDim = player != null && SpectrumDimensions.DIMENSION_KEY.equals(player.level().dimension());
        return !isInDim && original;
    }

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
	private void spectrum$disableCrosshairSomnolence(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {
		var potency = SleepStatusEffect.getSleepScaling(getCameraPlayer());
        if (potency > 0.25F)
			ci.cancel();
    }
	
	@Inject(method = "renderItemHotbar", at = @At("HEAD"), cancellable = true)
	private void spectrum$disableHotbarSomnolence(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {
		var potency = SleepStatusEffect.getSleepScaling(getCameraPlayer());
        if (potency > 0.4F)
			ci.cancel();
    }
	
	@Inject(method = "renderPlayerHealth", at = @At("HEAD"), cancellable = true)
	private void spectrum$disableStatusSomnolence(GuiGraphics context, CallbackInfo ci) {
		var potency = SleepStatusEffect.getSleepScaling(getCameraPlayer());
		if (potency > 0.4F)
			ci.cancel();
    }
	
	@ModifyArg(method = "renderEffects", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/GuiGraphics.blitSprite (Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 0))
	private ResourceLocation modifyAmbientEffectBackgrounds(ResourceLocation texture, @Local MobEffectInstance effect) {
		return StatusEffectHelper.getTextureLocation(texture, effect, StatusEffectHelper.RenderType.HUD_AMBIENT);
    }
	
	@ModifyArg(method = "renderEffects", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/GuiGraphics.blitSprite (Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 1))
	private ResourceLocation modifyEffectBackgrounds(ResourceLocation texture, @Local MobEffectInstance effect) {
		return StatusEffectHelper.getTextureLocation(texture, effect, StatusEffectHelper.RenderType.HUD_DEFAULT);
    }
}
