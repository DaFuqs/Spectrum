package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.entity.models.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

@Environment(EnvType.CLIENT)
public class KindlingCoughEntityRenderer extends EntityRenderer<KindlingCoughEntity> {
	
	private static final Identifier TEXTURE = SpectrumCommon.locate("textures/entity/kindling/cough.png");
	private final KindlingCoughEntityModel<KindlingCoughEntity> model;
	
	public KindlingCoughEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
		this.model = new KindlingCoughEntityModel(context.getPart(SpectrumModelLayers.KINDLING_COUGH));
	}
	
	public void render(KindlingCoughEntity kindlingCoughEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		matrixStack.push();
		matrixStack.translate(0.0, 0.15000000596046448, 0.0);
		matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(g, kindlingCoughEntity.prevYaw, kindlingCoughEntity.getYaw()) - 90.0F));
		matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(g, kindlingCoughEntity.prevPitch, kindlingCoughEntity.getPitch())));
		this.model.setAngles(kindlingCoughEntity, g, 0.0F, -0.1F, 0.0F, 0.0F);
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(TEXTURE));
		this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		matrixStack.pop();
		super.render(kindlingCoughEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}
	
	public Identifier getTexture(KindlingCoughEntity kindlingCoughEntity) {
		return TEXTURE;
	}
}
