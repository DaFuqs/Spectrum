package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.*;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.util.math.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
	
	@Inject(at = @At("HEAD"), method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V")
	private void spectrum$storeItemRenderMode1(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int light, int overlay, int seed, CallbackInfo ci) {
		SpectrumModelPredicateProviders.currentItemRenderMode = renderMode;
	}
	
	@Inject(at = @At("HEAD"), method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V")
	private void spectrum$storeItemRenderMode2(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
		SpectrumModelPredicateProviders.currentItemRenderMode = renderMode;
	}
	
	@Inject(at = @At("HEAD"), method = "innerRenderInGui(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;III)V")
	private void spectrum$storeItemRenderMode3(MatrixStack matrices, LivingEntity entity, World world, ItemStack stack, int x, int y, int seed, CallbackInfo ci) {
		SpectrumModelPredicateProviders.currentItemRenderMode = ModelTransformationMode.GUI;
	}
	
}
