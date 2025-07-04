package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.sugar.*;
import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.blocks.mob_head.*;
import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.block.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Environment(EnvType.CLIENT)
@Mixin(CustomHeadLayer.class)
public abstract class HeadFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T> & HeadedModel> {
	
	@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
			at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"), cancellable = true)
	private void spectrum$renderSkull(PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int light, T livingEntity, float animationProgress, float h, float j, float k, float l, float m, CallbackInfo ci, @Local SkullBlock.Type type) {
		if (type instanceof SpectrumSkullType spectrumSkullType) {
			SpectrumSkullBlockEntityRenderer.renderModels(0.0F, poseStack, vertexConsumerProvider, light, spectrumSkullType, null, 180.0F);
			poseStack.popPose();
			ci.cancel();
		}
	}
	
}
