package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;

@Environment(EnvType.CLIENT)
public class AxolotlHeadModel extends SpectrumHeadModel {

	public AxolotlHeadModel(ModelPart root) {
		super(root);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		Dilation dilation = new Dilation(0.001F);
		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 1)
						.cuboid(-4.0F, -5.0F, -3.0F, 8.0F, 5.0F, 5.0F, dilation),
				ModelTransform.NONE);
		head.addChild("top_gills", ModelPartBuilder.create().uv(3, 37)
						.cuboid(-4.0F, -8.0F, 1.0F, 8.0F, 3.0F, 0.0F, dilation),
				ModelTransform.NONE);
		head.addChild("left_gills", ModelPartBuilder.create().uv(0, 40)
						.cuboid(-7.0F, -7.0F, 1.0F, 3.0F, 7.0F, 0.0F, dilation),
				ModelTransform.NONE);
		head.addChild("right_gills", ModelPartBuilder.create().uv(11, 40)
						.cuboid(4.0F, -7.0F, 1.0F, 3.0F, 7.0F, 0.0F, dilation),
				ModelTransform.NONE);

		return TexturedModelData.of(modelData, 64, 64);
	}

}
