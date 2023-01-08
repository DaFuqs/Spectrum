package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.blocks.pastel_network.network.*;
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
        PastelNetwork network = entity.getNetwork();
        if (network != null) {

            for (PastelNodeBlockEntity node : network.getAllNodes()) {

                boolean shouldRenderLine = entity.getPos().compareTo(node.getPos()) < 0;
                if (shouldRenderLine) {
                    boolean canSee = entity.canSee(node);
                    int color = canSee ? 0xFF00FF00 : 0xFFFF0000;
                    Vec3d offset = Vec3d.ofCenter(node.getPos()).subtract(Vec3d.of(entity.getPos()));
                    Vec3d normalized = offset.normalize();

                    vertexConsumerProvider.getBuffer(RenderLayer.getLines())
                            .vertex(matrixStack.peek().getPositionMatrix(), 0.5F, 0.5F, 0.5F)
                            .color(color)
                            .normal((float) normalized.x, (float) normalized.y, (float) normalized.z)
                            .next();
                    vertexConsumerProvider.getBuffer(RenderLayer.getLines())
                            .vertex(matrixStack.peek().getPositionMatrix(), (float) offset.x, (float) offset.y, (float) offset.z)
                            .color(color)
                            .normal((float) normalized.x, (float) normalized.y, (float) normalized.z)
                            .next();
                }

            }

        }

    }

}
