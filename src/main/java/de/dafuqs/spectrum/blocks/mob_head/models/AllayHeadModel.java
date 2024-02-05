package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class AllayHeadModel extends SpectrumHeadModel {
	
	public AllayHeadModel(ModelPart root) {
		super(root);
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
				.uv(0, 0).cuboid(-13.0F, -10.0F, 3.0F, 10.0F, 10.0F, 10.0F), ModelTransform.NONE);
		
		return TexturedModelData.of(modelData, 64, 64);
	}
	
}