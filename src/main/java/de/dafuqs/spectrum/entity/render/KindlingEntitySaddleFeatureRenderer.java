package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.entity.models.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.*;

@Environment(EnvType.CLIENT)
public class KindlingEntitySaddleFeatureRenderer extends FeatureRenderer<KindlingEntity, KindlingEntityModel> {
	
	public static final Identifier TEXTURE = SpectrumCommon.locate("textures/entity/kindling/saddle.png");
	
	private final KindlingEntityModel model;
	
	public KindlingEntitySaddleFeatureRenderer(FeatureRendererContext<KindlingEntity, KindlingEntityModel> context, EntityModelLoader loader) {
		super(context);
		this.model = new KindlingEntityModel(loader.getModelPart(SpectrumModelLayers.KINDLING_SADDLE));
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, KindlingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (entity.isSaddled()) {
			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));
			this.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		}
	}
	
}
