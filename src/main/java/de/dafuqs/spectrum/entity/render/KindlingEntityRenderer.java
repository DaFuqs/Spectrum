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

@Environment(EnvType.CLIENT)
public class KindlingEntityRenderer extends MobEntityRenderer<KindlingEntity, KindlingEntityModel> {
	
	public static final Identifier TEXTURE = SpectrumCommon.locate("textures/entity/kindling/kindling_neutral.png");
	public static final Identifier TEXTURE_BLINKING = SpectrumCommon.locate("textures/entity/kindling/kindling_blink.png");
	public static final Identifier TEXTURE_ANGRY = SpectrumCommon.locate("textures/entity/kindling/kindling_angry.png");
	
	public KindlingEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new KindlingEntityModel(context.getPart(SpectrumModelLayers.KINDLING)), 0.7F);
		this.addFeature(new KindlingEntityArmorFeatureRenderer(this, context.getModelLoader()));
	}
	
	@Override
	public void render(KindlingEntity entity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
		super.render(entity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
	}
	
	@Override
	public Identifier getTexture(KindlingEntity entity) {
		if (entity.isAngry()) {
			return TEXTURE_ANGRY;
		}
		return (entity.getId() - entity.world.getTime()) % 120 == 0 ? TEXTURE_BLINKING : TEXTURE; // based on the entities' id, so not all blink at the same time
	}
	
}
