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
import org.joml.*;
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
				spectrum$renderOverlay(minecraftClient, matrixStack, SpectrumFluids.LIQUID_CRYSTAL_OVERLAY_TEXTURE, SpectrumFluids.LIQUID_CRYSTAL_OVERLAY_ALPHA);
			} else if (minecraftClient.player.isSubmergedIn(SpectrumFluidTags.MUD)) {
				spectrum$renderOverlay(minecraftClient, matrixStack, SpectrumFluids.MUD_OVERLAY_TEXTURE, SpectrumFluids.MUD_OVERLAY_ALPHA);
			} else if (minecraftClient.player.isSubmergedIn(SpectrumFluidTags.MIDNIGHT_SOLUTION)) {
				spectrum$renderOverlay(minecraftClient, matrixStack, SpectrumFluids.MIDNIGHT_SOLUTION_OVERLAY_TEXTURE, SpectrumFluids.MIDNIGHT_SOLUTION_OVERLAY_ALPHA);
			} else if (minecraftClient.player.isSubmergedIn(SpectrumFluidTags.DRAGONROT)) {
				spectrum$renderOverlay(minecraftClient, matrixStack, SpectrumFluids.DRAGONROT_OVERLAY_TEXTURE, SpectrumFluids.DRAGONROT_OVERLAY_ALPHA);
			}
		}
	}

	@Unique
	private static void spectrum$renderOverlay(MinecraftClient client, MatrixStack matrixStack, Identifier textureIdentifier, float alpha) {
		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
		RenderSystem.setShaderTexture(0, textureIdentifier);
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		BlockPos blockPos = BlockPos.ofFloored(client.player.getX(), client.player.getEyeY(), client.player.getZ());
		float f = LightmapTextureManager.getBrightness(client.player.getWorld().getDimension(), client.player.getWorld().getLightLevel(blockPos));
		RenderSystem.enableBlend();
		RenderSystem.setShaderColor(f, f, f, alpha);
		
		float m = -client.player.getYaw() / 64.0F;
		float n = client.player.getPitch() / 64.0F;
		Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferBuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).texture(4.0F + m, 4.0F + n).next();
		bufferBuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).texture(0.0F + m, 4.0F + n).next();
		bufferBuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).texture(0.0F + m, 0.0F + n).next();
		bufferBuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).texture(4.0F + m, 0.0F + n).next();
		BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableBlend();
	}
	
}