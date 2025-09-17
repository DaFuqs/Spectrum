package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.neoforged.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class FoxHeadModel extends SpectrumSkullModel {
	
	public FoxHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		PartDefinition head = modelPartData.addOrReplaceChild(
				PartNames.HEAD,
				CubeListBuilder.create().texOffs(1, 5).addBox(-4.0F, -6.0F, -3.0F, 8.0F, 6.0F, 6.0F),
				PartPose.ZERO
		);
		head.addOrReplaceChild(PartNames.RIGHT_EAR,
				CubeListBuilder.create().texOffs(8, 1).addBox(-4.0F, -8.0F, -2.0F, 2.0F, 2.0F, 1.0F),
				PartPose.ZERO
		);
		head.addOrReplaceChild(PartNames.LEFT_EAR,
				CubeListBuilder.create().texOffs(15, 1).addBox(2.0F, -8.0F, -2.0F, 2.0F, 2.0F, 1.0F),
				PartPose.ZERO
		);
		head.addOrReplaceChild(PartNames.NOSE,
				CubeListBuilder.create().texOffs(6, 18).addBox(-2.0F, -1.99F, -6.0F, 4.0F, 2.0F, 3.0F),
				PartPose.ZERO
		);
		
		return LayerDefinition.create(modelData, 48, 32);
	}
	
}