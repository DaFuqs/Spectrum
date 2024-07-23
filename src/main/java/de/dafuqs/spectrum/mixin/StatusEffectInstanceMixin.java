package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.SpectrumDamageTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StatusEffectInstance.class)
public abstract class StatusEffectInstanceMixin {

    @Shadow protected abstract boolean isActive();

    @Inject(method = "update", at = @At(value = "RETURN"))
    public void spectrum$fatalSlumberKill(LivingEntity entity, Runnable overwriteCallback, CallbackInfoReturnable<Boolean> cir) {
        if (!isActive()) {
            entity.damage(SpectrumDamageTypes.sleep(entity.getWorld(), null), Float.MAX_VALUE);
        }
    }
}
