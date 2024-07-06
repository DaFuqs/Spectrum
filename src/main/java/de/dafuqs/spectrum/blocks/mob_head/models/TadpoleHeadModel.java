package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class TadpoleHeadModel extends SpectrumHeadModel {
	
	public TadpoleHeadModel(ModelPart root) {
		super(root);
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData head = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-1.5F, -2.0F, -1.5F, 3.0F, 2.0F, 3.0F), ModelTransform.NONE);
		head.addChild("tail", ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 7.0F), ModelTransform.pivot(0.0F, -1.0F, 1.5F));
		
		return TexturedModelData.of(modelData, 16, 16);
	}
	
}
