package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class ArmadilloHeadModel extends SpectrumSkullModel {
	
	public ArmadilloHeadModel(ModelPart root) {
		super(root);
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData modelPartData3 = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -4.0F, 0.0F));
		modelPartData3.addChild("head_cube", ModelPartBuilder.create().uv(43, 15).cuboid(-1.5F, -1.0F, -1.0F, 3.0F, 5.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));
		ModelPartData modelPartData4 = modelPartData3.addChild(EntityModelPartNames.RIGHT_EAR, ModelPartBuilder.create(), ModelTransform.pivot(-1.0F, -1.0F, 0.0F));
		modelPartData4.addChild("right_ear_cube", ModelPartBuilder.create().uv(43, 10).cuboid(-2.0F, -3.0F, 0.0F, 2.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, 0.0F, -0.6F, 0.1886F, -0.3864F, -0.0718F));
		ModelPartData modelPartData5 = modelPartData3.addChild(EntityModelPartNames.LEFT_EAR, ModelPartBuilder.create(), ModelTransform.pivot(1.0F, -2.0F, 0.0F));
		modelPartData5.addChild("left_ear_cube", ModelPartBuilder.create().uv(47, 10).cuboid(0.0F, -3.0F, 0.0F, 2.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, 1.0F, -0.6F, 0.1886F, 0.3864F, 0.0718F));
		
		return TexturedModelData.of(modelData, 64, 64);
	}
	
}