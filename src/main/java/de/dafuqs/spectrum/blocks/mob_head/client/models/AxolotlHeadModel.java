package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.neoforged.api.distmarker.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

@OnlyIn(Dist.CLIENT)
public class AxolotlHeadModel extends SpectrumSkullModel {
	
	public AxolotlHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		CubeDeformation dilation = new CubeDeformation(0.001F);
		PartDefinition head = modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(0, 1)
						.addBox(-4.0F, -5.0F, -3.0F, 8.0F, 5.0F, 5.0F, dilation),
				PartPose.ZERO);
		head.addOrReplaceChild("top_gills", CubeListBuilder.create().texOffs(3, 37)
						.addBox(-4.0F, -8.0F, 1.0F, 8.0F, 3.0F, 0.0F, dilation),
				PartPose.ZERO);
		head.addOrReplaceChild("left_gills", CubeListBuilder.create().texOffs(0, 40)
						.addBox(-7.0F, -7.0F, 1.0F, 3.0F, 7.0F, 0.0F, dilation),
				PartPose.ZERO);
		head.addOrReplaceChild("right_gills", CubeListBuilder.create().texOffs(11, 40)
						.addBox(4.0F, -7.0F, 1.0F, 3.0F, 7.0F, 0.0F, dilation),
				PartPose.ZERO);
		
		return LayerDefinition.create(modelData, 64, 64);
	}
	
}
