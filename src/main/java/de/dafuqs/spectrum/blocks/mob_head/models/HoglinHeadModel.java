package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class HoglinHeadModel extends SpectrumHeadModel {

	public HoglinHeadModel(ModelPart root) {
		super(root);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData modelPartData3 = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(61, 1).cuboid(-7.0F, -3.0F, -19.0F, 14.0F, 6.0F, 19.0F), ModelTransform.of(0.0F, 2.0F, -12.0F, 0.87266463F, 0.0F, 0.0F));
		modelPartData3.addChild("right_ear", ModelPartBuilder.create().uv(1, 1).cuboid(-6.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F), ModelTransform.of(-6.0F, -2.0F, -3.0F, 0.0F, 0.0F, -0.6981317F));
		modelPartData3.addChild("left_ear", ModelPartBuilder.create().uv(1, 6).cuboid(0.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F), ModelTransform.of(6.0F, -2.0F, -3.0F, 0.0F, 0.0F, 0.6981317F));
		modelPartData3.addChild("right_horn", ModelPartBuilder.create().uv(10, 13).cuboid(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F), ModelTransform.pivot(-7.0F, 2.0F, -12.0F));
		modelPartData3.addChild("left_horn", ModelPartBuilder.create().uv(1, 13).cuboid(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F), ModelTransform.pivot(7.0F, 2.0F, -12.0F));
		
		return TexturedModelData.of(modelData, 128, 64);
	}

}
