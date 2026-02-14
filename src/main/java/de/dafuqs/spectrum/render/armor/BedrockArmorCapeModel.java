package de.dafuqs.spectrum.render.armor;

import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

public class BedrockArmorCapeModel {
	public static final ModelPart CAPE_MODEL = createCape();
	public static final ModelPart FRONT_CLOTH = createFrontCloth();
	
	private static ModelPart createCape() {
		MeshDefinition data = new MeshDefinition();
		var root = data.getRoot();
		
		root.addOrReplaceChild(
				"cape",
				CubeListBuilder.create()
						.texOffs(0, 80)
						.addBox(-5.5F, 0.0F, -0.05F, 11.0F, 23.0F, 1.0F),
				PartPose.offset(0.0F, 0.5F, 2.9F)
		);
		
		return data.getRoot().bake(128, 128);
	}
	
	private static ModelPart createFrontCloth() {
		MeshDefinition data = new MeshDefinition();
		var root = data.getRoot();
		
		root.addOrReplaceChild(
				"loincloth",
				CubeListBuilder.create()
						.texOffs(62, 55)
						.addBox(-3.5F, 0.0F, 0.0F, 7.0F, 14.0F, 1.0F),
				PartPose.offset(0.0F, 14.0F, 0F)
		);
		return data.getRoot().bake(128, 128);
	}
	
}