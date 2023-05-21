package de.dafuqs.spectrum.blocks.jade_vines;

import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.util.math.*;

@Environment(EnvType.CLIENT)
public class JadeVineRootsBlockEntityRenderer implements BlockEntityRenderer<JadeVineRootsBlockEntity> {
	
	public JadeVineRootsBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
	
	}
	
	@Override
	public void render(JadeVineRootsBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
		if (entity.getWorld() != null) {
			BlockState fenceBlockState = entity.getFenceBlockState();
			if (fenceBlockState.getRenderType() == BlockRenderType.MODEL && fenceBlockState.getRenderType() != BlockRenderType.INVISIBLE) {
				matrixStack.push();
				
				BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
				blockRenderManager.getModelRenderer().render(entity.getWorld(),
						blockRenderManager.getModel(fenceBlockState),
						fenceBlockState,
						entity.getPos(),
						matrixStack,
						vertexConsumerProvider.getBuffer(RenderLayers.getMovingBlockLayer(fenceBlockState)),
						true,
						entity.getWorld().random,
						fenceBlockState.getRenderingSeed(entity.getPos()),
						OverlayTexture.DEFAULT_UV
				);
				
				matrixStack.pop();
			}
		}
	}
	
}
