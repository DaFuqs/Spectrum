package de.dafuqs.spectrum.mixin.client;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.cca.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.hud.*;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {
    
    @Inject(method = "renderOverlays(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/util/math/MatrixStack;)V", at = @At(value = "HEAD"))
    private static void spectrum$renderPrimordialFire(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        if (!client.player.isSpectator()) {
            if (OnPrimordialFireComponent.isOnPrimordialFire(client.player)) {
                renderPrimordialFireOverlay(client, matrices);
            }
        }
    }
    
    @Inject(method = "renderFireOverlay(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/util/math/MatrixStack;)V", at = @At(value = "HEAD"), cancellable = true)
    private static void spectrum$cancelFireOverlayWithPrimordialFire(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        if (OnPrimordialFireComponent.isOnPrimordialFire(client.player)) {
            ci.cancel();
        }
    }
    
    @Unique
    private static void renderPrimordialFireOverlay(MinecraftClient client, MatrixStack matrices) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.depthFunc(519);
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableTexture();
        Sprite sprite = client.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(SpectrumCommon.locate("block/primordial_fire_1"));
        RenderSystem.setShaderTexture(0, sprite.getAtlas().getId());
        float f = sprite.getMinU();
        float g = sprite.getMaxU();
        float h = (f + g) / 2.0F;
        float i = sprite.getMinV();
        float j = sprite.getMaxV();
        float k = (i + j) / 2.0F;
        float l = sprite.getAnimationFrameDelta();
        float m = MathHelper.lerp(l, f, h);
        float n = MathHelper.lerp(l, g, h);
        float o = MathHelper.lerp(l, i, k);
        float p = MathHelper.lerp(l, j, k);
        
        for (int r = 0; r < 2; ++r) {
            matrices.push();
            matrices.translate(((float) (-(r * 2 - 1)) * 0.24F), -0.3, 0.0);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float) (r * 2 - 1) * 10.0F));
            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
            bufferBuilder.vertex(matrix4f, -0.5F, -0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).texture(n, p).next();
            bufferBuilder.vertex(matrix4f, 0.5F, -0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).texture(m, p).next();
            bufferBuilder.vertex(matrix4f, 0.5F, 0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).texture(m, o).next();
            bufferBuilder.vertex(matrix4f, -0.5F, 0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).texture(n, o).next();
            BufferRenderer.drawWithShader(bufferBuilder.end());
            matrices.pop();
        }
        
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.depthFunc(515);
    }
    
}
