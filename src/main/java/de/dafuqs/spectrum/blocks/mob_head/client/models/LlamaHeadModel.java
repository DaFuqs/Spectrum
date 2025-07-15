package de.dafuqs.spectrum.blocks.mob_head.client.models;


import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.neoforged.api.distmarker.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

@OnlyIn(Dist.CLIENT)
public class LlamaHeadModel extends SpectrumSkullModel {
	
	public LlamaHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		modelPartData.addOrReplaceChild(
				PartNames.HEAD,
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-2.0F, -8.0F, -7.0F, 4.0F, 4.0F, 9.0F)
						.texOffs(17, 0).addBox(1.0F, -13.0F, -1.0F, 3.0F, 3.0F, 2.0F)
						.texOffs(17, 0).addBox(-4.0F, -13.0F, -1.0F, 3.0F, 3.0F, 2.0F)
						.texOffs(0, 14).addBox(-4.0F, -10.0F, -3.0F, 8.0F, 10.0F, 6.0F),
				PartPose.ZERO
		);
		
		return LayerDefinition.create(modelData, 128, 64);
	}
	
}