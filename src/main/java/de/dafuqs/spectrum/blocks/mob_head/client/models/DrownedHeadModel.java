package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.neoforged.api.distmarker.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

@OnlyIn(Dist.CLIENT)
public class DrownedHeadModel extends SpectrumSkullModel {
	
	public DrownedHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		CubeDeformation dilation = new CubeDeformation(0.01F);
		modelPartData.addOrReplaceChild(
				PartNames.HEAD,
				CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation),
				PartPose.ZERO
		);
		
		return LayerDefinition.create(modelData, 64, 64);
	}
	
}
