package de.dafuqs.spectrum.mixin.client;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.blocks.mob_head.*;
import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.renderer.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Environment(EnvType.CLIENT)
@Mixin(BlockEntityWithoutLevelRenderer.class)
public abstract class BuiltinModelItemRendererMixin {
	
	@Inject(method = "renderByItem", at = @At("HEAD"), cancellable = true)
	private void getModel(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, CallbackInfo ci) {
		if (stack.getItem() instanceof BlockItem blockItem) {
			if (blockItem.getBlock() instanceof SpectrumSkullBlock spectrumSkullBlock) {
				SpectrumSkullType spectrumSkullType = spectrumSkullBlock.getType();
				SpectrumSkullBlockEntityRenderer.renderModels(0.0F, matrices, vertexConsumers, light, spectrumSkullType, null, 180.0F);
				ci.cancel();
			}
		}
	}
	
}
