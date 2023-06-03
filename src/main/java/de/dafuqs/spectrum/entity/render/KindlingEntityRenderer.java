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
	
	private static final Identifier TEXTURE = SpectrumCommon.locate("textures/entity/monstrosity.png");
	
	public KindlingEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new KindlingEntityModel(context.getPart(SpectrumModelLayers.KINDLING)), 1.8F);
	}
	
	@Override
	public void render(KindlingEntity entity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
		super.render(entity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
	}
	
	@Override
	public Identifier getTexture(KindlingEntity entity) {
		return TEXTURE;
	}
	
}
