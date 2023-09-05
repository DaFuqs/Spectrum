package de.dafuqs.spectrum.blocks.mob_head.models;


import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class WitchHeadModel extends SpectrumHeadModel {

    public WitchHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData ModelData = new ModelData();
        ModelPartData ModelPartData = ModelData.getRoot();

        ModelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
                .uv(0, 0).cuboid(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F)
                .uv(24, 0).cuboid(-1.0F, -3.0F, -6.0F, 2.0F, 4.0F, 2.0F)
                .uv(0, 64).cuboid(-5.0F, -11.0F, -5.0F, 10.0F, 1.0F, 10.0F)
                .uv(0, 76).cuboid(-3.5F, -15.0F, -3.5F, 7.0F, 4.0F, 7.0F)
                .uv(0, 87).cuboid(-2.0F, -19.0F, -2.0F, 4.0F, 4.0F, 4.0F)
                .uv(0, 95).cuboid(0.0F, -20.0F, -1.0F, 1.0F, 1.0F, 1.0F), ModelTransform.NONE);

        return TexturedModelData.of(ModelData, 64, 128);
    }

}