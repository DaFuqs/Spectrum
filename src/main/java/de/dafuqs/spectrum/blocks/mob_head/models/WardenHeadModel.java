package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class WardenHeadModel extends SpectrumHeadModel {

    public WardenHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        
        ModelPartData head = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 32).cuboid(-8.0F, -16.0F, -5.0F, 16.0F, 16.0F, 10.0F), ModelTransform.NONE);
        head.addChild(EntityModelPartNames.LEFT_TENDRIL, ModelPartBuilder.create().uv(52, 32).cuboid(-16.0F, -13.0F, 0.0F, 16.0F, 16.0F, 0.0F), ModelTransform.pivot(-8.0F, -12.0F, 0.0F));
        head.addChild(EntityModelPartNames.RIGHT_TENDRIL, ModelPartBuilder.create().uv(58, 0).cuboid(0.0F, -13.0F, 0.0F, 16.0F, 16.0F, 0.0F), ModelTransform.pivot(8.0F, -12.0F, 0.0F));

        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public float getScale() {
        return 0.6F;
    }

}