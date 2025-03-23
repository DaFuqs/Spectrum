package de.dafuqs.spectrum.blocks.mob_head.client.models;


import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class WolfHeadModel extends SpectrumSkullModel {

    public WolfHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        modelPartData.addChild(
                EntityModelPartNames.HEAD,
                ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-3.0F, -6.0F, -2.0F, 6.0F, 6.0F, 4.0F) // head
                        .uv(0, 10).cuboid(-1.5F, -3.001F, -5.0F, 3.0F, 3.0F, 4.0F) // hose
                        .uv(16, 14).cuboid(-3.0F, -8.0F, 0.0F, 2.0F, 2.0F, 1.0F) // right ear
                        .uv(16, 14).cuboid(1.0F, -8.0F, 0.0F, 2.0F, 2.0F, 1.0F), // left ear
                ModelTransform.NONE
        );

        return TexturedModelData.of(modelData, 64, 32);
    }

}