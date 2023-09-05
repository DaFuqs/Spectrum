package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class RabbitHeadModel extends SpectrumHeadModel {

    public RabbitHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData ModelData = new ModelData();
        ModelPartData ModelPartData = ModelData.getRoot();

        ModelPartData head = ModelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
                .uv(32, 9).cuboid(-0.5F, -2.0F, -3.0F, 1.0F, 1.0F, 1.0F)
                .uv(32, 0).cuboid(-2.5F, -4.0F, -2.5F, 5.0F, 4.0F, 5.0F), ModelTransform.NONE);

        head.addChild("cube_r1", ModelPartBuilder.create().uv(58, 0).cuboid(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(1.5F, -6.5F, 1.5F, 0.0F, 0.3927F, 0.0F));
        head.addChild("cube_r2", ModelPartBuilder.create().uv(52, 0).cuboid(-1.0F, -2.5F, -0.5F, 2.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(-1.5F, -6.5F, 1.5F, 0.0F, -0.3927F, 0.0F));

        return TexturedModelData.of(ModelData, 64, 32);
    }

}