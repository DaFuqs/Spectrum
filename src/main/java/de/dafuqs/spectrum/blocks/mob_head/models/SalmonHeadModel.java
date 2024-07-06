package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class SalmonHeadModel extends SpectrumHeadModel {

	public SalmonHeadModel(ModelPart root) {
		super(root);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData head = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0)
						.cuboid(-1.5F, -5.0F, -4.0F, 3.0F, 5.0F, 8.0F),
				ModelTransform.NONE);
		head.addChild("face", ModelPartBuilder.create().uv(22, 0)
						.cuboid(-1.0F, -4.0F, -7.0F, 2.0F, 4.0F, 3.0F),
				ModelTransform.NONE);
		head.addChild("top_front_fin", ModelPartBuilder.create().uv(2, 1)
						.cuboid(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 3.0F),
				ModelTransform.pivot(0.0F, -7.0F, 1.0F));
		head.addChild("top_back_fin", ModelPartBuilder.create().uv(0, 2)
						.cuboid(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 4.0F),
				ModelTransform.pivot(0.0F, -7.0F, 3.0F));
		head.addChild("right_fin", ModelPartBuilder.create().uv(-4, 0)
						.cuboid(-2.0F, 0.0F, 0.0F, 2.0F, 0.0F, 2.0F),
				ModelTransform.of(-1.5F, -1.0F, -4.0F, 0.0F, 0.0F, (-(float) Math.PI / 4F)));
		head.addChild("left_fin", ModelPartBuilder.create().uv(0, 0)
						.cuboid(0.0F, 0.0F, 0.0F, 2.0F, 0.0F, 2.0F),
				ModelTransform.of(1.5F, -1.0F, -4.0F, 0.0F, 0.0F, ((float) Math.PI / 4F)));
		
		return TexturedModelData.of(modelData, 32, 32);
	}

}
