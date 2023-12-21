package de.dafuqs.spectrum.blocks.starfield;

import de.dafuqs.spectrum.render.SpectrumRenderPhases;
import de.dafuqs.spectrum.render.SpectrumShaderPrograms;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class StarfieldBlockEntityRenderer implements BlockEntityRenderer<StarfieldBlockEntity> {

    public StarfieldBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    public void render(StarfieldBlockEntity endPortalBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        var pos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
        this.renderSides(matrix4f, vertexConsumerProvider.getBuffer(this.getLayer()));
    }

    private void renderSides(Matrix4f matrix, VertexConsumer vertexConsumer) {
        this.renderSide(matrix, vertexConsumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
        this.renderSide(matrix, vertexConsumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.renderSide(matrix, vertexConsumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.renderSide(matrix, vertexConsumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F);
        this.renderSide(matrix, vertexConsumer, 0.0F, 1.0F, 0, 0, 0.0F, 0.0F, 1.0F, 1.0F);
        this.renderSide(matrix, vertexConsumer, 0.0F, 1.0F, 1, 1, 1.0F, 1.0F, 0.0F, 0.0F);
    }

    private void renderSide(Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4) {
        vertex(vertices, model, x1, y1, z1);
        vertex(vertices, model, x2, y1, z2);
        vertex(vertices, model, x2, y2, z3);
        vertex(vertices, model, x1, y2, z4);
    }

    private void vertex(VertexConsumer vertices, Matrix4f model, float x, float y, float z) {
        //vertices.vertex(model, x, y, z).color(1F, 1F, 1F, 1F).next();
    }

    protected RenderLayer getLayer() {
        return SpectrumRenderPhases.STARFIELD;
    }
}
