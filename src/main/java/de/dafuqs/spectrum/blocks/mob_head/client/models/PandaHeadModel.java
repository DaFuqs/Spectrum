package de.dafuqs.spectrum.blocks.mob_head.client.models;


import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class PandaHeadModel extends SpectrumSkullModel {

    public PandaHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        
        modelPartData.addChild(
                EntityModelPartNames.HEAD,
                ModelPartBuilder.create()
                        .uv(0, 6).cuboid(-6.5F, -10.0F, -4.5F, 13.0F, 10.0F, 9.0F)
                        .uv(45, 16).cuboid("nose", -3.5F, -5.0F, -6.5F, 7.0F, 5.0F, 2.0F)
                        .uv(52, 25).cuboid("left_ear", 3.5F, -13.0F, -1.5F, 5.0F, 4.0F, 1.0F)
                        .uv(52, 25).cuboid("right_ear", -8.5F, -13.0F, -1.5F, 5.0F, 4.0F, 1.0F),
                ModelTransform.NONE
        );
    
        return TexturedModelData.of(modelData, 64, 64);
    }

}