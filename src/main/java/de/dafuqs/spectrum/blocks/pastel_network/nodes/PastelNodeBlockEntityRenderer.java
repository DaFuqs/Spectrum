package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.pastel.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;
import org.joml.*;

import java.lang.Math;

public class PastelNodeBlockEntityRenderer implements BlockEntityRenderer<PastelNodeBlockEntity> {

	private static final long REAL_DAY_LENGTH = 86400 * 20;

	private static final Crystal CONNECTION = new Crystal(SpectrumItems.CONNECTION_NODE_CRYSTAL.getDefaultStack(), 0.25, false);
	private static final Crystal PROVIDER = new Crystal(SpectrumItems.PROVIDER_NODE_CRYSTAL.getDefaultStack(), 0.1, true);
	private static final Crystal SENDER = new Crystal(SpectrumItems.SENDER_NODE_CRYSTAL.getDefaultStack(), 0.1, true);
	private static final Crystal STORAGE = new Crystal(SpectrumItems.STORAGE_NODE_CRYSTAL.getDefaultStack(), 0.15, true);
	private static final Crystal BUFFER = new Crystal(SpectrumItems.BUFFER_NODE_CRYSTAL.getDefaultStack(), 0.1, true);
	private static final Crystal GATHER = new Crystal(SpectrumItems.GATHER_NODE_CRYSTAL.getDefaultStack(), 0.1, false);

	private static final Identifier BASE = SpectrumCommon.locate("textures/block/pastel_node_base.png");

	private static final Identifier INNER_RING = SpectrumCommon.locate("textures/block/pastel_node_inner_ring_blank.png");

	private static final Identifier OUTER_RING = SpectrumCommon.locate("textures/block/pastel_node_outer_ring_blank.png");
	private static final Identifier REDSTONE_RING = SpectrumCommon.locate("textures/block/pastel_node_redstone_ring_blank.png");

	private final ModelPart base;

	public PastelNodeBlockEntityRenderer(BlockEntityRendererFactory.Context renderContext) {
		this.base = getItemNodeBaseTexturedModelData().createModel();
	}

