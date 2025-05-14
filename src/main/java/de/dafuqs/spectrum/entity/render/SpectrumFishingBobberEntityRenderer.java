package de.dafuqs.spectrum.entity.render;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.items.tools.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.*;

public abstract class SpectrumFishingBobberEntityRenderer extends EntityRenderer<SpectrumFishingBobberEntity> {
	
	public SpectrumFishingBobberEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}
	
	public abstract RenderType getLayer(SpectrumFishingBobberEntity bobber);
	
	@Override
	public void render(SpectrumFishingBobberEntity fishingBobberEntity, float f, float g, PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int i) {
		Player playerEntity = fishingBobberEntity.getPlayerOwner();
		if (playerEntity != null) {
			poseStack.pushPose();
			poseStack.pushPose();
			poseStack.scale(0.5F, 0.5F, 0.5F);
			poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
			PoseStack.Pose entry = poseStack.last();
			VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(getLayer(fishingBobberEntity));
			vertex(vertexConsumer, entry, i, 0.0F, 0, 0, 1);
			vertex(vertexConsumer, entry, i, 1.0F, 0, 1, 1);
			vertex(vertexConsumer, entry, i, 1.0F, 1, 1, 0);
			vertex(vertexConsumer, entry, i, 0.0F, 1, 0, 0);
			poseStack.popPose();
			float h = playerEntity.getAttackAnim(g);
			float j = Mth.sin(Mth.sqrt(h) * 3.1415927F);
			Vec3 vec3d = this.getHandPos(playerEntity, j, g);
			Vec3 vec3d2 = fishingBobberEntity.getPosition(g).add(0.0, 0.25, 0.0);
			float k = (float) (vec3d.x - vec3d2.x);
			float l = (float) (vec3d.y - vec3d2.y);
			float m = (float) (vec3d.z - vec3d2.z);
			VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(RenderType.lineStrip());
			PoseStack.Pose entry2 = poseStack.last();
			
			for (int o = 0; o <= 16; ++o) {
				renderFishingLine(fishingBobberEntity, k, l, m, vertexConsumer2, entry2, percentage(o, 16), percentage(o + 1, 16));
			}
			
			poseStack.popPose();
			super.render(fishingBobberEntity, f, g, poseStack, vertexConsumerProvider, i);
		}
	}
	
	private Vec3 getHandPos(Player player, float f, float tickDelta) {
		int i = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
		ItemStack itemStack = player.getMainHandItem();
		if (!(itemStack.getItem() instanceof SpectrumFishingRodItem)) {
			i = -i;
		}
		
		if (this.entityRenderDispatcher.options.getCameraType().isFirstPerson() && player == Minecraft.getInstance().player) {
			double m = 960.0 / (double) this.entityRenderDispatcher.options.fov().get();
			Vec3 vec3d = this.entityRenderDispatcher.camera.getNearPlane().getPointOnPlane((float) i * 0.525F, -0.1F).scale(m).yRot(f * 0.5F).xRot(-f * 0.7F);
			return player.getEyePosition(tickDelta).add(vec3d);
		} else {
			float g = Mth.lerp(tickDelta, player.yBodyRotO, player.yBodyRot) * 0.017453292F;
			double d = Mth.sin(g);
			double e = Mth.cos(g);
			float h = player.getScale();
			double j = (double) i * 0.35 * (double) h;
			double k = 0.8 * (double) h;
			float l = player.isCrouching() ? -0.1875F : 0.0F;
			return player.getEyePosition(tickDelta).add(-e * j - d * k, (double) l - 0.45 * (double) h, -d * j + e * k);
		}
	}
	
	private static float percentage(int value, int max) {
		return (float) value / (float) max;
	}
	
	private static void vertex(VertexConsumer buffer, PoseStack.Pose matrix, int light, float x, int y, int u, int v) {
		buffer.addVertex(matrix, x - 0.5F, (float) y - 0.5F, 0.0F).setColor(-1).setUv((float) u, (float) v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(matrix, 0.0F, 1.0F, 0.0F);
	}
	
	private static void renderFishingLine(SpectrumFishingBobberEntity bobber, float x, float y, float z, VertexConsumer buffer, PoseStack.Pose matrices, float segmentStart, float segmentEnd) {
		float f = x * segmentStart;
		float g = y * (segmentStart * segmentStart + segmentStart) * 0.5F + 0.25F;
		float h = z * segmentStart;
		float i = x * segmentEnd - f;
		float j = y * (segmentEnd * segmentEnd + segmentEnd) * 0.5F + 0.25F - g;
		float k = z * segmentEnd - h;
		float l = Mth.sqrt(i * i + j * j + k * k);
		i /= l;
		j /= l;
		k /= l;
		buffer.addVertex(matrices, f, g, h).setColor(bobber.getLineColor()).setNormal(matrices, i, j, k);
	}
	
}
