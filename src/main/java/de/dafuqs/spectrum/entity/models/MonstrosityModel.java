package de.dafuqs.spectrum.entity.models;

import de.dafuqs.spectrum.entity.entity.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.*;

@Environment(EnvType.CLIENT)
public class MonstrosityModel extends EntityModel<MonstrosityEntity> {
	
	private final ModelPart torso;
	
	public MonstrosityModel(ModelPart root) {
		this.torso = root.getChild("torso");
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("torso", ModelPartBuilder.create().uv(0, 0).cuboid(-10.0F, -12.0F, -10.0F, 20.0F, 12.0F, 20.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		
		return TexturedModelData.of(modelData, 128, 128);
	}
	
	@Override
	public void setAngles(MonstrosityEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		torso.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
	
}