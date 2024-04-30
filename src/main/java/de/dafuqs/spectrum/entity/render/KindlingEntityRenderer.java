package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.entity.models.*;
import de.dafuqs.spectrum.entity.variants.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

@Environment(EnvType.CLIENT)
public class KindlingEntityRenderer extends MobEntityRenderer<KindlingEntity, KindlingEntityModel> {
	
	public static final Identifier SADDLE_TEXTURE = SpectrumCommon.locate("textures/entity/kindling/saddle.png");
	
	public KindlingEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new KindlingEntityModel(context.getPart(SpectrumModelLayers.KINDLING)), 0.7F);
		this.addFeature(new SaddleFeatureRenderer<>(this, new KindlingEntityModel(context.getPart(SpectrumModelLayers.KINDLING_SADDLE)), SADDLE_TEXTURE));
		this.addFeature(new KindlingEntityArmorFeatureRenderer(this, context.getModelLoader()));
	}
	
	@Override
	public void render(KindlingEntity entity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
		super.render(entity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
	}
	
	@Override
	public Identifier getTexture(@NotNull KindlingEntity entity) {
		KindlingVariant variant = entity.getKindlingVariant();
		boolean isClipped = entity.isClipped();
		if (entity.getAngerTime() > 0) {
			return isClipped ? variant.angryClippedTexture() : variant.angryTexture();
		}

		boolean isBlinking = (entity.getId() - entity.getWorld().getTime()) % 120 == 0; // based on the entities' id, so not all blink at the same time
		if (isClipped) {
			return isBlinking ? variant.blinkingClippedTexture() : variant.clippedTexture();
		}

		return isBlinking ? variant.blinkingTexture() : variant.defaultTexture();
	}
	
}
