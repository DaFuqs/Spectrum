package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.neoforged.api.distmarker.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

@OnlyIn(Dist.CLIENT)
public class PreservationTurretHeadModel extends SpectrumSkullModel {
	
	public PreservationTurretHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		
		modelData.getRoot().addOrReplaceChild(
				PartNames.HEAD,
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 8.0F, 16.0F)
						.texOffs(0, 24).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 2.0F, 16.0F)
						.texOffs(0, 42).addBox(-7.0F, -14.0F, -7.0F, 14.0F, 6.0F, 14.0F),
				PartPose.ZERO
		);
		
		return LayerDefinition.create(modelData, 128, 128);
	}
	
	@Override
	public float getScale() {
		return 0.5F;
	}
	
}