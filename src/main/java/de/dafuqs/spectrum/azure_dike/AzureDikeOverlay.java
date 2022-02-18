package de.dafuqs.spectrum.azure_dike;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class AzureDikeOverlay {
    
    private final static Identifier texture = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/azure_dike_overlay.png");
    
    private final static int TEXTURE_HEIGHT = 7;
    private final static int TEXTURE_WIDTH = 80;
    
    private final static float strength = 1.0F;
    private final static float red = 1.0F;
    private final static float green = 1.0F;
    private final static float blue = 1.0F;
    
    public static void render() {
        MinecraftClient client = MinecraftClient.getInstance();
        Entity cameraEntity = client.getCameraEntity();
        if(cameraEntity instanceof LivingEntity livingEntity) {
            int charges = AzureDikeProvider.getAzureDikeCharges(livingEntity);
    
            int i = client.getWindow().getScaledWidth();
            int j = client.getWindow().getScaledHeight();
    
            double d, e, l, m, n;
            float g, h, k, a;
    
            StatusEffectInstance nausea = livingEntity.getStatusEffect(StatusEffects.NAUSEA);
            if (nausea == null) {
                g = red;
                h = green;
                k = blue;
                a = strength;
                e = i;
                l = j;
                m = 0;
                n = 0;
            } else {
                d = MathHelper.lerp(charges, 2.0D, 1.0D);
                g = red * charges;
                h = green * charges;
                k = blue * charges;
                e = (double) i * d;
                l = (double) j * d;
                m = ((double) i - e) / 2.0D;
                n = ((double) j - l) / 2.0D;
                a = 1.0F;
            }
    
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            if(nausea == null) {
                RenderSystem.defaultBlendFunc();
            } else {
                RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE);
            }
            
            RenderSystem.setShaderColor(g, h, k, a);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, texture);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            bufferBuilder.vertex(m, n + l, -90.0D).texture(0.0F, 1.0F).next();
            bufferBuilder.vertex(m + e, n + l, -90.0D).texture(1.0F, 1.0F).next();
            bufferBuilder.vertex(m + e, n, -90.0D).texture(1.0F, 0.0F).next();
            bufferBuilder.vertex(m, n, -90.0D).texture(0.0F, 0.0F).next();
            tessellator.draw();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
        }
    }
    
}