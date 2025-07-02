package de.dafuqs.spectrum.entity.render;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.entity.models.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;

@Environment(EnvType.CLIENT)
public class KindlingCoughEntityRenderer extends EntityRenderer<KindlingCoughEntity> {
	
	private static final ResourceLocation TEXTURE = SpectrumCommon.locate("textures/entity/kindling/cough.png");
	private final KindlingCoughEntityModel model;
	
	public KindlingCoughEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.model = new KindlingCoughEntityModel(context.bakeLayer(SpectrumModelLayers.KINDLING_COUGH));
	}
	
	public void render(KindlingCoughEntity kindlingCoughEntity, float f, float g, PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int i) {
		poseStack.pushPose();
		poseStack.translate(0.0, 0.15000000596046448, 0.0);
		poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(g, kindlingCoughEntity.yRotO, kindlingCoughEntity.getYRot()) - 90.0F));
		poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(g, kindlingCoughEntity.xRotO, kindlingCoughEntity.getXRot())));
		this.model.setupAnim(kindlingCoughEntity, g, 0.0F, -0.1F, 0.0F, 0.0F);
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.renderType(TEXTURE));
		this.model.renderToBuffer(poseStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY);
		poseStack.popPose();
		super.render(kindlingCoughEntity, f, g, poseStack, vertexConsumerProvider, i);
	}
	
	public ResourceLocation getTextureLocation(KindlingCoughEntity kindlingCoughEntity) {
		return TEXTURE;
	}
}
