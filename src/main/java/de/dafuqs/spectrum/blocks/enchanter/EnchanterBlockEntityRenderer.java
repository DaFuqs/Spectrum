package de.dafuqs.spectrum.blocks.enchanter;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.core.*;
import net.minecraft.world.item.*;
import org.joml.*;
import org.joml.Math;

public class EnchanterBlockEntityRenderer implements BlockEntityRenderer<EnchanterBlockEntity> {
	
	protected static final double ITEM_STACK_RENDER_HEIGHT = 0.95F;
	protected static EntityRenderDispatcher dispatcher;
	
	@SuppressWarnings("unused")
	public EnchanterBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
	}
	
	@Override
	public void render(EnchanterBlockEntity blockEntity, float tickDelta, PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int light, int overlay) {
		// The item lying on top of the enchanter
		ItemStack stack = blockEntity.getItem(0);
		if (!stack.isEmpty() && blockEntity.getItemFacingDirection() != null) {
			Direction itemFacingDirection = blockEntity.getItemFacingDirection();
			
			poseStack.pushPose();
			// item stack rotation
			switch (itemFacingDirection) {
				case NORTH -> {
					poseStack.translate(0.5, ITEM_STACK_RENDER_HEIGHT, 0.7);
					poseStack.mulPose(Axis.XP.rotationDegrees(270));
					poseStack.mulPose(Axis.YP.rotationDegrees(180));
				}
				case SOUTH -> { // perfect
					poseStack.translate(0.5, ITEM_STACK_RENDER_HEIGHT, 0.3);
					poseStack.mulPose(Axis.XP.rotationDegrees(90));
				}
				case EAST -> {
					poseStack.translate(0.3, ITEM_STACK_RENDER_HEIGHT, 0.5);
					poseStack.mulPose(Axis.XP.rotationDegrees(90));
					poseStack.mulPose(Axis.ZP.rotationDegrees(270));
				}
				case WEST -> {
					poseStack.translate(0.7, ITEM_STACK_RENDER_HEIGHT, 0.5);
					poseStack.mulPose(Axis.XP.rotationDegrees(270));
					poseStack.mulPose(Axis.ZP.rotationDegrees(90));
					poseStack.mulPose(Axis.YP.rotationDegrees(180));
				}
				default -> {
				}
			}
			
			Minecraft.getInstance().getItemRenderer().renderStatic(
					stack, ItemDisplayContext.GROUND, light, overlay, poseStack, vertexConsumerProvider, blockEntity.getLevel(), 0);
			poseStack.popPose();
		}
		
		// The Experience Item rendered in the air
		ItemStack experienceItemStack = blockEntity.getItem(1);
		if (!experienceItemStack.isEmpty()) {
			float timeWithTickDelta = (blockEntity.getLevel().getGameTime() % 50000) + tickDelta;
			float scale = 0.5F + (float) (Math.sin(timeWithTickDelta / 8.0) / 8.0);
			
			poseStack.pushPose();
			poseStack.translate(0.5D, 2.5D, 0.5D);
			poseStack.mulPose(dispatcher.camera.rotation());
			poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
			poseStack.scale(scale, scale, scale);
			
			Minecraft.getInstance().getItemRenderer().renderStatic(
					experienceItemStack, ItemDisplayContext.FIXED, LightTexture.FULL_BRIGHT,
					overlay, poseStack, vertexConsumerProvider, blockEntity.getLevel(), 0);
			
			poseStack.popPose();
		}
	}
	
}
