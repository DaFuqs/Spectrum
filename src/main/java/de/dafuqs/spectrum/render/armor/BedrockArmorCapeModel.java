package de.dafuqs.spectrum.render.armor;

import net.minecraft.client.model.*;

public class BedrockArmorCapeModel {
    public static final ModelPart CAPE_MODEL = createCape();
    public static final ModelPart FRONT_CLOTH = createFrontCloth();

    private static ModelPart createCape() {
        ModelData data = new ModelData();
        var root = data.getRoot();

        root.addChild(
                "cape_bone",
                ModelPartBuilder.create()
                        .uv(0, 80)
                        .cuboid(-5.5F, 0.0F, -0.05F, 11.0F, 23.0F, 1.0F),
                ModelTransform.pivot(0.0F, 0.5F, 2.9F)
        );

        return data.getRoot().createPart(128, 128);
    }

    private static ModelPart createFrontCloth() {
        ModelData data = new ModelData();
        var root = data.getRoot();

        root.addChild(
                "cock_bone",
                ModelPartBuilder.create()
                        .uv(62, 55)
                        .cuboid(-3.5F, 0.0F, 0.0F, 7.0F, 14.0F, 1.0F),
                ModelTransform.pivot(0.0F, 14.0F, 0F)
        );
        return data.getRoot().createPart(128, 128);
    }


}
