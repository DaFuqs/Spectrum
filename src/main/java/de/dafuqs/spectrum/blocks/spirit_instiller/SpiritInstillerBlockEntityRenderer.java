package de.dafuqs.spectrum.blocks.spirit_instiller;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.RotationAxis;

public class SpiritInstillerBlockEntityRenderer implements BlockEntityRenderer<SpiritInstillerBlockEntity> {
	
	protected double ITEM_STACK_RENDER_HEIGHT = 0.95F;
	
	public SpiritInstillerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
	
	}
	
	@Override
	public void render(SpiritInstillerBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
		
		// The item lying on top of the spirit instiller
		ItemStack stack = blockEntity.getStack(0);
		if (!stack.isEmpty() && blockEntity.getMultiblockRotation() != null) {
			BlockRotation itemFacingDirection = blockEntity.getMultiblockRotation();
			
			matrixStack.push();
			// item stack rotation
			switch (itemFacingDirection) {
				case NONE -> {
					matrixStack.translate(0.5, ITEM_STACK_RENDER_HEIGHT, 0.7);
					matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(270));
					matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
				}
				case CLOCKWISE_90 -> {
					matrixStack.translate(0.3, ITEM_STACK_RENDER_HEIGHT, 0.5);
					matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
					matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(270));
				}
				case CLOCKWISE_180 -> {
					matrixStack.translate(0.5, ITEM_STACK_RENDER_HEIGHT, 0.3);
					matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
				}
				case COUNTERCLOCKWISE_90 -> {
					matrixStack.translate(0.7, ITEM_STACK_RENDER_HEIGHT, 0.5);
					matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(270));
					matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90));
					matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
				}
			}
			
			MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, light, overlay, matrixStack, vertexConsumerProvider, blockEntity.getWorld(), 0);
			matrixStack.pop();
		}
		
	}
	
	
}
