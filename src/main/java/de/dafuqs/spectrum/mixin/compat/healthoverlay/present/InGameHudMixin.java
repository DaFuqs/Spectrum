package de.dafuqs.spectrum.mixin.compat.healthoverlay.present;

import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.render.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.*;
import net.minecraft.client.util.math.*;
import net.minecraft.entity.player.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import terrails.colorfulhearts.render.HeartRenderer;

@Environment(EnvType.CLIENT)
@Mixin(HeartRenderer.class)
public abstract class InGameHudMixin {

	// Execute the display after HealthOverlay did its own, to avoid conflict
	@SuppressWarnings("resource")
	@Inject(method = "renderPlayerHearts", at = @At("TAIL"), remap = false, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void renderPlayerHeartsAzureDikeInjector(MatrixStack poseStack, PlayerEntity player, int x, int y, int maxHealth, int currentHealth, int displayHealth, int absorption, boolean renderHighlight, CallbackInfo ci) {
		InGameHud hud = MinecraftClient.getInstance().inGameHud;
		int scaledWidth = ((InGameHudAccessor) hud).getWidth();
		int scaledHeight = ((InGameHudAccessor) hud).getHeight();

		// FIXME - Colorful Hearts, formerly HealthOverlay, does not give us the DrawContext, they have their own draw context at home
		// Ask them to actually provide the context properly so we do not have to do this
		DrawContext context = new DrawContext(MinecraftClient.getInstance(), MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers());
		context.getMatrices().multiplyPositionMatrix(poseStack.peek().getPositionMatrix());

		HudRenderers.renderAzureDike(context, scaledWidth, scaledHeight, player);
	}
}