package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.blocks.mob_head.SpectrumSkullBlock;
import de.dafuqs.spectrum.blocks.mob_head.SpectrumSkullBlockEntityRenderer;
import de.dafuqs.spectrum.blocks.mob_head.SpectrumWallSkullBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(BuiltinModelItemRenderer.class)
public abstract class BuiltinModelItemRendererMixin {
	
	@Inject(method = "render(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
			at = @At("HEAD"), cancellable = true)
	private void getModel(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
		Item item = stack.getItem();
		if (item instanceof BlockItem) {
			Block block = ((BlockItem) item).getBlock();
			if (block instanceof SpectrumSkullBlock || block instanceof SpectrumWallSkullBlock) {
				SpectrumSkullBlock.SpectrumSkullBlockType spectrumSkullBlockType = (SpectrumSkullBlock.SpectrumSkullBlockType) ((SpectrumSkullBlock) block).getSkullType();
				RenderLayer renderLayer = SpectrumSkullBlockEntityRenderer.getRenderLayer(spectrumSkullBlockType);
				SkullBlockEntityModel model = SpectrumSkullBlockEntityRenderer.getModel(spectrumSkullBlockType);
				SpectrumSkullBlockEntityRenderer.renderSkull(null, 180.0F, 0.0F, matrices, vertexConsumers, light, model, renderLayer);
				ci.cancel();
			}
		}
	}
	
}
