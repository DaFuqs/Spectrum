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
import org.jetbrains.annotations.*;

@Environment(EnvType.CLIENT)
public class KindlingEntityRenderer extends MobEntityRenderer<KindlingEntity, KindlingEntityModel> {
	
	public static final Identifier TEXTURE = SpectrumCommon.locate("textures/entity/kindling/kindling.png");
	public static final Identifier TEXTURE_BLINKING = SpectrumCommon.locate("textures/entity/kindling/kindling_blink.png");
	public static final Identifier TEXTURE_ANGRY = SpectrumCommon.locate("textures/entity/kindling/kindling_angry.png");
public static final Identifier TEXTURE_CLIPPED = SpectrumCommon.locate("textures/entity/kindling/kindling_clipped.png");
	public static final Identifier TEXTURE_BLINKING_CLIPPED = SpectrumCommon.locate("textures/entity/kindling/kindling_blink_clipped.png");
	public static final Identifier TEXTURE_ANGRY_CLIPPED = SpectrumCommon.locate("textures/entity/kindling/kindling_angry_clipped.png");

	public KindlingEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new KindlingEntityModel(context.getPart(SpectrumModelLayers.KINDLING)), 0.7F);
		this.addFeature(new KindlingEntityArmorFeatureRenderer(this, context.getModelLoader()));
	}
	
	@Override
	public void render(KindlingEntity entity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
		super.render(entity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
	}
	
	@Override
	public Identifier getTexture(@NotNull KindlingEntity entity) {
		boolean isClipped = entity.isClipped();
		if (entity.getAngerTime() > 0) {
			return isClipped ? TEXTURE_ANGRY_CLIPPED : TEXTURE_ANGRY;
		}

		boolean isBlinking = (entity.getId() - entity.getWorld().getTime()) % 120 == 0; // based on the entities' id, so not all blink at the same time
		if (isClipped) {
			return isBlinking ? TEXTURE_BLINKING_CLIPPED : TEXTURE_CLIPPED;
		}

		return isBlinking ? TEXTURE_BLINKING : TEXTURE;
	}
	
}
