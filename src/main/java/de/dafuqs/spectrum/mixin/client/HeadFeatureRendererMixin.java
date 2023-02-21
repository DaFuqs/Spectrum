package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.fabricmc.api.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Environment(EnvType.CLIENT)
@Mixin(HeadFeatureRenderer.class)
public abstract class HeadFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T> & ModelWithHead> {

	@Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;rotate(Lnet/minecraft/client/util/math/MatrixStack;)V", shift = At.Shift.AFTER),
			cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	private void spectrum$renderSkull(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, T livingEntity, float animationProgress, float h, float j, float k, float l, float m, CallbackInfo ci, ItemStack itemStack, Item item, boolean bl) {
		if (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof SpectrumSkullBlock spectrumSkullBlock) {
			m = 1.1875F;
			matrixStack.scale(m, -m, -m);
			if (bl) {
				matrixStack.translate(0.0D, 0.0625D, 0.0D);
			}

			matrixStack.translate(-0.5D, 0.0D, -0.5D);

			SpectrumSkullBlock.SpectrumSkullBlockType skullType = (SpectrumSkullBlock.SpectrumSkullBlockType) spectrumSkullBlock.getSkullType();
			RenderLayer renderLayer = SpectrumSkullBlockEntityRenderer.getRenderLayer(skullType);
			SkullBlockEntityModel model = SpectrumSkullBlockEntityRenderer.getModel(skullType);
			SpectrumSkullBlockEntityRenderer.renderSkull(null, 180.0F, animationProgress, matrixStack, vertexConsumerProvider, light, model, renderLayer);
			matrixStack.pop();
			ci.cancel();
		}
	}
	
}