	public static @NotNull TexturedModelData getItemNodeBaseTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("base", ModelPartBuilder.create().uv(6, 0).cuboid(-1.0F, 1.1F, -1.0F, 2.0F, 0.0F, 2.0F), ModelTransform.NONE);
		modelPartData.addChild("leaf1", ModelPartBuilder.create().uv(-4, 0).cuboid(-2.0F, 1.0F, -4.0F, 4.0F, 0.0F, 4.0F), ModelTransform.of(0.0F, 0.0F, -1.0F, 0.5236F, 0.0F, 0.0F));
		modelPartData.addChild("leaf2", ModelPartBuilder.create().uv(-4, 4).cuboid(-2.0F, 1.0F, 0.0F, 4.0F, 0.0F, 4.0F), ModelTransform.of(0.0F, 0.0F, 1.0F, -0.5236F, 0.0F, 0.0F));
		modelPartData.addChild("leaf3", ModelPartBuilder.create().uv(-4, 8).cuboid(0.0F, 1.0F, -2.0F, 4.0F, 0.0F, 4.0F), ModelTransform.of(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));
		modelPartData.addChild("leaf4", ModelPartBuilder.create().uv(-4, 12).cuboid(-4.0F, 1.0F, -2.0F, 4.0F, 0.0F, 4.0F), ModelTransform.of(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));
		return TexturedModelData.of(modelData, 16, 16);
	}

	@Override
	public void render(PastelNodeBlockEntity node, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (node.getState() == null)
			return;

		var world = node.getWorld();
		if (world == null)
			return;

		var time = (world.getTime() + node.getCreationStamp()) % REAL_DAY_LENGTH + tickDelta;

		var crystal = switch (node.getNodeType()) {
			case CONNECTION -> CONNECTION;
			case STORAGE -> STORAGE;
			case BUFFER -> BUFFER;
			case PROVIDER -> PROVIDER;
			case SENDER -> SENDER;
			case GATHER -> GATHER;
		};

		var minimal = SpectrumCommon.CONFIG.MinimalNodes;
		var heightMod = minimal ? 0.7F : 0.5F;

		switch (node.getState()) {
			case CONNECTED -> {
				node.rotationTarget = mod(time / (Math.PI * 3));
				node.heightTarget = (float) Math.sin(time / 19F) / 10F + heightMod;
				node.alphaTarget = 1F;
			}
			case DISCONNECTED -> {
				node.heightTarget = 0;
				node.alphaTarget = 0;
			}
			case ACTIVE -> {
				node.rotationTarget = mod(time / (Math.PI * 1));
				node.heightTarget = (float) Math.sin(time / 19F) / 10F + heightMod;
				node.alphaTarget = 1F;
			}
			case INACTIVE -> {
				node.rotationTarget = mod(time / (Math.PI * 7));
				node.heightTarget = (float) Math.sin(time / 19F) / 20F + heightMod / 2F;
				node.alphaTarget = 0.275F;
			}
		}

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

		matrices.translate(0, -0.5, 0);

		if (minimal) {
			float quarterCrystalRotation = node.crystalRotation / 2;
			matrices.multiply(RotationAxis.POSITIVE_Y.rotation(quarterCrystalRotation));
			var rootBuffer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(BASE));
			base.render(matrices, rootBuffer, light, overlay);

			matrices.multiply(RotationAxis.POSITIVE_Y.rotation(quarterCrystalRotation * 2));

			matrices.scale(0.6F, 0.6F, 0.6F);
		} else {
			matrices.multiply(RotationAxis.POSITIVE_Y.rotation(node.crystalRotation));
		}

		var ringHeight = node.crystalHeight - 0.3F;
		var innerRing = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(node.getInnerRing().map(PastelUpgradeSignature::innerRing).orElse(INNER_RING)));
		renderRing(matrices, innerRing, 3.75F + ringHeight / 2F, 7F, node.ringAlpha, overlay, facing);

		var redstoneRing = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(node.getRedstoneRing().map(PastelUpgradeSignature::outerRing).orElse(REDSTONE_RING)));
		renderRing(matrices, redstoneRing, 5F + ringHeight, 15F, node.ringAlpha * node.getRedstoneAlphaMult(), overlay, facing);

		if (crystal.hasOuterRing()) {
			var outerRing = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(node.getOuterRing().map(PastelUpgradeSignature::outerRing).orElse(OUTER_RING)));
			renderRing(matrices, outerRing, 5.75F + ringHeight * 2, 11F, node.ringAlpha, overlay, facing);
		}

		matrices.translate(0.0, node.crystalHeight + crystal.yOffset, 0.0);
		MinecraftClient.getInstance().getItemRenderer().renderItem(crystal.crystal, ModelTransformationMode.NONE, LightmapTextureManager.MAX_LIGHT_COORDINATE, overlay, matrices, vertexConsumers, node.getWorld(), 0);
		matrices.pop();
	}

	private float mod(double in) {
		return (float) (in % (Math.PI * 2));
	}

	private record Crystal(ItemStack crystal, double yOffset, boolean hasOuterRing) {
	}

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
		float u1 = 1 / 16F, v1 = 1 / 16F;
		float u2 = u1 + u / 16F, v2 = v1 + v / 16F;

		vertices.vertex(model, x1, 0, z1).color(1F, 1F, 1F, alpha).texture(u1, v1).overlay(overlay).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(0, 1, 0).next();
		vertices.vertex(model, x2, 0, z1).color(1F, 1F, 1F, alpha).texture(u2, v1).overlay(overlay).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(0, 1, 0).next();
		vertices.vertex(model, x2, 0, z2).color(1F, 1F, 1F, alpha).texture(u2, v2).overlay(overlay).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(0, 1, 0).next();
		vertices.vertex(model, x1, 0, z2).color(1F, 1F, 1F, alpha).texture(u1, v2).overlay(overlay).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(0, 1, 0).next();
	}
}
