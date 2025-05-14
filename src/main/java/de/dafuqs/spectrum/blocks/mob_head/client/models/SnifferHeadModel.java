package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

@Environment(EnvType.CLIENT)
public class SnifferHeadModel extends SpectrumSkullModel {
	
	public SnifferHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition head = modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create()
						.texOffs(8, 15).addBox(-6.5F, -15.0F, -3.0F, 13.0F, 18.0F, 11.0F)
						.texOffs(8, 4).addBox(-6.5F, 0.0F, -3.0F, 13.0F, 0.0F, 11.0F),
				PartPose.ZERO);
		head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(2, 0).addBox(0.0F, 0.0F, -3.0F, 1.0F, 19.0F, 7.0F), PartPose.offset(6.51F, -15.0F, 3.99F));
		head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(48, 0).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 19.0F, 7.0F), PartPose.offset(-6.51F, -15.0F, 3.99F));
		head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(10, 45).addBox(-6.5F, -2.0F, -9.0F, 13.0F, 2.0F, 9.0F), PartPose.offset(0.0F, -12.0F, -3.0F));
		head.addOrReplaceChild("lower_beak", CubeListBuilder.create().texOffs(10, 57).addBox(-6.5F, -7.0F, -8.0F, 13.0F, 12.0F, 9.0F), PartPose.offset(0.0F, -5.0F, -4.0F));
		
		return LayerDefinition.create(modelData, 192, 192);
	}
	
	@Override
	public float getScale() {
		return 0.5F;
	}
	
}