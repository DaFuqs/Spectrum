package de.dafuqs.spectrum.blocks.deeper_down_portal;

import de.dafuqs.spectrum.registries.SpectrumRenderLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;

@Environment(EnvType.CLIENT)
public class DeeperDownPortalBlockEntityRenderer<T extends DeeperDownPortalBlockEntity> implements BlockEntityRenderer<T> {
	
	public DeeperDownPortalBlockEntityRenderer(Context ctx) {
	
	}
	
	public void render(T blockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
		BlockState state = blockEntity.getWorld().getBlockState(blockEntity.getPos());
		if(state.getBlock() instanceof DeeperDownPortalBlock) {
			boolean facingUp = state.get(DeeperDownPortalBlock.FACING_UP);
			float bottomYOffset = facingUp ? 16.0F - this.getYOffset() : this.getYOffset();
			this.renderSides(facingUp, bottomYOffset, matrixStack.peek().getPositionMatrix(), vertexConsumerProvider.getBuffer(getLayer()));
		}
	}
	
	private void renderSides(boolean facingUp, float yOffset, Matrix4f matrix4f, VertexConsumer vertexConsumer) {
		this.renderSide(facingUp, matrix4f, vertexConsumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, Direction.SOUTH);
		this.renderSide(facingUp, matrix4f, vertexConsumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Direction.NORTH);
		this.renderSide(facingUp, matrix4f, vertexConsumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST);
		this.renderSide(facingUp, matrix4f, vertexConsumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST);
		this.renderSide(facingUp, matrix4f, vertexConsumer, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, Direction.DOWN);
		this.renderSide(facingUp, matrix4f, vertexConsumer, 0.0F, 1.0F, yOffset, yOffset, 1.0F, 1.0F, 0.0F, 0.0F, Direction.UP);
	}
	
	private void renderSide(boolean facingUp, Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, Direction direction) {
		if (shouldDrawSide(facingUp, direction)) {
			vertices.vertex(model, x1, y1, z1).next();
			vertices.vertex(model, x2, y1, z2).next();
			vertices.vertex(model, x2, y2, z3).next();
			vertices.vertex(model, x1, y2, z4).next();
		}
	}
	
	@Environment(EnvType.CLIENT)
	public boolean shouldDrawSide(boolean facingUp, Direction direction) {
		if(facingUp) {
			return direction == Direction.DOWN;
		} else {
			return direction == Direction.UP;
		}
	}
	
	protected float getYOffset() {
		return 0.25F;
	}
	
	protected RenderLayer getLayer() {
		return SpectrumRenderLayers.DeeperDownPortalRenderLayer.get();
	}
	
}
