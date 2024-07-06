package de.dafuqs.spectrum.blocks.mob_head.models;


import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class BearHeadModel extends SpectrumHeadModel {

    public BearHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        
        modelPartData.addChild(
                EntityModelPartNames.HEAD,
                ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-3.5F, -7.0F, -3.5F, 7.0F, 7.0F, 7.0F)
                        .uv(0, 44).cuboid("mouth", -2.5F, -3.0F, -6.5F, 5.0F, 3.0F, 3.0F)
                        .uv(26, 0).cuboid("right_ear", -4.5F, -8.0F, -1.5F, 2.0F, 2.0F, 1.0F)
                        .uv(26, 0).mirrored().cuboid("left_ear", 2.5F, -8.0F, -1.5F, 2.0F, 2.0F, 1.0F),
                ModelTransform.NONE
        );
    
        return TexturedModelData.of(modelData, 128, 64);
    }

}