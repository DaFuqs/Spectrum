package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.*;
import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
	
	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderModelLists(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemStack;IILcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;)V"), method = "render")
	private void spectrum$emissiveItems(ItemRenderer instance, BakedModel model, ItemStack stack, int light, int overlay, PoseStack matrices, VertexConsumer vertices, Operation<Void> original) {
		if (stack.is(SpectrumItemTags.EMISSIVE))
			light = LightTexture.FULL_BRIGHT;
		
		original.call(instance, model, stack, light, overlay, matrices, vertices);
	}
	
	@ModifyExpressionValue(method = "renderStatic(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;III)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;getModel(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)Lnet/minecraft/client/resources/model/BakedModel;"))
	private BakedModel spectrum$handleOversizedItemModels(BakedModel original, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) @Nullable Level world, @Local(argsOnly = true) @Nullable LivingEntity entity) {
		if (world instanceof ClientLevel clientWorld) {
			return original.getOverrides().resolve(original, stack, clientWorld, entity, 817210941);
		}
		return original;
	}

	/* All of this was commented as it used to handle over-sized item rendering. The method above handles it, but in case of mod compat issues, this is staying as a comment.
	@Inject(at = @At("HEAD"), method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V")
	private void spectrum$storeItemRenderMode1(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int light, int overlay, int seed, CallbackInfo ci) {
		SpectrumModelPredicateProviders.currentItemRenderMode = renderMode;
	}
	
	@Inject(at = @At("HEAD"), method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V")
	private void spectrum$storeItemRenderMode2(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
		SpectrumModelPredicateProviders.currentItemRenderMode = renderMode;
	}*/
	
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

	/* Same thing with the over-sized item rendering, but to handle REI-specific stuff.
	@Inject(at = @At("HEAD"), method = "renderBakedItemQuads(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Ljava/util/List;Lnet/minecraft/item/ItemStack;II)V")
	private void spectrum$storeItemRenderMode3(MatrixStack matrices, VertexConsumer vertices, List<BakedQuad> quads, ItemStack stack, int light, int overlay, CallbackInfo ci) {
		SpectrumModelPredicateProviders.currentItemRenderMode = ModelTransformationMode.GUI;
	}*/
	
}
