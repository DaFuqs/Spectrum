package de.dafuqs.spectrum.render.armor;

import net.minecraft.client.model.*;

public class BedrockArmorCapeModel {
    public static final ModelPart CAPE_MODEL = createCape();
    public static final ModelPart FRONT_CLOTH = createFrontCloth();

    private static ModelPart createCape() {
        ModelData data = new ModelData();
        var root = data.getRoot();
        // TODO - Move cape here

        return data.getRoot().createPart(128, 128);
    }

    private static ModelPart createFrontCloth() {
        ModelData data = new ModelData();
        var root = data.getRoot();

        root.addChild(
            "loincloth",
            ModelPartBuilder.create()
                .uv(72, 78)
                .cuboid(-3.5F, 0.0F, -0.3F, 7.0F, 15.0F, 0.0F, Dilation.NONE),
            ModelTransform.pivot(0.0F, 14.5F, 1.05F)
        );
        return data.getRoot().createPart(128, 128);
    }


}
