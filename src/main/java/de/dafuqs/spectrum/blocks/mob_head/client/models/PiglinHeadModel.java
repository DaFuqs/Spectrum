package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.neoforged.api.distmarker.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

@OnlyIn(Dist.CLIENT)
public class PiglinHeadModel extends SpectrumSkullModel {
	
	public PiglinHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		PartDefinition head = modelPartData.addOrReplaceChild(
				PartNames.HEAD,
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F)
						.texOffs(31, 1).addBox(-2.0F, -4.0F, -5.0F, 4.0F, 4.0F, 1.0F)
						.texOffs(2, 0).addBox(-3.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F)
						.texOffs(2, 0).addBox(2.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F),
				PartPose.ZERO
		);
		
		head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(51, 6).addBox(0.0F, -1.0F, -2.0F, 1.0F, 5.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(5.0F, -6.0F, 0.0F, 0.0F, 0.0F, -0.3927F));
		head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(39, 6).addBox(-1.0F, -1.0F, -2.0F, 1.0F, 5.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-5.0F, -6.0F, 0.0F, 0.0F, 0.0F, 0.3927F));
		
		return LayerDefinition.create(modelData, 64, 64);
	}
	
}