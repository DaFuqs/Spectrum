package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.mob_effect.*;
import de.dafuqs.spectrum.registries.*;
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
	
	@ModifyExpressionValue(method = "renderCameraOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;useFancyGraphics()Z"))
	private boolean spectrum$disableVignetteInDimension(boolean original) {
		var player = Minecraft.getInstance().player;
		var isInDim = player != null && SpectrumDimensionKeys.DIMENSION_KEY.equals(player.level().dimension());
		return !isInDim && original;
	}
	
	@Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
	private void spectrum$disableCrosshairSomnolence(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {
		var potency = SleepMobEffect.getSleepScaling(getCameraPlayer());
		if (potency > 0.25F)
			ci.cancel();
	}
	
	@ModifyArg(method = "renderEffects", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/GuiGraphics.blitSprite (Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 0))
	private ResourceLocation modifyAmbientEffectBackgrounds(ResourceLocation texture, @Local MobEffectInstance effect) {
		return MobEffectHelper.getTextureLocation(texture, effect, MobEffectHelper.RenderType.HUD_AMBIENT);
	}
	
	@ModifyArg(method = "renderEffects", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/GuiGraphics.blitSprite (Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 1))
	private ResourceLocation modifyEffectBackgrounds(ResourceLocation texture, @Local MobEffectInstance effect) {
		return MobEffectHelper.getTextureLocation(texture, effect, MobEffectHelper.RenderType.HUD_DEFAULT);
	}
}
