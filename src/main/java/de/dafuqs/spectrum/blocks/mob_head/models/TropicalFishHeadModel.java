package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.entity.passive.*;

@Environment(EnvType.CLIENT)
public class TropicalFishHeadModel extends SpectrumHeadModel {

	public TropicalFishHeadModel(ModelPart root) {
		super(root);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		Dilation dilation = new Dilation(0.01F);
		modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 20).cuboid(-1.0F, -3.0F, -3.0F, 2.0F, 6.0F, 6.0F, dilation), ModelTransform.pivot(0.0F, 19.0F, 0.0F));
		modelPartData.addChild("right_fin", ModelPartBuilder.create().uv(2, 16).cuboid(-2.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, dilation), ModelTransform.of(-1.0F, 20.0F, 0.0F, 0.0F, 0.7853982F, 0.0F));
		modelPartData.addChild("left_fin", ModelPartBuilder.create().uv(2, 12).cuboid(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, dilation), ModelTransform.of(1.0F, 20.0F, 0.0F, 0.0F, -0.7853982F, 0.0F));
		modelPartData.addChild("top_fin", ModelPartBuilder.create().uv(20, 11).cuboid(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 6.0F, dilation), ModelTransform.pivot(0.0F, 16.0F, -3.0F));
		modelPartData.addChild("bottom_fin", ModelPartBuilder.create().uv(20, 21).cuboid(0.0F, 0.0F, 0.0F, 0.0F, 4.0F, 6.0F, dilation), ModelTransform.pivot(0.0F, 22.0F, -3.0F));
		
		return TexturedModelData.of(modelData, 64, 64);
	}

}
