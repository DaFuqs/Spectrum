package de.dafuqs.spectrum.blocks.mob_head.client;

import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.util.math.*;

@Environment(EnvType.CLIENT)
public abstract class SpectrumSkullModel extends SkullBlockEntityModel {
	
	protected static final float ROTATION_VEC = (float) Math.PI / 180.0F;
	
	protected final ModelPart head;
	
	public SpectrumSkullModel(ModelPart root) {
		super();
		this.head = root.getChild("head");
	}
	
	public void render(MatrixStack matrices, VertexConsumer vertices, VertexConsumerProvider vertexConsumerProvider, int light, int overlay, float red, float green, float blue, float alpha) {
		float scale = getScale();
		matrices.scale(scale, scale, scale);
		this.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
	
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		this.head.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
	
	@Override
	public void setHeadRotation(float animationProgress, float yaw, float pitch) {
		this.head.yaw = yaw * ROTATION_VEC;
		this.head.pitch = pitch * ROTATION_VEC;
	}
	
	public float getScale() {
		return 0.86F;
	}
	
}