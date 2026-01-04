package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;


public class TurtleHeadModel extends SpectrumSkullModel {
	
	public TurtleHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		modelPartData.addOrReplaceChild(
				PartNames.HEAD,
				CubeListBuilder.create().texOffs(3, 0).addBox(-3.0F, -5.0F, -3.0F, 6.0F, 5.0F, 6.0F),
				PartPose.ZERO
		);
		
		return LayerDefinition.create(modelData, 128, 64);
	}
	
}