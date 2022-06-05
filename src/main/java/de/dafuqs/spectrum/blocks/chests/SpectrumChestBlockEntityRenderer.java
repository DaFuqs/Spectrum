package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.block.ChestAnimationProgress;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class SpectrumChestBlockEntityRenderer<T extends BlockEntity & ChestAnimationProgress> implements BlockEntityRenderer<T> {
	
	protected final ModelPart singleChestLid;
	protected final ModelPart singleChestBase;
	protected final ModelPart singleChestLatch;
	
	public SpectrumChestBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		ModelPart modelPart = getModel(ctx);
		this.singleChestBase = modelPart.getChild("bottom");
		this.singleChestLid = modelPart.getChild("lid");
		this.singleChestLatch = modelPart.getChild("lock");
	}
	
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = entity.getWorld();
		boolean bl = world != null;
		BlockState blockState = bl ? entity.getCachedState() : SpectrumBlocks.PRIVATE_CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
		
		Block block = blockState.getBlock();
		if (block instanceof SpectrumChestBlock spectrumChestBlock) {
			matrices.push();
			float f = (blockState.get(ChestBlock.FACING)).asRotation();
			matrices.translate(0.5D, 0.5D, 0.5D);
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-f));
			matrices.translate(-0.5D, -0.5D, -0.5D);
			
			float openFactor = entity.getAnimationProgress(tickDelta);
			openFactor = 1.0F - openFactor;
			openFactor = 1.0F - openFactor * openFactor * openFactor;
			
			VertexConsumer vertexConsumer = spectrumChestBlock.getTexture().getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);
			
			this.render(matrices, vertexConsumer, this.singleChestLid, this.singleChestLatch, this.singleChestBase, openFactor, light, overlay);
			
			matrices.pop();
		}
	}
	
	private void render(MatrixStack matrices, VertexConsumer vertices, ModelPart lid, ModelPart latch, ModelPart base, float openFactor, int light, int overlay) {
		lid.pitch = -(openFactor * 1.5707964F);
		latch.pitch = lid.pitch;
		lid.render(matrices, vertices, light, overlay);
		latch.render(matrices, vertices, light, overlay);
		base.render(matrices, vertices, light, overlay);
	}
	
	protected ModelPart getModel(BlockEntityRendererFactory.Context ctx) {
		ModelPart modelPart = ctx.getLayerModelPart(EntityModelLayers.CHEST);
		return modelPart;
	}
	
}