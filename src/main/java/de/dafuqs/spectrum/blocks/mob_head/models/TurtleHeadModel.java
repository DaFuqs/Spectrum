package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class TurtleHeadModel extends SpectrumHeadModel {

    public TurtleHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        
        modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(3, 0).cuboid(-3.0F, -1.0F, -3.0F, 6.0F, 5.0F, 6.0F), ModelTransform.NONE);
    
        return TexturedModelData.of(modelData, 128, 64);
    }

}