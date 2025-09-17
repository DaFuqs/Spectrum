package de.dafuqs.spectrum.blocks.mob_head.client.models;


import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.neoforged.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class WolfHeadModel extends SpectrumSkullModel {
	
	public WolfHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		modelPartData.addOrReplaceChild(
				PartNames.HEAD,
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-3.0F, -6.0F, -2.0F, 6.0F, 6.0F, 4.0F) // head
						.texOffs(0, 10).addBox(-1.5F, -3.001F, -5.0F, 3.0F, 3.0F, 4.0F) // hose
						.texOffs(16, 14).addBox(-3.0F, -8.0F, 0.0F, 2.0F, 2.0F, 1.0F) // right ear
						.texOffs(16, 14).addBox(1.0F, -8.0F, 0.0F, 2.0F, 2.0F, 1.0F), // left ear
				PartPose.ZERO
		);
		
		return LayerDefinition.create(modelData, 64, 32);
	}
	
}