package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.neoforged.api.distmarker.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

@OnlyIn(Dist.CLIENT)
public class CatHeadModel extends SpectrumSkullModel {
	
	public CatHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		CubeDeformation dilation = new CubeDeformation(0.01F);
		modelPartData.addOrReplaceChild(
				PartNames.HEAD,
				CubeListBuilder.create()
						.addBox("main", -2.5F, -4.0F, -2.5F, 5.0F, 4.0F, 5.0F, dilation)
						.addBox("nose", -1.5F, -2.001F, -3.5F, 3, 2, 2, dilation, 0, 24)
						.addBox("ear1", -2.0F, -5.0F, 0.5F, 1, 1, 2, dilation, 0, 10)
						.addBox("ear2", 1.0F, -5.0F, 0.5F, 1, 1, 2, dilation, 6, 10),
				PartPose.ZERO
		);
		
		return LayerDefinition.create(modelData, 64, 32);
	}
	
}