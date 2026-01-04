package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;


public class PhantomHeadModel extends SpectrumSkullModel {
	
	public PhantomHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		modelPartData.addOrReplaceChild(
				PartNames.HEAD,
				CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -3.0F, -2.5F, 7.0F, 3.0F, 5.0F),
				PartPose.ZERO
		);
		
		return LayerDefinition.create(modelData, 64, 64);
	}
	
}