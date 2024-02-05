package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class BeeHeadModel extends SpectrumHeadModel {

    public BeeHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-3.5F, -7.0F, -5.0F, 7.0F, 7.0F, 10.0F)
                .uv(2, 0).cuboid(-1.5F, -7.0F, -8.0F, 0.0F, 2.0F, 3.0F)
                .uv(3, 3).cuboid(-1.5F, -7.0F, -8.0F, 0.0F, 2.0F, 3.0F)
                .uv(3, 3).cuboid(1.5F, -7.0F, -8.0F, 0.0F, 2.0F, 3.0F)
                .uv(2, 0).cuboid(1.5F, -7.0F, -8.0F, 0.0F, 2.0F, 3.0F), ModelTransform.NONE);

        return TexturedModelData.of(modelData, 64, 64);
    }

}