package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class RavagerHeadModel extends SpectrumSkullModel {

	public RavagerHeadModel(ModelPart root) {
		super(root);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData head = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
						.uv(0, 0).cuboid(-8.0F, -20.0F, -8.0F, 16.0F, 20.0F, 16.0F) // head
						.uv(0, 0).cuboid(-2.0F, -6.0F, -12.0F, 4.0F, 8.0F, 4.0F), // nose
				ModelTransform.NONE
		);
		head.addChild("mouth", ModelPartBuilder.create().uv(0, 36).cuboid(-8.0F, 0.0F, -16.0F, 16.0F, 3.0F, 16.0F), ModelTransform.pivot(0.0F, -2.0F, 8.0F));
		head.addChild("right_horn", ModelPartBuilder.create().uv(74, 55).cuboid(0.0F, -14.0F, -2.0F, 2.0F, 14.0F, 4.0F), ModelTransform.of(-10.0F, -14.0F, -2.0F, 1.0995574F, 0.0F, 0.0F));
		head.addChild("left_horn", ModelPartBuilder.create().uv(74, 55).mirrored().cuboid(0.0F, -14.0F, -2.0F, 2.0F, 14.0F, 4.0F, true), ModelTransform.of(8.0F, -14.0F, -2.0F, 1.0995574F, 0.0F, 0.0F));
		
		return TexturedModelData.of(modelData, 128, 128);
	}
	
	@Override
	public float getScale() {
		return 0.45F;
	}

}
