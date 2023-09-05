package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class CatHeadModel extends SpectrumHeadModel {

    public CatHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData ModelData = new ModelData();
        ModelPartData ModelPartData = ModelData.getRoot();

        ModelPartData head = ModelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
                .uv(0, 0).cuboid(-2.5F, -4.0F, -3.0F, 5.0F, 4.0F, 5.0F)
                .uv(0, 10).cuboid(-2.0F, -5.0F, 0.0F, 1.0F, 1.0F, 2.0F)
                .uv(6, 10).cuboid(1.0F, -5.0F, 0.0F, 1.0F, 1.0F, 2.0F)
                .uv(0, 24).cuboid(-1.5F, -2.001F, -4.0F, 3.0F, 2.0F, 2.0F), ModelTransform.NONE);

        return TexturedModelData.of(ModelData, 64, 32);
    }

}