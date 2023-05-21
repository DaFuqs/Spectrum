package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.render.*;
import net.fabricmc.api.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.util.math.*;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Environment(EnvType.CLIENT)
@Mixin(ElytraFeatureRenderer.class)
public abstract class ElytraFeatureRendererMixin {
	
	@Inject(
			method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V",
			at = @At(value = "HEAD"))
	public void spectrum$clearElytraRender(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, LivingEntity livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		RenderingContext.isElytraRendered = false;
	}
	
	@Inject(
			method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"))
	public void spectrum$isElytraRendered(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, LivingEntity livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		RenderingContext.isElytraRendered = true;
	}
}
