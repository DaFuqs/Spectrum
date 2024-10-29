package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.item.*;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.screen.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

@Environment(EnvType.CLIENT)
public class BidentEntityRenderer extends EntityRenderer<BidentBaseEntity> {
	
	private final ItemRenderer itemRenderer;
	private final float scale;
	private final float offset;
	
	public BidentEntityRenderer(EntityRendererFactory.Context context) {
		this(context, 2F, -0.625F);
	}
	
	public BidentEntityRenderer(EntityRendererFactory.Context context, float scale, float offset) {
		super(context);
		this.itemRenderer = context.getItemRenderer();
		this.scale = scale;
		this.offset = offset;
	}
	
	@Override
	public void render(BidentBaseEntity bidentBaseEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
		ItemStack itemStack = bidentBaseEntity.getTrackedStack();
		renderAsItemStack(bidentBaseEntity, tickDelta, matrixStack, vertexConsumerProvider, light, itemStack);
		super.render(bidentBaseEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
	}
	
	private void renderAsItemStack(BidentBaseEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, ItemStack itemStack) {
		// Originally used for over-sized item rendering.
		//SpectrumModelPredicateProviders.currentItemRenderMode = ModelTransformationMode.NONE;
		BakedModel bakedModel = this.itemRenderer.getModel(itemStack, entity.getWorld(), null, 817210941);
		
		matrixStack.push();
		matrixStack.translate(0, entity.calculateBoundingBox().getAverageSideLength() / 2, 0);
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()) - 90.0F));
		matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-135 + MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch()) + 90.0F));
		matrixStack.translate(0, offset, 0);

		matrixStack.scale(scale, scale, scale);

		this.itemRenderer.renderItem(itemStack, ModelTransformationMode.NONE, false, matrixStack, vertexConsumerProvider, light, OverlayTexture.DEFAULT_UV, bakedModel);

		matrixStack.pop();
	}

	@Override
	public Identifier getTexture(BidentBaseEntity entity) {
		return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
	}

}
