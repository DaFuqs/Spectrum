package de.dafuqs.spectrum.render.armor;

import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

public class BedrockArmorCapeModel {
    public static final ModelPart CAPE_MODEL = createCape();
    public static final ModelPart FRONT_CLOTH = createFrontCloth();

    private static ModelPart createCape() {
		MeshDefinition data = new MeshDefinition();
        var root = data.getRoot();
		
		root.addOrReplaceChild("cape", CubeListBuilder.create()
				.texOffs(64, 14).addBox(-4.5F, 1.4167F, -0.6833F, 10.0F, 5.0F, 3.0F, CubeDeformation.NONE)
				.texOffs(0, 49).addBox(-4.0F, -1.8333F, 0.1667F, 9.0F, 21.0F, 0.0F, CubeDeformation.NONE)
				.texOffs(62, 78).addBox(-6.5F, 0.1667F, -0.0833F, 5.0F, 21.0F, 0.0F, CubeDeformation.NONE)
				.texOffs(52, 68).addBox(2.5F, 0.1667F, -0.0833F, 5.0F, 21.0F, 0.0F, CubeDeformation.NONE), PartPose.offset(-0.5F, 1.8333F, 2.6833F));
		
		
		return data.getRoot().bake(128, 128);
    }

    private static ModelPart createFrontCloth() {
		MeshDefinition data = new MeshDefinition();
        var root = data.getRoot();
		
		root.addOrReplaceChild(
            "loincloth",
				CubeListBuilder.create()
						.texOffs(72, 78)
						.addBox(-3.5F, -8.35F, -2.5F, 7.0F, 15.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 18.0F, 0.0F)
        );
		return data.getRoot().bake(128, 128);
    }


}
