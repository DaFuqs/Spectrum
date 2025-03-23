package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.blocks.mob_head.*;
import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Environment(EnvType.CLIENT)
@Mixin(BuiltinModelItemRenderer.class)
public abstract class BuiltinModelItemRendererMixin {
	
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void getModel(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
		Item item = stack.getItem();
		if (item instanceof BlockItem blockItem) {
			Block block = blockItem.getBlock();
			if (block instanceof SpectrumSkullBlock spectrumSkullBlock) {
				SpectrumSkullType spectrumSkullType = (SpectrumSkullType) (spectrumSkullBlock).getSkullType();
				RenderLayer renderLayer = SpectrumSkullBlockEntityRenderer.getRenderLayer(spectrumSkullType);
				SpectrumSkullModel model = SpectrumSkullBlockEntityRenderer.getModel(spectrumSkullType);
				SpectrumSkullBlockEntityRenderer.renderSkull(null, 180.0F, 0.0F, matrices, vertexConsumers, light, model, renderLayer);
				ci.cancel();
			}
		}
	}
	
}
