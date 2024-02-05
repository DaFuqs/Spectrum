package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class ParrotHeadModel extends SpectrumHeadModel {

    public ParrotHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData head = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
                .uv(2, 2).cuboid(-9.0F, -3.0F, 7.0F, 2.0F, 3.0F, 2.0F)
                .uv(10, 0).cuboid(-9.0F, -4.0F, 5.0F, 2.0F, 1.0F, 4.0F)
                .uv(11, 7).cuboid(-8.5F, -3.0F, 6.0F, 1.0F, 2.0F, 1.0F)
                .uv(17, 7).cuboid(-8.25F, -3.0F, 5.0F, 0.5F, 2.0F, 1.0F), ModelTransform.NONE);

        head.addChild("cube_r1", ModelPartBuilder.create()
                .uv(2, 18).cuboid(-0.25F, -2.5F, -1.0F, 0.0F, 5.0F, 4.0F), ModelTransform.NONE);

        return TexturedModelData.of(modelData, 32, 32);
    }

}