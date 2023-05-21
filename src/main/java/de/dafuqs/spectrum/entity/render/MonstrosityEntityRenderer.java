package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.entity.models.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.*;

@Environment(EnvType.CLIENT)
public class MonstrosityEntityRenderer extends MobEntityRenderer<MonstrosityEntity, MonstrosityEntityModel> {
    
    private static final Identifier TEXTURE = SpectrumCommon.locate("textures/entity/monstrosity.png");
    
    public MonstrosityEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new MonstrosityEntityModel(context.getPart(SpectrumModelLayers.MONSTROSITY)), 1.8F);
    }
    
    public void render(MonstrosityEntity bidentEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        super.render(bidentEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
    }
    
    @Override
    public Identifier getTexture(MonstrosityEntity entity) {
        return TEXTURE;
    }
    
}
