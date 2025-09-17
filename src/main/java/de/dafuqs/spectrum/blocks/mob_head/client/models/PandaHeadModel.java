package de.dafuqs.spectrum.blocks.mob_head.client.models;


import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.neoforged.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class PandaHeadModel extends SpectrumSkullModel {
	
	public PandaHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		modelPartData.addOrReplaceChild(
				PartNames.HEAD,
				CubeListBuilder.create()
						.texOffs(0, 6).addBox(-6.5F, -10.0F, -4.5F, 13.0F, 10.0F, 9.0F)
						.texOffs(45, 16).addBox("nose", -3.5F, -5.0F, -6.5F, 7.0F, 5.0F, 2.0F)
						.texOffs(52, 25).addBox("left_ear", 3.5F, -13.0F, -1.5F, 5.0F, 4.0F, 1.0F)
						.texOffs(52, 25).addBox("right_ear", -8.5F, -13.0F, -1.5F, 5.0F, 4.0F, 1.0F),
				PartPose.ZERO
		);
		
		return LayerDefinition.create(modelData, 64, 64);
	}
	
}