package de.dafuqs.spectrum.render.armor;

import net.minecraft.client.model.*;

public class BedrockArmorCapeModel {
    public static final ModelPart CAPE_MODEL = createCape();
    public static final ModelPart FRONT_CLOTH = createFrontCloth();

    private static ModelPart createCape() {
        ModelData data = new ModelData();
        var root = data.getRoot();

        root.addChild("cape", ModelPartBuilder.create()
            .uv(64, 14).cuboid(-4.5F, 1.4167F, -0.6833F, 10.0F, 5.0F, 3.0F, Dilation.NONE)
            .uv(0, 49).cuboid(-4.0F, -1.8333F, -0.0833F, 9.0F, 21.0F, 0.0F, Dilation.NONE)
            .uv(62, 78).cuboid(-6.5F, 0.1667F, 0.1667F, 5.0F, 21.0F, 0.0F, Dilation.NONE)
            .uv(52, 68).cuboid(2.5F, 0.1667F, 0.1667F, 5.0F, 21.0F, 0.0F, Dilation.NONE), ModelTransform.pivot(-0.5F, 1.8333F, 2.6833F));


        return data.getRoot().createPart(128, 128);
    }

    private static ModelPart createFrontCloth() {
        ModelData data = new ModelData();
        var root = data.getRoot();

        root.addChild(
            "loincloth",
            ModelPartBuilder.create()
                .uv(72, 78)
                .cuboid(-3.5F, -8.35F, -2.5F, 7.0F, 15.0F, 0.0F, Dilation.NONE),
            ModelTransform.pivot(0.0F, 18.0F, 0.0F)
        );
        return data.getRoot().createPart(128, 128);
    }


}
