package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.entity.decoration.ItemFrameEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ItemFrameEntityRenderer.class)
public abstract class ItemFrameEntityRendererMixin<T extends ItemFrameEntity> {
	
	@Inject(at = @At("HEAD"), method = "getLight(Lnet/minecraft/entity/decoration/ItemFrameEntity;II)I", cancellable = true)
	private void getLight(T itemFrame, int glowLight, int regularLight, CallbackInfoReturnable<Integer> callbackInfoReturnable) {
		if (itemFrame.getType() == SpectrumEntityTypes.PHANTOM_GLOW_FRAME) {
			callbackInfoReturnable.setReturnValue(glowLight);
		}
	}
	
}
