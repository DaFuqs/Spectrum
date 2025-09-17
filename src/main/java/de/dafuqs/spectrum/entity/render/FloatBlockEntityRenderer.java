package de.dafuqs.spectrum.entity.render;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.neoforged.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class FloatBlockEntityRenderer extends EntityRenderer<FloatBlockEntity> {
	private final RandomSource random = RandomSource.create();
	
	public FloatBlockEntityRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager);
		this.shadowRadius = 0.5F;
	}
	
	@Override
	public void render(FloatBlockEntity entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
		BlockState blockState = entity.getBlockState();
		
		if (blockState.getRenderShape() == RenderShape.MODEL) {
			Level world = entity.level();
			
			if (blockState != world.getBlockState(BlockPos.containing(entity.position())) && blockState.getRenderShape() != RenderShape.INVISIBLE) {
				matrices.pushPose();
				BlockPos blockpos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
				matrices.translate(-0.5, 0.0, -0.5);
				BlockRenderDispatcher blockRenderManager = Minecraft.getInstance().getBlockRenderer();
				blockRenderManager.getModelRenderer().tesselateBlock(world, blockRenderManager.getBlockModel(blockState), blockState, blockpos, matrices, vertexConsumers.getBuffer(ItemBlockRenderTypes.getMovingBlockRenderType(blockState)), false, random, blockState.getSeed(entity.getOrigin()), OverlayTexture.NO_OVERLAY);
				matrices.popPose();
				
				super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public ResourceLocation getTextureLocation(FloatBlockEntity entityIn) {
		return TextureAtlas.LOCATION_BLOCKS;
	}
}
