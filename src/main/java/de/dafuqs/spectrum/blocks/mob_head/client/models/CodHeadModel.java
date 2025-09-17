package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.neoforged.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class CodHeadModel extends SpectrumSkullModel {
	
	public CodHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		PartDefinition head = modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(11, 0).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 4.0F, 3.0F), PartPose.offset(0.0F, -2.0F, 0.0F));
		head.addOrReplaceChild(PartNames.BODY, CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 4.0F, 7.0F), PartPose.ZERO);
		head.addOrReplaceChild(PartNames.NOSE, CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 1.0F), PartPose.offset(0.0F, 0.0F, -3.0F));
		head.addOrReplaceChild(PartNames.RIGHT_FIN, CubeListBuilder.create().texOffs(22, 1).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 0.0F, 2.0F), PartPose.offsetAndRotation(-1.0F, 1.0F, 0.0F, 0.0F, 0.0F, -0.7853982F));
		head.addOrReplaceChild(PartNames.LEFT_FIN, CubeListBuilder.create().texOffs(22, 4).addBox(0.0F, 0.0F, -1.0F, 2.0F, 0.0F, 2.0F), PartPose.offsetAndRotation(1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.7853982F));
		head.addOrReplaceChild(PartNames.TAIL_FIN, CubeListBuilder.create().texOffs(22, 3).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 4.0F), PartPose.offset(0.0F, 0.0F, 7.0F));
		head.addOrReplaceChild(PartNames.TOP_FIN, CubeListBuilder.create().texOffs(20, -6).addBox(0.0F, -1.0F, -1.0F, 0.0F, 1.0F, 6.0F), PartPose.offset(0.0F, -2.0F, 0.0F));
		
		
		return LayerDefinition.create(modelData, 32, 32);
	}
	
}