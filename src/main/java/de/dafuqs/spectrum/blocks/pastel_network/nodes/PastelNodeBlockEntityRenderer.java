package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.cca.DDWorldEffectsComponent;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class PastelNodeBlockEntityRenderer implements BlockEntityRenderer<PastelNodeBlockEntity> {

    private static final Crystal CONNECTION = new Crystal(SpectrumItems.CONNECTION_NODE_CRYSTAL.getDefaultStack(), 0.25, false);
    private static final Crystal PROVIDER = new Crystal(SpectrumItems.PROVIDER_NODE_CRYSTAL.getDefaultStack(), 0.1, true);
    private static final Crystal SENDER = new Crystal(SpectrumItems.SENDER_NODE_CRYSTAL.getDefaultStack(), 0.1, true);
    private static final Crystal STORAGE = new Crystal(SpectrumItems.STORAGE_NODE_CRYSTAL.getDefaultStack(), 0.15, true);
    private static final Crystal BUFFER = new Crystal(SpectrumItems.BUFFER_NODE_CRYSTAL.getDefaultStack(), 0.1, true);
    private static final Crystal GATHER = new Crystal(SpectrumItems.GATHER_NODE_CRYSTAL.getDefaultStack(), 0.1, false);

    private static final Identifier INNER_RING = SpectrumCommon.locate("textures/block/pastel_inner_ring_blank.png");
    private static final Identifier OUTER_RING = SpectrumCommon.locate("textures/block/pastel_outer_ring_blank.png");
    private static final Identifier REDSTONE_RING = SpectrumCommon.locate("textures/block/pastel_redstone_ring_blank.png");

    public PastelNodeBlockEntityRenderer(BlockEntityRendererFactory.Context renderContext) {}

    @Override
    public void render(PastelNodeBlockEntity node, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (node.getState() == null)
            return;

        var world = node.getWorld();
        if (world == null)
            return;

        var time = (world.getTime() + node.getCreationStamp()) % DDWorldEffectsComponent.REAL_DAY_LENGTH + tickDelta;

        var crystal = switch (node.getNodeType()) {
            case CONNECTION -> CONNECTION;
            case STORAGE -> STORAGE;
            case BUFFER -> BUFFER;
            case PROVIDER -> PROVIDER;
            case SENDER -> SENDER;
            case GATHER -> GATHER;
        };

        switch (node.getState()) {
            case CONNECTED -> {
                node.rotationTarget = mod(time / (Math.PI * 3));
                node.heightTarget = (float) Math.sin(time / 19F) / 10F + 0.5F;
                node.alphaTarget = 1F;
            }
            case DISCONNECTED -> {
                node.heightTarget = 0;
                node.alphaTarget = 0;
            }
            case ACTIVE -> {
                node.rotationTarget = mod(time / (Math.PI * 1));
                node.heightTarget = (float) Math.sin(time / 19F) / 10F + 0.5F;
                node.alphaTarget = 1F;
            }
            case INACTIVE -> {
                node.rotationTarget = mod(time / (Math.PI * 7));
                node.heightTarget = (float) Math.sin(time / 19F) / 20F + 0.25F;
                node.alphaTarget = 0.275F;
            }
        };

        var interp = MathHelper.clamp((node.interpTicks + tickDelta) / node.interpLength, 0F, 1F);
        node.crystalRotation = MathHelper.lerp(interp, node.lastRotationTarget, node.rotationTarget);
        node.crystalHeight = MathHelper.lerp(interp, node.lastHeightTarget, node.heightTarget);
        node.ringAlpha = MathHelper.lerp(interp, node.lastAlphaTarget, node.alphaTarget);

        var facing = node.getCachedState().get(PastelNodeBlock.FACING);

        matrices.push();
        matrices.translate(0.5, 0.5, 0.5);

        switch (facing) {
            case DOWN -> matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
            case NORTH -> matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(270));
            case SOUTH -> matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
            case EAST -> {
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
            }
            case WEST -> {
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(270));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
            }
        }

        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(node.crystalRotation));
        matrices.translate(0, -0.5, 0);

        var innerRing = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(INNER_RING));

        var ringHeight = node.crystalHeight - 0.3F;
        renderRing(matrices, innerRing, 3.75F + ringHeight / 2F, 7F, node.ringAlpha, overlay, facing);

        var redstoneRing = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(REDSTONE_RING));
        renderRing(matrices, redstoneRing, 5F + ringHeight, 15F, node.ringAlpha * node.getRedstoneAlphaMult(), overlay, facing);

        if (crystal.hasOuterRing()) {
            var outerRing = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(OUTER_RING));
            renderRing(matrices, outerRing, 5.75F + ringHeight * 2, 11F, node.ringAlpha, overlay, facing);
        }


        matrices.translate(0.0, node.crystalHeight + crystal.yOffset, 0.0);
        MinecraftClient.getInstance().getItemRenderer().renderItem(crystal.crystal, ModelTransformationMode.NONE, LightmapTextureManager.MAX_LIGHT_COORDINATE, overlay, matrices, vertexConsumers, node.getWorld(), 0);
        matrices.pop();
    }

    private float mod(double in) {
        return (float) (in % (Math.PI * 2));
    }

    private record Crystal(ItemStack crystal, double yOffset, boolean hasOuterRing) {}

    private void renderRing(MatrixStack matrices, VertexConsumer vertices, float height, float scale, float alpha, int overlay, Direction facing) {
        height /= 16F;
        var size = scale / 16F;
        matrices.translate(-size / 2F, height, -size / 2F);

        var peek = matrices.peek();
        var model = peek.getPositionMatrix();
        var normals = peek.getNormalMatrix();
        var transform = normals.transform(new Vector3f(facing.getUnitVector()));

        renderSide(model, normals, vertices, alpha, scale, scale, 0, size, 0, size, transform.x, transform.y, transform.z, overlay);
        matrices.translate(size / 2F, -height, size / 2F);
    }

    private void renderSide(Matrix4f model, Matrix3f normals, VertexConsumer vertices, float alpha, float u, float v, float x1, float x2, float z1, float z2, float n1, float n2, float n3, int overlay) {
        float u1 = 1/16F, v1 = 1/16F;
        float u2 = u1 + u/16F, v2 = v1 + v/16F;

        vertices.vertex(model, x1, 0, z1).color(1F, 1F, 1F, alpha).texture(u1, v1).overlay(overlay).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(0, 1, 0).next();
        vertices.vertex(model, x2, 0, z1).color(1F, 1F, 1F, alpha).texture(u2, v1).overlay(overlay).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(0, 1, 0).next();
        vertices.vertex(model, x2, 0, z2).color(1F, 1F, 1F, alpha).texture(u2, v2).overlay(overlay).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(0, 1, 0).next();
        vertices.vertex(model, x1, 0, z2).color(1F, 1F, 1F, alpha).texture(u1, v2).overlay(overlay).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(0, 1, 0).next();
    }
}
