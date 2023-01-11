package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.texture.*;
import net.minecraft.client.util.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.math.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;

@Environment(EnvType.CLIENT)
public class PastelNodeBlockEntityRenderer<T extends PastelNodeBlockEntity> implements BlockEntityRenderer<T> {

    public static final SpriteIdentifier TEXTURE = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, SpectrumCommon.locate("block/pastel_line"));

    protected static EntityRenderDispatcher dispatcher;

    public PastelNodeBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super();
        dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
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

                int color = network.getColor();

                Vec3d offset = Vec3d.ofCenter(target.getPos()).subtract(Vec3d.of(entity.getPos()));
                Vec3d normalized = offset.normalize();

                VertexConsumer vertexConsumer = TEXTURE.getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntityTranslucent);

                Matrix4f positionMatrix = matrixStack.peek().getPositionMatrix();
                renderTranslucentLine(vertexConsumer, positionMatrix, color, offset, normalized); // TODO: this only renders black and not facing the player
                renderDebugLine(vertexConsumerProvider, color, offset, normalized, positionMatrix);

            }
        }
    }

    private static void renderDebugLine(VertexConsumerProvider vertexConsumerProvider, int color, Vec3d offset, Vec3d normalized, Matrix4f positionMatrix) {
        vertexConsumerProvider.getBuffer(RenderLayer.getLines())
                .vertex(positionMatrix, 0.5F, 0.5F, 0.5F)
                .color(color)
                .normal((float) normalized.x, (float) normalized.y, (float) normalized.z)
                .next();
        vertexConsumerProvider.getBuffer(RenderLayer.getLines())
                .vertex(positionMatrix, (float) offset.x, (float) offset.y, (float) offset.z)
                .color(color)
                .normal((float) normalized.x, (float) normalized.y, (float) normalized.z)
                .next();
    }

    private static void renderTranslucentLine(VertexConsumer vertexConsumer, Matrix4f positionMatrix, int color, Vec3d offset, Vec3d normalized) {
        float u1 = TEXTURE.getSprite().getFrameU(0);
        float u2 = TEXTURE.getSprite().getFrameU(1);
        float v1 = TEXTURE.getSprite().getFrameV(0);
        float v2 = TEXTURE.getSprite().getFrameV(1);

        vertexConsumer
                .vertex(positionMatrix, 0.25F, 0.25F, 0.25F)
                .color(color)
                .texture(u1, v2)
                .overlay(655360)
                .light(240)
                .normal((float) normalized.x, (float) normalized.y, (float) normalized.z)
                .next();
        vertexConsumer
                .vertex(positionMatrix, 0.75F, 0.75F, 0.75F)
                .color(color)
                .texture(u2, v2)
                .overlay(655360)
                .light(240)
                .normal((float) normalized.x, (float) normalized.y, (float) normalized.z)
                .next();
        vertexConsumer
                .vertex(positionMatrix, (float) offset.x - 0.25F, (float) offset.y - 0.25F, (float) offset.z - 0.25F)
                .color(color)
                .texture(u2, v1)
                .overlay(655360)
                .light(240)
                .normal((float) normalized.x, (float) normalized.y, (float) normalized.z)
                .next();
        vertexConsumer
                .vertex(positionMatrix, (float) offset.x + 0.25F, (float) offset.y + 0.25F, (float) offset.z + 0.25F)
                .color(color)
                .texture(u1, v1)
                .overlay(655360)
                .light(240)
                .normal((float) normalized.x, (float) normalized.y, (float) normalized.z)
                .next();
    }

}
