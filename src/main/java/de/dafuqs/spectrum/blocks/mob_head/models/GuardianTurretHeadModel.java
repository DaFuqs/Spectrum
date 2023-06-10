package de.dafuqs.spectrum.blocks.mob_head.models;

import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.util.math.*;

@Environment(EnvType.CLIENT)
public class GuardianTurretHeadModel extends SkullBlockEntityModel {
	private final ModelPart head;
	
	public GuardianTurretHeadModel(ModelPart root) {
		this.head = root.getChild("head");
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(56, 24).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
				.uv(0, 44).cuboid(-2.5F, -4.0F, -5.0F, 5.0F, 3.0F, 1.0F, new Dilation(0.0F))
				.uv(45, 0).cuboid(-5.02F, -9.0F, -5.0F, 10.0F, 7.0F, 10.0F, new Dilation(0.0F)), ModelTransform.NONE);
		
		return TexturedModelData.of(modelData, 128, 128);
	}
	
	@Override
	public void setHeadRotation(float animationProgress, float yaw, float pitch) {
		this.head.yaw = yaw * 0.017453292F;
		this.head.pitch = pitch * 0.017453292F;
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		matrices.push();
		matrices.scale(0.86F, 0.86F, 0.86F);
		this.head.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		matrices.pop();
	}
	
}