package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class CowHeadModel extends SpectrumSkullModel {

    public CowHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        
        modelPartData.addChild(
                EntityModelPartNames.HEAD,
                ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-4.0F, -8.0F, -3.0F, 8.0F, 8.0F, 6.0F)
                        .uv(22, 0).cuboid(EntityModelPartNames.RIGHT_HORN, -5.0F, -9.0F, -1.0F, 1.0F, 3.0F, 1.0F)
                        .uv(22, 0).cuboid(EntityModelPartNames.LEFT_HORN, 4.0F, -9.0F, -1.0F, 1.0F, 3.0F, 1.0F),
                ModelTransform.NONE
        );
        
        return TexturedModelData.of(modelData, 64, 32);
    }

}