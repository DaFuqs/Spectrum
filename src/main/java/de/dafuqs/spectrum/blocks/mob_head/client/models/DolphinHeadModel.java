package de.dafuqs.spectrum.blocks.mob_head.client.models;


import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class DolphinHeadModel extends SpectrumSkullModel {
	
	public DolphinHeadModel(ModelPart root) {
		super(root);
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData head = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -7.0F, -3.0F, 8.0F, 7.0F, 6.0F), ModelTransform.NONE);
		head.addChild(EntityModelPartNames.NOSE, ModelPartBuilder.create().uv(0, 13).cuboid(-1.0F, 2.0F, -7.0F, 2.0F, 2.0F, 4.0F), ModelTransform.pivot(0.0F, -4.0F, 0.0F));
		
		return TexturedModelData.of(modelData, 64, 64);
	}
	
}