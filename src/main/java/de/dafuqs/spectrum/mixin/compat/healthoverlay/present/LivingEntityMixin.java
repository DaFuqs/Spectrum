package de.dafuqs.spectrum.mixin.compat.healthoverlay.present;

import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Inject(method = "getHealth", at = @At(value = "RETURN", target = "Lterrails/healthoverlay/render/HeartRenderer;renderPlayerHearts(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/entity/player/PlayerEntity;)V"), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void heartRendererRenderPlayerHeartsGetHealthInjector(CallbackInfoReturnable<Float> cir) {
        if (hasStatusEffect(SpectrumStatusEffects.DIVINITY)) {
            cir.setReturnValue(9 * 5f);
        }
    }
}
