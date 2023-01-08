package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.blocks.pastel_network.*;
import net.fabricmc.api.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.math.*;

@Environment(EnvType.CLIENT)
public class PastelNodeBlockEntityRenderer<T extends PastelNodeBlockEntity> implements BlockEntityRenderer<T> {

    public PastelNodeBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super();
    }

    @Override
    public void render(PastelNodeBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
        int startColor = 0xff00ffff;
        int endColor = 0xffff00ff;

        PastelNetwork network = entity.getNetwork();
        if (network != null) {

            for (PastelNodeBlockEntity node : network.getAllNodes()) {
                boolean shouldRenderLine = entity.getPos().compareTo(node.getPos()) > 0;
                if (shouldRenderLine) {
                    var offset = Vec3d.ofCenter(entity.getPos()).subtract(Vec3d.of(entity.getPos()));

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

    }

}
