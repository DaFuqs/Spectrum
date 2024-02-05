package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class HorseHeadModel extends SpectrumHeadModel {

    public HorseHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 13).cuboid(-3.0F, -5.0F, -4.0F, 6.0F, 5.0F, 7.0F)
                .uv(19, 16).cuboid(-2.5F, -7.0F, 2.0F, 2.0F, 2.0F, 1.0F)
                .uv(19, 16).cuboid(0.5F, -7.0F, 2.0F, 2.0F, 2.0F, 1.0F)
                .uv(0, 25).cuboid(-2.0F, -5.0F, -9.0F, 4.0F, 5.0F, 5.0F), ModelTransform.NONE);

        return TexturedModelData.of(modelData, 64, 64);
    }

}