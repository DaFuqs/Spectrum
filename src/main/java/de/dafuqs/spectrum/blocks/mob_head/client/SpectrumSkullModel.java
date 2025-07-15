package de.dafuqs.spectrum.blocks.mob_head.client;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.renderer.*;
import net.neoforged.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public abstract class SpectrumSkullModel extends SkullModelBase {
	
	protected static final float ROTATION_VEC = (float) Math.PI / 180.0F;
	
	protected final ModelPart head;
	
	public SpectrumSkullModel(ModelPart root) {
		super();
		this.head = root.getChild("head");
	}
	
	public void render(PoseStack matrices, VertexConsumer vertices, MultiBufferSource vertexConsumerProvider, int light, int overlay, int color) {
		float scale = getScale();
		matrices.scale(scale, scale, scale);
		this.renderToBuffer(matrices, vertices, light, overlay, color);
	}
	
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		this.head.render(matrices, vertices, light, overlay, color);
	}
	
	@Override
	public void setupAnim(float animationProgress, float yaw, float pitch) {
		this.head.yRot = yaw * ROTATION_VEC;
		this.head.xRot = pitch * ROTATION_VEC;
	}
	
	public float getScale() {
		return 0.86F;
	}
	
}
