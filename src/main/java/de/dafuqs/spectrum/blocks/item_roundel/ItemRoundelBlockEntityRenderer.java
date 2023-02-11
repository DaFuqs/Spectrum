package de.dafuqs.spectrum.blocks.item_roundel;

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

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ItemRoundelBlockEntityRenderer<T extends ItemRoundelBlockEntity> implements BlockEntityRenderer<T> {
	
	private static final float distance = 0.29F;
	
	public ItemRoundelBlockEntityRenderer(BlockEntityRendererFactory.Context renderContext) {
	
	}
	
	public void render(ItemRoundelBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
		if (!blockEntity.isEmpty()) {
			// the floating item stacks
			List<ItemStack> inventoryStacks = new ArrayList<>();
			for (int i = 0; i < blockEntity.size(); i++) {
				ItemStack stack = blockEntity.getStack(i);
				if (!stack.isEmpty()) {
					if(blockEntity.renderStacksAsIndividualItems()) {
						for (int j = 0; j < stack.getCount(); j++) {
							inventoryStacks.add(stack);
						}
					} else {
						inventoryStacks.add(stack);
					}
				}
			}
			
			float time = blockEntity.getWorld().getTime() % 24000 + tickDelta;
			double radiant = Math.toRadians(360.0F / inventoryStacks.size());
			
			for (int i = 0; i < inventoryStacks.size(); i++) {
				matrixStack.push();
				
				double currentRadiant = radiant * i + (radiant * (time / 16.0) / (8.0F / inventoryStacks.size()));
				matrixStack.translate(distance * Math.sin(currentRadiant) + 0.5, 0.6, distance * Math.cos(currentRadiant) + 0.5); // position offset
				matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float) (i * 360 / inventoryStacks.size()) + (time / 16 / 8 * 360))); // item stack rotation; takes 0..360

				MinecraftClient.getInstance().getItemRenderer().renderItem(inventoryStacks.get(i), ModelTransformation.Mode.GROUND, light, overlay, matrixStack, vertexConsumerProvider, 0);
				matrixStack.pop();
			}
		}
	}
	
}