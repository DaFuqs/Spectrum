package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

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
        var text = original.call(string);
        if (effect.getEffectType() == SpectrumStatusEffects.ETERNAL_SLUMBER) {
            return Text.translatable("effect.spectrum.eternal_slumber.duration_inf");
        }
        return text;
    }
}
