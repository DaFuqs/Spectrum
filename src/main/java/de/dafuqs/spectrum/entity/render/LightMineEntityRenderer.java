package de.dafuqs.spectrum.entity.render;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.neoforged.api.distmarker.*;
import org.joml.*;

@OnlyIn(Dist.CLIENT)
public class LightMineEntityRenderer extends EntityRenderer<LightMineEntity> {
	
	public LightMineEntityRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
	}
	
	@Override
	public void render(LightMineEntity mine, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
		matrices.pushPose();
		
		var age = mine.tickCount;
		var alpha = Mth.clamp(1 - Mth.lerp(tickDelta, mine.getVanishingProgress(age - 1), mine.getVanishingProgress(age)), 0F, 1F);
		var scaleFactor = Mth.sin((age + tickDelta) / 8F) / 6F + mine.getScaleOffset();
		
		matrices.mulPose(this.entityRenderDispatcher.cameraOrientation());
		matrices.mulPose(Axis.YP.rotationDegrees(180f));
		matrices.scale(scaleFactor, scaleFactor, 1);
		matrices.translate(-0.5F, -0.5F, 0);
		
		var consumer = vertexConsumers.getBuffer(RenderType.entityTranslucentCull(getTextureLocation(mine)));
		var matrix = matrices.last();
		var positions = matrix.pose();
		
		Vector3f color = SpectrumColorHelper.colorIntToVec(mine.getColor());
		consumer.addVertex(positions, 0, 0, 0).setColor(color.x(), color.y(), color.z(), alpha).setUv(0, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(matrix, 0, 1, 0);
		consumer.addVertex(positions, 1, 0, 0).setColor(color.x(), color.y(), color.z(), alpha).setUv(1, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(matrix, 0, 1, 0);
		consumer.addVertex(positions, 1, 1, 0).setColor(color.x(), color.y(), color.z(), alpha).setUv(1, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(matrix, 0, 1, 0);
		consumer.addVertex(positions, 0, 1, 0).setColor(color.x(), color.y(), color.z(), alpha).setUv(0, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(matrix, 0, 1, 0);
		
		matrices.popPose();
		
		super.render(mine, yaw, tickDelta, matrices, vertexConsumers, light);
	}
	
	@Override
	public ResourceLocation getTextureLocation(LightMineEntity entity) {
		return entity.getTextureLocation();
	}
	
}
