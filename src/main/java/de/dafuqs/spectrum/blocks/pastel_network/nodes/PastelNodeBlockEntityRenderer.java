package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import net.fabricmc.api.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.math.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;

@Environment(EnvType.CLIENT)
public class PastelNodeBlockEntityRenderer<T extends PastelNodeBlockEntity> implements BlockEntityRenderer<T> {

    public PastelNodeBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super();
    }

    @Override
    public void render(PastelNodeBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
        PastelNetwork network = entity.getNetwork();
        if (network != null) {
            Graph<PastelNodeBlockEntity, DefaultEdge> graph = network.getGraph();
            if (!graph.containsVertex(entity)) {
                return;
            }
            for (DefaultEdge edge : graph.edgesOf(entity)) {
                PastelNodeBlockEntity target = graph.getEdgeTarget(edge);
                if (target == entity) {
                    continue;
                }

                Vec3d offset = Vec3d.ofCenter(target.getPos()).subtract(Vec3d.of(entity.getPos()));
                Vec3d normalized = offset.normalize();

                int color = network.getColor();

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
