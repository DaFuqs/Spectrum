package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.math.*;
import org.joml.*;

import java.lang.*;
import java.lang.Math;

public class ProjectorBlockEntityRenderer implements BlockEntityRenderer<ProjectorBlockEntity> {

	protected static EntityRenderDispatcher dispatcher;

	public ProjectorBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
	}

	@Override
	public void render(ProjectorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		float time = entity.getWorld().getTime() % 24000 + tickDelta;
		double bob = Math.sin((time / 19)) * 0.075 * entity.bobMultiplier;

		matrices.push();
		matrices.translate(0.5D, entity.heightOffset + bob, 0.5D);
		var center = Vec3d.of(entity.getPos()).add(0.5, 0, 0.5);
		var xOffset = center.getX() - dispatcher.camera.getPos().x;
		var zOffset = center.getZ() - dispatcher.camera.getPos().z;
		matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) MathHelper.atan2(xOffset, zOffset)));
		var buffer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(entity.texture));
		RenderHelper.renderFlatTrans(matrices, buffer, false, entity.scaling, 0.75F, 0F, overlay);
		matrices.pop();
	}
}
