package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class FoxHeadModel extends SpectrumHeadModel {

    public FoxHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
                .uv(1, 5).cuboid(-4.0F, -6.0F, -3.0F, 8.0F, 6.0F, 6.0F)
                .uv(6, 18).cuboid(-2.0F, -2.0F, -6.0F, 4.0F, 2.0F, 3.0F)
                .uv(8, 1).cuboid(-4.0F, -8.0F, -2.0F, 2.0F, 2.0F, 1.0F)
                .uv(15, 1).cuboid(2.0F, -8.0F, -2.0F, 2.0F, 2.0F, 1.0F), ModelTransform.NONE);

        return TexturedModelData.of(modelData, 48, 32);
    }

}