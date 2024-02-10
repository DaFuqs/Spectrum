package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class PufferFishHeadModel extends SpectrumHeadModel {

	public PufferFishHeadModel(ModelPart root) {
		super(root);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(12, 22).cuboid(-2.5F, -5.0F, -2.5F, 5.0F, 5.0F, 5.0F), ModelTransform.NONE);
		modelPartData.addChild("right_blue_fin", ModelPartBuilder.create().uv(24, 0).cuboid(-2.0F, 0.0F, 0.0F, 2.0F, 0.0F, 2.0F), ModelTransform.pivot(-2.5F, 17.0F, -1.5F));
		modelPartData.addChild("left_blue_fin", ModelPartBuilder.create().uv(24, 3).cuboid(0.0F, 0.0F, 0.0F, 2.0F, 0.0F, 2.0F), ModelTransform.pivot(2.5F, 17.0F, -1.5F));
		modelPartData.addChild("top_front_fin", ModelPartBuilder.create().uv(15, 16).cuboid(-2.5F, -1.0F, 0.0F, 5.0F, 1.0F, 1.0F), ModelTransform.of(0.0F, 17.0F, -2.5F, 0.7853982F, 0.0F, 0.0F));
		modelPartData.addChild("top_back_fin", ModelPartBuilder.create().uv(10, 16).cuboid(-2.5F, -1.0F, -1.0F, 5.0F, 1.0F, 1.0F), ModelTransform.of(0.0F, 17.0F, 2.5F, -0.7853982F, 0.0F, 0.0F));
		modelPartData.addChild("right_front_fin", ModelPartBuilder.create().uv(8, 16).cuboid(-1.0F, -5.0F, 0.0F, 1.0F, 5.0F, 1.0F), ModelTransform.of(-2.5F, 22.0F, -2.5F, 0.0F, -0.7853982F, 0.0F));
		modelPartData.addChild("right_back_fin", ModelPartBuilder.create().uv(8, 16).cuboid(-1.0F, -5.0F, 0.0F, 1.0F, 5.0F, 1.0F), ModelTransform.of(-2.5F, 22.0F, 2.5F, 0.0F, 0.7853982F, 0.0F));
		modelPartData.addChild("left_back_fin", ModelPartBuilder.create().uv(4, 16).cuboid(0.0F, -5.0F, 0.0F, 1.0F, 5.0F, 1.0F), ModelTransform.of(2.5F, 22.0F, 2.5F, 0.0F, -0.7853982F, 0.0F));
		modelPartData.addChild("left_front_fin", ModelPartBuilder.create().uv(0, 16).cuboid(0.0F, -5.0F, 0.0F, 1.0F, 5.0F, 1.0F), ModelTransform.of(2.5F, 22.0F, -2.5F, 0.0F, 0.7853982F, 0.0F));
		modelPartData.addChild("bottom_back_fin", ModelPartBuilder.create().uv(8, 22).cuboid(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F), ModelTransform.of(0.5F, 22.0F, 2.5F, 0.7853982F, 0.0F, 0.0F));
		modelPartData.addChild("bottom_front_fin", ModelPartBuilder.create().uv(17, 21).cuboid(-2.5F, 0.0F, 0.0F, 5.0F, 1.0F, 1.0F), ModelTransform.of(0.0F, 22.0F, -2.5F, -0.7853982F, 0.0F, 0.0F));

		return TexturedModelData.of(modelData, 32, 32);
	}

}
