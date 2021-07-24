package de.dafuqs.pigment.mixin;

import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InGameOverlayRenderer.class)
public class BlockOverlayRendererMixin {

    // Since the hack in PigmentFluid to allow swimming, sounds, particles for fluids
    // this does now work because "isSubmergedIn()" only matches for water
    /*private static final Identifier TEXTURE_IN_LIQUID_CRYSTAL = new Identifier(PigmentCommon.MOD_ID + ":textures/misc/liquid_crystal_overlay.png");
    private static final Identifier TEXTURE_IN_MUD = new Identifier(PigmentCommon.MOD_ID + ":textures/misc/mud_overlay.png");

    @Inject(method = "renderOverlays(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/util/math/MatrixStack;)V", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void blockOverlay(MinecraftClient minecraftClient, MatrixStack matrixStack, CallbackInfo ci) {
        if (!minecraftClient.player.isSpectator()) {
            if (minecraftClient.player.isSubmergedIn(PigmentFluidTags.LIQUID_CRYSTAL)) {
                renderOverlay(minecraftClient, matrixStack, TEXTURE_IN_LIQUID_CRYSTAL, 0.42F);
            }

            if (minecraftClient.player.isSubmergedIn(PigmentFluidTags.MUD)) {
                renderOverlay(minecraftClient, matrixStack, TEXTURE_IN_MUD, 0.98F);
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
        Matrix4f matrix4f = matrixStack.peek().getModel();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).texture(4.0F + m, 4.0F + n).next();
        bufferBuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).texture(0.0F + m, 4.0F + n).next();
        bufferBuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).texture(0.0F + m, 0.0F + n).next();
        bufferBuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).texture(4.0F + m, 0.0F + n).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
        RenderSystem.disableBlend();
    }*/

}