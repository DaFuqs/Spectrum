package de.dafuqs.spectrum.entity.render;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.neoforged.api.distmarker.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;

@OnlyIn(Dist.CLIENT)
public class LightSpearEntityRenderer extends EntityRenderer<LightShardBaseEntity> {
	
	public LightSpearEntityRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
	}
	
	@Override
	public void render(LightShardBaseEntity shard, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
		matrices.pushPose();
		
		var age = shard.tickCount;
		var alpha = Mth.clamp(1 - Mth.lerp(tickDelta, shard.getVanishingProgress(age - 1), shard.getVanishingProgress(age)), 0F, 1F);
		var scaleFactor = Mth.sin((age + tickDelta) / 8F) / 6F + shard.getScaleOffset();
		
		matrices.mulPose(this.entityRenderDispatcher.cameraOrientation());
		matrices.mulPose(Axis.YP.rotationDegrees(Mth.lerp(tickDelta, shard.yRotO, shard.getYRot()) - 45.0F));
		matrices.mulPose(Axis.ZP.rotationDegrees(0 + Mth.lerp(tickDelta, shard.xRotO, shard.getXRot())));
		
		matrices.scale(scaleFactor, scaleFactor, 1);
		matrices.translate(-0.5F, -0.5F, 0);
		
		var consumer = vertexConsumers.getBuffer(RenderType.entityTranslucentCull(getTextureLocation(shard)));
		var matrix = matrices.last();
		var positions = matrix.pose();
		
		consumer.addVertex(positions, 0, 0, 0).setColor(1f, 1f, 1f, alpha).setUv(0, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(matrix, 0, 1, 0);
		consumer.addVertex(positions, 1, 0, 0).setColor(1f, 1f, 1f, alpha).setUv(1, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(matrix, 0, 1, 0);
		consumer.addVertex(positions, 1, 1, 0).setColor(1f, 1f, 1f, alpha).setUv(1, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(matrix, 0, 1, 0);
		consumer.addVertex(positions, 0, 1, 0).setColor(1f, 1f, 1f, alpha).setUv(0, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(matrix, 0, 1, 0);
		
		matrices.popPose();
		
		super.render(shard, yaw, tickDelta, matrices, vertexConsumers, light);
	}
	
	@Override
	public ResourceLocation getTextureLocation(LightShardBaseEntity entity) {
		return entity.getTextureLocation();
	}
}
