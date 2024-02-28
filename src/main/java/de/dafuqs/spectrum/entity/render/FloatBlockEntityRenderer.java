package de.dafuqs.spectrum.entity.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import de.dafuqs.spectrum.entity.entity.FloatBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class FloatBlockEntityRenderer extends EntityRenderer<FloatBlockEntity> {
    private final Random random = Random.create();
    
    public FloatBlockEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager);
        this.shadowRadius = 0.5F;
    }

    @Override
    public void render(FloatBlockEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        BlockState blockState = entity.getBlockState();

        if (blockState.getRenderType() == BlockRenderType.MODEL) {
            World world = entity.getWorld();

            if (blockState != world.getBlockState(BlockPos.ofFloored(entity.getPos())) && blockState.getRenderType() != BlockRenderType.INVISIBLE) {
                matrices.push();
                BlockPos blockpos = BlockPos.ofFloored(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
                matrices.translate(-0.5, 0.0, -0.5);
                BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
                blockRenderManager.getModelRenderer().render(world, blockRenderManager.getModel(blockState), blockState, blockpos, matrices, vertexConsumers.getBuffer(RenderLayers.getMovingBlockLayer(blockState)), false, random, blockState.getRenderingSeed(entity.getOrigin()), OverlayTexture.DEFAULT_UV);
                matrices.pop();
                
                super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public Identifier getTexture(FloatBlockEntity entityIn) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}
