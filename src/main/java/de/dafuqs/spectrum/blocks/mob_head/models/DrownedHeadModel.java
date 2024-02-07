package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class DrownedHeadModel extends SpectrumHeadModel {

	public DrownedHeadModel(ModelPart root) {
		super(root);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		Dilation dilation = new Dilation(0.01F);
		modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
				.uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation),
				ModelTransform.pivot(0.0F, 0.0F, 0.0F)
		);

		return TexturedModelData.of(modelData, 64, 64);
	}

}
