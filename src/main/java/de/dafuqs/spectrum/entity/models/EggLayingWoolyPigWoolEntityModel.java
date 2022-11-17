package de.dafuqs.spectrum.entity.models;

import de.dafuqs.spectrum.entity.entity.EggLayingWoolyPigEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class EggLayingWoolyPigWoolEntityModel extends EntityModel<EggLayingWoolyPigEntity> {

	private final ModelPart torso;

	public EggLayingWoolyPigWoolEntityModel(ModelPart root) {
		super();
		this.torso = root.getChild("torso");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		modelPartData.addChild("torso", ModelPartBuilder.create()
			.uv(0, 0).cuboid(-6.5F, -15.5F, -9.5F, 13.0F, 13.0F, 19.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		return TexturedModelData.of(modelData, 128, 128);
	}
	@Override
	public void setAngles(EggLayingWoolyPigEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		if (child) {
			matrices.scale(0.6f, 0.6f, 0.6f);
			matrices.translate(0, 1, 0);
		}
		torso.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}