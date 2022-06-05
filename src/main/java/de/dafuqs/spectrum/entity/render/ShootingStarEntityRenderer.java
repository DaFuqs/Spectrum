package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.entity.entity.ShootingStarEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class ShootingStarEntityRenderer extends EntityRenderer<ShootingStarEntity> {
	
	public ShootingStarEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
		this.shadowRadius = 0.15F;
		this.shadowOpacity = 0.75F;
	}
	
	public void render(ShootingStarEntity shootingStarEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
		BlockState blockState = shootingStarEntity.getShootingStarType().getBlock().getDefaultState();
		
		if (blockState.getRenderType() == BlockRenderType.MODEL) {
			World world = shootingStarEntity.getWorld();
			
			if (blockState != world.getBlockState(new BlockPos(shootingStarEntity.getPos())) && blockState.getRenderType() != BlockRenderType.INVISIBLE) {
				matrixStack.push();
				
				BlockPos blockpos = new BlockPos(shootingStarEntity.getX(), shootingStarEntity.getBoundingBox().maxY, shootingStarEntity.getZ());
				matrixStack.translate(-0.5, 0.0, -0.5);
				BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
				blockRenderManager.getModelRenderer().render(world, blockRenderManager.getModel(blockState), blockState, blockpos, matrixStack, vertexConsumerProvider.getBuffer(RenderLayers.getMovingBlockLayer(blockState)), false, new Random(), blockState.getRenderingSeed(shootingStarEntity.getBlockPos()), OverlayTexture.DEFAULT_UV);
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