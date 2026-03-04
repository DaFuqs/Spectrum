package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.network.chat.*;
import net.minecraft.util.*;
import net.minecraft.world.effect.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(MobEffectUtil.class)
public class StatusEffectUtilMixin {
	
	@WrapOperation(method = "formatDuration", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Component;literal(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;"))
	private static MutableComponent spectrum$modifyDurationText(String string, Operation<MutableComponent> original, @Local int i, @Local(argsOnly = true) MobEffectInstance effect, @Local(argsOnly = true, ordinal = 0) float multiplier, @Local(argsOnly = true, ordinal = 1) float tickRate) {
		if (effect.getEffect() == SpectrumMobEffects.ETERNAL_SLUMBER) {
			return Component.translatable("effect.spectrum.eternal_slumber.duration", StringUtil.formatTickDuration(i, tickRate));
		}
		return original.call(string);
	}
	
	@WrapOperation(method = "formatDuration", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;"))
	private static MutableComponent spectrum$modifyDurationTextInfinite(String string, Operation<MutableComponent> original, @Local(argsOnly = true) MobEffectInstance effect) {
		if (effect.getEffect() == SpectrumMobEffects.ETERNAL_SLUMBER) {
			return Component.translatable("effect.spectrum.eternal_slumber.duration_inf");
		}
		return original.call(string);
	}
}
