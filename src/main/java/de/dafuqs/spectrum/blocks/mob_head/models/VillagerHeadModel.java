package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class VillagerHeadModel extends SpectrumHeadModel {

    public VillagerHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        modelPartData.addChild(
                EntityModelPartNames.HEAD,
                ModelPartBuilder.create()
                        .uv(0, 0).cuboid(0.0F, -10.0F, 4.0F, 8.0F, 10.0F, 8.0F)
                        .uv(24, 0).cuboid(3.0F, -3.0F, 2.0F, 2.0F, 4.0F, 2.0F),
                ModelTransform.NONE
        );

        return TexturedModelData.of(modelData, 64, 64);
    }

}