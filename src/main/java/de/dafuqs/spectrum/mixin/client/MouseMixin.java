package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import de.dafuqs.spectrum.status_effects.SleepStatusEffect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Mouse.class)
public class MouseMixin<T> {

    @ModifyExpressionValue(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/SimpleOption;getValue()Ljava/lang/Object;", ordinal = 0))
    public T spectrum$makeMouseSluggish(T original) {
        var sensitivity = (double) original;
        var player = MinecraftClient.getInstance().player;

        if (player == null)
            return original;

        var potency = SleepStatusEffect.getGeneralSleepVulnerability(player);

        return (T) (Object) MathHelper.clampedLerp(sensitivity, sensitivity / 2, potency / 2.5);
    }
}
