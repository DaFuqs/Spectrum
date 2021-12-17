package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.blocks.mob_head.SpectrumSkullBlock;
import de.dafuqs.spectrum.blocks.mob_head.SpectrumSkullBlockEntityRenderer;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.SkullBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeadFeatureRenderer.class)
public class HeadFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T> & ModelWithHead> {

	@Inject(method= "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractSkullBlock;getSkullType()Lnet/minecraft/block/SkullBlock$SkullType;"),
			cancellable = true)
	private void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, T livingEntity, float animationProgress, float g, float h, float j, float k, float l, CallbackInfo ci) {
		ItemStack itemStack = livingEntity.getEquippedStack(EquipmentSlot.HEAD);
		Item item = itemStack.getItem();
		
		if (item instanceof BlockItem && ((BlockItem)item).getBlock() instanceof SpectrumSkullBlock) {
			SkullBlock.SkullType skullType = ((SpectrumSkullBlock)((BlockItem)item).getBlock()).getSkullType();
			RenderLayer renderLayer = SpectrumSkullBlockEntityRenderer.getRenderLayer(skullType);
			SpectrumSkullBlockEntityRenderer.renderSkull(null, 180.0F, animationProgress, matrixStack, vertexConsumerProvider, light, renderLayer);
			matrixStack.pop();
			ci.cancel();
		}
	}

}
