package de.dafuqs.spectrum.blocks.starfield;

import de.dafuqs.spectrum.registries.client.SpectrumRenderLayers;
import de.dafuqs.spectrum.render.SpectrumRenderPhases;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class StarfieldBlockEntityRenderer implements BlockEntityRenderer<StarfieldBlockEntity> {

    public StarfieldBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    public void render(StarfieldBlockEntity endPortalBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        this.renderSides(endPortalBlockEntity, matrix4f, vertexConsumerProvider.getBuffer(this.getLayer()));
    }

    private void renderSides(StarfieldBlockEntity entity, Matrix4f matrix, VertexConsumer vertexConsumer) {
        float f = this.getBottomYOffset();
        float g = this.getTopYOffset();
        this.renderSide(entity, matrix, vertexConsumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, Direction.SOUTH);
        this.renderSide(entity, matrix, vertexConsumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Direction.NORTH);
        this.renderSide(entity, matrix, vertexConsumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST);
        this.renderSide(entity, matrix, vertexConsumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST);
        this.renderSide(entity, matrix, vertexConsumer, 0.0F, 1.0F, f, f, 0.0F, 0.0F, 1.0F, 1.0F, Direction.DOWN);
        this.renderSide(entity, matrix, vertexConsumer, 0.0F, 1.0F, g, g, 1.0F, 1.0F, 0.0F, 0.0F, Direction.UP);
    }

    private void renderSide(StarfieldBlockEntity entity, Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, Direction side) {
        var pos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
        vertex(pos, vertices, model, x1, y1, z1, 1, 1);
        vertex(pos, vertices, model, x2, y1, z2, 0, 1);
        vertex(pos, vertices, model, x2, y2, z3, 0, 0);
        vertex(pos, vertices, model, x1, y2, z4, 1, 0);
    }

    private void vertex(Vec3d pos, VertexConsumer vertices, Matrix4f model, float x, float y, float z, float u, float v) {
        vertices.vertex(model, x, y, z).texture(u, v).color(1F, 1F, 1F, 1F).normal((float) pos.getX(), (float) pos.getY(), (float) pos.getZ()).next();
    }

    protected float getTopYOffset() {
        return 0.75F;
    }

    protected float getBottomYOffset() {
        return 0.375F;
    }

    protected RenderLayer getLayer() {
        return SpectrumRenderPhases.STARFIELD;
    }
}
