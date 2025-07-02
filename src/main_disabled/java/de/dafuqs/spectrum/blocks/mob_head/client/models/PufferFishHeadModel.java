package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

@Environment(EnvType.CLIENT)
public class PufferFishHeadModel extends SpectrumSkullModel {
	
	public PufferFishHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		PartDefinition head = modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
		head.addOrReplaceChild("right_blue_fin", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F), PartPose.offset(-4.0F, -7.0F, -2.0F));
		head.addOrReplaceChild("left_blue_fin", CubeListBuilder.create().texOffs(24, 3).addBox(0.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F), PartPose.offset(4.0F, -7.0F, -2.0F));
		head.addOrReplaceChild("top_front_fin", CubeListBuilder.create().texOffs(15, 17).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 1.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -8.0F, -4.0F, ((float) Math.PI / 4F), 0.0F, 0.0F));
		head.addOrReplaceChild("top_middle_fin", CubeListBuilder.create().texOffs(14, 16).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 1.0F, 1.0F), PartPose.offset(0.0F, -8.0F, 0.0F));
		head.addOrReplaceChild("top_back_fin", CubeListBuilder.create().texOffs(23, 18).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 1.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -8.0F, 4.0F, (-(float) Math.PI / 4F), 0.0F, 0.0F));
		head.addOrReplaceChild("right_front_fin", CubeListBuilder.create().texOffs(5, 17).addBox(-1.0F, -8.0F, 0.0F, 1.0F, 8.0F, 0.0F), PartPose.offsetAndRotation(-4.0F, 0.0F, -4.0F, 0.0F, (-(float) Math.PI / 4F), 0.0F));
		head.addOrReplaceChild("left_front_fin", CubeListBuilder.create().texOffs(1, 17).addBox(0.0F, -8.0F, 0.0F, 1.0F, 8.0F, 0.0F), PartPose.offsetAndRotation(4.0F, 0.0F, -4.0F, 0.0F, ((float) Math.PI / 4F), 0.0F));
		head.addOrReplaceChild("bottom_front_fin", CubeListBuilder.create().texOffs(15, 20).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 1.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, (-(float) Math.PI / 4F), 0.0F, 0.0F));
		head.addOrReplaceChild("bottom_middle_fin", CubeListBuilder.create().texOffs(15, 20).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 1.0F, 0.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
		head.addOrReplaceChild("bottom_back_fin", CubeListBuilder.create().texOffs(15, 20).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 1.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, ((float) Math.PI / 4F), 0.0F, 0.0F));
		head.addOrReplaceChild("right_back_fin", CubeListBuilder.create().texOffs(9, 17).addBox(-1.0F, -8.0F, 0.0F, 1.0F, 8.0F, 0.0F), PartPose.offsetAndRotation(-4.0F, 0.0F, 4.0F, 0.0F, ((float) Math.PI / 4F), 0.0F));
		head.addOrReplaceChild("left_back_fin", CubeListBuilder.create().texOffs(9, 17).addBox(0.0F, -8.0F, 0.0F, 1.0F, 8.0F, 0.0F), PartPose.offsetAndRotation(4.0F, 0.0F, 4.0F, 0.0F, (-(float) Math.PI / 4F), 0.0F));
		
		return LayerDefinition.create(modelData, 32, 32);
	}
	
}
