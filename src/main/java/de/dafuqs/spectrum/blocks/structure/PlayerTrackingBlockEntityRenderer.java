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

public class PlayerTrackingBlockEntityRenderer implements BlockEntityRenderer<PlayerTrackerBlockEntity> {

	private static final SpriteIdentifier TEXTURE = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, SpectrumCommon.locate("block/manxi"));
	final double radiant = Math.toRadians(360.0F);

	protected static EntityRenderDispatcher dispatcher;
	private final ModelPart root, torso, head;

	public PlayerTrackingBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
		root = ManxiModel.getTexturedModelData().createModel();
		torso = root.getChild("root").getChild("torso");
		head = torso.getChild("head");
	}

	@Override
	public void render(PlayerTrackerBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();

		var state = entity.getCachedState();
		var taker = entity.hasTaken(MinecraftClient.getInstance().player);
		float time = entity.getWorld().getTime() % 24000 + tickDelta;

		// It would sure be nice if you could have multiple BERs for one block entity type... Anyways nice janky hacks you got there
		// Dafuqs: oh my god, this is so cursed
		if (state.isOf(SpectrumBlocks.MANXI)) {
			float f = (state.get(ChestBlock.FACING)).asRotation();
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
			if (!taker) {
				matrices.translate(-0.2, 1.4 + (yBreath / 6), -0.55);
				matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(100));
				matrices.scale(1.15F, 1.15F, 1.15F);
				MinecraftClient.getInstance().getItemRenderer().renderItem(SpectrumItems.POISONERS_HANDBOOK.getDefaultStack(), ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, entity.getWorld(), 0);
			}
		}
		else if (state.isOf(SpectrumBlocks.TREASURE_ITEM_BOWL) && !taker && TreasureItemBowlBlock.canInteract(MinecraftClient.getInstance().player)) {
			double currentRadiant = radiant + (radiant * (time / 16.0) / 8.0F);
			double height = Math.sin((time + currentRadiant) / 8.0) / 7.0; // item height
			matrices.translate(0.5, 0.8 + height, 0.5); // position offset
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time * 2)); // item stack rotation
			MinecraftClient.getInstance().getItemRenderer().renderItem(SpectrumItems.AETHER_GRACED_NECTAR_GLOVES.getDefaultStack(), ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, entity.getWorld(), 0);
		}

		matrices.pop();
	}
}