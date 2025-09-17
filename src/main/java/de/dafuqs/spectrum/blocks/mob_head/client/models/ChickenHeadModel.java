package de.dafuqs.spectrum.blocks.mob_head.client.models;


import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.neoforged.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class ChickenHeadModel extends SpectrumSkullModel {
	
	public ChickenHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		PartDefinition head = modelPartData.addOrReplaceChild(
				PartNames.HEAD,
				CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -6.0F, -1.5F, 4.0F, 6.0F, 3.0F),
				PartPose.ZERO
		);
		
		head.addOrReplaceChild(
				PartNames.BEAK,
				CubeListBuilder.create().texOffs(14, 0).addBox(-2.0F, -4.0F, -3.5F, 4.0F, 2.0F, 2.0F),
				PartPose.ZERO
		);
		head.addOrReplaceChild(
				"red_thing",
				CubeListBuilder.create().texOffs(14, 4).addBox(-1.0F, -2.0F, -2.5F, 2.0F, 2.0F, 2.0F),
				PartPose.ZERO
		);
		
		return LayerDefinition.create(modelData, 64, 32);
	}
	
}