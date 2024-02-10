package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class SilverfishHeadModel extends SpectrumHeadModel {

    public SilverfishHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        modelPartData.addChild(
                EntityModelPartNames.HEAD,
                ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-1.5F, -2.0F, -3.0F, 3.0F, 2.0F, 2.0F)
                        .uv(0, 4).cuboid(-2.0F, -3.0F, -1.0F, 4.0F, 3.0F, 2.0F)
                        .uv(22, 20).cuboid(-3.0F, -5.0F, -1.0F, 6.0F, 5.0F, 0.0F),
                ModelTransform.NONE
        );

        return TexturedModelData.of(modelData, 64, 32);
    }

}