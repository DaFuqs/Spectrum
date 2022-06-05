package de.dafuqs.spectrum.blocks.item_bowl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class ItemBowlBlockEntityRenderer implements BlockEntityRenderer<ItemBowlBlockEntity> {
	
	double radiant = Math.toRadians(360.0F);
	
	public ItemBowlBlockEntityRenderer(BlockEntityRendererFactory.Context renderContext) {
	
	}
	
	public void render(ItemBowlBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
		ItemStack stack = blockEntity.inventory.getStack(0);
		if (!stack.isEmpty()) {
			float time = blockEntity.getWorld().getTime() + tickDelta;
			
			matrixStack.push();
			double currentRadiant = radiant + (radiant * (time / 16.0) / 8.0F);
			double height = Math.sin((time + currentRadiant) / 8.0) / 7.0; // item height
			matrixStack.translate(0.5, 0.8 + height, 0.5); // position offset
			matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(time * 2)); // item stack rotation
			MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, matrixStack, vertexConsumerProvider, 0);
			matrixStack.pop();
		}
	}
	
}