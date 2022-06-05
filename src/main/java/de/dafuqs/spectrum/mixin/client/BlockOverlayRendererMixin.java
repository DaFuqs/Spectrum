package de.dafuqs.spectrum.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumFluidTags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(InGameOverlayRenderer.class)
public class BlockOverlayRendererMixin {
	
	// Since the hack in SpectrumFluid to allow swimming, sounds, particles for fluids
	// this does now work because "isSubmergedIn()" only matches for water
	private static final Identifier TEXTURE_IN_LIQUID_CRYSTAL = new Identifier(SpectrumCommon.MOD_ID + ":textures/misc/liquid_crystal_overlay.png");
	private static final Identifier TEXTURE_IN_MUD = new Identifier(SpectrumCommon.MOD_ID + ":textures/misc/mud_overlay.png");
	private static final Identifier TEXTURE_IN_MIDNIGHT_SOLUTION = new Identifier(SpectrumCommon.MOD_ID + ":textures/misc/midnight_solution_overlay.png");
	
	@Inject(method = "renderOverlays(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/util/math/MatrixStack;)V", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
	private static void blockOverlay(MinecraftClient minecraftClient, MatrixStack matrixStack, CallbackInfo ci) {
		if (!minecraftClient.player.isSpectator()) {
			if (minecraftClient.player.isSubmergedIn(SpectrumFluidTags.LIQUID_CRYSTAL)) {
				renderOverlay(minecraftClient, matrixStack, TEXTURE_IN_LIQUID_CRYSTAL, 0.6F);
			} else if (minecraftClient.player.isSubmergedIn(SpectrumFluidTags.MUD)) {
				renderOverlay(minecraftClient, matrixStack, TEXTURE_IN_MUD, 0.995F);
			} else if (minecraftClient.player.isSubmergedIn(SpectrumFluidTags.MIDNIGHT_SOLUTION)) {
				renderOverlay(minecraftClient, matrixStack, TEXTURE_IN_MIDNIGHT_SOLUTION, 0.995F);
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
		bufferBuilder.end();
		BufferRenderer.draw(bufferBuilder);
		RenderSystem.disableBlend();
	}
	
}