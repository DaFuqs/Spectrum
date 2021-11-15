package de.dafuqs.spectrum.blocks.enchanter;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

public class EnchanterBlockEntityRenderer<T extends EnchanterBlockEntity> implements BlockEntityRenderer<T> {
	
	public EnchanterBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
	
	}
	
	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
	
	}
	
}
