package de.dafuqs.spectrum.blocks.mob_head.models;

import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class LizardHeadModel extends SpectrumHeadModel {
	
	public LizardHeadModel(ModelPart root) {
		super(root);
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData head = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
				.uv(11, 58).cuboid(-2.5F, -6.0F, 1.0F, 5.0F, 6.0F, 5.0F)
				.uv(44, 44).cuboid(-2.0F, -6.0F, -8.0F, 4.0F, 3.0F, 9.0F)
				.uv(26, 21).cuboid(0.0F, -13.0F, -9.0F, 0.0F, 8.0F, 15.0F), ModelTransform.NONE);
		
		head.addChild("rightfrills_r1", ModelPartBuilder.create().uv(61, 40).cuboid(-1.9733F, -9.9307F, 0.0F, 8.0F, 10.0F, 0.0F), ModelTransform.of(2.5F, -6.0F, 1.0F, -0.8281F, 0.001F, 1.5679F));
		head.addChild("leftfrills_r1", ModelPartBuilder.create().uv(45, 68).cuboid(-6.0267F, -9.9307F, 0.0F, 8.0F, 10.0F, 0.0F), ModelTransform.of(-2.5F, -6.0F, 1.0F, -0.8282F, 0.0F, -1.5615F));
		head.addChild("topfrills_r1", ModelPartBuilder.create().uv(60, 56).cuboid(-4.5F, -11.75F, -0.15F, 9.0F, 12.0F, 0.0F), ModelTransform.of(0.0F, -6.0F, 1.0F, -0.8727F, 0.0F, 0.0F));
		head.addChild("jaw", ModelPartBuilder.create().uv(61, 0).cuboid(-1.5F, 0.0F, -5.5F, 3.0F, 1.0F, 6.0F), ModelTransform.pivot(0.0F, -3.0F, 1.0F));
		
		return TexturedModelData.of(modelData, 128, 128);
	}
	
}