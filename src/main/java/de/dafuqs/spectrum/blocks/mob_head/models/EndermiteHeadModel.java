package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class EndermiteHeadModel extends SpectrumHeadModel {

    public EndermiteHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        modelPartData.addChild(
                EntityModelPartNames.HEAD,
                ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-2.0F, -3.0F, -5.0F, 4.0F, 3.0F, 2.0F)
                        .uv(0, 5).cuboid(-3.0F, -4.0F, -3.0F, 6.0F, 4.0F, 5.0F),
                ModelTransform.NONE
        );

        return TexturedModelData.of(modelData, 64, 32);
    }

}