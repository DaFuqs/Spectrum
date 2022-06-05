package de.dafuqs.spectrum.blocks.spirit_instiller;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Vec3f;

public class SpiritInstillerBlockEntityRenderer implements BlockEntityRenderer<SpiritInstillerBlockEntity> {
	
	protected double itemStackRenderHeight = 0.95F;
	
	public SpiritInstillerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
	
	}
	
	@Override
	public void render(SpiritInstillerBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
		
		// The item lying on top of the spirit instiller
		ItemStack stack = blockEntity.inventory.getStack(0);
		if (!stack.isEmpty() && blockEntity.getMultiblockRotation() != null) {
			BlockRotation itemFacingDirection = blockEntity.getMultiblockRotation();
			
			matrixStack.push();
			// item stack rotation
			switch (itemFacingDirection) {
				case CLOCKWISE_90 -> {
					matrixStack.translate(0.5, itemStackRenderHeight, 0.7);
					matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(270));
					matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
				}
				case CLOCKWISE_180 -> { // perfect
					matrixStack.translate(0.5, itemStackRenderHeight, 0.3);
					matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
				}
				case COUNTERCLOCKWISE_90 -> {
					matrixStack.translate(0.3, itemStackRenderHeight, 0.5);
					matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
					matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(270));
				}
				case NONE -> {
					matrixStack.translate(0.7, itemStackRenderHeight, 0.5);
					matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(270));
					matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90));
					matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
				}
			}
			
			MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, matrixStack, vertexConsumerProvider, 0);
			matrixStack.pop();
		}
		
	}
	
	
}
