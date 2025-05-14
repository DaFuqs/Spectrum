package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

public class PigHeadModel extends SpectrumSkullModel {
	
	public PigHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		modelPartData.addOrReplaceChild(
				PartNames.HEAD,
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F)
						.texOffs(16, 16).addBox(-2.0F, -4.0F, -5.0F, 4.0F, 3.0F, 1.0F),
				PartPose.ZERO
		);
		
		return LayerDefinition.create(modelData, 64, 32);
		
	}
	
}