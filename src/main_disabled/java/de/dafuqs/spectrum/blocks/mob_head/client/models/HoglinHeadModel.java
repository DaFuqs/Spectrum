package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

@Environment(EnvType.CLIENT)
public class HoglinHeadModel extends SpectrumSkullModel {
	
	public HoglinHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		PartDefinition head = modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create(), PartPose.offset(0, -1, 0));
		head.addOrReplaceChild(PartNames.LEFT_HORN, CubeListBuilder.create().texOffs(1, 13).addBox(1.0F, -11.0F, -8.0F, 2.0F, 11.0F, 2.0F), PartPose.offsetAndRotation(5.0F, -3.0F, 1.0F, 0.7854F, 0.0F, 0.0F));
		head.addOrReplaceChild(PartNames.RIGHT_HORN, CubeListBuilder.create().texOffs(10, 13).addBox(1.0F, -11.0F, -3.0F, 2.0F, 11.0F, 2.0F), PartPose.offsetAndRotation(-9.0F, 0.0F, -3.0F, 0.7854F, 0.0F, 0.0F));
		head.addOrReplaceChild(PartNames.LEFT_EAR, CubeListBuilder.create().texOffs(1, 1).addBox(0.0F, 0.0F, -2.0F, 6.0F, 1.0F, 4.0F), PartPose.offsetAndRotation(7.0F, -9.0F, 2.0F, 0.7854F, -0.5236F, 0.5236F));
		head.addOrReplaceChild(PartNames.RIGHT_EAR, CubeListBuilder.create().texOffs(1, 6).addBox(-6.0F, 0.0F, -2.0F, 6.0F, 1.0F, 4.0F), PartPose.offsetAndRotation(-7.0F, -9.0F, 2.0F, 0.7854F, 0.5236F, -0.5236F));
		head.addOrReplaceChild("skull", CubeListBuilder.create().texOffs(61, 1).addBox(-9.0F, -6.0F, -8.0F, 14.0F, 6.0F, 19.0F), PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));
		
		return LayerDefinition.create(modelData, 128, 64);
	}
	
	@Override
	public float getScale() {
		return 0.65F;
	}
	
}
