package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.blocks.mob_head.*;
import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
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
		if (stack.getItem() instanceof BlockItem blockItem) {
			if (blockItem.getBlock() instanceof SpectrumSkullBlock spectrumSkullBlock) {
				SpectrumSkullType spectrumSkullType = spectrumSkullBlock.getSkullType();
				SpectrumSkullBlockEntityRenderer.renderModels(0.0F, matrices, vertexConsumers, light, spectrumSkullType, null, 180.0F);
				ci.cancel();
			}
		}
	}
	
}
