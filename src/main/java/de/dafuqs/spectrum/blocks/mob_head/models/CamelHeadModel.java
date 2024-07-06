package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;

public class CamelHeadModel extends SpectrumHeadModel {
	
	public CamelHeadModel(ModelPart root) {
		super(root);
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create()
						.uv(21, 0).cuboid(-3.5F, -14.0F, -3.5F, 7.0F, 14.0F, 7.0F)
						.uv(21, 0).cuboid(-3.5F, 0.0F, -3.5F, 7.0F, 0.0F, 7.0F),
				ModelTransform.NONE);
		head.addChild("nose", ModelPartBuilder.create()
						.uv(50, 0).cuboid(-2.5F, -14.0F, -9.5F, 5.0F, 5.0F, 6.0F),
				ModelTransform.NONE);
		head.addChild("left_ear", ModelPartBuilder.create()
						.uv(45, 0).cuboid(-0.5F, 0.5F, -1.0F, 3.0F, 1.0F, 2.0F),
				ModelTransform.pivot(3.0F, -14.0F, 2.0F));
		head.addChild("right_ear", ModelPartBuilder.create()
						.uv(67, 0).cuboid(-2.5F, 0.5F, -1.0F, 3.0F, 1.0F, 2.0F),
				ModelTransform.pivot(-3.0F, -14.0F, 2.0F));
		
		return TexturedModelData.of(modelData, 128, 128);
	}
	
	@Override
	public float getScale() {
		return 0.75F;
	}
	
}