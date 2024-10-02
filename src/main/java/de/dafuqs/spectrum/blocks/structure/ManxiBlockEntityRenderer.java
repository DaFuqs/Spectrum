package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.util.*;
import net.minecraft.client.util.math.*;
import net.minecraft.screen.*;
import net.minecraft.util.math.*;

public class ManxiBlockEntityRenderer implements BlockEntityRenderer<ManxiBlockEntity> {

	private static final SpriteIdentifier TEXTURE = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, SpectrumCommon.locate("block/manxi"));

	protected static EntityRenderDispatcher dispatcher;
	private final ModelPart root, torso, head;

	public ManxiBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
		root = ManxiModel.getTexturedModelData().createModel();
		torso = root.getChild("root").getChild("torso");
		head = torso.getChild("head");
	}

	@Override
	public void render(ManxiBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();

		var state = entity.getCachedState();
		float f = (state.get(ChestBlock.FACING)).asRotation();
		float time = entity.getWorld().getTime() % 24000 + tickDelta;
		double yBreath = Math.sin(time / 19) * 0.02;
		double xBreath = Math.sin(time / 19) * 0.0425;

		matrices.translate(0.5D, 1.5D, 0.5D);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f));
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
		torso.pivotY = (float) (1 - yBreath);
		torso.pivotZ = (float) (2 - (yBreath / 2));
		torso.pivotX = (float) (-xBreath);
		head.pivotX = (float) (-0.0384F + (xBreath / 2));

		var renderLayer = TEXTURE.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);
		root.render(matrices, renderLayer, light, overlay);

		assert MinecraftClient.getInstance().player != null;
		if (!entity.hasTaken(MinecraftClient.getInstance().player)) {
			matrices.translate(-0.2, 1.4 + (yBreath / 6), -0.55);
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(100));
			matrices.scale(1.15F, 1.15F, 1.15F);
			MinecraftClient.getInstance().getItemRenderer().renderItem(SpectrumItems.POISONERS_HANDBOOK.getDefaultStack(), ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, entity.getWorld(), 0);
		}

		matrices.pop();
	}
}
