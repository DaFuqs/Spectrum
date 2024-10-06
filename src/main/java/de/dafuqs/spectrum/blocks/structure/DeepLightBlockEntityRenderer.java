package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.helpers.ColorHelper;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.font.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.util.math.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;

import java.util.*;

public class DeepLightBlockEntityRenderer implements BlockEntityRenderer<DeepLightBlockEntity> {

	protected static EntityRenderDispatcher dispatcher;
	private final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
	private final List<Text> text = new ArrayList<>();

	public DeepLightBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
		for (int i = 0; i < 9; i++) {
			text.add(Text.translatable("block.spectrum.deep_light_chiseled_preservation_stone.puzzle" + i));
		}

	}

	@Override
	public void render(DeepLightBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (Math.sqrt(MinecraftClient.getInstance().player.squaredDistanceTo(entity.getPos().toCenterPos())) > 6F)
			return;

		matrices.push();

		float time = entity.getWorld().getTime() % 24000 + tickDelta;
		double bob = Math.sin(time / 37) * 0.05;

		float f = (entity.getCachedState().get(DeepLightBlock.FACING)).asRotation();
		matrices.translate(0.5, 0, 0.5);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f));
		matrices.translate(1.3, 1.4 + bob, -0.7);
		matrices.scale(0.00875F, 0.00875F, 0.00875F);

		var interp = MathHelper.clamp(Math.sqrt(MinecraftClient.getInstance().player.squaredDistanceTo(entity.getPos().toCenterPos())) / 8F, 0, 1) * 1.25F;
		var alpha = (int) MathHelper.clampedLerp(255, 2, interp);

		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
		for (int i = 0; i < text.size(); i++) {
			textRenderer.draw(text.get(i), 0, 14 * i + ((i == 0) ? -20 : 0),  (alpha & 255) << 24 | 0xe9c4ff, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, 0, LightmapTextureManager.MAX_LIGHT_COORDINATE);
		}

		matrices.pop();
	}
}
