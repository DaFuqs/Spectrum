package de.dafuqs.spectrum.blocks.enchanter;

import net.minecraft.client.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.util.math.*;
import org.joml.Math;

public class EnchanterBlockEntityRenderer implements BlockEntityRenderer<de.dafuqs.spectrum.blocks.enchanter.EnchanterBlockEntity> {

	protected static final double ITEM_STACK_RENDER_HEIGHT = 0.95F;
	protected static EntityRenderDispatcher dispatcher;

	public EnchanterBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
	}

	@Override
	public void render(EnchanterBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
		// The item lying on top of the enchanter
		ItemStack stack = blockEntity.getStack(0);
		if (!stack.isEmpty() && blockEntity.getItemFacingDirection() != null) {
			Direction itemFacingDirection = blockEntity.getItemFacingDirection();

			matrixStack.push();
			// item stack rotation
			switch (itemFacingDirection) {
				case NORTH -> {
					matrixStack.translate(0.5, ITEM_STACK_RENDER_HEIGHT, 0.7);
					matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(270));
					matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
				}
				case SOUTH -> { // perfect
					matrixStack.translate(0.5, ITEM_STACK_RENDER_HEIGHT, 0.3);
					matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
				}
				case EAST -> {
					matrixStack.translate(0.3, ITEM_STACK_RENDER_HEIGHT, 0.5);
					matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
					matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(270));
				}
				case WEST -> {
					matrixStack.translate(0.7, ITEM_STACK_RENDER_HEIGHT, 0.5);
					matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(270));
					matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90));
					matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
				}
				default -> {
				}
			}

			MinecraftClient.getInstance().getItemRenderer().renderItem(
					stack, ModelTransformationMode.GROUND, light, overlay, matrixStack, vertexConsumerProvider, blockEntity.getWorld(), 0);
			matrixStack.pop();
		}

		// The Experience Item rendered in the air
		ItemStack experienceItemStack = blockEntity.getStack(1);
		if (!experienceItemStack.isEmpty()) {
			float timeWithTickDelta = (blockEntity.getWorld().getTime() % 50000) + tickDelta;
			float scale = 0.5F + (float) (Math.sin(timeWithTickDelta / 8.0) / 8.0);

			matrixStack.push();
			matrixStack.translate(0.5D, 2.5D, 0.5D);
			matrixStack.multiply(dispatcher.camera.getRotation());
			matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
			matrixStack.scale(scale, scale, scale);

			MinecraftClient.getInstance().getItemRenderer().renderItem(
					experienceItemStack, ModelTransformationMode.FIXED, LightmapTextureManager.MAX_LIGHT_COORDINATE,
					overlay, matrixStack, vertexConsumerProvider, blockEntity.getWorld(), 0);

			matrixStack.pop();
		}
	}

}
