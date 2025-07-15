package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.neoforged.api.distmarker.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

@OnlyIn(Dist.CLIENT)
public class ShulkerHeadModel extends SpectrumSkullModel {
	
	public ShulkerHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		PartDefinition base = modelPartData.addOrReplaceChild(
				PartNames.HEAD,
				CubeListBuilder.create().texOffs(0, 28).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 8.0F, 16.0F),
				PartPose.ZERO
		);
		
		base.addOrReplaceChild(
				"shulker_head",
				CubeListBuilder.create().texOffs(0, 52).addBox(-3.0F, -7.0F, -3.0F, 6.0F, 6.0F, 6.0F),
				PartPose.ZERO
		);
		
		return LayerDefinition.create(modelData, 64, 64);
	}
	
	@Override
	public float getScale() {
		return 0.5F;
	}
	
}
