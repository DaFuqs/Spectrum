package de.dafuqs.spectrum.blocks.fusion_shrine;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.render.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.world.item.*;
import org.joml.*;
import org.joml.Math;

import java.util.*;

@Environment(EnvType.CLIENT)
public class FusionShrineBlockEntityRenderer<T extends FusionShrineBlockEntity> implements BlockEntityRenderer<T> {
	
	@SuppressWarnings("unused")
    public FusionShrineBlockEntityRenderer(Context ctx) {
	}
	
	@Override
	public void render(FusionShrineBlockEntity fusionShrineBlockEntity, float tickDelta, PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int light, int overlay) {
		// the fluid in the shrine
		FluidVariant fluidVariant = fusionShrineBlockEntity.getFluidVariant();
		if (!fluidVariant.isBlank()) {
			poseStack.pushPose();
			TextureAtlasSprite sprite = FluidVariantRendering.getSprite(fluidVariant);
			int[] colors = FluidRendering.unpackColorOf(fluidVariant, fusionShrineBlockEntity);
			FluidRendering.renderFluid(vertexConsumerProvider.getBuffer(RenderType.translucent()), poseStack.last().pose(), sprite, light, overlay, 2, 14, 0.9F, 2, 14, colors);
			poseStack.popPose();
		}
		
		if (!fusionShrineBlockEntity.isEmpty()) {
			// the floating item stacks
			List<ItemStack> inventoryStacks = new ArrayList<>();
			
			for (int i = 0; i < fusionShrineBlockEntity.getContainerSize(); i++) {
				ItemStack stack = fusionShrineBlockEntity.getItem(i);
				if (!stack.isEmpty()) {
					inventoryStacks.add(stack);
				}
			}
			
			float time = fusionShrineBlockEntity.getLevel().getGameTime() % 500000 + tickDelta;
			double radiant = Math.toRadians(360.0F / inventoryStacks.size());
			float distance = 1.2F;
			
			for (int i = 0; i < inventoryStacks.size(); i++) {
				poseStack.pushPose();
				double currentRadiant = radiant * i + (radiant * (time / 16.0) / (8.0F / inventoryStacks.size()));
				double height = Math.sin((time + currentRadiant) / 8.0) / 3.0; // item height
				poseStack.translate(distance * Math.sin(currentRadiant) + 0.5, 1.5 + height, distance * Math.cos(currentRadiant) + 0.5); // position offset
				poseStack.mulPose(Axis.YP.rotationDegrees((time) * 2)); // item stack rotation
				
				Minecraft.getInstance().getItemRenderer().renderStatic(inventoryStacks.get(i), ItemDisplayContext.GROUND, light, overlay, poseStack, vertexConsumerProvider, fusionShrineBlockEntity.getLevel(), 0);
				poseStack.popPose();
			}
		}
	}
	
}
