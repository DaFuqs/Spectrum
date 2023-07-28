package de.dafuqs.spectrum.blocks.mob_head.models;

import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class MonstrosityHeadModel extends SpectrumHeadModel {
	
	public MonstrosityHeadModel(ModelPart root) {
		super(root);
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		
		modelData.getRoot().addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(56, 24).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
				.uv(0, 44).cuboid(-2.5F, -4.0F, -5.0F, 5.0F, 3.0F, 1.0F, new Dilation(0.0F))
				.uv(45, 0).cuboid(-5.02F, -9.0F, -5.0F, 10.0F, 7.0F, 10.0F, new Dilation(0.0F)), ModelTransform.NONE);
		
		return TexturedModelData.of(modelData, 128, 128);
	}
	
}