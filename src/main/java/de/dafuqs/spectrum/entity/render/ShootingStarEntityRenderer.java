package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.entity.entity.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.texture.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

@Environment(EnvType.CLIENT)
public class ShootingStarEntityRenderer extends EntityRenderer<ShootingStarEntity> {
	
	public ShootingStarEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
		this.shadowRadius = 0.15F;
		this.shadowOpacity = 0.75F;
	}
	
	@Override
	public void render(ShootingStarEntity shootingStarEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
		BlockState blockState = shootingStarEntity.getShootingStarType().getBlock().getDefaultState();
		
		if (blockState.getRenderType() == BlockRenderType.MODEL) {
			World world = shootingStarEntity.getWorld();
			
			if (blockState != world.getBlockState(new BlockPos(shootingStarEntity.getPos())) && blockState.getRenderType() != BlockRenderType.INVISIBLE) {
				matrixStack.push();
				
				BlockPos blockpos = new BlockPos(shootingStarEntity.getX(), shootingStarEntity.getBoundingBox().maxY, shootingStarEntity.getZ());
				matrixStack.translate(-0.5, 0.0, -0.5);
				BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
				blockRenderManager.getModelRenderer().render(world, blockRenderManager.getModel(blockState), blockState, blockpos, matrixStack, vertexConsumerProvider.getBuffer(RenderLayers.getMovingBlockLayer(blockState)), false, shootingStarEntity.getWorld().random, blockState.getRenderingSeed(shootingStarEntity.getBlockPos()), OverlayTexture.DEFAULT_UV);
				matrixStack.pop();
				super.render(shootingStarEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
			}
		}
		
		super.render(shootingStarEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
	}
	
	@Override
	public Identifier getTexture(ShootingStarEntity entityIn) {
		return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
	}
}