package de.dafuqs.spectrum.render.armor;

import de.dafuqs.spectrum.items.armor.*;
import de.dafuqs.spectrum.render.RenderingContext;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents;
import net.minecraft.client.network.*;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.util.math.*;

public class BedrockCapeRenderer {
	
	/**
	 * Renders the bedrock cloth and cape on the player
	 */
	public static void renderBedrockCapeAndCloth(MatrixStack ms, VertexConsumerProvider vertices, int light, AbstractClientPlayerEntity player, float h, ItemStack stack) {

		// Transform and render front cloth
		var capeRotations = BedrockArmorModel.computeFrontClothRotation(player, h);
		float capeZOffset = capeRotations.getRight();

		VertexConsumer vertexConsumer = vertices.getBuffer(((BedrockArmorItem) stack.getItem()).getRenderLayer(stack));
		ms.push();
		ms.translate(0, 0.5, 0);
		ms.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.clamp(capeRotations.getLeft(), -25, 0)));
		if (!player.isInSneakingPose()) {
			ms.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(capeZOffset / 2.0F));
		}

		// Make some space for your legs if crouching
		ms.translate(0, -0.5, -0.025);
		if (player.isInSneakingPose()) {
			ms.translate(0, 0.05, 0.35);
		}
		BedrockArmorCapeModel.FRONT_CLOTH.render(ms, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
		ms.pop();

		// TODO - Respect player capes once armor tailoring is implemented
		// Respect Elytras and Fabrics Render Event
		if (RenderingContext.isElytraRendered || !LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.invoker().allowCapeRender(player)) {
			return;
		}

		// The front and back cape are almost matching, but inverted
		float backCapeRotation = MathHelper.clamp(-capeRotations.getLeft(), -30, 45);

		// Transform and render the custom cape
		ms.push();
		ms.translate(0, -0.05, 0.0); // Push up and backwards, then rotate
		ms.multiply(RotationAxis.POSITIVE_X.rotationDegrees(backCapeRotation));
		ms.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(capeZOffset / 2.0F));
		ms.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - capeZOffset / 3.5F));
		ms.translate(0, 0.05, -0.325); // Move back down
		if (player.isInSneakingPose()) {
			ms.translate(0, 0.15, 0.125);
		}

		BedrockArmorCapeModel.CAPE_MODEL.render(ms, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
		ms.pop();
	}
	
}
