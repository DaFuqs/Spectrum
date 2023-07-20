package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

@Environment(EnvType.CLIENT)
public class SpectrumChestBlockEntityRenderer<T extends BlockEntity & LidOpenable> implements BlockEntityRenderer<T> {
	
	protected final ModelPart singleChestLid;
	protected final ModelPart singleChestBase;
	protected final ModelPart singleChestLatch;
	
	public SpectrumChestBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		ModelPart modelPart = getModel(ctx);
		this.singleChestBase = modelPart.getChild("bottom");
		this.singleChestLid = modelPart.getChild("lid");
		this.singleChestLatch = modelPart.getChild("lock");
	}
	
	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = entity.getWorld();
		boolean bl = world != null;
		BlockState blockState = bl ? entity.getCachedState() : SpectrumBlocks.PRIVATE_CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
		
		Block block = blockState.getBlock();
		if (block instanceof SpectrumChestBlock spectrumChestBlock) {
			matrices.push();
			float f = (blockState.get(ChestBlock.FACING)).asRotation();
			matrices.translate(0.5D, 0.5D, 0.5D);
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f));
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
		return ctx.getLayerModelPart(EntityModelLayers.CHEST);
	}
	
}