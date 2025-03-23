package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.*;

@Environment(EnvType.CLIENT)
public class EndermanHeadModel extends SpectrumSkullModel {
	
	private static final Identifier EYES_TEXTURE = new Identifier("textures/entity/enderman/enderman_eyes.png");
	protected final ModelPart eyes;
	
	public EndermanHeadModel(ModelPart root, ModelPart eyes) {
		super(root);
		this.eyes = eyes;
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData head = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), ModelTransform.NONE);
		head.addChild(EntityModelPartNames.JAW, ModelPartBuilder.create().uv(0, 16).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(-0.5F)), ModelTransform.NONE);
		
		return TexturedModelData.of(modelData, 64, 32);
	}
	
	@Override
	public void setHeadRotation(float animationProgress, float yaw, float pitch) {
		super.setHeadRotation(animationProgress, yaw, pitch);
		this.eyes.yaw = yaw * ROTATION_VEC;
		this.eyes.pitch = pitch * ROTATION_VEC;
	}
	
	public void render(MatrixStack matrices, VertexConsumer vertices, VertexConsumerProvider vertexConsumerProvider, int light, int overlay, float red, float green, float blue, float alpha) {
		super.render(matrices, vertices, vertexConsumerProvider, light, overlay, red, green, blue, alpha);
		
		VertexConsumer eyesVertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEyes(EYES_TEXTURE));
		this.eyes.render(matrices, eyesVertexConsumer, 15728640, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
	}
	
}