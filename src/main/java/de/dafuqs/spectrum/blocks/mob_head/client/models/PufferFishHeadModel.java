package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class PufferFishHeadModel extends SpectrumSkullModel {
	
	public PufferFishHeadModel(ModelPart root) {
		super(root);
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData head = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), ModelTransform.NONE);
		head.addChild("right_blue_fin", ModelPartBuilder.create().uv(24, 0).cuboid(-2.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F), ModelTransform.pivot(-4.0F, -7.0F, -2.0F));
		head.addChild("left_blue_fin", ModelPartBuilder.create().uv(24, 3).cuboid(0.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F), ModelTransform.pivot(4.0F, -7.0F, -2.0F));
		head.addChild("top_front_fin", ModelPartBuilder.create().uv(15, 17).cuboid(-4.0F, -1.0F, 0.0F, 8.0F, 1.0F, 0.0F), ModelTransform.of(0.0F, -8.0F, -4.0F, ((float) Math.PI / 4F), 0.0F, 0.0F));
		head.addChild("top_middle_fin", ModelPartBuilder.create().uv(14, 16).cuboid(-4.0F, -1.0F, 0.0F, 8.0F, 1.0F, 1.0F), ModelTransform.pivot(0.0F, -8.0F, 0.0F));
		head.addChild("top_back_fin", ModelPartBuilder.create().uv(23, 18).cuboid(-4.0F, -1.0F, 0.0F, 8.0F, 1.0F, 0.0F), ModelTransform.of(0.0F, -8.0F, 4.0F, (-(float) Math.PI / 4F), 0.0F, 0.0F));
		head.addChild("right_front_fin", ModelPartBuilder.create().uv(5, 17).cuboid(-1.0F, -8.0F, 0.0F, 1.0F, 8.0F, 0.0F), ModelTransform.of(-4.0F, 0.0F, -4.0F, 0.0F, (-(float) Math.PI / 4F), 0.0F));
		head.addChild("left_front_fin", ModelPartBuilder.create().uv(1, 17).cuboid(0.0F, -8.0F, 0.0F, 1.0F, 8.0F, 0.0F), ModelTransform.of(4.0F, 0.0F, -4.0F, 0.0F, ((float) Math.PI / 4F), 0.0F));
		head.addChild("bottom_front_fin", ModelPartBuilder.create().uv(15, 20).cuboid(-4.0F, 0.0F, 0.0F, 8.0F, 1.0F, 0.0F), ModelTransform.of(0.0F, 0.0F, -4.0F, (-(float) Math.PI / 4F), 0.0F, 0.0F));
		head.addChild("bottom_middle_fin", ModelPartBuilder.create().uv(15, 20).cuboid(-4.0F, 0.0F, 0.0F, 8.0F, 1.0F, 0.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		head.addChild("bottom_back_fin", ModelPartBuilder.create().uv(15, 20).cuboid(-4.0F, 0.0F, 0.0F, 8.0F, 1.0F, 0.0F), ModelTransform.of(0.0F, 0.0F, 4.0F, ((float) Math.PI / 4F), 0.0F, 0.0F));
		head.addChild("right_back_fin", ModelPartBuilder.create().uv(9, 17).cuboid(-1.0F, -8.0F, 0.0F, 1.0F, 8.0F, 0.0F), ModelTransform.of(-4.0F, 0.0F, 4.0F, 0.0F, ((float) Math.PI / 4F), 0.0F));
		head.addChild("left_back_fin", ModelPartBuilder.create().uv(9, 17).cuboid(0.0F, -8.0F, 0.0F, 1.0F, 8.0F, 0.0F), ModelTransform.of(4.0F, 0.0F, 4.0F, 0.0F, (-(float) Math.PI / 4F), 0.0F));
		
		return TexturedModelData.of(modelData, 32, 32);
	}
	
}
