package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class HorseHeadModel extends SpectrumHeadModel {
	
	public HorseHeadModel(ModelPart root) {
		super(root);
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData head = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 35).cuboid(-2.0F, -6.0F, -3.5F, 4.0F, 6.0F, 7.0F), ModelTransform.NONE);
		ModelPartData face = head.addChild("face", ModelPartBuilder.create().uv(0, 13).cuboid(-3.0F, -11.0F, -3.5F, 6.0F, 5.0F, 7.0F), ModelTransform.NONE);
		head.addChild("mane", ModelPartBuilder.create().uv(56, 36).cuboid(-1.0F, -11.0F, 3.51F, 2.0F, 10.0F, 2.0F), ModelTransform.NONE);
		head.addChild("upper_mouth", ModelPartBuilder.create().uv(0, 25).cuboid(-2.0F, -11.0F, -8.5F, 4.0F, 5.0F, 5.0F), ModelTransform.NONE);
		face.addChild("left_ear", ModelPartBuilder.create().uv(19, 16).cuboid(0.55F, -13.0F, 2.5F, 2.0F, 3.0F, 1.0F, new Dilation(-0.001F)), ModelTransform.NONE);
		face.addChild("right_ear", ModelPartBuilder.create().uv(19, 16).cuboid(-2.55F, -13.0F, 2.5F, 2.0F, 3.0F, 1.0F, new Dilation(-0.001F)), ModelTransform.NONE);
		
		return TexturedModelData.of(modelData, 64, 64);
	}
	
}