package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.items.tools.SpectrumBowItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin {
	
	@Inject(method = "getFovMultiplier", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
	private void spectrum$applyCustomBowZoom(CallbackInfoReturnable<Float> cir, float f) {
		AbstractClientPlayerEntity thisPlayer = (AbstractClientPlayerEntity)(Object) this;
		Item item = thisPlayer.getActiveItem().getItem();
		if (thisPlayer.isUsingItem() && item instanceof SpectrumBowItem spectrumBowItem) {
			int i = thisPlayer.getItemUseTime();
			float g = (float) i / spectrumBowItem.getZoom();
			
			if (g > 1.0F) {
				g = 1.0F;
			} else {
				g *= g;
			}
			
			f *= 1.0F - g * 0.15F;
			cir.setReturnValue(MathHelper.lerp(MinecraftClient.getInstance().options.fovEffectScale, 1.0f, f));
		}
		
	}

}
