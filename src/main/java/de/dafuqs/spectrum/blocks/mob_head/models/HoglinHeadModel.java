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
		
		ModelPartData head = modelPartData.addChild(
				EntityModelPartNames.HEAD,
				ModelPartBuilder.create().uv(61, 1).cuboid(-7.0F, -6.0F, -9.5F, 14.0F, 6.0F, 19.0F),
				ModelTransform.NONE
		);
		head.addChild(EntityModelPartNames.RIGHT_EAR, ModelPartBuilder.create().uv(1, 1).cuboid(-6.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F), ModelTransform.of(-6.0F, -5.0F, 6.5F, 0.0F, 0.0F, -0.6981317F));
		head.addChild(EntityModelPartNames.LEFT_EAR, ModelPartBuilder.create().uv(1, 6).cuboid(0.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F), ModelTransform.of(6.0F, -5.0F, 6.5F, 0.0F, 0.0F, 0.6981317F));
		head.addChild(EntityModelPartNames.RIGHT_HORN, ModelPartBuilder.create().uv(10, 13).cuboid(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F), ModelTransform.pivot(-7.0F, -1.0F, -2.5F));
		head.addChild(EntityModelPartNames.LEFT_HORN, ModelPartBuilder.create().uv(1, 13).cuboid(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F), ModelTransform.pivot(7.0F, -1.0F, -2.5F));
		
		return TexturedModelData.of(modelData, 128, 64);
	}
	
	@Override
	public float getScale() {
		return 0.5F;
	}

}
