package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class PigHeadModel extends SpectrumHeadModel {
	
	public PigHeadModel(ModelPart root) {
		super(root);
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
				.uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
				.uv(16, 16).cuboid(-2.0F, -4.0F, -5.0F, 4.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.NONE);
		
		return TexturedModelData.of(modelData, 64, 32);
		
	}
	
}