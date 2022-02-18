package de.dafuqs.spectrum.azure_dike;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AzureDikeOverlay {
    

    private final static int TEXTURE_HEIGHT = 7;
    private final static int TEXTURE_WIDTH = 80;
    
    private final static float alpha = 1.0F;
    private final static float red = 1.0F;
    private final static float green = 1.0F;
    private final static float blue = 1.0F;
    
    public static void render() {
        /*MinecraftClient client = MinecraftClient.getInstance();
        Entity cameraEntity = client.getCameraEntity();
        if(cameraEntity instanceof LivingEntity livingEntity) {
            int charges = AzureDikeProvider.getAzureDikeCharges(livingEntity);
            
            int scaledWindowWidth = client.getWindow().getScaledWidth();
            int scaledWindowHeight = client.getWindow().getScaledHeight();
    
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            
            RenderSystem.setShaderColor(red, green, blue, alpha);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, texture);
            
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
    
            bufferBuilder.vertex(0, scaledWindowHeight, -90.0D).texture(0.0F, 1.0F).next();
            bufferBuilder.vertex(scaledWindowWidth, scaledWindowHeight, -90.0D).texture(1.0F, 1.0F).next();
            bufferBuilder.vertex(scaledWindowWidth, 0, -90.0D).texture(1.0F, 0.0F).next();
            bufferBuilder.vertex(0, 0, -90.0D).texture(0.0F, 0.0F).next();
            
            //bufferBuilder.vertex(0, scaledWindowHeight, -90.0D).texture(0.0F, 1.0F).next();
            //bufferBuilder.vertex(scaledWindowWidth, scaledWindowHeight, -90.0D).texture(1.0F, 1.0F).next();
            //bufferBuilder.vertex(scaledWindowWidth, 0, -90.0D).texture(1.0F, 0.0F).next();
            //bufferBuilder.vertex(0, 0, -90.0D).texture(0.0F, 0.0F).next();
            tessellator.draw();
            
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
        }*/
    }
    
}