package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class StriderHeadModel extends SpectrumSkullModel {
	
	public StriderHeadModel(ModelPart root) {
		super(root);
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData head = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -14.0F, -8.0F, 16.0F, 14.0F, 16.0F), ModelTransform.NONE);
		head.addChild("right_bottom_bristle", ModelPartBuilder.create().uv(16, 65).cuboid(-12.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F, true), ModelTransform.of(-8.0F, -4.0F, -8.0F, 0.0F, 0.0F, -1.2217305F));
		head.addChild("right_middle_bristle", ModelPartBuilder.create().uv(16, 49).cuboid(-12.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F, true), ModelTransform.of(-8.0F, -9.0F, -8.0F, 0.0F, 0.0F, -1.134464F));
		head.addChild("right_top_bristle", ModelPartBuilder.create().uv(16, 33).cuboid(-12.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F, true), ModelTransform.of(-8.0F, -13.0F, -8.0F, 0.0F, 0.0F, -0.87266463F));
		head.addChild("left_top_bristle", ModelPartBuilder.create().uv(16, 33).cuboid(0.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F), ModelTransform.of(8.0F, -14.0F, -8.0F, 0.0F, 0.0F, 0.87266463F));
		head.addChild("left_middle_bristle", ModelPartBuilder.create().uv(16, 49).cuboid(0.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F), ModelTransform.of(8.0F, -10.0F, -8.0F, 0.0F, 0.0F, 1.134464F));
		head.addChild("left_bottom_bristle", ModelPartBuilder.create().uv(16, 65).cuboid(0.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F), ModelTransform.of(8.0F, -5.0F, -8.0F, 0.0F, 0.0F, 1.2217305F));
		
		return TexturedModelData.of(modelData, 64, 128);
	}
	
	@Override
	public float getScale() {
		return 0.5F;
	}
	
}