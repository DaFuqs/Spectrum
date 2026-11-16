package de.dafuqs.spectrum.blocks.ink.gen;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.core.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.properties.*;


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
		
		// standing on top
		if (!stack2.isEmpty()) {
			Direction direction = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
			
			poseStack.pushPose();
			poseStack.translate(0.5 - direction.getStepX() * 0.1F, 0.7, 0.5 - direction.getStepZ() * 0.1F); // set a bit back
			poseStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot())); // facing the front
			poseStack.mulPose(Axis.XP.rotationDegrees(-30)); // leaning towards the back
			Minecraft.getInstance().getItemRenderer().renderStatic(stack2, ItemDisplayContext.GROUND, light, overlay, poseStack, vertexConsumerProvider, blockEntity.getLevel(), 0);
			poseStack.popPose();
		}
		
		// floating in air
		if (!stack.isEmpty()) {
			poseStack.pushPose();
			
			float time = blockEntity.getLevel().getGameTime() % 50000 + tickDelta;
			double height = Math.sin((time) / 8.0) / 6.0; // item height
			
			poseStack.translate(0.5, 1.5 + height, 0.5);
			poseStack.mulPose(client.getBlockEntityRenderDispatcher().camera.rotation());
			poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
			Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, poseStack, vertexConsumerProvider, blockEntity.getLevel(), 0);
			poseStack.popPose();
		}
	}
	
}
