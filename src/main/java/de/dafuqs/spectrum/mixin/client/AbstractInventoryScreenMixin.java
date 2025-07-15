package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.sugar.*;
import com.llamalad7.mixinextras.sugar.ref.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.resources.*;
import net.minecraft.world.effect.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(EffectRenderingInventoryScreen.class)
public class AbstractInventoryScreenMixin {
	
	@ModifyVariable(method = "renderBackgrounds", at = @At("STORE"))
	private MobEffectInstance spectrum$saveEffect(MobEffectInstance value, @Share("effect") LocalRef<MobEffectInstance> effect) {
		effect.set(value);
		return value;
	}
	
	@ModifyArg(method = "renderBackgrounds", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/GuiGraphics.blitSprite (Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 0))
	private ResourceLocation spectrum$modifyWideBackground(ResourceLocation texture, @Local MobEffectInstance effect) {
		return StatusEffectHelper.getTextureLocation(texture, effect, StatusEffectHelper.RenderType.GUI_LARGE);
	}
	
	@ModifyArg(method = "renderBackgrounds", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/GuiGraphics.blitSprite (Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 1))
	private ResourceLocation spectrum$modifyBackground(ResourceLocation texture, @Share("effect") LocalRef<MobEffectInstance> effect) {
		return StatusEffectHelper.getTextureLocation(texture, effect.get(), StatusEffectHelper.RenderType.GUI_SMALL);
	}
	
}
