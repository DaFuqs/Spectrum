package de.dafuqs.pigment.blocks.deeper_down_portal;

import de.dafuqs.pigment.PigmentCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;

@Environment(EnvType.CLIENT)
public class DeeperDownPortalBlockEntityRenderer<T extends DeeperDownPortalBlockEntity> implements BlockEntityRenderer<T> {

    public static final Identifier OVERLAY_TEXTURE = new Identifier(PigmentCommon.MOD_ID, "textures/entity/portal/deeper_down_portal_overlay.png");
    public static final Identifier PORTAL_TEXTURE = new Identifier(PigmentCommon.MOD_ID, "textures/entity/portal/deeper_down_portal.png");

    private static final RenderLayer DEEPER_DOWN_PORTAL_RENDER_LAYER = RenderLayer.of("deeper_down_portal",
            VertexFormats.POSITION,
            VertexFormat.DrawMode.QUADS,
            256,
            false,
            false,
            RenderLayer.MultiPhaseParameters.builder().method_34578(new RenderPhase.class_5942(GameRenderer::method_34535)).method_34577(
                    RenderPhase.class_5940.method_34560().method_34563(DeeperDownPortalBlockEntityRenderer.OVERLAY_TEXTURE, false, false)
                            .method_34563(DeeperDownPortalBlockEntityRenderer.PORTAL_TEXTURE, false, false)
                            .method_34562()
            ).build(false));


    public DeeperDownPortalBlockEntityRenderer(Context ctx) {

    }

    public void render(T deeperDownPortalBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        float g = this.getTopYOffset();
        Matrix4f matrix4f = matrixStack.peek().getModel();
        this.renderSides(deeperDownPortalBlockEntity, g, matrix4f, vertexConsumerProvider.getBuffer(this.method_34589()));
    }

    private void renderSides(T entity, float topYOffset, Matrix4f matrix4f, VertexConsumer vertexConsumer) {
        this.renderSide(entity, matrix4f, vertexConsumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, Direction.SOUTH);
        this.renderSide(entity, matrix4f, vertexConsumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Direction.NORTH);
        this.renderSide(entity, matrix4f, vertexConsumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST);
        this.renderSide(entity, matrix4f, vertexConsumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST);
        this.renderSide(entity, matrix4f, vertexConsumer, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, Direction.DOWN);
        this.renderSide(entity, matrix4f, vertexConsumer, 0.0F, 1.0F, topYOffset, topYOffset, 1.0F, 1.0F, 0.0F, 0.0F, Direction.UP);
    }

    private void renderSide(T entity, Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, Direction direction) {
        if (entity.shouldDrawSide(direction)) {
            vertices.vertex(model, x1, y1, z1).next();
            vertices.vertex(model, x2, y1, z2).next();
            vertices.vertex(model, x2, y2, z3).next();
            vertices.vertex(model, x1, y2, z4).next();
        }
    }

    protected float getTopYOffset() {
        return 0.15F;
    }

    protected RenderLayer method_34589() {
        return DEEPER_DOWN_PORTAL_RENDER_LAYER;
    }
}
