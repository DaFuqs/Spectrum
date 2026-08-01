package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;


@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
	
	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderModelLists(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemStack;IILcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;)V"), method = "render")
	private void spectrum$emissiveItems(ItemRenderer instance, BakedModel model, ItemStack stack, int light, int overlay, PoseStack matrices, VertexConsumer vertices, Operation<Void> original) {
		if (stack.is(SpectrumItemTags.EMISSIVE))
			light = LightTexture.FULL_BRIGHT;
		
		original.call(instance, model, stack, light, overlay, matrices, vertices);
	}
	
	@Inject(at = @At("HEAD"), method = "renderStatic(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;III)V")
	private void spectrum$handleOversizedItemModels(LivingEntity entity, ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, Level level, int combinedLight, int combinedOverlay, int seed, CallbackInfo ci) {
		SpectrumModelPredicateProviders.DISPLAY_CONTEXT = displayContext;
	}
	
	@Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V")
	private void spectrum$handleOversizedItemModels2(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel p_model, CallbackInfo ci) {
		SpectrumModelPredicateProviders.DISPLAY_CONTEXT = displayContext;
	}
	
	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	private void spectrum$dynRender(ItemStack stack, ItemDisplayContext renderMode, boolean leftHanded, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
		// if model is a dynamic one, check the renderers
		if (model instanceof DynamicRenderModel dm) {
			DynamicItemRenderer renderer = DynamicItemRenderer.RENDERERS.get(stack.getItem());
			// shouldn't happen normally but check anyway
			if (renderer != null) {
				// unwrap the model here so that the custom renderer doesn't have to do it
				renderer.render((ItemRenderer) (Object) this, stack, renderMode, leftHanded, matrices, vertexConsumers, light, overlay, dm.getWrappedModel());
				ci.cancel();
			}
		}
	}
	
}
