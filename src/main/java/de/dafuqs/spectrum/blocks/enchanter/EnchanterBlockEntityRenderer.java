package de.dafuqs.spectrum.blocks.enchanter;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

public class EnchanterBlockEntityRenderer implements BlockEntityRenderer<de.dafuqs.spectrum.blocks.enchanter.EnchanterBlockEntity> {
	
	double renderHeight = 0.65F;
	
	public EnchanterBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
	
	}
	
	@Override
	public void render(EnchanterBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
		ItemStack stack = blockEntity.inventory.getStack(0);
		if(!stack.isEmpty() && blockEntity.getItemFacingDirection() != null) {
			float time = blockEntity.getWorld().getTime() + tickDelta;
			Direction itemFacingDirection = blockEntity.getItemFacingDirection();
			
			matrixStack.push();
			// item stack rotation
			switch (itemFacingDirection) {
				case NORTH -> {
					matrixStack.translate(0.5, renderHeight, 0.7);
					matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(270));
					matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
				}
				case SOUTH -> { // perfect
					matrixStack.translate(0.5, renderHeight, 0.3);
					matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
				}
				case EAST -> {
					matrixStack.translate(0.3, renderHeight, 0.5);
					matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
					matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(270));
				}
				case WEST -> {
					matrixStack.translate(0.7, renderHeight, 0.5);
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
