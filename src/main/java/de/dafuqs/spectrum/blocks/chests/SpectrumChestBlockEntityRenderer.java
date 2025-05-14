package de.dafuqs.spectrum.blocks.chests;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

@Environment(EnvType.CLIENT)
public class SpectrumChestBlockEntityRenderer<T extends BlockEntity & LidBlockEntity> implements BlockEntityRenderer<T> {
	
	protected final ModelPart singleChestLid;
	protected final ModelPart singleChestBase;
	protected final ModelPart singleChestLatch;
	
	public SpectrumChestBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		ModelPart modelPart = getModel(ctx);
		this.singleChestBase = modelPart.getChild("bottom");
		this.singleChestLid = modelPart.getChild("lid");
		this.singleChestLatch = modelPart.getChild("lock");
	}
	
	@Override
	public void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		Level world = entity.getLevel();
		boolean bl = world != null;
		BlockState blockState = bl ? entity.getBlockState() : SpectrumBlocks.HEARTBOUND_CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
		
		Block block = blockState.getBlock();
		if (block instanceof SpectrumChestBlock spectrumChestBlock) {
			matrices.pushPose();
			float f = (blockState.getValue(ChestBlock.FACING)).toYRot();
			matrices.translate(0.5D, 0.5D, 0.5D);
			matrices.mulPose(Axis.YP.rotationDegrees(-f));
			matrices.translate(-0.5D, -0.5D, -0.5D);
			
			float openFactor = entity.getOpenNess(tickDelta);
			openFactor = 1.0F - openFactor;
			openFactor = 1.0F - openFactor * openFactor * openFactor;
			
			VertexConsumer vertexConsumer = spectrumChestBlock.getTextureLocation().buffer(vertexConsumers, RenderType::entityCutout);
			
			this.render(matrices, vertexConsumer, this.singleChestLid, this.singleChestLatch, this.singleChestBase, openFactor, light, overlay);
			
			matrices.popPose();
		}
	}
	
	private void render(PoseStack matrices, VertexConsumer vertices, ModelPart lid, ModelPart latch, ModelPart base, float openFactor, int light, int overlay) {
		lid.xRot = -(openFactor * 1.5707964F);
		latch.xRot = lid.xRot;
		lid.render(matrices, vertices, light, overlay);
		latch.render(matrices, vertices, light, overlay);
		base.render(matrices, vertices, light, overlay);
	}
	
	protected ModelPart getModel(BlockEntityRendererProvider.Context ctx) {
		return ctx.bakeLayer(ModelLayers.CHEST);
	}
	
}