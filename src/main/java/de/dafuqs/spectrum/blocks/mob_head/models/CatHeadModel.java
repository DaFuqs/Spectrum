package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class CatHeadModel extends SpectrumHeadModel {

    public CatHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
    
        Dilation dilation = new Dilation(0.01F);
        modelPartData.addChild(
                EntityModelPartNames.HEAD,
                ModelPartBuilder.create()
                        .cuboid("main", -2.5F, -4.0F, -2.5F, 5.0F, 4.0F, 5.0F, dilation)
                        .cuboid("nose", -1.5F, -2.001F, -3.5F, 3, 2, 2, dilation, 0, 24)
                        .cuboid("ear1", -2.0F, -5.0F, 0.5F, 1, 1, 2, dilation, 0, 10)
                        .cuboid("ear2", 1.0F, -5.0F, 0.5F, 1, 1, 2, dilation, 6, 10),
                ModelTransform.NONE
        );
    
        return TexturedModelData.of(modelData, 64, 32);
    }

}