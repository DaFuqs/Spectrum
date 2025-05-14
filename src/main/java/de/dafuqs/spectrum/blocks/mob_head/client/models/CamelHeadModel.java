package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

@Environment(EnvType.CLIENT)
public class CamelHeadModel extends SpectrumSkullModel {
	
	public CamelHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		PartDefinition head = modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create()
						.texOffs(21, 0).addBox(-3.5F, -14.0F, -3.5F, 7.0F, 14.0F, 7.0F)
						.texOffs(21, 0).addBox(-3.5F, 0.0F, -3.5F, 7.0F, 0.0F, 7.0F),
				PartPose.ZERO);
		head.addOrReplaceChild("nose", CubeListBuilder.create()
						.texOffs(50, 0).addBox(-2.5F, -14.0F, -9.5F, 5.0F, 5.0F, 6.0F),
				PartPose.ZERO);
		head.addOrReplaceChild("left_ear", CubeListBuilder.create()
						.texOffs(45, 0).addBox(-0.5F, 0.5F, -1.0F, 3.0F, 1.0F, 2.0F),
				PartPose.offset(3.0F, -14.0F, 2.0F));
		head.addOrReplaceChild("right_ear", CubeListBuilder.create()
						.texOffs(67, 0).addBox(-2.5F, 0.5F, -1.0F, 3.0F, 1.0F, 2.0F),
				PartPose.offset(-3.0F, -14.0F, 2.0F));
		
		return LayerDefinition.create(modelData, 128, 128);
	}
	
	@Override
	public float getScale() {
		return 0.75F;
	}
	
}