package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class AxolotlHeadModel extends SpectrumHeadModel {

	public AxolotlHeadModel(ModelPart root) {
		super(root);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		Dilation dilation = new Dilation(0.001F);
		ModelPartData modelPartData3 = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 1).cuboid(-4.0F, -3.0F, -5.0F, 8.0F, 5.0F, 5.0F, dilation), ModelTransform.pivot(0.0F, 0.0F, -9.0F));
		ModelPartBuilder modelPartBuilder = ModelPartBuilder.create().uv(3, 37).cuboid(-4.0F, -3.0F, 0.0F, 8.0F, 3.0F, 0.0F, dilation);
		ModelPartBuilder modelPartBuilder2 = ModelPartBuilder.create().uv(0, 40).cuboid(-3.0F, -5.0F, 0.0F, 3.0F, 7.0F, 0.0F, dilation);
		ModelPartBuilder modelPartBuilder3 = ModelPartBuilder.create().uv(11, 40).cuboid(0.0F, -5.0F, 0.0F, 3.0F, 7.0F, 0.0F, dilation);
		modelPartData3.addChild("top_gills", modelPartBuilder, ModelTransform.pivot(0.0F, -3.0F, -1.0F));
		modelPartData3.addChild("left_gills", modelPartBuilder2, ModelTransform.pivot(-4.0F, 0.0F, -1.0F));
		modelPartData3.addChild("right_gills", modelPartBuilder3, ModelTransform.pivot(4.0F, 0.0F, -1.0F));

		return TexturedModelData.of(modelData, 64, 64);
	}

}
