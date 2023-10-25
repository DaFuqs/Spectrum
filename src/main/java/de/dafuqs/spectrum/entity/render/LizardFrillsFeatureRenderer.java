package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.entity.models.*;
import de.dafuqs.spectrum.entity.variants.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.util.math.*;
import org.joml.*;

@Environment(EnvType.CLIENT)
public class LizardFrillsFeatureRenderer<T extends LizardEntity> extends FeatureRenderer<T, LizardEntityModel<T>> {
    
    public LizardFrillsFeatureRenderer(FeatureRendererContext<T, LizardEntityModel<T>> context) {
        super(context);
    }
    
    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T lizard, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        LizardFrillVariant frills = lizard.getFrills();
        if (frills != LizardFrillVariant.NONE) {
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(SpectrumRenderLayers.GlowInTheDarkRenderLayer.get(frills.texture()));
            Vector3f color = lizard.getColor().getColor();
            this.getContextModel().render(matrices, vertexConsumer, 15728640, OverlayTexture.DEFAULT_UV, color.x(), color.y(), color.z(), 1.0F);
        }
    }
    
}
