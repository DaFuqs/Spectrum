package de.dafuqs.spectrum.blocks.item_roundel;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.world.item.*;
import net.neoforged.api.distmarker.*;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public class ItemRoundelBlockEntityRenderer<T extends ItemRoundelBlockEntity> implements BlockEntityRenderer<T> {
	
	private static final float distance = 0.29F;
	
	@SuppressWarnings("unused")
	public ItemRoundelBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
	}
	
	@Override
	public void render(ItemRoundelBlockEntity blockEntity, float tickDelta, PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int light, int overlay) {
		if (!blockEntity.isEmpty()) {
			// the floating item stacks
			List<ItemStack> inventoryStacks = new ArrayList<>();
			for (int i = 0; i < blockEntity.getContainerSize(); i++) {
				ItemStack stack = blockEntity.getItem(i);
				if (!stack.isEmpty()) {
					if (blockEntity.renderStacksAsIndividualItems()) {
						for (int j = 0; j < stack.getCount(); j++) {
							inventoryStacks.add(stack);
						}
					} else {
						inventoryStacks.add(stack);
					}
				}
			}
			
			float time = blockEntity.getLevel().getGameTime() % 24000 + tickDelta;
			double radiant = Math.toRadians(360.0F / inventoryStacks.size());
			if (blockEntity.reversed) {
				radiant = -radiant;
			}

			for (int i = 0; i < inventoryStacks.size(); i++) {
				poseStack.pushPose();
				
				double bob = Math.sin((time / 19) + i) * 0.075;
				
				double currentRadiant = radiant * i + (radiant * (time / 16.0) / (8.0F / inventoryStacks.size()));
				poseStack.translate(distance * Math.sin(currentRadiant) + 0.5, 0.3 + bob, distance * Math.cos(currentRadiant) + 0.5); // position offset
				poseStack.mulPose(Axis.YP.rotationDegrees((float) (i * 360 / inventoryStacks.size()) + (time / 16 / 8 * 360))); // item stack rotation; takes 0..360
				
				Minecraft.getInstance().getItemRenderer().renderStatic(inventoryStacks.get(i), ItemDisplayContext.GROUND, light, overlay, poseStack, vertexConsumerProvider, blockEntity.getLevel(), 0);
				poseStack.popPose();
			}
		}
	}
	
}