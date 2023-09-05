package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class BatHeadModel extends SpectrumHeadModel {

    public BatHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData ModelData = new ModelData();
        ModelPartData ModelPartData = ModelData.getRoot();

        ModelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F)
                .uv(24, 0).cuboid(-4.0F, -9.0F, -2.0F, 3.0F, 4.0F, 1.0F)
                .uv(24, 0).cuboid(1.0F, -9.0F, -2.0F, 3.0F, 4.0F, 1.0F), ModelTransform.NONE);

        return TexturedModelData.of(ModelData, 64, 64);
    }

}