package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;


public class KindlingHeadModel extends SpectrumSkullModel {
	
	public KindlingHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		
		PartDefinition head = modelData.getRoot().addOrReplaceChild(
				PartNames.HEAD,
				CubeListBuilder.create()
						.texOffs(0, 48).addBox(-3.5F, -8.0F, -3.5F, 7.0F, 8.0F, 7.0F)
						.texOffs(30, 76).addBox(-4.0F, -8.5F, -4.0F, 8.0F, 9.0F, 8.0F)
						.texOffs(29, 21).addBox(-1.5F, -3.01F, -5.5F, 3.0F, 3.0F, 2.0F),
				PartPose.ZERO
		);
		
		head.addOrReplaceChild("middlehorn_r1", CubeListBuilder.create().texOffs(54, 0).addBox(0.0F, -10.5F, -4.0F, 0.0F, 12.0F, 8.0F), PartPose.offsetAndRotation(0.0F, -7.5F, -3.5F, 0.3927F, 0.0F, 0.0F));
		
		PartDefinition righthorns = head.addOrReplaceChild("righthorns", CubeListBuilder.create(), PartPose.ZERO);
		righthorns.addOrReplaceChild("lowerrighthorn_r1", CubeListBuilder.create().texOffs(46, 0).addBox(-3.0F, 2.5F, -0.5F, 7.0F, 6.0F, 0.0F), PartPose.offsetAndRotation(5.0F, -7.5F, 1.0F, 0.0F, -0.4363F, 0.0F));
		righthorns.addOrReplaceChild("upperrighthorn_r1", CubeListBuilder.create().texOffs(64, 48).addBox(-3.5F, -4.5F, -0.25F, 9.0F, 7.0F, 0.0F), PartPose.offsetAndRotation(5.0F, -6.5F, 1.0F, -0.0203F, -0.4359F, -0.4318F));
		
		PartDefinition lefthorns = head.addOrReplaceChild("lefthorns", CubeListBuilder.create(), PartPose.ZERO);
		lefthorns.addOrReplaceChild("lowerrighthorn_r2", CubeListBuilder.create().texOffs(0, 41).addBox(-4.0F, 2.5F, -0.5F, 7.0F, 6.0F, 0.0F), PartPose.offsetAndRotation(-5.0F, -7.5F, 1.0F, 0.0F, 0.4363F, 0.0F));
		lefthorns.addOrReplaceChild("upperrighthorn_r2", CubeListBuilder.create().texOffs(63, 0).addBox(-5.5F, -4.5F, -0.25F, 9.0F, 7.0F, 0.0F), PartPose.offsetAndRotation(-5.0F, -6.5F, 1.0F, -0.0203F, 0.4359F, 0.4318F));
		
		return LayerDefinition.create(modelData, 128, 128);
	}
	
}