package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

@Environment(EnvType.CLIENT)
public class SlimeHeadModel extends SpectrumSkullModel {

	public SlimeHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		PartDefinition head = modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, -7.0F, -3.0F, 6.0F, 6.0F, 6.0F), PartPose.ZERO);
		head.addOrReplaceChild("right_eye", CubeListBuilder.create().texOffs(32, 0).addBox(-3.25F, -6.0F, -3.5F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
		head.addOrReplaceChild("left_eye", CubeListBuilder.create().texOffs(32, 4).addBox(1.25F, -6.0F, -3.5F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
		head.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(32, 8).addBox(0.0F, -3.0F, -3.5F, 1.0F, 1.0F, 1.0F), PartPose.ZERO);
		
		return LayerDefinition.create(modelData, 64, 32);
	}

}
