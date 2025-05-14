package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

@Environment(EnvType.CLIENT)
public class SalmonHeadModel extends SpectrumSkullModel {
	
	public SalmonHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		PartDefinition head = modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -5.0F, -4.0F, 3.0F, 5.0F, 8.0F), PartPose.ZERO);
		head.addOrReplaceChild("face", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -4.0F, -7.0F, 2.0F, 4.0F, 3.0F), PartPose.ZERO);
		head.addOrReplaceChild(PartNames.TOP_FIN, CubeListBuilder.create().texOffs(2, 1).addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 3.0F), PartPose.offset(0.0F, -7.0F, 1.0F));
		head.addOrReplaceChild(PartNames.BACK_FIN, CubeListBuilder.create().texOffs(0, 2).addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 4.0F), PartPose.offset(0.0F, -7.0F, 3.0F));
		head.addOrReplaceChild(PartNames.RIGHT_FIN, CubeListBuilder.create().texOffs(-4, 0).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 0.0F, 2.0F), PartPose.offsetAndRotation(-1.5F, -1.0F, -4.0F, 0.0F, 0.0F, -(float) Math.PI / 4.0F));
		head.addOrReplaceChild(PartNames.LEFT_FIN, CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 2.0F, 0.0F, 2.0F), PartPose.offsetAndRotation(1.5F, -1.0F, -4.0F, 0.0F, 0.0F, (float) Math.PI / 4.0F));
		
		return LayerDefinition.create(modelData, 32, 32);
	}
	
}
