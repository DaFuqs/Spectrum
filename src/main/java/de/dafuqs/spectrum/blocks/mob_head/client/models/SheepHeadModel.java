package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

@Environment(EnvType.CLIENT)
public class SheepHeadModel extends SpectrumSkullModel {
	
	public SheepHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -4.0F, 6.0F, 6.0F, 8.0F, new CubeDeformation(0.6F)), PartPose.ZERO);
		
		return LayerDefinition.create(modelData, 64, 32);
	}
	
}
