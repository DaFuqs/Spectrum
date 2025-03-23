package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.client.*;
import net.minecraft.util.math.*;
import org.objectweb.asm.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(Mouse.class)
public class MouseMixin<T> {

    @ModifyExpressionValue(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/SimpleOption;getValue()Ljava/lang/Object;", ordinal = 0))
    public T spectrum$makeMouseSluggish(T original) {
        var sensitivity = (double) original;
        var player = MinecraftClient.getInstance().player;

        if (player == null)
            return original;
		
		var potency = SleepStatusEffect.getSleepScaling(player);

        if (potency == -1)
            return original;

        return (T) (Object) MathHelper.clampedLerp(sensitivity, sensitivity / 2, potency / 2.5);
    }

    // AHAHAHAH, I FINALLY FIGURED OUT HOW TO DO IT!
    @ModifyExpressionValue(method = "updateMouse", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/option/GameOptions;smoothCameraEnabled:Z"))
    public boolean spectrum$forceSmoothCamera(boolean original) {
        var player = MinecraftClient.getInstance().player;

        if (player == null)
            return original;

        var potency = SleepStatusEffect.getSleepScaling(player);

        if (potency < 1.9)
            return original;

        return true;
    }
}
