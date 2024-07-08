package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class SnifferHeadModel extends SpectrumSkullModel {
	
	public SnifferHeadModel(ModelPart root) {
		super(root);
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
						.uv(8, 15).cuboid(-6.5F, -15.0F, -3.0F, 13.0F, 18.0F, 11.0F)
						.uv(8, 4).cuboid(-6.5F, 0.0F, -3.0F, 13.0F, 0.0F, 11.0F),
				ModelTransform.NONE);
		head.addChild("left_ear", ModelPartBuilder.create().uv(2, 0).cuboid(0.0F, 0.0F, -3.0F, 1.0F, 19.0F, 7.0F), ModelTransform.pivot(6.51F, -15.0F, 3.99F));
		head.addChild("right_ear", ModelPartBuilder.create().uv(48, 0).cuboid(-1.0F, 0.0F, -3.0F, 1.0F, 19.0F, 7.0F), ModelTransform.pivot(-6.51F, -15.0F, 3.99F));
		head.addChild("nose", ModelPartBuilder.create().uv(10, 45).cuboid(-6.5F, -2.0F, -9.0F, 13.0F, 2.0F, 9.0F), ModelTransform.pivot(0.0F, -12.0F, -3.0F));
		head.addChild("lower_beak", ModelPartBuilder.create().uv(10, 57).cuboid(-6.5F, -7.0F, -8.0F, 13.0F, 12.0F, 9.0F), ModelTransform.pivot(0.0F, -5.0F, -4.0F));
		
		return TexturedModelData.of(modelData, 192, 192);
	}
	
	@Override
	public float getScale() {
		return 0.5F;
	}
	
}