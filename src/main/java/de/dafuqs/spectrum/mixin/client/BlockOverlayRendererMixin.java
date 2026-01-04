package de.dafuqs.spectrum.mixin.client;

import net.minecraft.client.renderer.*;
import org.spongepowered.asm.mixin.*;


@Mixin(ScreenEffectRenderer.class)
public abstract class BlockOverlayRendererMixin {
	
	// TODO: still needed?
	/*
	@Inject(method = "renderScreenEffect", at = @At("TAIL"))
	private static void spectrum$renderFluidOverlay(Minecraft minecraft, PoseStack poseStack, CallbackInfo ci) {
		if (!minecraft.player.isSpectator()) {
			if (minecraft.player.isEyeInFluid(SpectrumFluidTags.LIQUID_CRYSTAL)) {
				spectrum$renderOverlay(minecraft, poseStack, SpectrumFluids.LIQUID_CRYSTAL_OVERLAY_TEXTURE, SpectrumFluids.LIQUID_CRYSTAL_OVERLAY_ALPHA);
			} else if (minecraft.player.isEyeInFluid(SpectrumFluidTags.SLUDGE)) {
				spectrum$renderOverlay(minecraft, poseStack, SpectrumFluids.SLUDGE_OVERLAY_TEXTURE, SpectrumFluids.SLUDGE_OVERLAY_ALPHA);
			} else if (minecraft.player.isEyeInFluid(SpectrumFluidTags.MIDNIGHT_SOLUTION)) {
				spectrum$renderOverlay(minecraft, poseStack, SpectrumFluids.MIDNIGHT_SOLUTION_OVERLAY_TEXTURE, SpectrumFluids.MIDNIGHT_SOLUTION_OVERLAY_ALPHA);
			} else if (minecraft.player.isEyeInFluid(SpectrumFluidTags.DRAGONROT)) {
				spectrum$renderOverlay(minecraft, poseStack, SpectrumFluids.DRAGONROT_OVERLAY_TEXTURE, SpectrumFluids.DRAGONROT_OVERLAY_ALPHA);
			}
		}
	}

	@Unique
	private static void spectrum$renderOverlay(Minecraft client, PoseStack poseStack, ResourceLocation textureIdentifier, float alpha) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, textureIdentifier);
		BlockPos blockPos = BlockPos.containing(client.player.getX(), client.player.getEyeY(), client.player.getZ());
		float f = LightTexture.getBrightness(client.player.level().dimensionType(), client.player.level().getMaxLocalRawBrightness(blockPos));
		RenderSystem.enableBlend();
		RenderSystem.setShaderColor(f, f, f, alpha);
		
		float m = -client.player.getYRot() / 64.0F;
		float n = client.player.getXRot() / 64.0F;
		Matrix4f matrix4f = poseStack.last().pose();
		BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.addVertex(matrix4f, -1.0F, -1.0F, -0.5F).setUv(4.0F + m, 4.0F + n);
		bufferBuilder.addVertex(matrix4f, 1.0F, -1.0F, -0.5F).setUv(0.0F + m, 4.0F + n);
		bufferBuilder.addVertex(matrix4f, 1.0F, 1.0F, -0.5F).setUv(0.0F + m, 0.0F + n);
		bufferBuilder.addVertex(matrix4f, -1.0F, 1.0F, -0.5F).setUv(4.0F + m, 0.0F + n);
		BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableBlend();
	}*/
	
}