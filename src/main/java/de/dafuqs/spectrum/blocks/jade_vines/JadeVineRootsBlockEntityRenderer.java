package de.dafuqs.spectrum.blocks.jade_vines;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;


public class JadeVineRootsBlockEntityRenderer implements BlockEntityRenderer<JadeVineRootsBlockEntity> {
	private final RandomSource random = RandomSource.create();
	
	@SuppressWarnings("unused")
	public JadeVineRootsBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
	}
	
	@Override
	public void render(JadeVineRootsBlockEntity entity, float tickDelta, PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int light, int overlay) {
		Level world = entity.getLevel();
		if (entity.getLevel() != null) {
			BlockState fenceBlockState = entity.getFenceBlockState();
			if (fenceBlockState.getRenderShape() == RenderShape.MODEL && fenceBlockState.getRenderShape() != RenderShape.INVISIBLE) {
				poseStack.pushPose();
				
				BlockRenderDispatcher blockRenderManager = Minecraft.getInstance().getBlockRenderer();
				blockRenderManager.getModelRenderer().tesselateBlock(entity.getLevel(),
						blockRenderManager.getBlockModel(fenceBlockState),
						fenceBlockState,
						entity.getBlockPos(),
						poseStack,
						vertexConsumerProvider.getBuffer(ItemBlockRenderTypes.getMovingBlockRenderType(fenceBlockState)),
						true,
						random,
						fenceBlockState.getSeed(entity.getBlockPos()),
						OverlayTexture.NO_OVERLAY
				);
				
				poseStack.popPose();
			}
		}
	}
	
}
