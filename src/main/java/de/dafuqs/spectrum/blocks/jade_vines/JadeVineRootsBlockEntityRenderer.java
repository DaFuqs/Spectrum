package de.dafuqs.spectrum.blocks.jade_vines;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Random;

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
						new Random(),
						fenceBlockState.getRenderingSeed(entity.getPos()),
						OverlayTexture.DEFAULT_UV
				);
				
				matrixStack.pop();
			}
		}
	}
	
}
