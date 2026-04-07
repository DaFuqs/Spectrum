package de.dafuqs.spectrum.entity.render;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;


public class BidentEntityRenderer extends EntityRenderer<BidentBaseEntity> {
	
	private final ItemRenderer itemRenderer;
	private final float scale;
	private final float offset;
	
	public BidentEntityRenderer(EntityRendererProvider.Context context) {
		this(context, 2F, -0.625F);
	}
	
	public BidentEntityRenderer(EntityRendererProvider.Context context, float scale, float offset) {
		super(context);
		this.itemRenderer = context.getItemRenderer();
		this.scale = scale;
		this.offset = offset;
	}
	
	@Override
	public void render(BidentBaseEntity bidentBaseEntity, float yaw, float tickDelta, PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int light) {
		ItemStack itemStack = bidentBaseEntity.getDefaultPickupItem();
		renderAsItemStack(bidentBaseEntity, tickDelta, poseStack, vertexConsumerProvider, light, itemStack);
		super.render(bidentBaseEntity, yaw, tickDelta, poseStack, vertexConsumerProvider, light);
	}
	
	private void renderAsItemStack(BidentBaseEntity entity, float tickDelta, PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int light, ItemStack itemStack) {
		BakedModel bakedModel = this.itemRenderer.getModel(itemStack, entity.level(), null, SpectrumModelPredicateProviders.MAGICAL_OVERSIZED_SEED);
		
		poseStack.pushPose();
		poseStack.translate(0, entity.getBoundingBox().getSize() / 2, 0);
		poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(tickDelta, entity.yRotO, entity.getYRot()) - 90.0F));
		poseStack.mulPose(Axis.ZP.rotationDegrees(-135 + Mth.lerp(tickDelta, entity.xRotO, entity.getXRot()) + 90.0F));
		poseStack.translate(0, offset, 0);
		
		poseStack.scale(scale, scale, scale);
		
		this.itemRenderer.render(itemStack, ItemDisplayContext.NONE, false, poseStack, vertexConsumerProvider, light, OverlayTexture.NO_OVERLAY, bakedModel);
		
		poseStack.popPose();
	}
	
	@Override
	public ResourceLocation getTextureLocation(BidentBaseEntity entity) {
		return InventoryMenu.BLOCK_ATLAS;
	}
	
}
