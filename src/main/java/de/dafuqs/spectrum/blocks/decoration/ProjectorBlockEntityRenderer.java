package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;
import net.minecraft.world.phys.*;

public class ProjectorBlockEntityRenderer implements BlockEntityRenderer<ProjectorBlockEntity> {
	
	protected static EntityRenderDispatcher dispatcher;
	
	@SuppressWarnings("unused")
	public ProjectorBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
	}
	
	@Override
	public void render(ProjectorBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		float time = entity.getLevel().getGameTime() % 24000 + tickDelta;
		double bob = Math.sin((time / 19)) * 0.075 * entity.bobMultiplier;
		
		matrices.pushPose();
		matrices.translate(0.5D, entity.heightOffset + bob, 0.5D);
		var center = Vec3.atLowerCornerOf(entity.getBlockPos()).add(0.5, 0, 0.5);
		var xOffset = center.x() - dispatcher.camera.getPosition().x;
		var zOffset = center.z() - dispatcher.camera.getPosition().z;
		matrices.mulPose(Axis.YP.rotation((float) Mth.atan2(xOffset, zOffset)));
		var buffer = vertexConsumers.getBuffer(RenderType.entityTranslucent(entity.texture));
		RenderHelper.renderFlatTrans(matrices, buffer, false, entity.scaling, 0.75F, 0F, overlay);
		matrices.popPose();
	}
}
