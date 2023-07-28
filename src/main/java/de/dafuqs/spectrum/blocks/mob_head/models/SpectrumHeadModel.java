package de.dafuqs.spectrum.blocks.mob_head.models;

import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.*;

@Environment(EnvType.CLIENT)
public abstract class SpectrumHeadModel extends SkullBlockEntityModel {
	
	private final ModelPart head;
	
	public SpectrumHeadModel(ModelPart root) {
		this.head = root.getChild(EntityModelPartNames.HEAD);
	}
	
	@Override
	public void setHeadRotation(float animationProgress, float yaw, float pitch) {
		this.head.yaw = yaw * 0.017453292F;
		this.head.pitch = pitch * 0.017453292F;
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		matrices.push();
		float scale = getScale();
		matrices.scale(scale, scale, scale);
		this.head.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		matrices.pop();
	}
	
	public float getScale() {
		return 0.86F;
	}
	
}