package de.dafuqs.spectrum.mixin.client;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.hud.*;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Environment(EnvType.CLIENT)
@Mixin(InGameOverlayRenderer.class)
public abstract class BlockOverlayRendererMixin {

	@Inject(method = "renderOverlays(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/util/math/MatrixStack;)V", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
	private static void spectrum$renderFluidOverlay(MinecraftClient minecraftClient, MatrixStack matrixStack, CallbackInfo ci) {
		if (!minecraftClient.player.isSpectator()) {
			if (minecraftClient.player.isSubmergedIn(SpectrumFluidTags.LIQUID_CRYSTAL)) {
				renderOverlay(minecraftClient, matrixStack, SpectrumFluids.LIQUID_CRYSTAL_OVERLAY_TEXTURE, SpectrumFluids.LIQUID_CRYSTAL_OVERLAY_ALPHA);
			} else if (minecraftClient.player.isSubmergedIn(SpectrumFluidTags.MUD)) {
				renderOverlay(minecraftClient, matrixStack, SpectrumFluids.MUD_OVERLAY_TEXTURE, SpectrumFluids.MUD_OVERLAY_ALPHA);
			} else if (minecraftClient.player.isSubmergedIn(SpectrumFluidTags.MIDNIGHT_SOLUTION)) {
				renderOverlay(minecraftClient, matrixStack, SpectrumFluids.MIDNIGHT_SOLUTION_OVERLAY_TEXTURE, SpectrumFluids.MIDNIGHT_SOLUTION_OVERLAY_ALPHA);
			} else if (minecraftClient.player.isSubmergedIn(SpectrumFluidTags.DRAGONROT)) {
				renderOverlay(minecraftClient, matrixStack, SpectrumFluids.DRAGONROT_OVERLAY_TEXTURE, SpectrumFluids.DRAGONROT_OVERLAY_ALPHA);
			}
		}
	}
	
	private static void renderOverlay(MinecraftClient client, MatrixStack matrixStack, Identifier textureIdentifier, float alpha) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.enableTexture();
		RenderSystem.setShaderTexture(0, textureIdentifier);
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		float f = client.player.getBrightnessAtEyes();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShaderColor(f, f, f, alpha);
		
		float m = -client.player.getYaw() / 64.0F;
		float n = client.player.getPitch() / 64.0F;
		Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferBuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).texture(4.0F + m, 4.0F + n).next();
		bufferBuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).texture(0.0F + m, 4.0F + n).next();
		bufferBuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).texture(0.0F + m, 0.0F + n).next();
		bufferBuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).texture(4.0F + m, 0.0F + n).next();
		BufferRenderer.drawWithShader(bufferBuilder.end());
		RenderSystem.disableBlend();
	}
	
}