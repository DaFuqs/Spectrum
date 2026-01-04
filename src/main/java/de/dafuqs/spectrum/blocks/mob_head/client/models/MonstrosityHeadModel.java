package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;


public class MonstrosityHeadModel extends SpectrumSkullModel {
	
	public MonstrosityHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		
		modelData.getRoot().addOrReplaceChild(
				PartNames.HEAD,
				CubeListBuilder.create()
						.texOffs(56, 24).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F)
						.texOffs(0, 44).addBox(-2.5F, -4.0F, -5.0F, 5.0F, 3.0F, 1.0F)
						.texOffs(45, 0).addBox(-5.02F, -9.0F, -5.0F, 10.0F, 7.0F, 10.0F),
				PartPose.ZERO
		);
		
		return LayerDefinition.create(modelData, 128, 128);
	}
	
}