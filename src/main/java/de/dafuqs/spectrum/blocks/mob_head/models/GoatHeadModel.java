package de.dafuqs.spectrum.blocks.mob_head.models;


import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class GoatHeadModel extends SpectrumHeadModel {

    public GoatHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData ModelData = new ModelData();
        ModelPartData ModelPartData = ModelData.getRoot();

        ModelPartData head = ModelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
                .uv(12, 55).cuboid(0.25F, -14.0F, -1.0F, 2.0F, 7.0F, 2.0F)
                .uv(12, 55).cuboid(-2.25F, -14.0F, -1.0F, 2.0F, 7.0F, 2.0F)
                .uv(2, 61).cuboid(-5.5F, -9.0F, -1.0F, 3.0F, 2.0F, 1.0F)
                .uv(2, 61).mirrored().cuboid(2.5F, -9.0F, -1.0F, 3.0F, 2.0F, 1.0F).mirrored(false)
                .uv(23, 52).cuboid(0.0F, -2.0F, -7.0F, 0.0F, 7.0F, 5.0F), ModelTransform.NONE);

        head.addChild("cube_r1", ModelPartBuilder.create()
                .uv(34, 46).cuboid(-2.0F, -4.0F, -5.0F, 5.0F, 7.0F, 10.0F), ModelTransform.of(-0.5F, -3.5F, -2.0F, 0.7854F, 0.0F, 0.0F));

        return TexturedModelData.of(ModelData, 64, 64);
    }

}