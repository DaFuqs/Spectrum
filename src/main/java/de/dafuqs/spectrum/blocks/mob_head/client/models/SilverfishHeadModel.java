package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.neoforged.api.distmarker.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

@OnlyIn(Dist.CLIENT)
public class SilverfishHeadModel extends SpectrumSkullModel {
	
	public SilverfishHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		modelPartData.addOrReplaceChild(
				PartNames.HEAD,
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.5F, -2.0F, -2.0F, 3.0F, 2.0F, 2.0F)
						.texOffs(0, 4).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 2.0F)
						.texOffs(22, 20).addBox(-3.0F, -5.0F, -0.5F, 6.0F, 5.0F, 0.0F),
				PartPose.ZERO
		);
		
		return LayerDefinition.create(modelData, 64, 32);
	}
	
}