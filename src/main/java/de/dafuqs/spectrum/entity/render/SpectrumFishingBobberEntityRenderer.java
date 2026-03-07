package de.dafuqs.spectrum.entity.render;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.*;

public abstract class SpectrumFishingBobberEntityRenderer extends EntityRenderer<SpectrumFishingHook> {
	
	private static final double VIEW_BOBBING_SCALE = 960.0;
	
	public SpectrumFishingBobberEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}
	
	public abstract RenderType getLayer(SpectrumFishingHook bobber);
	
	@Override
	public void render(SpectrumFishingHook entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		Player player = entity.getPlayerOwner();
		if (player != null) {
			poseStack.pushPose();
			poseStack.pushPose();
			poseStack.scale(0.5F, 0.5F, 0.5F);
			poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
			PoseStack.Pose pose = poseStack.last();
			VertexConsumer vertexConsumer = buffer.getBuffer(getLayer(entity));
			vertex(vertexConsumer, pose, packedLight, 0.0F, 0, 0, 1);
			vertex(vertexConsumer, pose, packedLight, 1.0F, 0, 1, 1);
			vertex(vertexConsumer, pose, packedLight, 1.0F, 1, 1, 0);
			vertex(vertexConsumer, pose, packedLight, 0.0F, 1, 0, 0);
			poseStack.popPose();
			float f = player.getAttackAnim(partialTicks);
			float g = Mth.sin(Mth.sqrt(f) * 3.1415927F);
			Vec3 vec3 = this.getPlayerHandPos(player, g, partialTicks);
			Vec3 vec32 = entity.getPosition(partialTicks).add(0.0, 0.25, 0.0);
			float h = (float) (vec3.x - vec32.x);
			float i = (float) (vec3.y - vec32.y);
			float j = (float) (vec3.z - vec32.z);
			VertexConsumer vertexConsumer2 = buffer.getBuffer(RenderType.lineStrip());
			PoseStack.Pose pose2 = poseStack.last();
			
			int lineColor = entity.getLineColor();
			for (int l = 0; l <= 16; ++l) {
				stringVertex(h, i, j, vertexConsumer2, pose2, fraction(l, 16), fraction(l + 1, 16), lineColor);
			}
			
			poseStack.popPose();
			super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
		}
	}
	
	private Vec3 getPlayerHandPos(Player player, float f, float partialTick) {
		int i = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
		ItemStack itemStack = player.getMainHandItem();
		if (!itemStack.is(SpectrumItemTags.FISHING_RODS)) {
			i = -i;
		}
		
		if (this.entityRenderDispatcher.options.getCameraType().isFirstPerson() && player == Minecraft.getInstance().player) {
			double m = VIEW_BOBBING_SCALE / (double) this.entityRenderDispatcher.options.fov().get();
			Vec3 vec3 = this.entityRenderDispatcher.camera.getNearPlane().getPointOnPlane((float) i * 0.525F, -0.1F).scale(m).yRot(f * 0.5F).xRot(-f * 0.7F);
			return player.getEyePosition(partialTick).add(vec3);
		} else {
			float g = Mth.lerp(partialTick, player.yBodyRotO, player.yBodyRot) * 0.017453292F;
			double d = Mth.sin(g);
			double e = Mth.cos(g);
			float h = player.getScale();
			double j = (double) i * 0.35 * (double) h;
			double k = 0.8 * (double) h;
			float l = player.isCrouching() ? -0.1875F : 0.0F;
			return player.getEyePosition(partialTick).add(-e * j - d * k, (double) l - 0.45 * (double) h, -d * j + e * k);
		}
	}
	
	private static float fraction(int numerator, int denominator) {
		return (float) numerator / (float) denominator;
	}
	
	private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, int packedLight, float x, int y, int u, int v) {
		consumer.addVertex(pose, x - 0.5F, (float) y - 0.5F, 0.0F).setColor(-1).setUv((float) u, (float) v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, 0.0F, 1.0F, 0.0F);
	}
	
	private static void stringVertex(float x, float y, float z, VertexConsumer consumer, PoseStack.Pose pose, float stringFraction, float nextStringFraction, int lineColor) {
		float f = x * stringFraction;
		float g = y * (stringFraction * stringFraction + stringFraction) * 0.5F + 0.25F;
		float h = z * stringFraction;
		float i = x * nextStringFraction - f;
		float j = y * (nextStringFraction * nextStringFraction + nextStringFraction) * 0.5F + 0.25F - g;
		float k = z * nextStringFraction - h;
		float l = Mth.sqrt(i * i + j * j + k * k);
		i /= l;
		j /= l;
		k /= l;
		consumer.addVertex(pose, f, g, h).setColor(lineColor).setNormal(pose, i, j, k);
	}
	
}
