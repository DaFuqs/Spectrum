package de.dafuqs.spectrum.blocks.mob_head.models;


import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class GoatHeadModel extends SpectrumHeadModel {

    public GoatHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        
        ModelPartData head = modelPartData.addChild(
                EntityModelPartNames.HEAD,
                ModelPartBuilder.create()
                        .uv(2, 61).cuboid(EntityModelPartNames.RIGHT_EAR, -6.0F, -11.0F, -10.0F, 3.0F, 2.0F, 1.0F)
                        .uv(2, 61).mirrored().cuboid(EntityModelPartNames.LEFT_EAR, 2.0F, -11.0F, -10.0F, 3.0F, 2.0F, 1.0F)
                        .uv(23, 52).cuboid("goatee", -0.5F, -3.0F, -14.0F, 0.0F, 7.0F, 5.0F),
                ModelTransform.NONE
        );
        head.addChild(
                EntityModelPartNames.LEFT_HORN,
                ModelPartBuilder.create().uv(12, 55).cuboid(-0.01F, -16.0F, -10.0F, 2.0F, 7.0F, 2.0F),
                ModelTransform.NONE
        );
        head.addChild(
                EntityModelPartNames.RIGHT_HORN,
                ModelPartBuilder.create().uv(12, 55).cuboid(-2.99F, -16.0F, -10.0F, 2.0F, 7.0F, 2.0F),
                ModelTransform.NONE
        );
        head.addChild(
                EntityModelPartNames.NOSE,
                ModelPartBuilder.create().uv(34, 46).cuboid(-3.0F, -4.0F, -8.0F, 5.0F, 7.0F, 10.0F),
                ModelTransform.of(0.0F, -8.0F, -8.0F, 0.9599F, 0.0F, 0.0F)
        );
    
        return TexturedModelData.of(modelData, 64, 64);
    }

}