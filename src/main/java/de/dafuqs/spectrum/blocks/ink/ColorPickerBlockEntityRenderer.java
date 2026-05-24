package de.dafuqs.spectrum.blocks.ink;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.world.item.*;


public class ColorPickerBlockEntityRenderer<T extends ColorPickerBlockEntity> implements BlockEntityRenderer<T> {
	
	@SuppressWarnings("unused")
	public ColorPickerBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
	}
	
	@Override
	public void render(ColorPickerBlockEntity blockEntity, float tickDelta, PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int light, int overlay) {
		Minecraft client = Minecraft.getInstance();
		// The item on top
		ItemStack stack = blockEntity.getItem(0);
		ItemStack stack2 = blockEntity.getItem(1);
		// lying on top
		if (!stack.isEmpty()) {
			poseStack.pushPose();
			poseStack.translate(0.5, 0.7, 0.6);
			poseStack.mulPose(Axis.XP.rotationDegrees(270));
			poseStack.mulPose(Axis.YP.rotationDegrees(180));
			Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, poseStack, vertexConsumerProvider, blockEntity.getLevel(), 0);
			poseStack.popPose();
		}
		// floating in air
		if (!stack2.isEmpty()) {
			poseStack.pushPose();
			
			float time = blockEntity.getLevel().getGameTime() % 50000 + tickDelta;
			double height = Math.sin((time) / 8.0) / 6.0; // item height
			
			poseStack.translate(0.5, 1.0 + height, 0.5);
			poseStack.mulPose(client.getBlockEntityRenderDispatcher().camera.rotation());
			poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
			Minecraft.getInstance().getItemRenderer().renderStatic(stack2, ItemDisplayContext.GROUND, light, overlay, poseStack, vertexConsumerProvider, blockEntity.getLevel(), 0);
			poseStack.popPose();
		}
	}
	
}
