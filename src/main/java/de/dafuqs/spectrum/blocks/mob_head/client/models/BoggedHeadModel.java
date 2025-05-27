package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class BoggedHeadModel extends SpectrumSkullModel {
	
	public BoggedHeadModel(ModelPart root) {
		super(root);
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData head = modelPartData.addChild(
				EntityModelPartNames.HEAD,
				ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.25F)),
				ModelTransform.NONE
		);
		head.addChild("red_mushroom_1", ModelPartBuilder.create().uv(50, 16).cuboid(-3.0F, -3.0F, 0.0F, 6.0F, 4.0F, 0.0F), ModelTransform.of(3.0F, -8.0F, 3.0F, 0.0F, 0.7853982F, 0.0F));
		head.addChild("red_mushroom_2", ModelPartBuilder.create().uv(50, 16).cuboid(-3.0F, -3.0F, 0.0F, 6.0F, 4.0F, 0.0F), ModelTransform.of(3.0F, -8.0F, 3.0F, 0.0F, 2.3561945F, 0.0F));
		head.addChild("brown_mushroom_1", ModelPartBuilder.create().uv(50, 22).cuboid(-3.0F, -3.0F, 0.0F, 6.0F, 4.0F, 0.0F), ModelTransform.of(-3.0F, -8.0F, -3.0F, 0.0F, 0.7853982F, 0.0F));
		head.addChild("brown_mushroom_2", ModelPartBuilder.create().uv(50, 22).cuboid(-3.0F, -3.0F, 0.0F, 6.0F, 4.0F, 0.0F), ModelTransform.of(-3.0F, -8.0F, -3.0F, 0.0F, 2.3561945F, 0.0F));
		head.addChild("brown_mushroom_3", ModelPartBuilder.create().uv(50, 28).cuboid(-3.0F, -4.0F, 0.0F, 6.0F, 4.0F, 0.0F), ModelTransform.of(-2.0F, -1.0F, 4.0F, -1.5707964F, 0.0F, 0.7853982F));
		head.addChild("brown_mushroom_4", ModelPartBuilder.create().uv(50, 28).cuboid(-3.0F, -4.0F, 0.0F, 6.0F, 4.0F, 0.0F), ModelTransform.of(-2.0F, -1.0F, 4.0F, -1.5707964F, 0.0F, 2.3561945F));
		
		return TexturedModelData.of(modelData, 64, 32);
	}
	
	public static TexturedModelData getTexturedModelDataOverlay() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		modelPartData.addChild(
				EntityModelPartNames.HEAD,
				ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.25F)),
				ModelTransform.NONE
		);
		
		return TexturedModelData.of(modelData, 64, 32);
	}
	
}