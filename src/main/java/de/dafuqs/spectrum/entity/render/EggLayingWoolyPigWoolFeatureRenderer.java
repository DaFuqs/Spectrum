package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.entity.entity.EggLayingWoolyPigEntity;
import de.dafuqs.spectrum.entity.models.EggLayingWoolyPigEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EggLayingWoolyPigWoolFeatureRenderer extends FeatureRenderer<EggLayingWoolyPigEntity, EggLayingWoolyPigEntityModel<EggLayingWoolyPigEntity>> {
    
    private static final Identifier SKIN = new Identifier("textures/entity/sheep/sheep_fur.png");
    private final EggLayingWoolyPigEntityModel<EggLayingWoolyPigEntity> model;
    
    public EggLayingWoolyPigWoolFeatureRenderer(EggLayingWoolyPigEntityRenderer context, EntityModelLoader loader) {
        super(context);
        this.model = new EggLayingWoolyPigEntityModel(loader.getModelPart(EntityModelLayers.SHEEP_FUR));
    }
    
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, EggLayingWoolyPigEntity entity, float f, float g, float h, float j, float k, float l) {
        if (!entity.isSheared()) {
            if (entity.isInvisible()) {
                MinecraftClient minecraftClient = MinecraftClient.getInstance();
                boolean bl = minecraftClient.hasOutline(entity);
                if (bl) {
                    this.getContextModel().copyStateTo(this.model);
                    this.model.animateModel(entity, f, g, h);
                    this.model.setAngles(entity, f, g, j, k, l);
                    VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getOutline(SKIN));
                    this.model.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(entity, 0.0F), 0.0F, 0.0F, 0.0F, 1.0F);
                }
            } else {
                float[] rgbColor = EggLayingWoolyPigEntity.getRgbColor(entity.getColor());
                render(this.getContextModel(), this.model, SKIN, matrixStack, vertexConsumerProvider, i, entity, f, g, j, k, l, h, rgbColor[0], rgbColor[1], rgbColor[2]);
            }
        }
    }
}
