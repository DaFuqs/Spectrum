package de.dafuqs.spectrum.blocks.mob_head.client.models;


import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;


public class BearHeadModel extends SpectrumSkullModel {
	
	public BearHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		modelPartData.addOrReplaceChild(
				PartNames.HEAD,
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-3.5F, -7.0F, -3.5F, 7.0F, 7.0F, 7.0F)
						.texOffs(0, 44).addBox("mouth", -2.5F, -3.0F, -6.5F, 5.0F, 3.0F, 3.0F)
						.texOffs(26, 0).addBox("right_ear", -4.5F, -8.0F, -1.5F, 2.0F, 2.0F, 1.0F)
						.texOffs(26, 0).mirror().addBox("left_ear", 2.5F, -8.0F, -1.5F, 2.0F, 2.0F, 1.0F),
				PartPose.ZERO
		);
		
		return LayerDefinition.create(modelData, 128, 64);
	}
	
}