package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.entity.*;
import net.fabricmc.api.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.entity.decoration.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Environment(EnvType.CLIENT)
@Mixin(ItemFrameEntityRenderer.class)
public abstract class ItemFrameEntityRendererMixin<T extends ItemFrameEntity> {
	
	@Inject(at = @At("HEAD"), method = "getLight(Lnet/minecraft/entity/decoration/ItemFrameEntity;II)I", cancellable = true)
	private void getLight(T itemFrame, int glowLight, int regularLight, CallbackInfoReturnable<Integer> callbackInfoReturnable) {
		if (itemFrame.getType() == SpectrumEntityTypes.GLOW_PHANTOM_FRAME) { // TODO: still needed? Redstone aware?
			callbackInfoReturnable.setReturnValue(glowLight);
		}
	}
	
}
