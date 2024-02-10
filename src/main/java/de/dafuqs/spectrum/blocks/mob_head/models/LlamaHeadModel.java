package de.dafuqs.spectrum.blocks.mob_head.models;


import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class LlamaHeadModel extends SpectrumHeadModel {

    public LlamaHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        modelPartData.addChild(
                EntityModelPartNames.HEAD,
                ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-2.0F, -17.0F, -8.0F, 4.0F, 4.0F, 9.0F)
                        .uv(17, 0).cuboid(-4.0F, -22.0F, -1.0F, 3.0F, 3.0F, 2.0F)
                        .uv(17, 0).cuboid(1.0F, -22.0F, -1.0F, 3.0F, 3.0F, 2.0F)
                        .uv(0, 14).cuboid(-4.0F, -19.0F, -4.0F, 8.0F, 18.0F, 6.0F),
                ModelTransform.NONE
        );

        return TexturedModelData.of(modelData, 128, 64);
    }

}