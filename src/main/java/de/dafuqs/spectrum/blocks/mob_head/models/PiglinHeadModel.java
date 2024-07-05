package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class PiglinHeadModel extends SpectrumHeadModel {

    public PiglinHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        
        ModelPartData head = modelPartData.addChild(
                EntityModelPartNames.HEAD,
                ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F)
                        .uv(31, 1).cuboid(-2.0F, -4.0F, -5.0F, 4.0F, 4.0F, 1.0F)
                        .uv(2, 0).cuboid(-3.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F)
                        .uv(2, 0).cuboid(2.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F),
                ModelTransform.pivot(0.0F, 24.0F, 0.0F)
        );
		
		head.addChild("cube_r1", ModelPartBuilder.create().uv(51, 6).cuboid(0.0F, -1.0F, -2.0F, 1.0F, 5.0F, 4.0F, Dilation.NONE), ModelTransform.of(5.0F, -6.0F, 0.0F, 0.0F, 0.0F, -0.3927F));
		head.addChild("cube_r2", ModelPartBuilder.create().uv(39, 6).cuboid(-1.0F, -1.0F, -2.0F, 1.0F, 5.0F, 4.0F, Dilation.NONE), ModelTransform.of(-5.0F, -6.0F, 0.0F, 0.0F, 0.0F, 0.3927F));
    
        return TexturedModelData.of(modelData, 64, 64);
    }

}