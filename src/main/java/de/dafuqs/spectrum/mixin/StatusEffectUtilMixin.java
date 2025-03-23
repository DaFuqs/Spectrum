package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.effect.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(StatusEffectUtil.class)
public class StatusEffectUtilMixin {

    @WrapOperation(method = "getDurationText", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text;literal(Ljava/lang/String;)Lnet/minecraft/text/MutableText;"))
    private static MutableText spectrum$modifyDurationText(String string, Operation<MutableText> original, @Local int i, @Local(argsOnly = true) StatusEffectInstance effect) {
        if (effect.getEffectType() == SpectrumStatusEffects.ETERNAL_SLUMBER) {
            return Text.translatable("effect.spectrum.eternal_slumber.duration", StringHelper.formatTicks(i));
        }
        return original.call(string);
    }

    @WrapOperation(method = "getDurationText", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;"))
    private static MutableText spectrum$modifyDurationTextInfinite(String string, Operation<MutableText> original, @Local(argsOnly = true) StatusEffectInstance effect) {
        if (effect.getEffectType() == SpectrumStatusEffects.ETERNAL_SLUMBER) {
            return Text.translatable("effect.spectrum.eternal_slumber.duration_inf");
        }
        return original.call(string);
    }
}
