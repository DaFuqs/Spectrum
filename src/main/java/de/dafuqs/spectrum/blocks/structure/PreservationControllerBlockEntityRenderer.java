package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import org.joml.*;

import java.lang.*;
import java.lang.Math;

public class PreservationControllerBlockEntityRenderer implements BlockEntityRenderer<PreservationControllerBlockEntity> {

	private static final Identifier AETHER_CORE = SpectrumCommon.locate("textures/block/preservation_controller_aether.png");
	protected static EntityRenderDispatcher dispatcher;

	public PreservationControllerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
	}

	@Override
	public void render(PreservationControllerBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		matrices.translate(0.5, 0.5, 0.5);
		matrices.multiply(dispatcher.camera.getRotation());
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
		var buffer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(AETHER_CORE));

		float time = entity.getWorld().getTime() % 24000 + tickDelta;

		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(time / 1.5F));
		float pulse = (float) (Math.sin((time / 19)));
		float scale = pulse * 0.2F + 0.8F;
		matrices.scale(scale, scale, scale);
		RenderHelper.renderFlatTrans(matrices, buffer, false, 15F, pulse * 0.25F + 0.75F, 0F, overlay);
		matrices.pop();
	}
}
