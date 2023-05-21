package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.client.*;
import de.dafuqs.spectrum.render.*;
import de.dafuqs.spectrum.render.armor.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.client.network.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Environment(EnvType.CLIENT)
@Mixin(CapeFeatureRenderer.class)
public abstract class CapeFeatureRendererMixin extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

	public CapeFeatureRendererMixin(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> ctx) {
		super(ctx);
	}

    /**
     * Renders a custom flap on the front of the Bedrock Armor, as well as a custom cape render
     */
	@Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;FFFFFF)V", at = @At("HEAD"), cancellable = true)
	public void spectrum$renderBedrockCape(MatrixStack ms, VertexConsumerProvider vertices, int light, AbstractClientPlayerEntity player, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		// If the player has disabled their cape from rendering, do not render
		if (!player.isPartVisible(PlayerModelPart.CAPE)) return;

		// Check for the chestplate, and begin rendering the cape if equipped
		if (player.getEquippedStack(EquipmentSlot.CHEST).getItem() == SpectrumItems.BEDROCK_CHESTPLATE) {

			// Vanilla cape values
			double x = MathHelper.lerpAngleDegrees(h / 2, (float) player.prevCapeX, (float) player.capeX)
					- MathHelper.lerpAngleDegrees(h / 2, (float) player.prevX, (float) player.getX());
			double y = MathHelper.lerpAngleDegrees(h / 2, (float) player.prevCapeY, (float) player.capeY)
					- MathHelper.lerpAngleDegrees(h / 2, (float) player.prevY, (float) player.getY());
			double z = MathHelper.lerpAngleDegrees(h / 2, (float) player.prevCapeZ, (float) player.capeZ)
					- MathHelper.lerpAngleDegrees(h / 2, (float) player.prevZ, (float) player.getZ());
			float yaw = player.prevBodyYaw + (player.bodyYaw - player.prevBodyYaw);
			double o = MathHelper.sin(yaw * (float) (Math.PI / 180.0));
			double p = -MathHelper.cos(yaw * (float) (Math.PI / 180.0));
			float q = (float) y * 10.0F;
			q = MathHelper.clamp(q, -6.0F, 32.0F);
			float r = (float) (x * o + z * p) * 100.0F;
			r = MathHelper.clamp(r, 0.0F, 150.0F);
			float capeZOffset = (float) (x * p - z * o) * 100.0F;
			capeZOffset = MathHelper.clamp(capeZOffset, -20.0F, 20.0F);
			if (r < 0.0F) {
				r = 0.0F;
			}


			float t = MathHelper.lerp(h, player.prevStrideDistance, player.strideDistance);
			q += MathHelper.sin(MathHelper.lerp(h, player.prevHorizontalSpeed, player.horizontalSpeed) * 6.0F) * 32.0F * t;

			if (player.isInSneakingPose()) {
				q += 25.0F;
			}

			float frontCapeRotation = MathHelper.clamp(-(6.0F + r / 2.0F + q), -25, 0);

            // Transform and render front cloth
            VertexConsumer vertexConsumer = vertices.getBuffer(RenderLayer.getEntitySolid(SpectrumModelLayers.BEDROCK_ARMOR_LOCATION));
            ms.push();
            ms.translate(0, 0.35, 0);
            ms.multiply(RotationAxis.POSITIVE_X.rotationDegrees(frontCapeRotation));
            if (!player.isInSneakingPose()) {
                ms.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(capeZOffset / 2.0F));
            }

            // Make some space for your legs if crouching
            ms.translate(0, -0.65, -0.15);
            if (player.isInSneakingPose()) {
                ms.translate(0, 0.05, 0.35);
            }
            BedrockArmorCapeModel.FRONT_CLOTH.render(ms, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
            ms.pop();

            // Respect the players own cape, Elytras and Fabrics Render Event
            if (player.getCapeTexture() != null || RenderingContext.isElytraRendered || !LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.invoker().allowCapeRender(player)) {
                return;
            }

            float backCapeRotation = MathHelper.clamp(6.0F + r / 2.0F + q, -30, 60);

			// Transform and render the custom cape
			ms.push();
			ms.translate(0, -0.05, 0.0); // Push up and backwards, then rotate
			ms.multiply(RotationAxis.POSITIVE_X.rotationDegrees(backCapeRotation));
			ms.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(capeZOffset / 2.0F));
			ms.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - capeZOffset / 1.25F));
			ms.translate(0, 0.05, -0.325); // Move back down
			if (player.isInSneakingPose()) {
				ms.translate(0, 0.15, 0.125);
			}

			BedrockArmorCapeModel.CAPE_MODEL.render(ms, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
			ms.pop();

            // Cancel any other capes from rendering
			ci.cancel();
		}
	}
}
