package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.neoforged.api.distmarker.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

@OnlyIn(Dist.CLIENT)
public class RabbitHeadModel extends SpectrumSkullModel {
	
	public RabbitHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		PartDefinition head = modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(32, 0).addBox(-2.5F, -4.0F, -2.5F, 5.0F, 4.0F, 5.0F), PartPose.ZERO);
		head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(32, 9).addBox(-0.5F, -2.5F, -3.0F, 1.0F, 1.0F, 1.0F), PartPose.ZERO);
		head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(52, 0).addBox(-2.5F, -9.0F, -1.0F, 2.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 2.5F, 0.0F, -0.2617994F, 0.0F));
		head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(58, 0).addBox(0.5F, -9.0F, -1.0F, 2.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 2.5F, 0.0F, 0.2617994F, 0.0F));
		
		return LayerDefinition.create(modelData, 64, 32);
	}
	
}