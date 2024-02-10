package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class ShulkerHeadModel extends SpectrumHeadModel {

	public ShulkerHeadModel(ModelPart root) {
		super(root);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData base = modelPartData.addChild(
				EntityModelPartNames.HEAD,
				ModelPartBuilder.create().uv(0, 28).cuboid(-8.0F, -8.0F, -8.0F, 16.0F, 8.0F, 16.0F),
				ModelTransform.NONE
		);
		
		base.addChild(
				"shulker_head",
				ModelPartBuilder.create().uv(0, 52).cuboid(-3.0F, -7.0F, -3.0F, 6.0F, 6.0F, 6.0F),
				ModelTransform.NONE
		);

		return TexturedModelData.of(modelData, 64, 64);
	}

}
