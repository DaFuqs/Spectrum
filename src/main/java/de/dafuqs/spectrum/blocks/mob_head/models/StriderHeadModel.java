package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class StriderHeadModel extends SpectrumHeadModel {

    public StriderHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        
        ModelPartData modelPartData2 = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -6.0F, -8.0F, 16.0F, 14.0F, 16.0F), ModelTransform.NONE);
        modelPartData2.addChild("right_bottom_bristle", ModelPartBuilder.create().uv(16, 65).cuboid(-12.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F, true), ModelTransform.of(-8.0F, 4.0F, -8.0F, 0.0F, 0.0F, -1.2217305F));
        modelPartData2.addChild("right_middle_bristle", ModelPartBuilder.create().uv(16, 49).cuboid(-12.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F, true), ModelTransform.of(-8.0F, -1.0F, -8.0F, 0.0F, 0.0F, -1.134464F));
        modelPartData2.addChild("right_top_bristle", ModelPartBuilder.create().uv(16, 33).cuboid(-12.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F, true), ModelTransform.of(-8.0F, -5.0F, -8.0F, 0.0F, 0.0F, -0.87266463F));
        modelPartData2.addChild("left_top_bristle", ModelPartBuilder.create().uv(16, 33).cuboid(0.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F), ModelTransform.of(8.0F, -6.0F, -8.0F, 0.0F, 0.0F, 0.87266463F));
        modelPartData2.addChild("left_middle_bristle", ModelPartBuilder.create().uv(16, 49).cuboid(0.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F), ModelTransform.of(8.0F, -2.0F, -8.0F, 0.0F, 0.0F, 1.134464F));
        modelPartData2.addChild("left_bottom_bristle", ModelPartBuilder.create().uv(16, 65).cuboid(0.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F), ModelTransform.of(8.0F, 3.0F, -8.0F, 0.0F, 0.0F, 1.2217305F));
    
        return TexturedModelData.of(modelData, 64, 128);
    }

}