package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class PhantomHeadModel extends SpectrumHeadModel {

    public PhantomHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData ModelData = new ModelData();
        ModelPartData ModelPartData = ModelData.getRoot();

        ModelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
                .uv(0, 0).cuboid(-3.0F, -3.0F, -3.0F, 7.0F, 3.0F, 5.0F), ModelTransform.NONE);

        return TexturedModelData.of(ModelData, 64, 64);
    }

}