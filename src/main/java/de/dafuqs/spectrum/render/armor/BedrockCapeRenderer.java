package de.dafuqs.spectrum.render.armor;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.items.armor.*;
import de.dafuqs.spectrum.render.*;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.client.player.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;

public class BedrockCapeRenderer {
	
	/**
	 * Renders the bedrock cloth and cape on the player
	 */
	public static boolean renderBedrockCapeAndCloth(PoseStack ms, MultiBufferSource vertices, int light, AbstractClientPlayer player, float h, ItemStack stack) {
		// Vanilla cape values
		var capeRotations = BedrockArmorModel.computeFrontClothRotation(player, h);
		float capeZOffset = capeRotations.getB();
		
		// Transform and render front cloth
		VertexConsumer vertexConsumer = vertices.getBuffer(((BedrockArmorItem) stack.getItem()).getRenderLayer(stack));
		ms.pushPose();
		ms.translate(0, 0.5, 0);
		ms.mulPose(Axis.XP.rotationDegrees(Mth.clamp(capeRotations.getA(), -25, 0)));
		if (!player.isCrouching()) {
			ms.mulPose(Axis.ZP.rotationDegrees(capeZOffset / 2.0F));
		}
		
		// Make some space for your legs if crouching
		ms.translate(0, -0.65, -0.15);
		if (player.isCrouching()) {
			ms.translate(0, 0.05, 0.35);
		}
		BedrockArmorCapeModel.FRONT_CLOTH.render(ms, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
		ms.popPose();
		
		// Respect the players own cape, Elytras and Fabrics Render Event
		if (RenderingContext.isElytraRendered || !LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.invoker().allowCapeRender(player)) {
			return true;
		}
		
		float backCapeRotation = Mth.clamp(-capeRotations.getA(), -30, 45);
		
		// Transform and render the custom cape
		ms.pushPose();
		ms.translate(0, -0.05, 0.0); // Push up and backwards, then rotate
		ms.mulPose(Axis.XP.rotationDegrees(backCapeRotation));
		ms.mulPose(Axis.ZP.rotationDegrees(capeZOffset / 2.0F));
		ms.mulPose(Axis.YP.rotationDegrees(180.0F - capeZOffset / 3.5F));
		ms.translate(0, 0.05, -0.325); // Move back down
		if (player.isCrouching()) {
			ms.translate(0, 0.15, 0.125);
		}
		
		BedrockArmorCapeModel.CAPE_MODEL.render(ms, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
		ms.popPose();
		return false;
	}
	
}
