package de.dafuqs.spectrum.blocks.fusion_shrine;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.render.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.client.extensions.common.*;
import net.neoforged.neoforge.client.textures.*;
import net.neoforged.neoforge.fluids.*;
import javax.annotation.*;
import org.joml.Math;

import java.util.*;


public class FusionShrineBlockEntityRenderer<T extends FusionShrineBlockEntity> implements BlockEntityRenderer<T> {
	
	@SuppressWarnings("unused")
	public FusionShrineBlockEntityRenderer(Context ctx) {
	}
	
	@Override
	public void render(FusionShrineBlockEntity fusionShrineBlockEntity, float tickDelta, PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int light, int overlay) {
		// the fluid in the shrine
		FluidStack fluidStack = fusionShrineBlockEntity.getTank().getFluid();
		if (!fluidStack.isEmpty()) {
			poseStack.pushPose();
			IClientFluidTypeExtensions renderData = IClientFluidTypeExtensions.of(fluidStack.getFluid());
			TextureAtlasSprite sprite = FluidSpriteCache.getSprite(renderData.getStillTexture(fluidStack));
			int[] colors = FluidRendering.unpackColor(renderData.getTintColor(fluidStack.getFluid().defaultFluidState(), fusionShrineBlockEntity.getLevel(), fusionShrineBlockEntity.getBlockPos()));
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
