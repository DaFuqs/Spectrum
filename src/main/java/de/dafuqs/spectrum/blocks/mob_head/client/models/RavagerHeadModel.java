package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;


public class RavagerHeadModel extends SpectrumSkullModel {
	
	public RavagerHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		PartDefinition head = modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create()
						.texOffs(0, 0).addBox(-8.0F, -20.0F, -8.0F, 16.0F, 20.0F, 16.0F) // head
						.texOffs(0, 0).addBox(-2.0F, -6.0F, -12.0F, 4.0F, 8.0F, 4.0F), // nose
				PartPose.ZERO
		);
		head.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(0, 36).addBox(-8.0F, 0.0F, -16.0F, 16.0F, 3.0F, 16.0F), PartPose.offset(0.0F, -2.0F, 8.0F));
		head.addOrReplaceChild("right_horn", CubeListBuilder.create().texOffs(74, 55).addBox(0.0F, -14.0F, -2.0F, 2.0F, 14.0F, 4.0F), PartPose.offsetAndRotation(-10.0F, -14.0F, -2.0F, 1.0995574F, 0.0F, 0.0F));
		head.addOrReplaceChild("left_horn", CubeListBuilder.create().texOffs(74, 55).mirror().addBox(0.0F, -14.0F, -2.0F, 2.0F, 14.0F, 4.0F, true), PartPose.offsetAndRotation(8.0F, -14.0F, -2.0F, 1.0995574F, 0.0F, 0.0F));
		
		return LayerDefinition.create(modelData, 128, 128);
	}
	
	@Override
	public float getScale() {
		return 0.45F;
	}
	
}
