package de.dafuqs.spectrum.blocks.mob_head.models;


import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class DolphinHeadModel extends SpectrumHeadModel {

    public DolphinHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData ModelData = new ModelData();
        ModelPartData ModelPartData = ModelData.getRoot();

        ModelPartData head = ModelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
                .uv(0, 0).cuboid(-4.0F, -7.0F, -3.0F, 8.0F, 7.0F, 6.0F)
                .uv(0, 13).cuboid(-1.0F, -2.0F, -7.0F, 2.0F, 2.0F, 4.0F), ModelTransform.NONE);

        return TexturedModelData.of(ModelData, 64, 64);
    }

}