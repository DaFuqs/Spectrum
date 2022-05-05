package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class PastelNetworkNodeBlockEntityRenderer<T extends PastelNetworkNodeBlockEntity> implements BlockEntityRenderer<T> {
	
	public PastelNetworkNodeBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		super();
	}
	
	@Override
	public void render(PastelNetworkNodeBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
		int startColor = 0xff00ffff;
		int endColor = 0xffff00ff;
		
		for (BlockPos pos : entity.receivers) {
			var offset = Vec3d.ofCenter(pos).subtract(Vec3d.of(entity.getPos()));
			
			vertexConsumerProvider.getBuffer(RenderLayer.LINES)
					.vertex(matrixStack.peek().getPositionMatrix(), .5f, .5f, .5f)
					.color(startColor)
					.normal(1, 0, 1)
					.next();
			
			vertexConsumerProvider.getBuffer(RenderLayer.LINES)
					.vertex(matrixStack.peek().getPositionMatrix(), (float) offset.x, (float) offset.y, (float) offset.z)
					.color(endColor)
					.normal(1, 0, 1)
					.next();
		}
		
	}
	
}
