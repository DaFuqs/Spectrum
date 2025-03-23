package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class FrogHeadModel extends SpectrumSkullModel {
	
	public FrogHeadModel(ModelPart root) {
		super(root);
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData head = modelPartData.addChild(
				EntityModelPartNames.HEAD,
				ModelPartBuilder.create()
						.uv(3, 1).cuboid(-3.5F, -3.0F, -4.5F, 7.0F, 3.0F, 9.0F)
						.uv(23, 22).cuboid(-3.5F, -2.0F, -4.5F, 7.0F, 0.0F, 9.0F),
				ModelTransform.NONE
		);
		
		ModelPartData face = head.addChild("face", ModelPartBuilder.create()
						.uv(23, 13).cuboid(-3.5F, -1.0F, -7.0F, 7.0F, 0.0F, 9.0F)
						.uv(0, 13).cuboid(-3.5F, -2.0F, -7.0F, 7.0F, 3.0F, 9.0F),
				ModelTransform.pivot(0.0F, -3.0F, 2.5F));
		head.addChild(EntityModelPartNames.TONGUE, ModelPartBuilder.create()
						.uv(17, 13).cuboid(-2.0F, 0.0F, -7.1F, 4.0F, 0.0F, 7.0F),
				ModelTransform.pivot(0.0F, -2.01F, 4.5F));
		face.addChild(EntityModelPartNames.RIGHT_EYE, ModelPartBuilder.create()
						.uv(0, 0).cuboid(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 3.0F),
				ModelTransform.pivot(-2.0F, -3.0F, -4.5F));
		face.addChild(EntityModelPartNames.LEFT_EYE, ModelPartBuilder.create()
						.uv(0, 5).cuboid(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 3.0F),
				ModelTransform.pivot(2.0F, -3.0F, -4.5F));
		head.addChild(EntityModelPartNames.CROAKING_BODY, ModelPartBuilder.create()
						.uv(26, 5).cuboid(-3.5F, -0.1F, -2.9F, 7.0F, 2.0F, 3.0F, new Dilation(-0.1F)),
				ModelTransform.pivot(0.0F, -3.0F, -1.5F));
		
		return TexturedModelData.of(modelData, 48, 48);
	}
	
}
