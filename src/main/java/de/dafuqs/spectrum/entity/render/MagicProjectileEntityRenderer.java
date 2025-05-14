package de.dafuqs.spectrum.entity.render;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.inventory.*;
import org.joml.*;

import java.lang.Math;

public class MagicProjectileEntityRenderer extends EntityRenderer<MagicProjectileEntity> {
	
	private static final ResourceLocation TEXTURE = ResourceLocation.parse("textures/entity/experience_orb.png");
	private static final RenderType LAYER = RenderType.itemEntityTranslucentCull(TEXTURE);
	
	public MagicProjectileEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(MagicProjectileEntity magicProjectileEntity, float yaw, float tickDelta, PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int light) {
		poseStack.pushPose();
		Vector3f starColor = magicProjectileEntity.getInkColor().getColorVec();
		
		double time = (magicProjectileEntity.level().getGameTime() % 24000) + tickDelta + RandomSource.create(magicProjectileEntity.getId()).nextInt(200);
		float scale = 0.75F + 0.1F * (float) Math.sin(time / 10);
		poseStack.scale(scale, scale, scale);
		
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
		
		float h = 0.75F;
		float k = 1F;
		float l = 0F;
		float m = 0.25F;
		int s = (int) (starColor.x() * 255.0F);
		int t = (int) (starColor.y() * 255.0F);
		int u = (int) (starColor.z() * 255.0F);
		PoseStack.Pose entry = poseStack.last();
		Matrix4f matrix4f = entry.pose();
		
		poseStack.translate(0.0D, 0.10000000149011612D, 0.0D);
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
		
		vertex(vertexConsumer, entry, matrix4f, -0.5F, -0.25F, s, t, u, h, m, light);
		vertex(vertexConsumer, entry, matrix4f, 0.5F, -0.25F, s, t, u, k, m, light);
		vertex(vertexConsumer, entry, matrix4f, 0.5F, 0.75F, s, t, u, k, l, light);
		vertex(vertexConsumer, entry, matrix4f, -0.5F, 0.75F, s, t, u, h, l, light);
		poseStack.popPose();
	}
	
	private static void vertex(VertexConsumer vertexConsumer, PoseStack.Pose matrix, Matrix4f positionMatrix, float x, float y, int red, int green, int blue, float u, float v, int light) {
		vertexConsumer.addVertex(positionMatrix, x, y, 0.0F).setColor(red, green, blue, 128).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(matrix, 0.0F, 1.0F, 0.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(MagicProjectileEntity entity) {
		return InventoryMenu.BLOCK_ATLAS;
	}
	
}
