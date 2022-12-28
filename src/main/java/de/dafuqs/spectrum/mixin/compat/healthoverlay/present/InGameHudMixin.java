package de.dafuqs.spectrum.mixin.compat.healthoverlay.present;

import de.dafuqs.spectrum.mixin.accessors.InGameHudAccessor;
import de.dafuqs.spectrum.render.HudRenderers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import terrails.healthoverlay.render.HeartRenderer;

@Environment(EnvType.CLIENT)
@Mixin(HeartRenderer.class)
public abstract class InGameHudMixin {

	// Execute the display after HealthOverlay did its own, to avoid conflict
	@Inject(method = "renderPlayerHearts", at = @At("TAIL"), remap = false, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void renderPlayerHeartsAzureDikeInjector(MatrixStack poseStack, PlayerEntity player, CallbackInfo ci) {
		InGameHud hud = MinecraftClient.getInstance().inGameHud;
		int scaledWidth = ((InGameHudAccessor) hud).getWidth();
		int scaledHeight = ((InGameHudAccessor) hud).getHeight();
		HudRenderers.renderAzureDike(poseStack, scaledWidth, scaledHeight, player);
	}

	// Doesn't work, so left aside until some eventual fix
	/*@Redirect(method = "renderPlayerHearts", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getHealth()F"), remap = false)
	private float renderPlayerHeartsPlayerGetHealthRedirect(PlayerEntity instance) {
		if (instance.hasStatusEffect(SpectrumStatusEffects.DIVINITY)) {
			return 9 * 5f;
		}
		return instance.getHealth();
	}*/
}