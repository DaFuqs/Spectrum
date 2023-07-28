package de.dafuqs.spectrum.blocks.mob_head.models;

import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class WardenHeadModel extends SpectrumHeadModel {
	
	public WardenHeadModel(ModelPart root) {
		super(root);
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		
		modelData.getRoot().addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
						.uv(0, 32).cuboid(-8.0F, -16.0F, -5.0F, 16.0F, 16.0F, 10.0F, new Dilation(0.0F))
						.uv(58, 2).cuboid(8.0F, -21.0F, 0.0F, 10.0F, 16.0F, 0.0F, new Dilation(0.0F))
						.uv(58, 34).cuboid(-18.0F, -21.0F, 0.0F, 10.0F, 16.0F, 0.0F, new Dilation(0.0F)),
				ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		
		return TexturedModelData.of(modelData, 128, 128);
	}
	
	@Override
	public float getScale() {
		return 0.65F;
	}
	
}