package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class RabbitHeadModel extends SpectrumSkullModel {

    public RabbitHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        
        ModelPartData head = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(32, 0).cuboid(-2.5F, -4.0F, -2.5F, 5.0F, 4.0F, 5.0F), ModelTransform.NONE);
        head.addChild("nose", ModelPartBuilder.create().uv(32, 9).cuboid(-0.5F, -2.5F, -3.0F, 1.0F, 1.0F, 1.0F), ModelTransform.NONE);
        head.addChild("right_ear", ModelPartBuilder.create().uv(52, 0).cuboid(-2.5F, -9.0F, -1.0F, 2.0F, 5.0F, 1.0F), ModelTransform.of(0.0F, 0.0F, 2.5F, 0.0F, -0.2617994F, 0.0F));
        head.addChild("left_ear", ModelPartBuilder.create().uv(58, 0).cuboid(0.5F, -9.0F, -1.0F, 2.0F, 5.0F, 1.0F), ModelTransform.of(0.0F, 0.0F, 2.5F, 0.0F, 0.2617994F, 0.0F));
    
        return TexturedModelData.of(modelData, 64, 32);
    }

}