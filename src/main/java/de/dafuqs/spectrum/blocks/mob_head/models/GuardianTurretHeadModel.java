package de.dafuqs.spectrum.blocks.mob_head.models;

import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class GuardianTurretHeadModel extends SpectrumHeadModel {
	
	public GuardianTurretHeadModel(ModelPart root) {
		super(root);
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		
		modelData.getRoot().addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
				.uv(0, 0).cuboid(-8.0F, -8.0F, -8.0F, 16.0F, 8.0F, 16.0F, Dilation.NONE)
				.uv(0, 24).cuboid(-8.0F, -16.0F, -8.0F, 16.0F, 2.0F, 16.0F, Dilation.NONE)
				.uv(0, 42).cuboid(-7.0F, -14.0F, -7.0F, 14.0F, 6.0F, 14.0F, Dilation.NONE), ModelTransform.NONE);
		
		return TexturedModelData.of(modelData, 128, 128);
	}
	
	@Override
	public float getScale() {
		return 0.5F;
	}
	
}