package de.dafuqs.spectrum.blocks.mob_head.models;


import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class PandaHeadModel extends SpectrumHeadModel {

    public PandaHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData ModelData = new ModelData();
        ModelPartData ModelPartData = ModelData.getRoot();

        ModelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
                .uv(0, 6).cuboid(-6.5F, -10.0F, -4.0F, 13.0F, 10.0F, 9.0F)
                .uv(45, 16).cuboid(-3.5F, -5.0F, -6.0F, 7.0F, 5.0F, 2.0F)
                .uv(52, 25).cuboid(-8.5F, -13.0F, 0.0F, 5.0F, 4.0F, 1.0F)
                .uv(52, 25).cuboid(3.5F, -13.0F, 0.0F, 5.0F, 4.0F, 1.0F), ModelTransform.NONE);

        return TexturedModelData.of(ModelData, 64, 64);
    }

}