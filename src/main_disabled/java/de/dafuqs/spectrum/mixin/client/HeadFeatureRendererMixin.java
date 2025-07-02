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
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Environment(EnvType.CLIENT)
@Mixin(CustomHeadLayer.class)
public abstract class HeadFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T> & HeadedModel> {
	
	@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/CustomHeadLayer;translateToHead(Lcom/mojang/blaze3d/vertex/PoseStack;Z)V", shift = At.Shift.AFTER), cancellable = true)
	private void spectrum$renderSkull(PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int light, T livingEntity, float animationProgress, float h, float j, float k, float l, float m, CallbackInfo ci, @Local Item item, @Local boolean bl) {
		if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof SpectrumSkullBlock spectrumSkullBlock) {
			m = 1.1875F;
			poseStack.scale(m, -m, -m);
			if (bl) {
				poseStack.translate(0.0D, 0.0625D, 0.0D);
			}
			
			poseStack.translate(-0.5D, 0.0D, -0.5D);
			
			SpectrumSkullType skullType = spectrumSkullBlock.getType();
			SpectrumSkullBlockEntityRenderer.renderModels(0.0F, poseStack, vertexConsumerProvider, light, skullType, null, 180.0F);
			poseStack.popPose();
			ci.cancel();
		}
	}
	
}
