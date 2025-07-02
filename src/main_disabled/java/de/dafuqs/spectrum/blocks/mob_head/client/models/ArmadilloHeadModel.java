package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

@Environment(EnvType.CLIENT)
public class ArmadilloHeadModel extends SpectrumSkullModel {
	
	public ArmadilloHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		PartDefinition modelPartData3 = modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, 0.0F));
		modelPartData3.addOrReplaceChild("head_cube", CubeListBuilder.create().texOffs(43, 15).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));
		PartDefinition modelPartData4 = modelPartData3.addOrReplaceChild(PartNames.RIGHT_EAR, CubeListBuilder.create(), PartPose.offset(-1.0F, -1.0F, 0.0F));
		modelPartData4.addOrReplaceChild("right_ear_cube", CubeListBuilder.create().texOffs(43, 10).addBox(-2.0F, -3.0F, 0.0F, 2.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, -0.6F, 0.1886F, -0.3864F, -0.0718F));
		PartDefinition modelPartData5 = modelPartData3.addOrReplaceChild(PartNames.LEFT_EAR, CubeListBuilder.create(), PartPose.offset(1.0F, -2.0F, 0.0F));
		modelPartData5.addOrReplaceChild("left_ear_cube", CubeListBuilder.create().texOffs(47, 10).addBox(0.0F, -3.0F, 0.0F, 2.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 1.0F, -0.6F, 0.1886F, 0.3864F, 0.0718F));
		
		return LayerDefinition.create(modelData, 64, 64);
	}
	
}