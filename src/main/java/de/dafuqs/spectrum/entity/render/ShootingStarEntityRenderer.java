package de.dafuqs.spectrum.entity.render;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

@Environment(EnvType.CLIENT)
public class ShootingStarEntityRenderer extends EntityRenderer<ShootingStarEntity> {
	
	public ShootingStarEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.15F;
		this.shadowStrength = 0.75F;
	}
	
	@Override
	public void render(ShootingStarEntity shootingStarEntity, float yaw, float tickDelta, PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int light) {
		BlockState blockState = shootingStarEntity.getShootingStarType().getBlock().defaultBlockState();
		
		if (blockState.getRenderShape() == RenderShape.MODEL) {
			Level world = shootingStarEntity.level();
			
			if (blockState != world.getBlockState(BlockPos.containing(shootingStarEntity.position())) && blockState.getRenderShape() != RenderShape.INVISIBLE) {
				poseStack.pushPose();
				
				BlockPos blockpos = BlockPos.containing(shootingStarEntity.getX(), shootingStarEntity.getBoundingBox().maxY, shootingStarEntity.getZ());
				poseStack.translate(-0.5, 0.0, -0.5);
				BlockRenderDispatcher blockRenderManager = Minecraft.getInstance().getBlockRenderer();
				blockRenderManager.getModelRenderer().tesselateBlock(world, blockRenderManager.getBlockModel(blockState), blockState, blockpos, poseStack, vertexConsumerProvider.getBuffer(ItemBlockRenderTypes.getMovingBlockRenderType(blockState)), false, world.random, blockState.getSeed(shootingStarEntity.blockPosition()), OverlayTexture.NO_OVERLAY);
				poseStack.popPose();
				super.render(shootingStarEntity, yaw, tickDelta, poseStack, vertexConsumerProvider, light);
			}
		}
		
		super.render(shootingStarEntity, yaw, tickDelta, poseStack, vertexConsumerProvider, light);
	}
	
	@Override
	public ResourceLocation getTextureLocation(ShootingStarEntity entityIn) {
		return InventoryMenu.BLOCK_ATLAS;
	}
}
