package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public abstract class PastelNetworkNodeBlockEntityRenderer<T extends PastelNetworkNodeBlockEntity> implements BlockEntityRenderer<T> {
	
	public PastelNetworkNodeBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		super();
	}
	
	@Override
	public void render(T entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
	
	}
	
}
