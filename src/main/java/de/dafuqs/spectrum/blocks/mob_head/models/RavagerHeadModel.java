package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class RavagerHeadModel extends SpectrumHeadModel {

	public RavagerHeadModel(ModelPart root) {
		super(root);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData modelPartData3 = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -20.0F, -14.0F, 16.0F, 20.0F, 16.0F).uv(0, 0).cuboid(-2.0F, -6.0F, -18.0F, 4.0F, 8.0F, 4.0F), ModelTransform.pivot(0.0F, 16.0F, -17.0F));
		modelPartData3.addChild("right_horn", ModelPartBuilder.create().uv(74, 55).cuboid(0.0F, -14.0F, -2.0F, 2.0F, 14.0F, 4.0F), ModelTransform.of(-10.0F, -14.0F, -8.0F, 1.0995574F, 0.0F, 0.0F));
		modelPartData3.addChild("left_horn", ModelPartBuilder.create().uv(74, 55).mirrored().cuboid(0.0F, -14.0F, -2.0F, 2.0F, 14.0F, 4.0F), ModelTransform.of(8.0F, -14.0F, -8.0F, 1.0995574F, 0.0F, 0.0F));
		modelPartData3.addChild("mouth", ModelPartBuilder.create().uv(0, 36).cuboid(-8.0F, 0.0F, -16.0F, 16.0F, 3.0F, 16.0F), ModelTransform.pivot(0.0F, -2.0F, 2.0F));
		
		return TexturedModelData.of(modelData, 128, 128);
	}

}
