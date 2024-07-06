package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class TropicalFishHeadModel extends SpectrumHeadModel {
	
	public TropicalFishHeadModel(ModelPart root) {
		super(root);
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		Dilation dilation = new Dilation(0.01F);
		ModelPartData head = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -3.0F, -3.0F, 2.0F, 3.0F, 6.0F, dilation), ModelTransform.NONE);
		head.addChild("tail", ModelPartBuilder.create().uv(22, -6).cuboid(0.0F, -3.0F, 3.0F, 0.0F, 3.0F, 6.0F, dilation), ModelTransform.NONE);
		head.addChild("right_fin", ModelPartBuilder.create().uv(2, 16).cuboid(-2.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, dilation), ModelTransform.of(-1.0F, -1.0F, 0.0F, 0.0F, ((float) Math.PI / 4F), 0.0F));
		head.addChild("left_fin", ModelPartBuilder.create().uv(2, 12).cuboid(0.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, dilation), ModelTransform.of(1.0F, -1.0F, 0.0F, 0.0F, (-(float) Math.PI / 4F), 0.0F));
		head.addChild("top_fin", ModelPartBuilder.create().uv(10, -5).cuboid(0.0F, -6.0F, -3.0F, 0.0F, 3.0F, 6.0F, dilation), ModelTransform.NONE);
		
		return TexturedModelData.of(modelData, 64, 64);
	}
	
}
